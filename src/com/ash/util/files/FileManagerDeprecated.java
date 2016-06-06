package com.ash.util.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import com.ash.util.ProcessManager;

/**
 * The old FileManager
 * @author Aaron Hilbig
 *
 */
@Deprecated
public class FileManagerDeprecated {
	
	public static void appendTextFile(String path,/* String name,*/String content){
		try
		{
			File file = new File(path/* + "\\"+name+".txt"*/);
		    FileWriter fw = new FileWriter(file,true);
		    fw.write("");
		    fw.write(content);
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}
	
	public static String getFromTextFile(String filename) {
		BufferedReader br = null;
		String s = null;
		
	    try {
	    	
	        StringBuilder sb = new StringBuilder();
	        br = new BufferedReader(new FileReader(filename));
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line + "\n");
	            line = br.readLine();
	        }
	        
	        s = sb.toString();
	        s = s.trim();
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		return s;
	}
	
	public static File openFileDialog(String currentDirectory, boolean useFileFilter, FileFilter f){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(currentDirectory));
		fileChooser.setFileFilter(f);
		fileChooser.setDialogTitle("Open resource to load.");
		int returnVal = fileChooser.showOpenDialog(fileChooser);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        return file;
	    } else {
	       System.out.println("File access cancelled by user.");
	       return null;
	    }
	}
	
	public static String getCurrentPath() {
		return System.getProperty("user.dir");
		/*try {
			return ProcessManager.executeBatchCommands("chdir");
		} catch (IOException e) {
			return System.getProperty("user.dir");
		} catch (InterruptedException e) {
			return System.getProperty("user.dir");
		}*/
	}
	
	public static String getTreeVision(String path) throws IOException, InterruptedException{
		return ProcessManager.executeBatchCommands("@echo off","cd " + path,"tree").replace("Ä", "-").replace("Ã", "+").replace("³", "|").replace("À", "L");
	}
	
}
