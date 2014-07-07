package com.busico.android.training.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.busico.android.training.entities.Item;
import com.busico.android.training.managers.ItemManager;
import com.busico.android.training.utils.Closure;

import java.util.LinkedList;

public class SearchItemsTask extends AsyncTask<Object, Void, LinkedList<Item>> {

    private static final String TAG = "SearchItemsTask";
    private final Closure<LinkedList<Item>> closure;

    public static void searchItems(String queryString, int limit, int offset, Closure<LinkedList<Item>> closure) {
        new SearchItemsTask(closure).execute(queryString, new Integer(limit), new Integer(offset));
    }

    private SearchItemsTask(Closure<LinkedList<Item>> closure) {
        this.closure = closure;
    }

    @Override
    protected LinkedList<Item> doInBackground(Object... parameters) {
        String queryString = parameters[0].toString();
        Integer limit = (Integer) parameters[1];
        Integer offset = (Integer) parameters[2];

        LinkedList<Item> result = null;
        try {
            result = new ItemManager().searchItems(queryString, limit, offset);
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