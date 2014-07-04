package com.busico.android.training.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.busico.android.training.entities.Item;
import com.busico.android.training.managers.ItemManager;
import com.busico.android.training.utils.Closure;

import java.util.LinkedList;

public class SearchItemsTask extends AsyncTask<String, Void, LinkedList<Item>> {

    private static final String TAG = "SearchItemsTask";
    private final Closure<LinkedList<Item>> closure;

    public static void searchItems(String queryString, Closure<LinkedList<Item>> closure) {
        new SearchItemsTask(closure).execute(queryString);
    }

    private SearchItemsTask(Closure<LinkedList<Item>> closure) {
        this.closure = closure;
    }

    @Override
    protected LinkedList<Item> doInBackground(String... parameters) {
        String queryString = parameters[0];

        LinkedList<Item> result = null;
        try {
            result = new ItemManager().searchItems(queryString, 100, 0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(LinkedList<Item> results) {
        closure.executeOnSuccess(results);
    }
}