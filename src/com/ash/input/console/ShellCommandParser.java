package com.ash.input.console;

import java.awt.AWTException;
import java.io.IOException;

import com.ash.bot.AutoPos;
import com.ash.util.ProcessManager;
import com.ash.util.files.EZRGB;

/**
 * CommandParserInterface that introduces windows shell command usage for the {@link Console}.
 * @author Aaron Hilbig
 *
 */
public class ShellCommandParser implements CommandParserInterface {
	
	public String loadMessage = "ShellCommandParser";
	
	private String temp = "";
	private boolean tempBool = false;

	@Override
	public boolean parseCommand(Console c, String com, String[] args) {
		tempBool = c.enableTimestamp;
		if(com.equals("shell") || com.equals("sh")){
			if(args==null || args.length<1){
				c.printlnColored(new EZRGB(255,0,0), "Not enough args.");
				return false;
			} else {
				
				Thread t = new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							String com = "";
							for(String s : args){
								com += s + " ";
							}
							c.enableTimestamp = false;
							c.printlnColored(new EZRGB(10,100,10), "The shell thread will run in the background until finished.");
							temp = ProcessManager.executeBatchCommands(com);
						} catch (IOException e) {
							e.printStackTrace();
							c.printlnColored(new EZRGB(255,0,0), e.getMessage());
						} catch (InterruptedException e) {
							e.printStackTrace();
							c.printlnColored(new EZRGB(255,0,0), e.getMessage());
						}
						c.printlnColored(new EZRGB(10,150,10), "" + temp);
						try {
							Thread.sleep(4);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						c.enableTimestamp = tempBool;
						c.println("");
					}
				});
				t.start();
				//String s = ProcessManager.executeBatchCommands(args);
				return true;
			}
		} else if(com.equals("autopos")){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						AutoPos ap = new AutoPos();
						while (!ap.done) {
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
								c.printlnColored(new EZRGB(255, 0, 0), e.getMessage());
							}
						}
						c.println("\n"+ap.result);
						c.shlConsole.setMinimized(false);
						c.focus();
					} catch (AWTException e) {
						e.printStackTrace();
						c.printlnColored(new EZRGB(255, 0, 0), e.getMessage());
					}
				}
			});
			c.println("Waiting for autopos to finish...");
			c.shlConsole.setMinimized(true);
			t.start();
			return true;
		}
		return false;
	}

	@Override
	public boolean syntax(Console c, String com, int argNum) {
		return false;
	}

	@Override
	public String getLoadMessage() {
		return loadMessage;
	}

	@Override
	public String getHelpMessage() {
		return "Avail. Commands:\n\t\tshell <shellcom>\n\t\tWarning (Known Bugs): Usage might lead to disabling timestamps temporarily,\n\t\tfailing whatever command is entered next or freezing.";
	}
	
}
