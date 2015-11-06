package com.q42.qlassified.Provider;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class QlassifiedCrypto {

    public static final String CHARSET = "UTF8";
    public static final String ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String ANDROID_MODE = "AndroidOpenSSL";
    public static final int BASE64_MODE = Base64.DEFAULT;

    public String encrypt(String input, RSAPublicKey publicKey) {

        if (input == null) {
            return null;
        }

        try {
            byte[] dataBytes = input.getBytes(CHARSET);
            Cipher cipher = Cipher.getInstance(ALGORITHM, ANDROID_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(cipher.doFinal(dataBytes), BASE64_MODE);
        } catch (IllegalBlockSizeException |
                BadPaddingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                UnsupportedEncodingException |
                InvalidKeyException |
                NoSuchProviderException e) {
            Log.e("QlassifiedCrypto", String.format("Could not encrypt this string. Stacktrace: %s", e));
            return null;
        }
    }

    public String decrypt(String input, RSAPrivateKey rsaPrivateKey) {

        if (input == null) {
            return null;
        }

        try {
            byte[] dataBytes = Base64.decode(input, BASE64_MODE);
            Cipher cipher = Cipher.getInstance(ALGORITHM, ANDROID_MODE);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            return new String((cipher.doFinal(dataBytes)));
        } catch (IllegalBlockSizeException |
                BadPaddingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                NoSuchProviderException e) {
            Log.e("QlassifiedCrypto", String.format("Could not decrypt this string. Stacktrace: %s", e));
            return null;
        }
    }
}
