package com.busico.android.training.utils;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageDownloader extends IntentService {

    private static final String TAG = "ImageDownloader";
    public static final String IN_URL = "url";
    public static final String OUT_ACTION = "ImageDownloader.DOWNLOAD_COMPLETED";
    public static final String OUT_IMAGE = "image";

    public ImageDownloader() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String url = intent.getStringExtra(IN_URL);
            byte[] content = UrlRequester.getContent(url);
            Bitmap image = BitmapFactory.decodeByteArray(content, 0, content.length);

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(OUT_ACTION);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(OUT_IMAGE, image);

            sendBroadcast(broadcastIntent);
        } catch (Exception ex) {
            Log.e(TAG, "Error while downloading image.", ex);
        }
    }

}
