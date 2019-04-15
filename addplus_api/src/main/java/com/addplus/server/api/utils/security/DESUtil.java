package com.addplus.server.api.utils.security;



import com.addplus.server.api.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil
{
    private static byte[] iv = { 97, 100, 100, 112, 108, 117, 115, 43,97, 100, 100, 112, 108, 117, 115, 43};

    public static String encryptDES(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key,zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));

            return Base64.encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptDES(String decryptString, String decryptKey)  {
        try {
            byte[] byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key,zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);
            return new String(decryptedData,"utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args){
        String ecstring =encryptDES("1529415916000.post.businessmodule.UserService.getUserById","0355973c15ac33d7");
        System.out.println("密文："+ecstring.replaceAll(" +",""));
        long startTime = System.currentTimeMillis();
        System.out.println(decryptDES("5XGlfkfbjVFLTVs9BzGVoKLXwQV2wOmwgLe6gId7VoWcz5qUMuezJdYXTDliJYHcp0qdsgwME8jSeUjCfDs4Lw==","0355973c15ac33d7"));
        System.out.println(System.currentTimeMillis()-startTime);
    }
}
