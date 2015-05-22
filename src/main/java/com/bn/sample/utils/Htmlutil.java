package com.bn.sample.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chen-pc on 2015/3/11.
 */
public class Htmlutil
{
    public static StringBuffer decodeUnicode(String dataStr) {
        dataStr=dataStr.replace("&#x", "\\u");
        final StringBuffer buffer = new StringBuffer();
        String tempStr = "";
        String operStr = dataStr;
        if (operStr != null && operStr.indexOf("\\u") == -1)
            return buffer.append(operStr);
        if (operStr != null && !operStr.equals("")
                && !operStr.startsWith("\\u")) {
            tempStr = operStr.substring(0, operStr.indexOf("\\u"));
            operStr = operStr.substring(operStr.indexOf("\\u"), operStr
                    .length());
        }
        buffer.append(tempStr);
        while (operStr != null && !operStr.equals("")
                && operStr.startsWith("\\u")) {
            tempStr = operStr.substring(0, 6);
            operStr = operStr.substring(7, operStr.length());
            String charStr = "";
            charStr = tempStr.substring(2, tempStr.length());
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(new Character(letter).toString());
            if (operStr.indexOf("\\u") == -1) {
                buffer.append(operStr);
            } else {
                tempStr = operStr.substring(0, operStr.indexOf("\\u"));
                operStr = operStr.substring(operStr.indexOf("\\u"), operStr
                        .length());
                buffer.append(tempStr);
            }
        }
        return buffer;
    }
    public static String getHtmlString(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection ucon = url.openConnection();
            InputStream instr = ucon.getInputStream();
//            Log.d("urlurl",url.toString());
            BufferedInputStream bis = new BufferedInputStream(instr);
            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            return EncodingUtils.getString(baf.toByteArray(), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

}
