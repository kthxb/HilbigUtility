package com.ash.util.files;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.ash.input.InputInterface;
import com.ash.input.InputInterfaceXml;

//%CLASSPATH%;C:\opencv\build\x86\vc10\lib;C:\opencv\b­uild\common\tbb\ia32\vc10;C:\opencv\buil­d\x86\vc10\staticlib
//javadoc -d C:\Users\Aaron\workspace\HilbigUtility\doc -sourcepath C:\Users\Aaron\workspace\HilbigUtility\src com.ash.bot com.ash.input com.ash.input.console com.ash.linearcrypt com.ash.util com.ash.util.files com.ash.util.math com.ash.util.video
public class FileDownloader {
	public static void main(String[] args) throws Exception {
		String s = getFile("http://aaron.winbert.selfhost.eu/dev/update/test.xml");
		System.out.println(s);
		InputInterface i = InputInterfaceXml.interfaceFromXml(s);
		i.open();
	}
	
	/**
	 * Gets internet page or document from the web as string.
	 * !!! First "null" content gets deleted!<br>Uses UTF-8
	 * @param URL
	 * @return
	 */
	public static String getFile(String URL){
		String ret = null;
		try {
			URL url = new URL(URL);
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	            ret += inputLine + "\n";
	        in.close();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		if(ret!=null)
			return ret.replaceFirst("null", "");
		else return null;
	}
}
