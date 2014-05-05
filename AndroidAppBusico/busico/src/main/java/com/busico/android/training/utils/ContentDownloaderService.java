package com.busico.android.training.utils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;

public class ContentDownloaderService extends IntentService {

    private static final String TAG = "ContentDownloaderService";
    public static final String URL = "url";
    public static final String CONTENT_ID = "contentId";
    public static final String CONTENT = "content";
    public static final String INDEX = "index";
    public static final String ACTION = "ContentDownloaderService.DOWNLOAD_COMPLETED";

    public ContentDownloaderService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ContentDownloaderService onCreate method called.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String url = intent.getStringExtra(URL);
            String contentId = intent.getStringExtra(CONTENT_ID);
            int index = intent.getIntExtra(INDEX, 0);

            Log.d(TAG, "Download content id " + contentId + " from URL: " + url);

            byte[] content = UrlRequester.getContent(url);

            notifyFileDownloaded(contentId, content, index);
        } catch (Exception ex) {
            Log.e(TAG, "Error while downloading content from url.", ex);
        }
    }

    private void notifyFileDownloaded(String contentId, byte[] content, int index) {
        Log.d(TAG, "Notify content id " + contentId + " is downloaded");

        Intent broadcastIntent = new Intent();

        broadcastIntent.setAction(ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(CONTENT_ID, contentId);
        broadcastIntent.putExtra(CONTENT, content);
        broadcastIntent.putExtra(INDEX, index);

        sendBroadcast(broadcastIntent);
    }
}
