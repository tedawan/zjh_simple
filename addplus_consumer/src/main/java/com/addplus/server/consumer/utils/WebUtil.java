package com.addplus.server.consumer.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lbj on 16/6/28.
 */
public class WebUtil {

    public static  String inputStreamString (InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[]   b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;)   {
            out.append(new String(b, 0, n,"UTF-8"));
        }
        return   out.toString();
    }
}
