package org.dragonguard.server.utils;

import java.util.Base64;

public class XorEncryptionUtil {

	public final static int Key[] = {-30, -79, -68, -30, -90, -106, -30, -119, -111, -30, -104, -67, -30, -102, -73, -30, -118, -112, -30, -118, -127, -30, -82, -68, -30, -101, -125, -30, -127, -80, -30, -83, -118, -30, -66, -114, -30, -82, -71, -30, -84, -127, -30, -83, -74, -30, -75, -78, -30, -90, -73, -30, -98, -108, -30, -125, -106, -30, -103, -128, -30, -95, -123, -30, -102, -95, -30, -73, -65, -30, -110, -88, -30, -115, -102, -30, -119, -99, -30, -68, -107, -30, -95, -69, -30, -111, -109, -30, -114, -126, -30, -90, -103, -30, -120, -91, -30, -84, -110, -30, -106, -101, -30, -70, -92, -30, -68, -126, -30, -94, -113, -30, -113, -66, -30, -94, -113, -30, -107, -72, -30, -108, -67, -30, -71, -115, -30, -99, -123, -30, -76, -106, -30, -113, -120, -30, -108, -76, -30, -117, -128, -30, -71, -69, -30, -113, -90, -30, -128, -127, -30, -98, -87, -30, -74, -113, -30, -78, -69, -30, -110, -111, -30, -101, -89, -30, -83, -95, -30, -72, -97, -30, -89, -102, -30, -70, -69, -30, -70, -86, -30, -126, -128, -30, -69, -77, -30, -119, -78, -30, -85, -95, -30, -106, -107, -30, -69, -106, -30, -83, -85, -30, -75, -105, -30, -112, -124, -30, -92, -122, -30, -100, -66, -30, -72, -67, -30, -123, -96, -30, -75, -98, -30, -104, -100, -30, -84, -98, -30, -67, -101, -30, -98, -120, -30, -126, -81, -30, -85, -117, -30, -117, -104, -30, -93, -102, -30, -88, -77, -30, -100, -85, -30, -82, -85, -30, -72, -125, -30, -67, -117, -30, -98, -81, -30, -84, -106, -30, -70, -113, -30, -99, -73, -30, -90, -93, -30, -105, -102, -30, -95, -108, -30, -74, -84, -30, -117, -69, -30, -108, -124, -30, -73, -85, -30, -109, -75, -30, -69, -118};
	
    private static byte[] xor(byte[] b) {
    	int len = b.length;
    	int keyLen = Key.length;

    	byte[] result = new byte[len];
    	for(int i=0;i<len;i++) {
    		result[i] = (byte)(b[i] ^ Key[i % keyLen]);
    	}

    	return result;
    }
    
    public static String encryptXORBase64(String s) {
    	return Base64.getEncoder().encodeToString(xor(s.getBytes()));
    }
    
    public static String decryptXORBase64(String s) {
    	return new String(xor(Base64.getDecoder().decode(s)));
    }
    
    public static String XOR(String s) {
    	return new String(xor(s.getBytes()));
    }
}
