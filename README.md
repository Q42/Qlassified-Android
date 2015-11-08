[![Qlassified][qlassified-image]][qlassified-url]

# Qlassified Android Library
__An Android Wrapper Library for easy Keystore Encryption__

[![Build Status][travis-image]][travis-url]

## Storing sensitive data

We all need to store sensitive data now and again. On Android, keeping your data secure can be a real pain. Because even when you create a key and a salt and encrypt your data with it, where are you going to put this key and salt? The APK can be pulled apart which reveals your secrets for all the world to see. Ever since API 18, Android made their Keystore available through a Public API. This should be the place where you can easily store this sensitive data. But guess what, the Keystore is also quite a drag to use... So this library aims to make this all a lot easier and more accessible. 

## Getting started

Add the gradle dependency to your application.

```java
compile 'com.q42:qlassified:0.1.0'
```

The library is available on both jCenter and Maven Central so you can use whichever. Whatever floats your boat!

## Basic usage

To access your Application specific Keystore, Qlassified needs your application context. Therefore, even though the entry to this library is easy and static, we still need to instantiate it. Furthermore, we need a place to store the encrypted data. This library includes a Shared Preference service which can be used, but you can also [Roll your own Custom Storage Service](https://github.com/Q42/Qlassified-Android#custom-storage-service).

```java
// Initialize the Qlassified service
Qlassified.Service.start(this);
Qlassified.Service.setStorageService(new QlassifiedSharedPreferencesService(this, "My Amazing Secure App"));

// Save a key/value pair encrypted
Qlassified.Service.put("SomeKey", "SomeValue");

// Get (and decrypt) a value by its key
Qlassified.Service.getString("SomeKey");
```

As you can see, it's pretty easy to save and secure your key/value paired data.

__[An example project](https://github.com/Q42/Qlassified-Android/tree/master/example), using the above Shared Preference Service is available in this repository__

## Return values

The put method will return the primitive type boolean representing whether or not the data could be saved.

When the data could not be fetched through the get method, for any reason whatsoever, it will return null no matter what data type is expected. All get methods return non-primitive types.

## Data types

When putting data in, the library takes care of saving the data type with the data so it can be returned as the right type. When getting the data back, you will need to address the right method containing the name of the data type. E.G. getString("key") or getBoolean("key").

Available data types at the moment:

* Boolean
* Float
* Integer
* Long
* String

The library uses non-primitive types so it can return a null when a value could not be fetched.

## Custom Storage Service

If you want / need to create your own storage service where the encrypted data will be stored then you can do so by extending the abstract class [QlassifiedStorageService](https://github.com/Q42/Qlassified-Android/blob/master/qlassified/src/main/java/com/q42/qlassified/Storage/QlassifiedStorageService.java). After inserting the class into the [Qlassified.Service.setStorageService()](https://github.com/Q42/Qlassified-Android/blob/master/qlassified/src/main/java/com/q42/qlassified/Qlassified.java#L63) function, your class will be receiving encrypted data through the onSaveRequest() function and is required to return this encrypted data from the onGetRequest() function.

```java
public class QlassifiedSharedPreferencesService extends QlassifiedStorageService {

    private final SharedPreferences preferences;

    public QlassifiedSharedPreferencesService(Context context, String storageName) {
        this.preferences = context.getSharedPreferences(storageName, 0);
    }

    @Override
    public void onSaveRequest(EncryptedEntry encryptedEntry) {
        // Save the given encrypted value with the given key as a reference
        Log.d("Storage", String.format("Save key: %s", encryptedEntry.getKey()));
        Log.d("Storage", String.format("Save encrypted value: %s", encryptedEntry.getEncryptedValue()));
    }

    @Override
    public EncryptedEntry onGetRequest(String key) {
        Log.d("Storage", String.format("Get by key: %s", key));
        // Fetch the saved encrypted data by the key given
        Log.d("Storage", String.format("Got encrypted value: %s", encryptedValue));
        return new EncryptedEntry(key, encryptedValue);
    }
}
```

The __[EncryptedEntry](https://github.com/Q42/Qlassified-Android/blob/master/qlassified/src/main/java/com/q42/qlassified/Entry/EncryptedEntry.java)__ class contains all the data required for this library to work. Just fill it with the data you have in your storage service, or extract data from it when receiving it in the onSaveRequest() method.

## Feature wish list

This library will keep being improved while it's being used. Below are some features I thought of which I believe can improve this Library.

* Supporting <= API 17 (right now only from API 18, data is secured)
* Debug mode
* Encrypted Keys (right now only the value is encrypted)
* Add Serializable data type (it's already sort of in the library, but not quite responding as expected)
* Fingerprint, security code and hardware backed encryption support
* Configuration builder for specific user configurations

[qlassified-url]: https://github.com/Q42/Qlassified-Android
[qlassified-image]: https://raw.githubusercontent.com/Q42/Qlassified-Android/master/qlassified.png
[travis-url]: https://travis-ci.org/Q42/Qlassified-Android
[travis-image]: http://img.shields.io/travis/Q42/Qlassified-Android.svg
