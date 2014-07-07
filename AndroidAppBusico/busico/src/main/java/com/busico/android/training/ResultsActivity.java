package com.busico.android.training;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.busico.android.training.adapters.ItemsAdapter;
import com.busico.android.training.entities.Item;
import com.busico.android.training.receivers.ImageDownloadedReceiver;
import com.busico.android.training.tasks.SearchItemsTask;
import com.busico.android.training.utils.Closure;
import com.busico.android.training.utils.ContentDownloaderService;

import java.util.LinkedList;

public class ResultsActivity extends Activity {

    private static final String TAG = "Results";
    private final LinkedList<Item> items = new LinkedList<Item>();
    private String queryString;
    private ItemsAdapter itemsAdapter;
    private ImageDownloadedReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        itemsAdapter = new ItemsAdapter(getLayoutInflater(), items);
        ListView listView = (ListView) findViewById(R.id.lstResults);
        listView.setAdapter(itemsAdapter);

        if (savedInstanceState == null) {
            queryString = getIntent().getStringExtra("queryString");
            itemsAdapter.setQueryString(queryString);
            SearchItemsTask.searchItems(queryString, 15, 0, new Closure<LinkedList<Item>>() {
                @Override
                public void executeOnSuccess(LinkedList<Item> result) {
                    showProducts(result);
                }
            });
        } else {
            LinkedList<Item> items = (LinkedList<Item>) savedInstanceState.getSerializable("items");
            queryString = savedInstanceState.getString("queryString");
            itemsAdapter.setQueryString(queryString);
            showProducts(items);
        }

        //Register the Image Downloader Receiver.
        IntentFilter filter = new IntentFilter(ContentDownloaderService.ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ImageDownloadedReceiver(itemsAdapter);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putSerializable("items", items);
        outState.putString("queryString", queryString);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void showProducts(LinkedList<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        itemsAdapter.notifyDataSetChanged();
    }
}
