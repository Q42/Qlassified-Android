package com.q42.qlassified;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.q42.qlassified.Entry.QlassifiedBoolean;
import com.q42.qlassified.Entry.QlassifiedFloat;
import com.q42.qlassified.Entry.QlassifiedInteger;
import com.q42.qlassified.Entry.QlassifiedLong;
import com.q42.qlassified.Entry.QlassifiedSerializable;
import com.q42.qlassified.Entry.QlassifiedString;
import com.q42.qlassified.Storage.QlassifiedStorageService;

import java.io.Serializable;

/**
 *  QlassifiedService singleton class - the enum way
 *
 *  This is the main entry point for Qlassified. Everyone is entitled to use the library as
 *  they desire, but it's most common to use the get and put methods presented in this service.
 *  They are statically available and the entire library should be instantiated only once.
 *  The single instance is regulated automatically by the fact that it's an enum.
 *
 *  --------------------------------------------------------------------------------------------
 *
 *  Singleton pattern by utilizing the Java enum
 *
 *  Joshua Bloch explained this approach in his Effective Java Reloaded talk at Google I/O 2008:
 *
 * "This approach is functionally equivalent to the public field approach, except that it is more
 *  concise, provides the serialization machinery for free, and provides an ironclad guarantee
 *  against multiple instantiation, even in the face of sophisticated serialization or reflection
 *  attacks. While this approach has yet to be widely adopted, a single-element enum type is the
 *  best way to implement a singleton."
 *
 *  ------- http://www.drdobbs.com/jvm/creating-and-destroying-java-objects-par/208403883?pgno=3
 */
public enum Qlassified {

    Service;

    /**
     * The constructor method
     * Creating a final and only factory service responsible
     * for creating the keystore and loosly coupling a storage
     * service to it.
     */
    Qlassified() {
        classifiedFactory = new QlassifiedFactory();
    }
    private final QlassifiedFactory classifiedFactory;

    public void start(Context context) {
        classifiedFactory.create(context);
    }

    /**
     * Loose coupled storage services can be added to which the encrypted data will be saved
     * after being encrypted with help from the Android Keystore system.
     * @param service {QlassifiedStorageService} A service extending from QlassifiedStorageService
     */
    public void setStorageService(@NonNull QlassifiedStorageService service) {
        if(service == null) {
            Log.e("Qlassified","Storage service prodided is null");
            return;
        }
        classifiedFactory.setStorageService(service);
    }

    /**
     * Get methods
     */

    public Boolean getBoolean(@NonNull String key) {
        return classifiedFactory.getBoolean(key);
    }

    public Float getFloat(@NonNull String key) {
        return classifiedFactory.getFloat(key);
    }

    public Integer getInt(@NonNull String key) {
        return classifiedFactory.getInt(key);
    }

    public Long getLong(@NonNull String key) {
        return classifiedFactory.getLong(key);
    }

    public String getString(@NonNull String key) {
        return classifiedFactory.getString(key);
    }

    public Serializable getSerializable(@NonNull String key) {
        return classifiedFactory.getSerializable(key);
    }

    /**
     * Put methods
     */

    public boolean put(@NonNull String key, @NonNull Boolean booleanValue) {
        return classifiedFactory.put(new QlassifiedBoolean(key, booleanValue));
    }

    public boolean put(@NonNull String key, @NonNull Float floatValue) {
        return classifiedFactory.put(new QlassifiedFloat(key, floatValue));
    }

    public boolean put(@NonNull String key, @NonNull Integer integerValue) {
        return classifiedFactory.put(new QlassifiedInteger(key, integerValue));
    }

    public boolean put(@NonNull String key, @NonNull Long longValue) {
        return classifiedFactory.put(new QlassifiedLong(key, longValue));
    }

    public boolean put(@NonNull String key, @NonNull String stringValue) {
        return classifiedFactory.put(new QlassifiedString(key, stringValue));
    }

    public boolean put(@NonNull String key, @NonNull Serializable serializableValue) {
        return classifiedFactory.put(new QlassifiedSerializable(key, serializableValue));
    }
}
