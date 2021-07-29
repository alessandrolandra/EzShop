package it.polito.ezshop.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoyaltyCardCodeValidator {
	/**
	 * The card code must be a string of 10 digits
	 * @param loyaltyCardCode: loyaltyCardCode String to be validated
	 * @return boolean: true if string is valid, false if not
	 */
	public static boolean validate(String loyaltyCardCode) {
		if(loyaltyCardCode == null || loyaltyCardCode.length() > 10 || loyaltyCardCode.length() < 10)
			return false;
		try {
			String regex = "[0-9]+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(loyaltyCardCode);
			if(m.matches())
				Long.parseLong(loyaltyCardCode);
			else
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
