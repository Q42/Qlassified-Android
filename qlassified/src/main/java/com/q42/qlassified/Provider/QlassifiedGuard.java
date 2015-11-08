package com.q42.qlassified.Provider;

import com.q42.qlassified.Entry.QlassifiedEntry;
import com.q42.qlassified.Entry.QlassifiedString;
import com.q42.qlassified.Entry.EncryptedEntry;

/**
 * WARNING, this class is a replacement class for the Android keystore
 * when trying to access it from an Android API version below 18.
 *
 * This class gives a false sense of security as the keys used here are
 * easily accessible to a medium level scriptkiddy. Fortunately this class
 * will be used less and less over the coming years as everyone will
 * slowely move to an Android version greater than 17.
 */
public class QlassifiedGuard implements QlassifiedSecurity {

    public EncryptedEntry encryptEntry(QlassifiedEntry classifiedEntry) {
        return new EncryptedEntry(classifiedEntry.getKey(), String.valueOf(classifiedEntry.getValue()));
    }

    public QlassifiedEntry decryptEntry(EncryptedEntry entry) {
        return new QlassifiedString(entry.getKey(), entry.getEncryptedValue());
    }
}
