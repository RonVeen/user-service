package org.ninjaware.binder;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Singleton
public class HashService {

    private static final int ITERATIONS = 10_000;
    private static final int KEY_LENGTH = 512;

    public String hash(final char[] password, final byte[] salt) {
        return hash(password, salt, ITERATIONS);
    }


    public String hash(final char[] password, final byte[] salt, final int iterations) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            String result = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            return result;
        } catch (NoSuchAlgorithmException  | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
