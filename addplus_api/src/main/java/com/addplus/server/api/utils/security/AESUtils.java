package com.addplus.server.api.utils.security;



import com.addplus.server.api.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils
{
    //utf后是addplus+addplus+
    private final static byte[] iv = { 97, 100, 100, 112, 108, 117, 115, 43, 97, 100, 100, 112, 108, 117, 115, 43};

    private final static byte[] iv2 = { 43, 115, 117, 108, 112, 100, 100, 97, 43, 115, 117, 108, 112, 100, 100, 97};

    private final static String AEC_MODEL="AES/CBC/PKCS5Padding";

    public static String encryptAES(String encryptString, String encryptKey,Integer type) {
        try {
            IvParameterSpec zeroIv=null;
            if (type==0){
                zeroIv = new IvParameterSpec(iv);
            }else{
                zeroIv = new IvParameterSpec(iv2);
            }
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance(AEC_MODEL);
            cipher.init(Cipher.ENCRYPT_MODE, key,zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));

            return Base64.encode(encryptedData).replaceAll(" ","");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptAES(String decryptString, String decryptKey,Integer type)  {
        try {
            byte[] byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = null;
            if (type==0){
                zeroIv = new IvParameterSpec(iv);
            }else{
                zeroIv = new IvParameterSpec(iv2);
            }
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance(AEC_MODEL);
            cipher.init(Cipher.DECRYPT_MODE, key,zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);
            return new String(decryptedData,"utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args){
        String ecstring =encryptAES("{\"id\":2,\"token\":\"5B27DF450355973C15AC33D7|MTUyOTM0MDc5MDAwMC41YjI3ZGY0NjAzNTU5NzNjMTVhYzMzZDg=\"}","135012fd0e631b5c",1);
        System.out.println("密文："+ecstring.replaceAll(" +",""));
        long startTime = System.currentTimeMillis();
        System.out.println(decryptAES("" +
                "0CLOhr4mRK3zjWCsMnw9TRzQq2P1p7LWuTDQkO86B9TM7GLkueBdQNaHH7Yfc543_Aayupqhafe/hnEyoAOs1gUTs5_LGmQ8wFg_/cqqabzvDFB2yOn8Vum6 AHzvljCX0sBSwuNyhdVzt985Td8ROQ==","f5993492c8a0d66e",1));
        System.out.println(System.currentTimeMillis()-startTime);
    }
}
