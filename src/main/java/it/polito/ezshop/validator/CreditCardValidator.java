package it.polito.ezshop.validator;

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

public class CreditCardValidator {
	
	public static boolean validate(String cardNumber) {
		return LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(cardNumber);
	}
}
