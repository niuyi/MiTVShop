/**
 *   Copyright(c) 2012 DuoKan TV Group
 *
 *   Security.java
 *
 *   @author tianli (tianli@duokan.com)
 *
 *   @date 2012-6-24
 */
package com.xiaomi.mitv.shop.network;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author tianli
 */
public class Security {
	
    public static final String SECRET_TOKEN = "f655b8b7d6be4ac1bfcded8198ef8a1f";
    public static final String SECRET_KEY = "25cc3b3900314efe973b5a4be39d5745";
    
//    private static final String HMAC_SHA1 = "HmacSHA1";
    
    private static final String HMAC_SHA1 = "HmacSHA1";
    
    public static String signature(byte[] data, byte[] key, String algorithm) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);
            byte[] bytes = mac.doFinal(data);
            return Hex.byte2Hex(bytes);
        }catch(Exception e){
        }
        return null;
    }
    
    public static String signature(byte[] data, byte[] key) {
        return signature(data, key, HMAC_SHA1);
    }
   
}
