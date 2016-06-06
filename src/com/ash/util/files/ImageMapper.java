package com.ash.util.files;

import java.awt.Color;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

import com.ash.util.Utility;

/**
 * Assigns an object for any colors and provides an alternative storing system by reading in image files, pixel by pixel.
 * 
 * @author Aaron Hilbig
 *
 */
public class ImageMapper {
	
	public static void main(String[] args) throws UnsupportedFlavorException, IOException {
		//EZRGB.printAvailableColors();
		
		//ImageMapper im = new ImageMapper(FileManager.getImage(FileManager.filenameToPackage("com.ash.util.files", "sample.png")));
		ImageMapper im = new ImageMapper(Utility.getFromClipboardAsImage());
		System.out.print("Colors: ");
		im.forEachColor((c)->{
			System.out.print(c.getColorName(5));
		});
		im.define(EZRGB.getColorByName("black"), "Schwarz");
		im.define(EZRGB.getColorByName("white"), "Weiﬂ");
		im.define(EZRGB.getColorByName("darksalmon"), "Lachs");
		im.define(237, 28, 36, "Rot");
		im.define(255, 127, 39, "Orange");
		System.out.println("");
		im.forEach((c)->{
			if(c!=null && c instanceof String && !((String)c).equals("Weiﬂ"))
				System.out.println(c);
		});
		//im.define(0, 0, 0, "Schwarz");
		//System.out.println("\n"+im.getDefinition(new EZRGB(0,0,0)));
	}
	
	int width, height;
	EZRGB[][] matrix;
	HashMap<EZRGB, Object> dictionary;
	
	public ImageMapper(BufferedImage img){
		width = img.getWidth();
		height = img.getHeight();
		matrix = new EZRGB[height][width];
		for(int y = 0; y<height; y++)
			for(int x = 0; x<width; x++)
				matrix[y][x] = EZRGB.fromColor(new Color(img.getRGB(x, y)));
		dictionary = new HashMap<>();
	}
	
	public void forEachColor(Consumer<EZRGB> c){
		for(int y = 0; y<height; y++)
			for(int x = 0; x<width; x++)
				c.accept(matrix[y][x]);
	}
	
	public void forEach(Consumer<Object> c){
		for(int y = 0; y<height; y++)
			for(int x = 0; x<width; x++)
				c.accept(get(x, y));
	}
	
	public EZRGB getColorAt(int x, int y){
		return matrix[y][x];
	}
	
	public Object get(int x, int y){
		for(EZRGB rgb : dictionary.keySet())
			if(rgb.toString().equals(matrix[y][x].toString()))
				return dictionary.get(rgb);
		return null;
		//return dictionary.get(matrix[y][x]);
	}
	
	public Object getDefinition(EZRGB e){
		for(EZRGB rgb : dictionary.keySet()){
			System.out.println(rgb.toString() + " != " + e.toString());
			if(rgb.toString().equals(e.toString()))
				return dictionary.get(rgb);
		}
		return null;
		//return dictionary.get(e);
	}
	
	public void define(EZRGB e, Object o){
		dictionary.put(e, o);
	}
	
	public void define(int r, int g, int b, Object o){
		dictionary.put(new EZRGB(r, g, b), o);
	}
}
