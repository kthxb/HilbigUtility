package com.ash.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * General utilities
 * @author Aaron Hilbig
 *
 */
public class Utility {
	
	public static void copyToClipboard(String s){
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(s);
        clpbrd.setContents(stringSelection, null);
	}
	
	public static void main(String[] args) throws UnsupportedFlavorException, IOException {
		//System.out.println(getFromClipboardAsFilelist().get(0));
		//Point p = argumentPosition("hallomallo %exoc%", "%exoc%");
	}
	
	public static String getFromClipboardAsString() throws UnsupportedFlavorException, IOException {
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trs = clpbrd.getContents(null);
        return (String) trs.getTransferData(DataFlavor.stringFlavor);
	}
	
	public static BufferedImage getFromClipboardAsImage() throws UnsupportedFlavorException, IOException {
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trs = clpbrd.getContents(null);
        return (BufferedImage) trs.getTransferData(DataFlavor.imageFlavor);
	}
	
	@SuppressWarnings("unchecked")
	public static List<File> getFromClipboardAsFilelist() throws UnsupportedFlavorException, IOException {
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trs = clpbrd.getContents(null);
        return (List<File>) trs.getTransferData(DataFlavor.javaFileListFlavor);
	}
	
	/**
	 * Use {@link EZRGB} instead
	 */
	@Deprecated
	public static Color getColorRGB(int r, int g, int b) {
		return Color.getHSBColor(Color.RGBtoHSB(r, g, b, null)[0], Color.RGBtoHSB(r, g, b, null)[1], Color.RGBtoHSB(r, g, b, null)[2]);
	}
	
	public static String replaceAll(String[] replace, String replaceBy, String inString){
		String ret = inString;
		for(int i = 0; i < replace.length; i++){
			ret = ret.replaceAll(replace[i], replaceBy);
		}
		return ret;
	}
	
	public static String[] makeArray(String... s) {
		return s;
	}
	
	public static Object[] makeArray(Object... s) {
		return s;
	}
	
	public static List<String> makeList(String... s) {
		return Arrays.asList(s);
	}
	
	public static List<Object> makeList(Object... s) {
		return Arrays.asList(s);
	}
	
	public static Object arrayNext(Object[] array, int index, int advance){
		if(array.length-1 > index + advance){
			return array[index + advance];
		} else return null;
	}
	

	public static String time() {
		Date d = new Date();
		return String.format("%tH:%1$tM", d);
	}
	
	public static String timeSec() {
		Date d = new Date();
		return String.format("%tT", d);
	}

	public static String date() {
		Date d = new Date();
		return String.format("%te. %1$tB %1$tY", d);
	}

	public static String getCurrentWorkingDir(){
		return System.getProperty("user.dir");
	}
	
	public static String getUsername(){
		return System.getProperty("user.name");
	}
	
	/**
	 * x is startIndex, y is stopIndex
	 * Moved to {@link TextEditor}
	 * @return
	 */
	@Deprecated
	public static Point argumentPosition(String in, String arg){
		return new Point(in.indexOf(arg), in.indexOf(arg) + arg.length());
	}
	
}
