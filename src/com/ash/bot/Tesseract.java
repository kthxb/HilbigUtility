package com.ash.bot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.ash.util.ProcessManager;

/**
 * @author Aaron Hilbig
 * 
 * Class for working with tesseract OCR - Work in progress
 *
 */
public class Tesseract {
	
	@Deprecated
	public void tesseract(Rectangle r, String imgname, String outname){
//		tesseract D:\Programmieren\Tesseract-OCR\test.png D:\Programmieren\Tesseract-OCR\out -l deu
//		@exit 69
		String com = "tesseract D:\\Programmieren\\Tesseract-OCR\\" + imgname + " D:\\Programmieren\\Tesseract-OCR\\" + outname + " -l deu";
		try {
			ProcessManager.executeBatchCommands(com);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String outfilename = "out.txt";
	
	@Deprecated
	public String getTesseractFromScreenRegion(Rectangle r, int delay) throws InterruptedException{
		saveScreenshot(r);
		executeTesseract();
		Thread.sleep(delay);
		String out = readTesseractOutputFile();
//		System.out.println("Text Recognition Returned: " + out);
		return out;
	}
	
	@Deprecated
	public void deleteOldFile(){
		try {
		File f = new File("D:\\Programmieren\\Tesseract-OCR\\" + outfilename);
		if(f.exists()){
			f.delete();
			System.out.println("Deleted old file.");
		}
		} catch(Exception e){}
	}
	
	@Deprecated
	public void saveScreenshot(Rectangle r){
		BufferedImage img = null;
		try {
			img = new Bot().performAWT().createScreenCapture(r);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		File f = new File("D:\\Programmieren\\Tesseract-OCR\\quizduell.png");
		try {
			ImageIO.write(img, "PNG", f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Deprecated
	public void executeTesseract() throws InterruptedException{
		//return b.perform().run("tesseract.bat", "D:\\Programmieren\\Tesseract-OCR");
		try {
			Runtime.getRuntime().exec("D:\\Programmieren\\Tesseract-OCR\\tesseract.bat", null, new File("D:\\Programmieren\\Tesseract-OCR"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*try {
			Process p = Runtime.getRuntime().exec("D:\\Programmieren\\Tesseract-OCR\\tesseract.bat");
			p.waitFor();
			return p.exitValue();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return 0;*/
	}
	
	@Deprecated
	public String readTesseractOutputFile(){
		BufferedReader in;
		String s = null;
		try {
			File file = new File("D:\\Programmieren\\Tesseract-OCR\\out.txt");
			in = new BufferedReader( new InputStreamReader(new FileInputStream(file), "UTF-8") );
			//in = new BufferedReader(new FileInputStream(new FileReader(file), "UTF-8")); // new FileInputStream("file.txt") , "UTF-8") 
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				s += line;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if(s==null) return null;
		return s.replaceFirst("null", "");
	}
	
}
