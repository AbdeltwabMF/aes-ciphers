package utility;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyManager {
    private final KeyGenerator keyGenerator;

    public KeyManager() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(128);
    }

    public byte[] generateKey(){
       return this.keyGenerator.generateKey().getEncoded();
    }
}
