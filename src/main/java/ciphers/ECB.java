package ciphers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ECB {
  public byte[] encrypt(byte[] key, byte[] plaintText)
    throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    byte[] cipherText = cipher.doFinal(plaintText);

    return cipherText;
  }

  public byte[] decrypt(byte[] key, byte[] cipherText)
    throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidKeyException, IllegalBlockSizeException,
    BadPaddingException {

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    byte[] plaintText = cipher.doFinal(cipherText);

    return plaintText;
  }
}
