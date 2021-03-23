package nl.tudelft.oopp.demo.Encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static String encryptionKey = "fl;akjp[0cxioslakdmasdkljop[cvi,m.npcvzxca.lk";

    public static String encrypt(String strClearText) {
        String strData="";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(encryptionKey.getBytes(),"Blowfish");
            Cipher cipher= Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(strClearText.getBytes());
            strData=new String(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    public static String decrypt(String strEncrypted,String strKey) {
        String strData="";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(encryptionKey.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted=cipher.doFinal(strEncrypted.getBytes());
            strData=new String(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }
}
