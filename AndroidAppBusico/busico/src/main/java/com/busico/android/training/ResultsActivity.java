package com.busico.android.training;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.busico.android.training.adapters.ItemsAdapter;
import com.busico.android.training.entities.Item;
import com.busico.android.training.utils.ContentDownloaderService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;


public class ResultsActivity extends Activity {

    private static final String TAG = "Results";
    private final LinkedList<Item> items = new LinkedList<Item>();
    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        itemsAdapter = new ItemsAdapter(getLayoutInflater(), items);
        ListView listView = (ListView) findViewById(R.id.lstResults);
        listView.setAdapter(itemsAdapter);

        if (savedInstanceState == null) {
            String queryString = getIntent().getStringExtra("queryString");
            new SearchTask().execute(queryString);
        } else {
            LinkedList<Item> items = (LinkedList<Item>) savedInstanceState.getSerializable("items");
            showProducts(items);
        }

        //Register the Image Downloader Receiver.
        IntentFilter filter = new IntentFilter(ContentDownloaderService.ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        ImageDownloadedReceiver receiver = new ImageDownloadedReceiver(itemsAdapter);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("items", items);
    }

    private void showProducts(LinkedList<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        itemsAdapter.notifyDataSetChanged();
    }

    private class SearchTask extends AsyncTask<String, Void, LinkedList<Item>> {

        @Override
        protected LinkedList<Item> doInBackground(String... parameters) {
            String queryString = parameters[0];

            LinkedList<Item> result = null;
            try {
                result = searchInMeli(queryString);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(LinkedList<Item> results) {
            showProducts(results);
        }

        private LinkedList<Item> searchInMeli(String queryString) throws IOException, JSONException {
            Log.d(TAG, "Query is: " + queryString);

            String uri = "https://api.mercadolibre.com/sites/MLA/search";
            uri += "?q=" + URLEncoder.encode(queryString, "utf-8");
            uri += "&limit=100";

            Log.d(TAG, "URI is: " + uri);

            HttpClient client = new DefaultHttpClient();
            HttpUriRequest request = new HttpGet(uri);
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            basicHttpParams.setParameter("q", queryString);
            basicHttpParams.setParameter("limit", 100);
            request.setParams(basicHttpParams);


            HttpResponse response = client.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());

            Log.d(TAG, "Response is: " + responseString);

            JSONObject jsonObject = new JSONObject(responseString);

            LinkedList<Item> results = new LinkedList<Item>();
            JSONArray products = jsonObject.getJSONArray("results");
            for (int i = 0; i < products.length(); i++) {
                JSONObject jsonProduct = products.getJSONObject(i);
                String id = jsonProduct.getString("id");
                String productUrl = jsonProduct.getString("permalink");
                String title = jsonProduct.getString("title");
                double price = jsonProduct.getDouble("price");
                String imageUrl = jsonProduct.getString("thumbnail");

                Item item = new Item(id, productUrl, title, price, imageUrl);

                results.add(item);
            }

            return results;
        }
    }

    private static class ImageDownloadedReceiver extends BroadcastReceiver {

        private static final String TAG = "ImageDownloadedReceiver";
        private ItemsAdapter itemsAdapter;

        public ImageDownloadedReceiver(ItemsAdapter itemsAdapter) {
            this.itemsAdapter = itemsAdapter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String contentId = intent.getStringExtra(ContentDownloaderService.CONTENT_ID);
                byte[] content = intent.getByteArrayExtra(ContentDownloaderService.CONTENT);
                int index = intent.getIntExtra(ContentDownloaderService.INDEX, 0);

                Log.d(TAG, "onReceive method called for contentId " + contentId);

                Bitmap image = BitmapFactory.decodeByteArray(content, 0, content.length);
                Item item = (Item) itemsAdapter.getItem(index);
                if (item.getId().equals(contentId)) {
                    item.setImage(image);
                    itemsAdapter.notifyDataSetChanged();
                }
            } catch (Exception ex) {
                Log.e(TAG, "Error in onReceive method", ex);
            }
        }
    }
}
