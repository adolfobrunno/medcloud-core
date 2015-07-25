package com.medcloud.core.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;


public class ImageUtils {
	
	public static String getBase64(InputStream is) throws IOException{
		
		byte[] bytes = IOUtils.toByteArray(is);
		byte[] base64 = Base64.encodeBase64(bytes);
		
		return new String(base64);
	}

}
