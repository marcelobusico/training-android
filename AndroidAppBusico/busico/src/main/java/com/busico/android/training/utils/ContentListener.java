package com.busico.android.training.utils;

public interface ContentListener {

    int getContentListenerId();

    void notifyContentAvailable(int contentId, Object content);
}
