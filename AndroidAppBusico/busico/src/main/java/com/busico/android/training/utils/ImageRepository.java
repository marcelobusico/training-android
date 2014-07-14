package com.busico.android.training.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageRepository {

    private static final String TAG = "ImageRepository";
    private static final ImageRepository INSTANCE = new ImageRepository();
    private ThreadPoolExecutor threadPoolExecutor;
    private LruCache<String, Bitmap> imageCache;

    private ImageRepository() {
        threadPoolExecutor = new ThreadPoolExecutor(2, 10, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        int cacheSizeInBytes = 16 * 1024 * 1024;
        imageCache = new LruCache<String, Bitmap>(cacheSizeInBytes);
    }

    public static ImageRepository getInstance() {
        return INSTANCE;
    }

    public void getImage(final int contentListenerId, final int contentId, final String imageUrl) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap image = imageCache.get(imageUrl);

                    if (image == null) {
                        Log.d(TAG, "Download image from URL: " + imageUrl);

                        byte[] content = UrlRequester.getContent(imageUrl);
                        image = BitmapFactory.decodeByteArray(content, 0, content.length);
                        imageCache.put(imageUrl, image);
                    } else {
                        Log.d(TAG, "Getting image from cache for URL: " + imageUrl);
                    }

                    Message message = Message.obtain(MainThreadHandler.getInstance(),
                            MainThreadHandler.IMAGE_DOWNLOADED, contentListenerId, contentId, image);
                    message.sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "Error while trying to get image.", e);
                }
            }
        });
    }
}
