package com.ash.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Facade for htmlUnit. 
 * @author Aaron Hilbig
 *
 */
public class EZWebsite {
	
	public WebClient webClient;
	public HtmlPage webPage;
	
	/**
	 * Open a new website
	 * @param url
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public EZWebsite(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webPage = (HtmlPage) webClient.getPage(url);
	}
	
	public EZWebsite() {
	}
	
	public void newWebsite(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		webPage = (HtmlPage) webClient.getPage(url);
	}
	
	/**
	 * @param xpath
	 * @return HmlElement from xpath
	 * @throws Exception
	 */
	public HtmlElement getElement(String xpath) throws Exception {
		HtmlElement ret;
		try {
			ret = (HtmlElement) webPage.getByXPath(xpath).get(0);
			if(ret==null)
				throw new java.lang.IllegalArgumentException("Element not found.");
		} catch(Exception e){
			throw new java.lang.IllegalArgumentException("Element not found. - " + e.getMessage());
		}
		return ret;
	}
	
	public HtmlInput getInput(String xpath) throws Exception {
		HtmlInput ret;
		try {
			ret = (HtmlInput) webPage.getByXPath(xpath).get(0);
			if(ret==null)
				throw new java.lang.IllegalArgumentException("Input not found.");
		} catch(Exception e){
			throw new java.lang.IllegalArgumentException("Input not found.");
		}
		return ret;
	}
	
	/**
	 * Get a webpage element as string by xpath
	 * @param xpath
	 * @return
	 * @throws Exception
	 */
	public String getString(String xpath) throws Exception {
		HtmlElement e = getElement(xpath);
		return e.asText();
	}
	
	public void writeToInput(HtmlInput i, String s) throws Exception {
		i.type(s);
	}
	
	public void writeToInput(String input, String s) throws Exception {
		HtmlInput i = getInput(s);
		i.type(s);
	}
	
	/**
	 * Untested
	 * @author Aaron Hilbig
	 *
	 */
	@Deprecated
	public class AndroidCompatible {
		
		public String read(String url) throws Exception {
	        URL yahoo = new URL(url);
	        URLConnection yc = yahoo.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;
	        String ret = "";
	        while ((inputLine = in.readLine()) != null) 
	            ret += inputLine;
	        in.close();
	        return ret;
	    }
		
	}
	
}
