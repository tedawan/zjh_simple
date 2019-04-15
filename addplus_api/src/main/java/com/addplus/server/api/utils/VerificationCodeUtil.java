package com.addplus.server.api.utils;

import java.util.Random;

/**
 * 类描述:验证码生成工具类
 *
 * @author Jerry
 * @version V1.0
 * @date 2016/7/6 18:27
 */
public class VerificationCodeUtil {

    /**
     * 方法描述:六位数字验证码
     *
     * @author Jerry
     * @version V1.0
     * @date 2016/7/6 19:24
     */
    public static String getVerificationCode() {
        Random rad = new Random();
        String result = rad.nextInt(1000000) + "";
        if (result.length() != 6) {
            return getVerificationCode();
        }
        return result;
    }

    /**
     * 方法描述:任意正整数位数字验证码
     *
     * @author Jerry
     * @version V1.0
     * @date 2016/7/6 19:52
     */
    public static String getVerificationCode(int num) {
        Random rad = new Random();
        final String empty="";
        if (num <= 0) {
            return "";
        }
        String result = rad.nextInt(Integer.parseInt(String.valueOf(Math.pow(10, num)))) + "";
        if (result.length() != num) {
            return getVerificationCode(num);
        }
        return result;
    }

}
