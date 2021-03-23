package nl.tudelft.oopp.demo.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static String encryptionKey = "fl;akjp[0cxioslakdmasdkljop[cvi,m.npcvzxca.lk";

    /**
     * Code found on http://www.adeveloperdiary.com/java/how-to-easily-encrypt-and-decrypt-text-in-java/
     * Methods returns encrypted string
     * @param strClearText String to encrypt
     * @return  Encrypted String
     */
    public static String encrypt(String strClearText) {
        String strData = "";

        try {
            SecretKeySpec skeyspec = new SecretKeySpec(encryptionKey.getBytes(),"Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted = cipher.doFinal(strClearText.getBytes());
            strData = new String(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    /**
     * Code found on http://www.adeveloperdiary.com/java/how-to-easily-encrypt-and-decrypt-text-in-java/
     * Methods returns Decrypted string
     * @param strEncrypted String to decrypt
     * @return  Decrypted String
     */
    public static String decrypt(String strEncrypted) {
        String strData = "";

        try {
            SecretKeySpec skeyspec = new SecretKeySpec(encryptionKey.getBytes(),"Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted = cipher.doFinal(strEncrypted.getBytes());
            strData = new String(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }
}
