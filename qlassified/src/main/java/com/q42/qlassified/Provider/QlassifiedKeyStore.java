package com.q42.qlassified.Provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.q42.qlassified.Entry.*;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

@TargetApi(18)
public class QlassifiedKeyStore implements QlassifiedSecurity {

    public static final String ANDROID_KEYSTORE_INSTANCE = "AndroidKeyStore";
    public static final String TYPE_DELIMITER = "|";

    private final KeyStore keyStoreInstance;
    private final QlassifiedCrypto crypto;
    private final Context context;

    public QlassifiedKeyStore(Context context) throws
            KeyStoreException,
            CertificateException,
            NoSuchAlgorithmException,
            IOException {

        keyStoreInstance = java.security.KeyStore.getInstance(ANDROID_KEYSTORE_INSTANCE);
        // Weird artifact of Java API.  If you don't have an InputStream to load, you still need
        // to call "load", or it'll crash.
        keyStoreInstance.load(null);

        // Hold on to the context, we need it to fetch the key
        this.context = context;
        // Create the crypto instance
        crypto = new QlassifiedCrypto();
    }

    /**
     * Creates a public and private key and stores it using the Android Key Store, so that only
     * this application will be able to access the keys.
     */
    private void createKeys() throws
            NoSuchProviderException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException {

        String alias = getUniqueDeviceId(this.context);
        KeyPairGenerator keyPairGenerator;

        /**
         * On Android Marshmellow we can use new security features
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE_INSTANCE);

            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            alias,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .setAlgorithmParameterSpec(new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4))
                            .build());
        /**
         * On versions below Marshmellow but above Jelly Bean, use the next best thing
         */
        } else {

            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.ERA, 1);

            KeyPairGeneratorSpec keyPairGeneratorSpec =
                    new KeyPairGeneratorSpec.Builder(context)
                            // You'll use the alias later to retrieve the key.  It's a key for the key!
                            .setAlias(alias)
                                    // The subject used for the self-signed certificate of the generated pair
                            .setSubject(new X500Principal("CN=" + alias))
                                    // The serial number used for the self-signed certificate of the
                                    // generated pair.
                            .setSerialNumber(BigInteger.valueOf(1337))
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();

            keyPairGenerator = KeyPairGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE_INSTANCE);
            keyPairGenerator.initialize(keyPairGeneratorSpec);
        /**
         * On versions below that...
         * Well we're sorry but you don't get a fancy encryption baby...
         */
        }

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Log.d("KeyStore", String.format("Public key: %s", keyPair.getPublic()));
        Log.d("KeyStore", String.format("Private key: %s", keyPair.getPrivate()));
    }

    /**
     * Amazing method from the interwebs, found here: http://stackoverflow.com/a/2853253
     * This method ensures a unique identifier for each Android device
     * @param context {Context} The application context
     * @return A string unique to the specific device
     */
    private String getUniqueDeviceId(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public EncryptedEntry encryptEntry(QlassifiedEntry classifiedEntry) {
        return new EncryptedEntry(classifiedEntry.getKey(), encrypt(String.format(
                String.format("%s%s%s", "%s", TYPE_DELIMITER, "%s"),
                classifiedEntry.getValue(),
                classifiedEntry.getType().name())));
    }

    public QlassifiedEntry decryptEntry(EncryptedEntry entry) {
        final String decryptedString = decrypt(entry.getEncryptedValue());
        if(decryptedString == null) {
            return null;
        }
        final Integer splitPosition = decryptedString.lastIndexOf(TYPE_DELIMITER);
        if(splitPosition == -1) {
            return null;
        }
        final String decryptedType = decryptedString.substring(splitPosition + 1);
        final String decryptedValue = decryptedString.substring(0, splitPosition);

        switch (QlassifiedEntry.Type.valueOf(decryptedType)) {
            case BOOLEAN: return new QlassifiedBoolean(decrypt(entry.getKey()), Boolean.valueOf(decryptedValue));
            case FLOAT: return new QlassifiedFloat(decrypt(entry.getKey()), Float.valueOf(decryptedValue));
            case INTEGER: return new QlassifiedInteger(decrypt(entry.getKey()), Integer.valueOf(decryptedValue));
            case LONG: return new QlassifiedLong(decrypt(entry.getKey()), Long.valueOf(decryptedValue));
            default: return new QlassifiedString(decrypt(entry.getKey()), decryptedValue);
        }
    }

    private boolean checkKeyAvailability() {

        String alias = getUniqueDeviceId(this.context);
        // Create keys based on the unique device identifier
        try {
            if(!keyStoreInstance.containsAlias(alias)) {
                createKeys();
            }
            return true;
        } catch (KeyStoreException |
                NoSuchProviderException |
                NoSuchAlgorithmException |
                InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            Log.e("QlassifiedKeyStore", String.format("Could not create a KeyStore instance. Stacktrace: %s", e));
            return false;
        }
    }

    private String encrypt(String input) {

        if(!checkKeyAvailability()) {
            return null;
        }

        String alias = getUniqueDeviceId(this.context);
        try {
            final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStoreInstance.getEntry(alias, null);
            final RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            return crypto.encrypt(input, publicKey);
        } catch (NoSuchAlgorithmException |
                UnrecoverableEntryException |
                KeyStoreException e) {
            Log.e("QlassifiedKeyStore", String.format("Could not encrypt this string. Stacktrace: %s", e));
            return null;
        }
    }

    private String decrypt(String input) {

        if(!checkKeyAvailability()) {
            return null;
        }

        String alias = getUniqueDeviceId(this.context);
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStoreInstance.getEntry(alias, null);
            PrivateKey privateKey =  privateKeyEntry.getPrivateKey();
            return crypto.decrypt(input, privateKey);
        } catch (NoSuchAlgorithmException |
                UnrecoverableEntryException |
                KeyStoreException e) {
            Log.e("QlassifiedKeyStore", String.format("Could not decrypt this string. Stacktrace: %s", e));
            return null;
        }
    }
}
