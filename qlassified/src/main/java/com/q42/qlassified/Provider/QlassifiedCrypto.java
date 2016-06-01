package com.q42.qlassified.Provider;

import android.util.Base64;
import android.util.Log;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

public class QlassifiedCrypto {

    public static final String CHARSET = "UTF8";
    public static final String ALGORITHM = "RSA/NONE/PKCS1Padding";
    public static final int BASE64_MODE = Base64.DEFAULT;

    public String encrypt(String input, RSAPublicKey publicKey) {

        if (input == null) {
            return null;
        }

        try {
            byte[] dataBytes = input.getBytes(CHARSET);
            Cipher cipher = Cipher.getInstance(ALGORITHM, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(cipher.doFinal(dataBytes), BASE64_MODE);
        } catch (IllegalBlockSizeException |
                BadPaddingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                UnsupportedEncodingException |
                InvalidKeyException e) {
            Log.e("QlassifiedCrypto", String.format("Could not encrypt this string. Stacktrace: %s", e));
            return null;
        }
    }

    public String decrypt(String input, PrivateKey privateKey) {

        if (input == null) {
            return null;
        }

        try {
            byte[] dataBytes = Base64.decode(input, BASE64_MODE);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(dataBytes));
        } catch (IllegalBlockSizeException |
                BadPaddingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException e) {
            Log.e("QlassifiedCrypto", String.format("Could not decrypt this string. Stacktrace: %s", e));
            return null;
        }
    }
}
