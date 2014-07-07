package com.busico.android.training.managers;

import android.util.Log;

import com.busico.android.training.entities.Item;

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

public class ItemManager {

    private static final String TAG = "ItemManager";

    public LinkedList<Item> searchItems(String queryString, int limit, int offset) throws IOException, JSONException {
        Log.d(TAG, "Query is: " + queryString);

        StringBuilder uriStringBuilder = new StringBuilder();
        uriStringBuilder.append("https://api.mercadolibre.com/sites/MLA/search");
        uriStringBuilder.append("?q=").append(URLEncoder.encode(queryString, "utf-8"));
        uriStringBuilder.append("&limit=").append(limit);
        uriStringBuilder.append("&offset=").append(offset);

        String uri = uriStringBuilder.toString();

        Log.d(TAG, "URI is: " + uri);

        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = new HttpGet(uri);
        BasicHttpParams basicHttpParams = new BasicHttpParams();
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
            Double price = jsonProduct.getDouble("price");
            String imageUrl = jsonProduct.getString("thumbnail");
            Integer quantity = jsonProduct.getInt("available_quantity");
            String subtitle = jsonProduct.getString("subtitle");

            Item item = new Item(id, productUrl, title, price, imageUrl);
            item.setAvailableQuantity(quantity);
            item.setSubtitle(subtitle != null && !subtitle.equals("null") ? subtitle : null);

            results.add(item);
        }

        return results;
    }
}
