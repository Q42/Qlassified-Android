package com.q42.qlassified.Storage;

import com.q42.qlassified.Entry.EncryptedEntry;

public abstract class QlassifiedStorageService {

    public abstract void onSaveRequest(EncryptedEntry entry);
    public abstract EncryptedEntry onGetRequest(String key);
}
