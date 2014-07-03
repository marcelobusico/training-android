package com.busico.android.training.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.busico.android.training.adapters.ItemsAdapter;
import com.busico.android.training.entities.Item;
import com.busico.android.training.utils.ContentDownloaderService;

public class ImageDownloadedReceiver extends BroadcastReceiver {

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
