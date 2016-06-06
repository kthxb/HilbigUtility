package com.ash.util;

import net.sf.json.JSONObject;

/**
 * Provides simple JSON utility.
 * @author Aaron Hilbig
 *
 */
public class EZJSON {
	
	/**
	 * Turns a valve script file to a parsable JSON-String
	 * @param s
	 * @return
	 */
	public static String valveToJSONParsable(String s){
		s = s.replaceAll("\"\t", "\":");
		s = s.replaceAll("\"\n\t", "\"\n");
		s = s.replaceAll("\t", "");
		s = s.replaceAll("\"\"", "\",\n\"");
		s = s.replaceAll("\"\\s\"", "\",\n\"");
		s = s.replaceAll("\\{", "\\:\\{");
		s = s.replaceAll("\\}\\s\"", "\\}\\,\"");
		s = "{" + s + "}";
		
		return s;
	}
	
	/**
	 * How to use:
	 * @param lastObject -> overall Object to search in
	 * @param children sub-nodes, f.e.: /info/stats:"hello" -> "info","stats" returns "hello"
	 * @return Object
	 */
	public static Object getTree(JSONObject lastObject, String... children){
		for(String s : children){
			try {
				lastObject = (JSONObject) lastObject.get(s);
			} catch(ClassCastException e){
				Object o = lastObject.get(s);
				return o;
			}
		}
		return lastObject;
	}
	
	/**
	 * How to use:
	 * @param lastObject -> overall Object to search in
	 * @param children sub-nodes, f.e.: /info/stats:"hello" -> "info","stats" returns "hello"
	 * @return JSONObject for further search
	 */
	public static JSONObject getTreeJSON(JSONObject lastObject, String... children){
		for(String s : children){
			try {
				lastObject = (JSONObject) lastObject.get(s);
			} catch(ClassCastException e){
				return lastObject;
			}
		}
		return lastObject;
	}
	
}
