package com.ash.util.files;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ash.util.ProcessManager;

/**
 * All utilities youll need for file-handling
 * @author Aaron Hilbig
 *
 */
public class FileManager {
	
	public static String wrongFileTypeMessage = "Sie müssen eine Datei mit der Endung \"" + "%fileFilter%" + "\" auswählen!";
	public static String wrongDirectoryMessage = "Sie müssen den Ordner \"" + "%nameFilter%" + "\" auswählen!";
	public static boolean showErrorMessage = true;
	
	public static void main(String... args){
		//System.out.println(getFileWithDialog("Wählen sie eine Datei zum öffnen...", true, "C://", ".txt"));
		//System.out.println(getDirectoryWithDialog("Wählen sie einen Ordner...", "...einen tollen...", "C://", "lib"));
		//makeTextfile("hallomallo",filenameToPackage("com.ash.util.files","file.txt"));
		//System.out.println(getTextfileFromPackage("com.ash.util.files","file.txt"));
		//BufferedImage img = getImageFromSourceFolder("image.png");
		//Color c = new Color(img.getRGB(1, 1));
		System.out.println(getCurrentWorkingDir());
	}
	
	/**
	 * Öffnet den Dialog zum Öffnen/Speichern von Dateien.
	 * 
	 * @param title - Titel des Dialogs
	 * @param load - Wenn wahr -> "Datei öffnen", wenn falsch -> "Datei speichern"
	 * @param directory - Startordner. Funktioniert nicht immer.
	 * @param fileFilter - Funktioniert auf Windows nur, wenn FileManager.showErrorMessage = true ist.
	 * @return
	 */
	public static String getFileWithDialog(String title, boolean load, String directory, String fileFilter){
		FileDialog fd = new FileDialog(new Frame(), title, load ? FileDialog.LOAD : FileDialog.SAVE);
		fd.setDirectory(directory!=null ? directory : "c:\\");
		//fd.setFile("*.xml");
		if(fileFilter != null) fd.setFilenameFilter(new FilenameFilter(){
			@Override
			public boolean accept(File f, String s) {
				if(f.getName().endsWith(fileFilter))
					return true;
				return false;
			}
		});
		fd.setVisible(true);
		if(fd.getFile()!=null && fileFilter!=null && !fd.getFile().endsWith(fileFilter) && showErrorMessage){
			JOptionPane.showMessageDialog(null, wrongFileTypeMessage.replaceAll("%fileFilter%", fileFilter), "Fehler", JOptionPane.ERROR_MESSAGE);
			return getFileWithDialog(title, load, directory, fileFilter);
		}
		System.out.println(fd.getDirectory() + fd.getFile());
		return fd.getDirectory() + fd.getFile();
	}
	
	/**
	 * 
	 * @param title - Titel des Dialogs
	 * @param text - Textinfo des Dialogs
	 * @param directory - Startordner. Funktioniert nicht immer.
	 * @param nameFilter - Limitiert auf den genannten Ordnername wenn FileManager.showErrorMessage = true. Gibt null zurück falls dieser nicht geöffnet wurde
	 * @return
	 */
	public static String getDirectoryWithDialog(String title, String text, String directory, String nameFilter){
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setVisible(false);
	    DirectoryDialog dialog = new DirectoryDialog(shell);
	    dialog.setFilterPath(directory!=null ? directory : "c:\\"); // Windows specific
	    dialog.setMessage(text!=null?text:"");
	    dialog.setText(title!=null?title:"Ordner wählen...");
	    String s = dialog.open();
	    if(s!=null && nameFilter!=null && !s.equals(nameFilter) && showErrorMessage){
			JOptionPane.showMessageDialog(null, wrongDirectoryMessage.replaceAll("%nameFilter%", nameFilter), "Fehler", JOptionPane.ERROR_MESSAGE);
			display.dispose();
			return null;
		}
	    display.dispose();
	    return s;
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
	
	public static String getTreeVision(String path) throws IOException, InterruptedException {
		return ProcessManager.executeBatchCommands("@echo off","cd " + path,"tree").replace("Ä", "-").replace("Ã", "+").replace("³", "|").replace("À", "L");
	}
	

	public static void appendToFile(String path,/* String name,*/String content){
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
	
	/*public static String getFromTextFile(String path) {
		BufferedReader br = null;
		String s = null;
		
	    try {
	    	
	        StringBuilder sb = new StringBuilder();
	        br = new BufferedReader(new FileReader(path));
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
	}*/
	
	//getTextfile("src/default")
	
	/**
	 * Wandelt Paketnamen und Dateinamen zu einem gültigen Pfad um.<br>
	 * Beispiel: pkge = "com.ash.util.files", filename = "file.txt"<br>
	 * Rückgabe: "src/com/ash/util/files/file.txt"
	 * @param pkge
	 * @param filename
	 * @return
	 */
	public static String filenameToPackage(String pkge, String filename){
		if(pkge.startsWith(".") || pkge.endsWith("."))
			throw new IllegalArgumentException("Package name invalid (cannot start or end with dot)");
		return "src/" + pkge.replaceAll("\\.", "/") + "/" + filename;
	}
	
	public static BufferedImage getImage(String filename){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public static void writeImage(BufferedImage i, String filename, String ext){
		try {
		    File outputfile = new File(filename + "." + ext);
		    ImageIO.write(i, ext, outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static void writeImage(BufferedImage i, String filename){
		try {
		    File outputfile = new File(filename);
		    ImageIO.write(i, filename.split("\\.")[1], outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static BufferedImage getImageFromSourceFolder(String filename){
		return getImage("src/" + filename);
	}
	
	public static File getResource(String name){
		File f = new File("res/" + name);
		if(f.exists())
			return f;
		return null;
	}
	
	/**
	 * SAVE METHOD
	 * @param pkge
	 * @param filename
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getTextfileFromPackage(String pkge, String filename) {
		String s;
		try {
			s = getTextfile(filenameToPackage(pkge, filename));
			if(true)
				return s;
		} catch(Exception e){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(1) Couldn't find file \"" + pkge + " // " + filename + "\", searching in src");
		}
		try {
			s = getTextfileFromSourceFolder(filename);
			if(true){
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(2) You´re lucky, the file was found in src instead. Returning...");
				return s;
			}
		} catch(Exception e){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(2) Couldn't find file \"" + pkge + " // " + filename + "\" in src, searching in directory");
		}
		try {
			s =  getTextfile(filename);
			if(true){
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(3) You´re lucky, the file was found in the directory above. Returning...");
				return s;
			}
		} catch(Exception e){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(3) Couldn't find file \"" + pkge + " // " + filename + "\" in directory, searching for resource folder");
		}
		try {
			s = getTextfile("res/" + filename);
			if(true){
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(4) You´re lucky, the file was found in the resource folder. Returning...");
				return s;
			}
		} catch(Exception e){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("(4) Couldn't find file \"" + pkge + " // " + filename + "\" in resource folder, returning null;");
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("\n+++\n+++\n+++ The program is missing this file: " + filename + " - Please copy or replace it.\n+++\n+++");
			return null;
		}
		return s;
	}
	
	public static String getTextfileFromSourceFolder(String filename) {
		return getTextfile("src/" + filename);
	}
	
	public static String getTextfile(String filename) {
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
	    	System.err.println("(An exception occurred in getTextfile(\"" + filename + "\"))");
			//e.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		return s;
	}
	
	/**
	 * Generates textfile. Encoding: UTF-8
	 * @param content
	 * @param path
	 */
	public static void makeTextfile(String content, String path){
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.print(content);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes String to the end of the given file.
	 * @param content
	 * @param path
	 */
	public static void appendToTextfile(String content, String path){
		try {
			File file = new File(path);
		    FileWriter fw = new FileWriter(file,true);
		    fw.write("");
		    fw.write(content);
		    fw.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
	}
	
	/**
	 * Generates file.
	 * @param content
	 * @param path
	 */
	public static void makeFile(String content, String path){
		try {
			File file = new File(path);
		    FileWriter fw = new FileWriter(file,false);
		    fw.write("");
		    fw.write(content);
		    fw.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
	}
	
	public static String getCurrentWorkingDir(){
		return System.getProperty("user.dir");
	}
	
	public static org.eclipse.swt.graphics.Image getSWTImage(String uri){
		return new Image(Display.getCurrent(), uri);
	}
	
	public void deleteFile(File f){
		try {
			if(f.exists()){
				f.delete();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteFile(String file){
		try {
			File f = new File(file);
			if(f.exists()){
				f.delete();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String readFileToString(String filename) {
		return getTextfile(filename);
	}
	
	public static String readFile(Reader r) {
		BufferedReader br = null;
		String s = null;
	    try {
	        StringBuilder sb = new StringBuilder();
	        br = new BufferedReader(r);
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line + "\n");
	            line = br.readLine();
	        }
	        
	        s = sb.toString();
	        s = s.trim();
	    } catch (IOException e) {
	    	System.err.println("(An exception occurred in readFile(\"" + r.toString() + "\"))");
			//e.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		return s;
	}
	
	/**
	 * Save an object to a file
	 * @param path
	 * @param o
	 */
	public static void serializeAndWriteToFile(String path, Object o){
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	/**
	 * Load an object from a file
	 * @param <T>
	 * @param path
	 * @param insertTo
	 * @return
	 */
	public static <T> T deserialize(String path, T insertTo){
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			insertTo = (T) in.readObject();
			in.close();
			fileIn.close();
			return insertTo;
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.err.println(insertTo.getClass().getName() + " class not found");
			c.printStackTrace();
			return null;
		}
	}
	
}
