package utility;

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

public class CBC {

    public byte[] encrypt(byte[] key, byte[] plaintText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        return cipher.doFinal(plaintText);
    }

    public byte[] decrypt(byte[] key, byte[] plaintText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        return cipher.doFinal(plaintText);
    }
}
