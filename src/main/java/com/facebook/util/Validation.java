package com.facebook.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.http.Part;

public class Validation {
	
	private static List<String> allowedImageTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");
    
	public static boolean email(String email) {
	  String emailFormat = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	  Pattern pattern = Pattern.compile(emailFormat);
	  return pattern.matcher(email).matches();
	}
	
	public static boolean phoneNumber(String phoneNumber) {
		return phoneNumber.length()==10 && isInteger(phoneNumber);
	}
	
	public static boolean image(Part imagePart) {
		String contentType = imagePart.getContentType();
        return allowedImageTypes.contains(contentType);
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}
