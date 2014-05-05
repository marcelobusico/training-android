package com.busico.android.training.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UrlRequester {

    public static byte[] getContent(String url) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request = new HttpGet(url);
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        return EntityUtils.toByteArray(entity);
    }
}
