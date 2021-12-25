package ciphers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CTR {
  public byte[] encrypt(byte[] key, byte[] plaintText)
    throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidKeyException, IllegalBlockSizeException,
    BadPaddingException, InvalidAlgorithmParameterException {

    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

    byte[] IV = new byte[16];
    SecureRandom random = new SecureRandom();
    random.nextBytes(IV);

    IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
    byte[] cipherText = cipher.doFinal(plaintText);

    /** Return IV + Cipher combined text  */
    byte[] ivAndCipherCombinedText = combineIvAndCipherText(IV, cipherText);

    return ivAndCipherCombinedText;
  }

  public byte[] decrypt(byte[] key, byte[] cipherText)
    throws NoSuchPaddingException, NoSuchAlgorithmException,
    InvalidKeyException, IllegalBlockSizeException,
    BadPaddingException, InvalidAlgorithmParameterException {

    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

    /** Separate the header of encrypted file which is the IV from the cipher body */
    byte[] IV = new byte[16];
    for (int i = 0; i < 16; ++i) {
      IV[i] = cipherText[i];
    }
    byte[] cipherTextBody = new byte[cipherText.length - 16];
    for (int i = 16; i < cipherText.length; ++i) {
      cipherTextBody[i - 16] = cipherText[i];
    }

    IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
    byte[] plaintText = cipher.doFinal(cipherTextBody);

    return plaintText;
  }

  /**
   * Adding IV vector at the header of the encrypted text
   */
  private byte[] combineIvAndCipherText(byte[] IV, byte[] encryptedText) {
    byte[] combinedText = new byte[16 + encryptedText.length];

    for (int i = 0; i < 16; ++i) {
      combinedText[i] = IV[i];
    }
    for (int i = 0; i < encryptedText.length; ++i) {
      combinedText[i + 16] = encryptedText[i];
    }

    return combinedText;
  }
}
