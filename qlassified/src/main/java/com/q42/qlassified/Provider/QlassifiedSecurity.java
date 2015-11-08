package com.q42.qlassified.Provider;

import com.q42.qlassified.Entry.QlassifiedEntry;
import com.q42.qlassified.Entry.EncryptedEntry;

public interface QlassifiedSecurity {

    EncryptedEntry encryptEntry(QlassifiedEntry classifiedEntry);
    QlassifiedEntry decryptEntry(EncryptedEntry entry);
}
