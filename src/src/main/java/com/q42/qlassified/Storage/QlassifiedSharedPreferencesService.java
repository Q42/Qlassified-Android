package com.q42.qlassified.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.q42.qlassified.Entry.EncryptedEntry;

public class QlassifiedSharedPreferencesService extends QlassifiedStorageService {

    private final SharedPreferences preferences;

    public QlassifiedSharedPreferencesService(Context context, String storageName) {
        this.preferences = context.getSharedPreferences(storageName, 0);
    }

    @Override
    public void onSaveRequest(EncryptedEntry encryptedEntry) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(encryptedEntry.getKey(), encryptedEntry.getEncryptedValue());
        editor.apply();
        Log.d("Storage", String.format("Saved key: %s", encryptedEntry.getKey()));
        Log.d("Storage", String.format("Saved encrypted value: %s", encryptedEntry.getEncryptedValue()));
    }

    @Override
    public EncryptedEntry onGetRequest(String key) {
        Log.d("Storage", String.format("Get by key: %s", key));
        final String encryptedValue = this.preferences.getString(key, null);
        Log.d("Storage", String.format("Got encrypted value: %s", encryptedValue));
        return new EncryptedEntry(key, encryptedValue);
    }
}
