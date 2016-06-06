package com.ash.test;

import com.ash.util.LoginWindow;
import com.ash.util.ServerConnection;

public class Test3 {
	
	public String s = "hallo";
	public int testValue = 10;
	
	public static void main(String[] args) throws Exception {
		/*ServerConnection s = new ServerConnection();
		s.getAllUsers().forEach((u)->{
			System.out.println(u.name);
		});*/
		//System.out.println(s.getValue(u, "passwordHash"));
		
		//System.out.println(DebugHelper.toFieldName(Test3.class, 10));
		
		LoginWindow lw = new LoginWindow();
		ServerConnection.User user = lw.login();
		ServerConnection sc = new ServerConnection();
		System.out.println(user.name);
		System.out.println(sc.getValue(user, "passwordHash").substring(0,10) + "...");
		System.out.println(sc.getValue(user, "email").trim());
		System.out.println(sc.getValue(user, "premium"));
	}
}
