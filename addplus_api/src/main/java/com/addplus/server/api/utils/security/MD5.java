package com.addplus.server.api.utils.security;

import java.security.MessageDigest;

/**
 * Created by ljt on 2017/2/15.
 */
public class MD5 {
    /*
     * 首先初始化一个字符数组，用来存放每个16进制字符
     */
    private final static char hexDigits[]={
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F'
    };

    /**
     * 加密
     * @param s
     * @return
     */
    public final static String encoderByMd5(String s) {

        try {
            // 输入的字符串转换成字节数组
            byte[] btInput = s.getBytes();
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // btInput是输入字符串转换得到的字节数组
            mdInst.update(btInput);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] md = mdInst.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(md);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    public static void main(String[] args) {
        System.out.println(encoderByMd5("ainiugu_djc"));
    }
}
