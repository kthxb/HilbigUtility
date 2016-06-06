package com.ash.util;

import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.function.Function;
import static com.ash.util.math.Preconditions.*;

/**
 * Provides text editing functionality<br>
 * 
 * @author Aaron Hilbig
 *
 */
public class TextEditor {
	private TextEditor(){}
	
	public static void main(String[] args) {
		String s = "Eine Geschichte\nist sehr\nspannend und so\nund sehr\nhart geil";
		
		System.out.println(intelligentIndexSearch(s, new TextSearchQueue("sehr", 0, 0), new TextSearchQueue("geil", 14, 6)));
		
		//System.out.println("Davor: " + s);
		/*s = editRegion(s, endingIndexOf(s, "Eine "), s.indexOf("Geschichte"), (str) -> {
			return str += "tolle ";
		});*/
		
		/*s = editRegion(s, "Eine ", "Geschichte", (str) -> {
			return str += "super ";
		});*/
		
		//s = insertBetween(s, "Eine ", "Geschichte", "hammer ");
		
		//s = visibleLines(insertBetweenLines(s, "sehr", "spannend", "(hartgeil)"));
		
		//System.out.println(endOfLineIndex(s, "sehr"));
		//System.out.println(startOfLineIndex(s, "sehr"));
		//System.out.println(s.substring(startOfLineIndex(s, "sehr")));
		
		//System.out.println("Danach: " + s);
		
		//System.out.println(firstIndexAfterIndex("abec mallo abec","mallo","abec"));
		
		//System.out.println(firstIndexAfterIndex("abec mallo abec",5,"abec"));
		
		//System.out.println("\n\n" + firstIndexBeforeC("hallo abec mallo abec","mallo","abec"));
	}
	
	/**
	 * Returns the index of where {@code content} in {@code s} ends.<br>
	 * 
	 * @param s - The string to be edited
	 * @param content
	 * @return
	 */
	public static int endingIndexOf(String s, String content){
		if(s.indexOf(content)==-1) return -1;
		return s.indexOf(content) + content.length();
	}
	
	/**
	 * Converts the normal {@code indexOf(content)} to the index where {@code content} ends.<br>
	 * 
	 * @param s - The string to be edited
	 * @param content
	 * @return
	 */
	public static int toEndingIndex(int index, String content){
		if(index==-1) return -1;
		return index + content.length();
	}
	
	/**
	 * Applys a {@code function} to the specified region of the string<br>Example:
	 * <pre>String s = "Eine Geschichte";
	 *s = editRegion(s, "Eine ", "Geschichte", (str) -> {
	 *return str += "spannende ";
	 *System.out.println(s); //Eine spannende Geschichte
	 *});
	 * </pre>
	 * @param s - The string to be edited
	 * @param startIndex
	 * @param endIndex
	 * @param function
	 * @return the string s with all changes applied
	 */
	public static String editRegion(String s, int startIndex, int endIndex, Function<String, String> function){
		checkNotNull(s);
		checkNotNull(function);
		String everythingBeforeTheCut = s.substring(0, startIndex);
		String everythingAfterTheCut = s.substring(endIndex);
		String cut = s.substring(startIndex, endIndex);
		String edit = function.apply(cut);
		//System.out.printf("Source: %s Before: %s, After: %s; insert: %s%n", s, everythingBeforeTheCut, everythingAfterTheCut, edit);
		return everythingBeforeTheCut + edit + everythingAfterTheCut;
	}
	
	/**
	 * Allows to apply a function to the Region which returns an Integer (f.e. to return an index)
	 * That way you can perform a search in a region of the string.
	 * @param s - The string to be edited
	 * @param s - The string to be editedtartIndex
	 * @param endIndex
	 * @param function
	 * @return the string s with all changes applied
	 */
	public static int indexRegion(String s, int startIndex, int endIndex, Function<String, Integer> function){
		checkNotNull(s);
		checkNotNull(function);
		//String everythingBeforeTheCut = s.substring(0, startIndex);
		//String everythingAfterTheCut = s.substring(endIndex);
		String cut = s.substring(startIndex, endIndex);
		int edit = function.apply(cut);
		//System.out.printf("Source: %s Before: %s, After: %s; insert: %s%n", s, everythingBeforeTheCut, everythingAfterTheCut, edit);
		return edit;
	}
	
	public static Object editRegionObject(String s, int startIndex, int endIndex, Function<String, Object> function){
		checkNotNull(s);
		checkNotNull(function);
		//String everythingBeforeTheCut = s.substring(0, startIndex);
		//String everythingAfterTheCut = s.substring(endIndex);
		String cut = s.substring(startIndex, endIndex);
		Object edit = function.apply(cut);
		//System.out.printf("Source: %s Before: %s, After: %s; insert: %s%n", s, everythingBeforeTheCut, everythingAfterTheCut, edit);
		return edit;
	}
	
	/**
	 * Applys a {@code function} to the specified region of the string<br>
	 * The region is defined by the end of "regionStart" and the beginning of "regionEnd"
	 * @param s - The string to be edited
	 * @param s - The string to be editedtartIndex
	 * @param endIndex
	 * @param function
	 * @return the string s with all changes applied
	 */
	public static String editRegion(String s, String regionStart, String regionEnd, Function<String, String> function){
		checkNotNull(s);
		checkNotNull(function);
		String everythingBeforeTheCut = s.substring(0, endingIndexOf(s, regionStart));
		String everythingAfterTheCut = s.substring(s.indexOf(regionEnd));
		String cut = s.substring(endingIndexOf(s, regionStart), s.indexOf(regionEnd));
		String edit = function.apply(cut);
		//System.out.printf("Source: %s Before: %s, After: %s; insert: %s%n", s, everythingBeforeTheCut, everythingAfterTheCut, edit);
		return everythingBeforeTheCut + edit + everythingAfterTheCut;
	}
	
	/**
	 * Inserts something between the end of "regionStart" and "regionEnd"
	 * @param s - The string to be edited
	 * @param regionStart
	 * @param regionEnd
	 * @param insert
	 * @return the string s with all changes applied
	 */
	public static String insertBetween(String s, String regionStart, String regionEnd, String insert){
		checkNotNull(s);
		checkNotNull(regionStart);
		checkNotNull(regionEnd);
		checkNotNull(insert);
		s = editRegion(s, endingIndexOf(s, regionStart), s.indexOf(regionEnd), (str) -> {
			return str += insert;
		});
		return s;
	}
	
	/**
	 * Inserts "insert" between the first line that contains "containsLineBefore" and the first line after that, which contains "containsLineAfter". It also automatically adds a new line.
	 * @param s - The string to be edited
	 * @param containsLineBefore
	 * @param containsLineAfter
	 * @param insert
	 * @return the string s with all changes applied
	 */
	public static String insertBetweenLines(String s, String containsLineBefore, String containsLineAfter, String insert){
		String[] lines = s.split("\n");
		String newString = null;
		int positionToInsertAfter = -1;
		int i = 0;
		for(String str : lines){
			if(str.contains(containsLineBefore)){
				System.out.println("Found: " + containsLineBefore + " @ " + i);
				if(i + 1 < lines.length){
					if(lines[i + 1].contains(containsLineAfter)){
						System.out.println("Found: " + containsLineAfter + " @ " + (i+1));
						positionToInsertAfter = i;
						break;
					}
				}
			}
			i++;
		}
		if(positionToInsertAfter>0){
			newString = "";
			int i0 = 0;
			for(String str : lines){
				newString += str + "\n";
				if(i0 == positionToInsertAfter)
					newString += insert + "\n";
				i0++;
			}
		}
		if(newString==null)
			return s;
		return newString;
	}
	
	/**
	 * Replaces all "\n" (next-line) with a readable mark and prints.
	 * @param s - The string to be edited
	 */
	public static void visibleLines(String s){
		checkNotNull(s);
		try {
			System.out.println(s.replaceAll("\n", "[\\\\n]"));
		} catch(Exception e){
			System.out.println(s);
		}
	}
	
	/**
	 * Returns the index of the end of the first line that contains "contains"
	 * @param s - The string to be edited
	 * @param contains
	 * @return
	 */
	public static int endOfLineIndex(String s, String contains){
		return firstIndexAfterC(s, contains, "\n") + 1;
	}
	
	/**
	 * Returns the index of the start of the first line that contains "contains"
	 * @param s - The string to be edited
	 * @param contains
	 * @return
	 */
	public static int startOfLineIndex(String s, String contains){
		String[] split = s.split("\n");
		for(String str : split)
			if(str.contains(contains))
				return s.indexOf(str);
		return -1;//firstIndexAfterIndexOf(s, contains, "\n");
	}
	
	/**
	 * Returns the index of the last "indexOf" in "s" that is before "contains"
	 * @param s - The string to be edited
	 * @param contains
	 * @param indexOf
	 * @return
	 */
	public static int firstIndexBeforeC(String s, String contains, String indexOf){
		if(!s.contains(contains))return -1;
		return indexRegion(s,0,s.indexOf(contains),(str)->{
			return str.lastIndexOf(indexOf);
		});
	}
	
	/**
	 * Returns the index of the last "indexOf" in "s" that is before index
	 * @param s - The string to be edited
	 * @param contains
	 * @param indexOf
	 * @return
	 */
	public static int firstIndexBeforeC(String s, int index, String indexOf){
		if(index==-1)return -1;
		return indexRegion(s,0,index,(str)->{
			return str.lastIndexOf(indexOf);
		});
	}
	
	/**
	 * Returns the index of the first "indexOf" in "s" that is after "contains"
	 * @param s - The string to be edited
	 * @param contains
	 * @param indexOf
	 * @return
	 */
	public static int firstIndexAfterC(String s, String contains, String indexOf){
		if(endingIndexOf(s, contains) != -1)
			return indexRegion(s, endingIndexOf(s, contains), s.length(), (str)->{
				return str.indexOf(indexOf);
			}) + endingIndexOf(s, contains);
		else return -1;
	}
	
	/**
	 * Returns the index of the first "indexOf" in "s" that is after "index"
	 * @param s - The string to be edited
	 * @param index
	 * @param indexOf
	 * @return
	 */
	public static int firstIndexOfAfterIndex(String s, int index, String indexOf){
		if(index != -1)
			return indexRegion(s, index, s.length(), (str)->{
				return str.indexOf(indexOf);
			}) + index;
		else return -1;
	}
	
	/**
	 * Returns the index in String s to which the following applies:<br>
	 * <li>
	 * Every text of every TextSearchQueue-Object in the search-array is found in s.<br></li><li>
	 * Every text of every TextSearchQueue-Object is in range to the last TextSearchQueue-Object with its given search-radius<br></li><li>
	 * Every text of every TextSearchQueue-Object is at minimum its {@code distance} away from the last TextSearchQueue-Object</li>
	 * <br>
	 * @param s
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unused")
	public static int intelligentIndexSearch(String s, TextSearchQueue... search){
		int i = 0;
		for(int index : indexOf(s, search[0].text)){
			TextSearchQueue last = null;
			int o = 0;
			boolean containsAll = true;
			for(TextSearchQueue sq : search){
				//System.out.println("Searching for next strings: " + sq.text);
				if(sq.equals(search[0])){
					last = sq;
					//System.out.println("Skipperino...");
					continue;
				}
				String searchRange;
				if(sq.searchLength > 0)
					searchRange = s.substring(index, Math.min((sq.searchLength + index), s.length()));
				else
					searchRange = s.substring(index + sq.searchLength, index);
				//System.out.println("The searchRange is: " + searchRange + " / ");
				if(searchRange.contains(sq.text)){
					//System.out.print("Found it! ");
					if(searchRange.indexOf(sq.text) - last.text.length() >= sq.minimumLength){
						//System.out.println("... and its in range. Continue!");
						last = sq;	
					} else {
						//System.out.println("... but not in range. Returning false.");
						containsAll = false;
						break;
					}
				} else {
					//System.out.println("Can't find it.");
					containsAll = false;
					break;
				}
				o++;
			}
			if(containsAll)
				return index;
			i++;
		}
		return -1;
	}
	
	public static TextSearchQueue textSearchQueue(String text, int searchRadius, int minimumDistance){
		return new TextSearchQueue(text, searchRadius, minimumDistance);
	}
	
	public static class TextSearchQueue {
		public String text;
		int searchLength, minimumLength;
		
		public TextSearchQueue(String text, int searchLength, int minimumLength) {
			this.text = text;
			this.searchLength = searchLength;
			this.minimumLength = minimumLength;
		}
	}
	
	/**
	 * Returns positions of all found substrings
	 * @param s
	 * @param of
	 * @return
	 */
	public static ArrayList<Integer> indexOf(String s, String of){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int i = s.indexOf(of);
		if(i >= 0)
			ret.add(i);
		while(i >= 0) {
		     i = s.indexOf(of, i+1);
		     if(i >= 0)
		    	 ret.add(i);
		}
		return ret;
	}
	
	/**
	 * x is startIndex, y is stopIndex
	 * @return
	 */
	public static Point argumentPosition(String in, String arg){
		return new Point(in.indexOf(arg), in.indexOf(arg) + arg.length());
	}
}
