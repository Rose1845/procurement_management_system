package com.rose.procurement.utils;

//import org.apache.tomcat.util.net.openssl.ciphers.MessageDigest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5UserAvatar {
    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String generateAvatar (String email) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");

            String msg =  hex (md.digest(email.getBytes("CP1252")));
            return "https://gravatar.com/avatar/"+msg+"?d=mp";
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
