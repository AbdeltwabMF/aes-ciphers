package ciphers;

public class Validate {
  public static boolean checkHex(String key) {
    return key.matches("^[0-9a-f-A-F]+$");
  }

  public static byte[] hexToByte(String hex) {
    if ((hex.length() % 2) != 0) {
      throw new IllegalArgumentException("Invalid hex string (length % 2 != 0)");
    }
    byte[] bytes = new byte[hex.length() / 2];
    for (int i = 0, bytesIndex = 0; i < hex.length(); i += 2, ++bytesIndex) {
      bytes[bytesIndex] = Integer.valueOf(hex.substring(i, i + 2), 16).byteValue();
    }
    return bytes;
  }

  public static String byteToHexString(byte[] bytes) {
    String sb = "";
    for (byte b : bytes)
      sb += String.format("%02x", b);
    return sb;
  }
}