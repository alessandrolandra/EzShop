package it.polito.ezshop.validator;

public class BarCodeValidator {
	/**
	 * validate the barCode, following the GTIN-12, GTIN-13 and GTIN-14 standard
	 * at the end the last digit of the code (check digit) should be equal to the difference between the smallest 
	 * multiple of 10 greater or equal to the weighted sum of all the other barCode digits and the sum itself  
	 * @param barCode
	 * @return
	 */
	public static boolean validate(String barCode) {
		String pattern = "[0-9]{12,14}";
		if(!barCode.matches(pattern)) {
			return false;
		}
		int sum=0;
		int currentDigit;
		for(int i=0;i<barCode.length()-1;i++) {
			currentDigit = barCode.charAt(barCode.length()-2-i)-'0';
			if(i%2==0) {
				currentDigit*=3;
			}
			sum+=currentDigit;
		}
		return ((int)(Math.ceil(sum/10.0)*10-sum))==(barCode.charAt(barCode.length()-1)-'0');
	}
}
