package com.busico.android.training.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;

public class MainThreadHandler extends Handler {

    public static final int IMAGE_DOWNLOADED = 20001;
    private static final MainThreadHandler INSTANCE = new MainThreadHandler();

    private HashMap<Integer, ContentListener> contentListenerById;

    private MainThreadHandler() {
        super(Looper.getMainLooper());
        contentListenerById = new HashMap<Integer, ContentListener>();
    }

    public static MainThreadHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == IMAGE_DOWNLOADED) {
            if (msg.obj == null) {
                return;
            }

            Bitmap image = (Bitmap) msg.obj;
            int contentListenerId = msg.arg1;
            int contentId = msg.arg2;

            ContentListener contentListener = contentListenerById.get(contentListenerId);
            contentListener.notifyContentAvailable(contentId, image);
        }
    }

    public void registerContentListener(ContentListener contentListener) {
        contentListenerById.put(contentListener.getContentListenerId(), contentListener);
    }
}
