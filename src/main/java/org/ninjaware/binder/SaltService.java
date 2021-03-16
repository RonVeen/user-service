package org.ninjaware.binder;

import javax.inject.Singleton;
import java.security.SecureRandom;

@Singleton
public class SaltService {

    private SecureRandom random = new SecureRandom();

    public byte[] generate() {
        byte[] salt = new byte[16];
        return salt;
    }
}
