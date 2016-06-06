package com.ash.util.files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Gets and sets default config.properties properties.
 * 
 * @author Aaron Hilbig
 *
 */
public class Settings {
	
	public static void main(String[] args) throws IOException {
		Settings s = new Settings("settings.xml", true);
		s.setProperty("value2", true);
		System.out.println(s.getPropertyAsBoolean("value2"));
	}
	
	private String filename;
	private Properties p = new Properties();
	public Properties getProperties() {
		return p;
	}
	private OutputStream out = null;
	private InputStream in = null;
	private boolean xml;
	boolean inClasspath = false;
	
	/**
	 * Inititalize new Settings Object.
	 * <br>You might also need to set the 'inClasspath' variable.
	 * 
	 * @param filename f.e.: 'config.properties'
	 * @param xml
	 */
	public Settings(String filename, boolean xml){
		this.filename = filename;
	}
	
	public void setProperty(String key, String value) throws IOException {
		out = new FileOutputStream(filename);
		p.setProperty(key, value);
		if(xml)
			p.storeToXML(out, null);
		else
			p.store(out, null);
		if(out!=null)
			out.close();
	}
	
	public void setProperty(String key, Object value) throws IOException{
		setProperty(key,value.toString());
	}
	
	public String getProperty(String key) throws IOException {
		if(inClasspath){
			in = this.getClass().getClassLoader().getResourceAsStream(filename);
		} else
			in = new FileInputStream(filename);
		
		if(xml)
			p.loadFromXML(in);
		else
			p.load(in);
		if(in!=null)
			in.close();
		return p.getProperty(key);
	}
	
	public boolean getPropertyAsBoolean(String key) throws IOException {
		String s = getProperty(key).toLowerCase().trim();
		if(s.equals("1") || s.equals("true")) return true;
		return false;
	}
	
	public int getPropertyAsInt(String key) throws IOException {
		String s = getProperty(key).toLowerCase().trim();
		return Integer.parseInt(s);
	}
	
	public float getPropertyAsFloat(String key) throws IOException {
		String s = getProperty(key).toLowerCase().trim();
		return Float.parseFloat(s);
	}
	
	public double getPropertyAsDouble(String key) throws IOException {
		String s = getProperty(key).toLowerCase().trim();
		return Double.parseDouble(s);
	}

	public boolean isInClasspath() {
		return inClasspath;
	}

	public void setInClasspath(boolean inClasspath) {
		this.inClasspath = inClasspath;
	}
	
	
	
}
