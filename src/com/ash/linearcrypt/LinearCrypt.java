package com.ash.linearcrypt;

/**
 * 
 * Provides simple character de-/encryption.<br>Use cypher instead.
 * 
 * @author Aaron Hilbig
 *
 */
@Deprecated
public class LinearCrypt {
	
	public static void main(String[] args) {
		System.out.println(encrypt("v}pl",7));
	}
	
	public static String crypt(String s, int seed){
		String crypt = "";
		for(char c : s.toCharArray())
		{
			crypt += Character.toString((char)(Character.valueOf(c) + seed));
		}
		return crypt;
	}
	
	public static String encrypt(String s, int seed){
		String encrypt = "";
		for(char c : s.toCharArray())
		{
			encrypt += Character.toString((char)(Character.valueOf(c) - seed));
		}
		return encrypt;
	}
	
	
}
