package com.androcid.zomato.util;
import android.telephony.PhoneNumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation Class
 */
public class Validate {
	
	/**
	 * Min Length of password
	 */
	public static final int PASSWORDLENGTH = 5;
	
	public static boolean emailValidator(String email)
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	
	public static boolean isCharacter(String text)
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[\\p{L} .'-]+$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(text);
	    return matcher.matches();
	}
	
	public static boolean isNumber(String text)
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "[0-9]+";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(text);
	    return matcher.matches();
	}
	
	public static boolean isValidEmail(String target) {
	    if (target.equals("")) {
	        return false;
	    } else {
	    	
	    	if(android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches())
	    	{
	    		String topLevelDomain = target.substring(target.lastIndexOf(".") + 1);
	    		//Log.e(TAG, "topLevelDomain : "+topLevelDomain);
	    		if(android.util.Patterns.TOP_LEVEL_DOMAIN.matcher(topLevelDomain).matches())
	    		{
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	}
	

	public static boolean isValidWebsite(String target) {
	    if (target.equals("")) {
	        return false;
	    } else {
	    	
	    	if(android.util.Patterns.WEB_URL.matcher(target).matches())
	    	{
	    		return true;
	    	}
	    	return false;
	    }
	}
	
	public static boolean isValidPhone(String target) {
	    if (target.equals("")) {
	        return false;
	    }
	    else if(target.length()!=10){
	    	return false;
	    }
	    else {
	    	return PhoneNumberUtils.isGlobalPhoneNumber(target);
	    }
	}
	
	public static boolean isAtleastValidLength(String target, int targetLength) {
	    if (target.equals("")) {
	        return false;
	    } else {
	    	if(target.length()>=targetLength)
	    		return true;
	    	else
	    		return false;
	    }
	}
	
	public static boolean isMaxLength(String target, int targetLength) {
	    if (target.equals("")) {
	        return false;
	    } else {
	    	if(target.length()<=targetLength)
	    		return true;
	    	else
	    		return false;
	    }
	}
	
	public static boolean isLength(String target, int targetLength) {
	    if (target.equals("")) {
	        return false;
	    } else {
	    	if(target.length()==targetLength)
	    		return true;
	    	else
	    		return false;
	    }
	}
	
	public static boolean isNotEmpty(String target) {
	    return isAtleastValidLength(target, 1);
	}
	
	public static boolean isValueBetween(String val, int start, int end) {
		String tVal = val;
		
		if(tVal.contains("."))
		{
			val = tVal.split("\\.")[0];
		}
		try {
            int value = Integer.parseInt(val);
            if(value>=start && value<=end)
                return true;
            return false;
        }catch(Exception e){
            return false;
        }
	}
	
	public static boolean isDecimals(String string) {
	    return string.matches("^\\d+\\.\\d+$");
	  }
	
	public static boolean isNumberWith2Decimals(String string) {
	    return string.matches("^\\d+\\.\\d{2}$");
	  }
	
}
