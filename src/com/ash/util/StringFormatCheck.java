package com.ash.util;

import com.ash.util.math.Comparison;

/**
 * Custon string format supplier.<br>
 * Valid formatting:<br>
 * '@': any letter or number<br>
 * '§': only letters<br>
 * '$': only numbers
 * @author Aaron Hilbig
 *
 */
public class StringFormatCheck {
	
	public static boolean notEmpty = true;
	
	public static final String FORMAT_DATE = "$$.$$.$$$$";
	public static final String FORMAT_TIME = "$$:$$";
	public static final String FORMAT_IPv4 = "$$$.$$$.$$$.$$$";
	
	/**
	 * Checks if String s is formatted like format<br>
	 * Valid formatting:<br>
	 * '@': any letter or number<br>
	 * '§': only letters<br>
	 * '$': only numbers
	 * 
	 * @param s
	 * @param format
	 * @return
	 * @throws FormatException 
	 */
	public static boolean validFormat(String s, String format){
		try {
			return validFormatException(s,format);
		} catch (FormatException e) {
			e.printStackTrace();
			return false;
		}
		/*boolean valid = true;
		for(int i = 0; i<s.length(); i++){
			String org = "" + s.charAt(i);
			String form;
			try {
				form = "" + format.charAt(i);
			} catch(StringIndexOutOfBoundsException e){
				return true;
			}
			if(form.equals("@"))
				valid = true;
			else if(form.equals("§") && !Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals("$") && Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals(org))
				valid = true;
			else return false;
		}
		return valid;*/
	}
	
	/**
	 * Checks if String s is formatted like format<br>
	 * Valid formatting:<br>
	 * '@': any letter or number<br>
	 * '§': only letters<br>
	 * '$': only numbers
	 * 
	 * @param s
	 * @param format
	 * @return
	 * @throws FormatException 
	 */
	public static boolean validFormatException(String s, String format) throws FormatException{
		boolean valid = true;
		if(s.equals("") && !format.equals("") && notEmpty)
			throw new FormatException("String can't be empty unless StringFormatCheck.notEmpty is set to true");
		for(int i = 0; i<s.length(); i++){
			String org = "" + s.charAt(i);
			String form;
			try {
				form = "" + format.charAt(i);
			} catch(StringIndexOutOfBoundsException e){
				return true;
			}
			if(form.equals("@"))
				valid = true;
			else if(form.equals("§") && !Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals("$") && Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals(org))
				valid = true;
			else
				throw new FormatException("Invalid format at index " + i + ": Allowed format: " + form + " (" + (form.equals("§") ? "Any letter, no numbers" : form.equals("$") ? "Only numbers" : ("Can only be '" + form + "'")) + ")");
		}
		return valid;
	}
	
	/**
	 * FormatException
	 * @author Aaron Hilbig
	 *
	 */
	public static class FormatException extends Exception {
		private static final long serialVersionUID = 1L;
		public FormatException(String s){
			super(s);
		}
	}
	
	/**
	 * Converts format to a nice descriptive String<br>
	 * @param format
	 * @return
	 */
	public static String toReadableFormat(String format){
		String ret = "";
		int number = 0;
		for(int i = 0; i<format.length(); i++){
			String form = "" + format.charAt(i);
			if(form.equals("@")){
				String a = Comparison.alphabet[i*2];
				ret += a;
			} else if(form.equals("§")){
				String a = Comparison.alphabet[i*2+1];
				ret += a;
			}
			else if(form.equals("$")){
				number++;
				if(number==10)
					number = 1;
				ret += number;
			}
			else ret += form;
		}
		return ret;
	}
	
}
