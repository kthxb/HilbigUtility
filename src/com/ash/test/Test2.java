package com.ash.test;

import com.ash.util.EZWebsite;

@Deprecated
public class Test2 {
	public static void main(String[] args) throws Exception {
		String results = "";
		EZWebsite e;
		for(int i = 0; i<300; i++){
			e = new EZWebsite("http://www.hltv.org/results/" + (i!=0?i + "/":""));
			String s = e.webPage.asText().substring(e.webPage.asText().indexOf("Best of"), e.webPage.asText().indexOf("Best of")+100).replaceAll("\n", "%%%");
			String bestof = s.split("%%%")[0].trim();
			String team1 = s.split("%%%")[1].trim();
			String result = s.split("%%%")[2].trim();
			String team2 = s.split("%%%")[3].trim();
			System.out.println("" + bestof + ": " + team1 + " vs " + team2 + "; " + result);
			results += "" + bestof + ": " + team1 + " vs " + team2 + "; " + result + "\n";
		}
		System.out.println("---\n\n" + results);
	}
}
