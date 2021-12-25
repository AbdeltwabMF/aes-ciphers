package ciphers;

public class Hex {
  public static String toHex(String key) throws Exception {
    if (!key.matches("^[0-9a-f-A-F]+$")) {
      throw new Exception("Key must consist of HEX numbers only!");
    }
    return key;
  }
}
