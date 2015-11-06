package com.q42.qlassified.Entry;

public class EncryptedEntry {

    private final String key;
    private final String encryptedValue;

    public EncryptedEntry(String key, String encryptedValue) {
        this.key = key;
        this.encryptedValue = encryptedValue;
    }

    public String getKey() {
        return key;
    }
    public String getEncryptedValue() {
        return encryptedValue;
    }
}
