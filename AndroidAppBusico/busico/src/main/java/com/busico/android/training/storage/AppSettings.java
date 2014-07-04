package com.busico.android.training.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppSettings {

    private static final String TAG = "AppSettings";
    private DatabaseOpenHelper databaseOpenHelper;

    public AppSettings(Context context) {
        databaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public String getSavedValue(String key) {
        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select value from app_settings where key = ?", new String[]{key});

        if (cursor == null || cursor.getCount() == 0) {
            Log.d(TAG, "getSavedValue - Key: " + key + " - Value: null");
            return null;
        }
        cursor.moveToFirst();

        String value = cursor.getString(cursor.getColumnIndex("value"));
        Log.d(TAG, "getSavedValue - Key: " + key + " - Value: " + value);
        return value;
    }

    public void saveValue(String key, String value) {
        Log.d(TAG, "saveValue - Key: " + key + " - Value: " + value);
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        database.beginTransaction();

        Cursor cursor = database.rawQuery("select value from app_settings where key = ?", new String[]{key});

        ContentValues contentValues = new ContentValues();
        contentValues.put("key", key);
        contentValues.put("value", value);

        if (cursor == null || cursor.getCount() == 0) {
            database.insert("app_settings", null, contentValues);
        } else {
            database.update("app_settings", contentValues, "key = ?", new String[]{key});
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
