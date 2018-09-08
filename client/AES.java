//package client;
//
//import javax.crypto.*;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//
//public class AES {
//
//    private SecretKey secretKey;
//
//    public AES(String key) {
//        setSecretKey(key);
//    }
//
//    public byte[] encryptAES (byte[] str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
//        byte[] output = cipher.doFinal(str);
//
//        return output;
//    }
//
//    public byte[] decryptAES (byte[] str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
//        byte[] output = cipher.doFinal(str);
//
//        return output;
//    }
//
//    public void setSecretKey(String key) {
//        key = key.replaceAll("\\s","");
//        byte[] decodedKey = Base64.getDecoder().decode(key);
//        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//    }
//
//}
