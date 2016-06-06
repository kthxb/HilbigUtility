package com.ash.util.math;

import java.util.Arrays;

/**
 * Various functionality for comparing objects etc.
 * @author Aaron Hilbig
 *
 */
public class Comparison {
	
	public static String[] alphabet = new String[]{
			"A","a","B","b","C","c","D","d","E","e","F","f","G","g","H","h","I","i","J","j","K","k","L","l","M","m","N","n","O","o","P","p","Q","q",
			"R","r","S","s","T","t","U","u","V","v","W","w","X","x","Y","y","Z","z","Ä","ä","Ö","ö","Ü","Ü","ß"
	};
	
	public static String[] hex = new String[]{
			"A","B","C","D","E","F","0","1","2","3","4","5","6","7","8","9"
	};
	
	/**
	 * @param comp
	 * @param equ
	 * @return If one of the params in equ equals comp.
	 */
	public static boolean equalsOr(Object comp, Object... equ) {
		for(Object o : equ) {
			if(o.equals(comp))
				return true;
		}
		return false;
	}
	
	/**
	 * @param comp
	 * @param equ
	 * @return If all of the params in equ equals comp.
	 */
	public static boolean equalsAnd(Object comp, Object... equ) {
		for(Object o : equ) {
			if(!o.equals(comp))
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if test="leeel" is similar to source="lel"
	 * @param test
	 * @param source
	 * @return True if test contains/starts with same letters as source. 
	 */
	public static boolean isSimilar(String test, String source){
		boolean b = true;
		int pointInSource = 0;
		String currentCharInSource = source.substring(0,1);
		String nextCharInSource = source.substring(1,2);
		for(int i=0; i<test.length()-1; i++){
			if(test.substring(i,i+1).equalsIgnoreCase(currentCharInSource))
				b=true;
			else if(test.substring(i,i+1).equalsIgnoreCase(nextCharInSource)){
				pointInSource++;
				currentCharInSource = source.substring(pointInSource,pointInSource+1);
				if(pointInSource+2>source.length())
					nextCharInSource = null;
				else
					nextCharInSource = source.substring(pointInSource+1,pointInSource+2);
				b=true;
			} else {
				b=false;
				break;
			}
		}
		return b;
	}
	
	/**
	 * Checks if the arrays have same contents (regardless of order)<br>
	 * nullsAreEqual: if this is true, null==null -> true.<br>
	 * ignoreLength: If length should be ignored. If this value is true, the first array (o)<br>
	 * will be the 'leading array': if the other array contains all objects of the leading array,<br>
	 * the function returns true.
	 * @param o
	 * @param p
	 * @param nullsAreEqual
	 * @param ignoreLength
	 * @return
	 */
	public static boolean arrayEquals(Object[] o, Object[] p, boolean nullsAreEqual, boolean ignoreLength){
		if(Arrays.equals(o, p))
			return true;
		if(!ignoreLength && o.length!=p.length)
			return false;
		for(Object subO : o){
			if(!contains(subO, p, nullsAreEqual))
				return false;
		}
		return true;
	}
	
	public static boolean contains(Object o, Object[] array, boolean nullsAreEqual){
		for(Object o1 : array){
			if(o1==null && o==null && nullsAreEqual)
				return true;
			if(o1!=null && o!=null && o.equals(o1))
				return true;
		}
		return false;
	}
}
