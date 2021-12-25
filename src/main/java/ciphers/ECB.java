package ciphers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ECB {
  private Cipher cipher;
  private SecretKeySpec keySpec;

  public ECB(byte[] key)
    throws NoSuchPaddingException, NoSuchAlgorithmException {

    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    keySpec = new SecretKeySpec(key, "AES");
  }

  public byte[] encrypt(byte[] plaintText)
    throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    byte[] cipherText = cipher.doFinal(plaintText);

    return cipherText;
  }

  public byte[] decrypt(byte[] cipherText)
    throws InvalidKeyException, IllegalBlockSizeException,
    BadPaddingException {

    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    byte[] plaintText = cipher.doFinal(cipherText);

    return plaintText;
  }
}
