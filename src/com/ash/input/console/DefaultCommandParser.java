package com.ash.input.console;

import java.io.IOException;

import com.ash.util.DebugHelper;
import com.ash.util.EZWebsite;
import com.ash.util.Utility;
import com.ash.util.files.EZRGB;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * CommandParserInterface that introduces most basic commands to the {@link Console}
 * @author Aaron Hilbig
 *
 */
public class DefaultCommandParser implements CommandParserInterface {
	
	public String loadMessage = "DefaultCommandParser";

	@Override
	public boolean parseCommand(Console c, String com, String[] args) {
//		System.out.println("\"" + com + "\"");
		boolean executeWithoutError = false;
		String errorMsg = "Unknown command / No error message available.";
		int offset = 0;
		if(args!=null)
			for(int i=0;i<args.length;i++){
				if(i + offset >= args.length)
					break;
				args[i] = args[i + offset].trim();
				if(args[i + offset].equals("")){
					offset++;
					System.out.println("Empty args, offset = " + offset);
					args[i] = args[i + offset].trim();
				}
			}
		if(com.equals("close")){
			c.getDisplay().syncExec(new Runnable(){
				@Override
				public void run() {
					c.shlConsole.close();
				}
			});
			executeWithoutError = true;
		} else if(com.equals("exit") || com.equals("xx")){
			System.exit(0);
			executeWithoutError = true;
		} else if(com.equals("bgc")){
			if(args==null || args.length<3){
				executeWithoutError = false;
				errorMsg = "Not enough arguments!";
			} else {
				try {
					if(Integer.parseInt(args[0])>255){
						executeWithoutError = false;
						errorMsg = "arg0 is greater than 255!";
					} else if(Integer.parseInt(args[1])>255){
						executeWithoutError = false;
						errorMsg = "arg1 is greater than 255!";
					} else if(Integer.parseInt(args[2])>255){
						executeWithoutError = false;
						errorMsg = "arg2 is greater than 255!";
					} else {
						executeWithoutError = true;
						c.setBackground(new EZRGB(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])));
					}
				} catch(Exception e){
					e.printStackTrace();
					executeWithoutError = false;
					errorMsg = "All arguments need to be valid numbers!";
				}
			}
		} else if(com.equals("fontsize")){
			if(args==null || args.length<1){
				executeWithoutError = false;
				errorMsg = "Not enough arguments!";
			} else {
				try {
					c.setFontsize(Integer.parseInt(args[0]));
					executeWithoutError = true;
				} catch(Exception e){
					e.printStackTrace();
					executeWithoutError = false;
					errorMsg = "All arguments need to be valid numbers!";
				}
			}
		} else if(com.equals("size")){
			if(args==null || args.length<2){
				executeWithoutError = false;
				errorMsg = "Not enough arguments!";
			} else {
				try {
					c.setSize(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
					executeWithoutError = true;
				} catch(Exception e){
					e.printStackTrace();
					executeWithoutError = false;
					errorMsg = "All arguments need to be valid numbers!";
				}
			}
		} else if(com.equals("help") || com.equals("?")){
			executeWithoutError = true;
			c.println();
			String s = "";
			int i0 = 0;
			for(CommandParserInterface i : c.commandparserList){
				if(i0>0){
					s += "Showing '" + i.getLoadMessage() + "' help:\n";
				}
				s += i.getHelpMessage() + "\n";
				i0++;
			}
			c.printlnColored(EZRGB.SWT_COLORS.BLUE, s);
		} else if(com.equals("debug")){
			if(args==null || args.length<1){
				executeWithoutError = false;
				errorMsg = "Not enough arguments!";
			} else {
				try {
					String s = new DebugHelper().dumpInfo(Class.forName(args[0]), false, false);
					for(String s0 : s.split("\n"))
							c.println(s0);
					executeWithoutError = true;
				} catch (IllegalArgumentException e) {
					executeWithoutError = false;
					errorMsg = "Error: " + e.getMessage();
				} catch (IllegalAccessException e) {
					executeWithoutError = false;
					errorMsg = "Error: " + e.getMessage();
				} catch (ClassNotFoundException e) {
					executeWithoutError = false;
					errorMsg = "Error: " + e.getMessage();
				}
			}
		} else if(com.equals("web")){
			if(args==null || args.length<1){
				executeWithoutError = false;
				errorMsg = "Not enough arguments!";
			} else {
				try {
					c.println();
					EZWebsite ws = new EZWebsite(args[0]);
					c.printlnColored(EZRGB.SWT_COLORS.YELLOW, ws.webPage.asText());
					executeWithoutError = true;
				} catch (FailingHttpStatusCodeException | IOException e) {
					executeWithoutError = false;
					errorMsg = "FailingHttpStatusCodeException/IOException: " + e.getMessage();
				}
			}
		} else if(com.equals("time")){
			c.println();
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c.printlnColorFormatterAsync("%50,100,100%" + Utility.date() + "%100,100,50% " + Utility.time());
			executeWithoutError = true;
		} else if(com.equals("plugins") || com.equals("pluginlist")){
			c.println();
			c.printlnColored(EZRGB.SWT_COLORS.YELLOW, "Loaded plugins: ");
			int i0 = 0;
			for(CommandParserInterface i : c.commandparserList){
				i0++;
				c.printlnColored(EZRGB.SWT_COLORS.YELLOW, " - " + i.getLoadMessage());
			}
			c.printlnColored(EZRGB.SWT_COLORS.YELLOW, "Total: " + i0);
			executeWithoutError = true;
		}
		if(!executeWithoutError){
			c.println();
			if(c.commandparserList.size()<=1 || (c.commandparserList.get(c.commandparserList.size()-1) instanceof DefaultCommandParser)) c.printlnColored(new EZRGB(255,0,0), errorMsg);
		} else {
//			System.out.println("Returning w/out error");
			c.println();
		}
		return executeWithoutError;
	}

	@Override
	public boolean syntax(Console c, String com, int argNum) {
		if(com==null || com.trim().equals("")){
			c.printSameLine("!!");
		}
		//System.out.println((c==null ? "null" : "c") + com + argNum);
		if(com.equals("debug") && argNum==0){
			c.printSameLine("com.ash.packet.sub.Class");
			return true;
		} else if(com.equals("size") && argNum==0){
			c.printSameLine("" + c.getBounds().x + " " + c.getBounds().y);
			return true;
		} else if(com.equals("bgc") && argNum==0){
				c.printSameLine("000 000 000");
				return true;
		} else if(com.equals("web") && argNum==0){
			c.printSameLine("http://www.");
			return true;
	}
		return false;
	}

	@Override
	public String getLoadMessage() {
		return loadMessage;
	}

	@Override
	public String getHelpMessage() {
		return "HUtil Colored Console Interface\n\t\tby Aaron Hilbig\n\t\tAvail. Commands:\n\t\tclose, exit / xx, bgc, size, fontsize, debug\n\t\tShortcuts: '!!': Last command; '!!!': Last command with other args.";
	}

}
