package ciphers;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyManager {
  private final KeyGenerator keyGenerator;
  private int keySize;
  private final String ALGORITHM = "AES";
  private byte[] symmetricKey;

  public KeyManager(int keySize) throws NoSuchAlgorithmException {
    this.keyGenerator = KeyGenerator.getInstance(this.ALGORITHM);
    setKeySize(keySize);
    this.keyGenerator.init(getKeySize());
    symmetricKey = generateKey();
  }

  public byte[] generateKey() {
    return this.keyGenerator.generateKey().getEncoded();
  }

  public void setSymmetricKey(byte[] loadedKey) {
    this.symmetricKey = loadedKey;
  }

  public void setKeySize(int keySize) {
    this.keySize = keySize;
  }

  public int getKeySize() {
    return this.keySize;
  }
}
