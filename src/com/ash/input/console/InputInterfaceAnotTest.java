package com.ash.input.console;

import com.ash.input.ClassInputInterfaceGenerator;
import com.ash.input.UserParameter;

@Deprecated
public class InputInterfaceAnotTest {
	
	@UserParameter
	public static String hallo;
	
	@UserParameter
	public static int integer;
	
	@UserParameter
	public static boolean diesdas;
	
	public static void main(String[] args) {
		try {
			ClassInputInterfaceGenerator.classToInputInterface(InputInterfaceAnotTest.class, "TestKlasse", "").open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
