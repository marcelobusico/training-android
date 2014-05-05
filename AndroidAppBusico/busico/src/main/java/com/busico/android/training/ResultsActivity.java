package com.busico.android.training;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.busico.android.training.adapters.ProductsAdapter;
import com.busico.android.training.entities.Product;

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
    private LinkedList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        if(savedInstanceState==null) {
            String queryString = getIntent().getStringExtra("queryString");
            new SearchTask().execute(queryString);
        } else {
            LinkedList<Product> products = (LinkedList<Product>) savedInstanceState.getSerializable("products");
            showProducts(products);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("products", products);
    }

    private void showProducts(LinkedList<Product> products) {
        this.products = products;

        ListView listView = (ListView) findViewById(R.id.lstResults);
        ListAdapter listAdapter = new ProductsAdapter(getLayoutInflater(), products);
        listView.setAdapter(listAdapter);
    }

    private class SearchTask extends AsyncTask<String, Void, LinkedList<Product>> {

        @Override
        protected LinkedList<Product> doInBackground(String... parameters) {
            String queryString = parameters[0];

            LinkedList<Product> result = null;
            try {
                result = searchInMeli(queryString);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(LinkedList<Product> results) {
            showProducts(results);
        }

        private LinkedList<Product> searchInMeli(String queryString) throws IOException, JSONException {
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

            LinkedList<Product> results = new LinkedList<Product>();
            JSONArray products = jsonObject.getJSONArray("results");
            for (int i = 0; i < products.length(); i++) {
                JSONObject jsonProduct = products.getJSONObject(i);
                String id = jsonProduct.getString("id");
                String productUrl = jsonProduct.getString("permalink");
                String title = jsonProduct.getString("title");
                double price = jsonProduct.getDouble("price");
                String imageUrl = jsonProduct.getString("thumbnail");

                Product product = new Product(id, productUrl, title, price, imageUrl);

                results.add(product);
            }

            return results;
        }
    }

}
