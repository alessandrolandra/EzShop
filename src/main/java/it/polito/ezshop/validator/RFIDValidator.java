package it.polito.ezshop.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RFIDValidator {
	/**
	 * The RFID must be a string of 12 digits
	 * @param RFID: RFID String to be validated
	 * @return boolean: true if string is valid, false if not
	 */
	public static boolean validate(String RFID) {
		if(RFID == null || RFID.length() != 12)
			return false;
		String regex = "[0-9]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(RFID);
		return m.matches();
	}
}
