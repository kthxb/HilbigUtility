
package com.ash.test;

import com.ash.util.EZWebsite;
import com.ash.util.EZWebsite.AndroidCompatible;
import com.ash.util.math.Comparison;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Deprecated
public class Test {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		/*
		Update30 upd = new Update30(() -> {System.out.println("Hi"); return false;});
		Update30 upd2 = new Update30(() -> {System.out.println("lolkek"); return false;});
		upd2.start();
		upd.start();
		upd.run = false;
		*/
		
		String s = new EZWebsite().new AndroidCompatible().read("http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=B85FE65EC6076829BEB5080BFADC8B50&steamid=76561198114070892");
		
		JSONObject json = null;
		
		try {
			json = (JSONObject) JSONSerializer.toJSON(s);
		} catch(Exception e){
			e.printStackTrace();
			String msg = e.getMessage().substring(0,100);
			int pos = Integer.parseInt(msg.substring(msg.indexOf("character") + 10, msg.indexOf("of") - 1).trim());
			System.out.print("\n===\n\n" + e.getMessage().substring(0,msg.indexOf("of") + 2) + ":\n");
			System.out.println(e.getMessage().substring(pos-10,pos+10));
			System.exit(1);
		}
		
		JSONArray statList = (JSONArray) getTree(json,"playerstats","stats");
		statList.forEach((Object o) -> {
			JSONObject obj = (JSONObject) o;
			String name = ((String) getTree(obj, "name")).replaceAll("_", " ");
			if(name.startsWith("total")){
				name = name.replaceFirst("total ", "");
			} else {
				//Dont add, unimportant value
				return;
			}
			
			String result = "";
			boolean nextLetterAction = true;
			for(char c : name.toCharArray()){
				if(Comparison.equalsOr(c, ' ', " ")){
					nextLetterAction = true;
					result += c;
					continue;
				}
				if(nextLetterAction){
					c = Character.toUpperCase(c);
					nextLetterAction = false;
				}
				result += c;
			}
			result = result.trim();
			name = result;
			
			int value = (int) getTree(obj, "value");
			System.out.println(name + ": " + value);
		});
		
		//System.out.println(json.get("playerstats/stats"));
	}
	
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
