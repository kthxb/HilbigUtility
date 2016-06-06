package com.ash.test;

import java.io.Serializable;

import com.ash.util.files.FileManager;

public class Test4 implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public String name = "";
	public int version = 0;
	
	public static void main(String[] args) {
		Test4 t = new Test4();
		//t.name = "Test4 - Klasse";
		//t.version = 2016;
		//FileManager.serializeAndWriteToFile("D:\\Programmieren\\Test4.obj", t);
		t = FileManager.deserialize("D:\\Programmieren\\Test4.obj", t);
		System.out.println(t.name);
		System.out.println(t.version);
	}

}
