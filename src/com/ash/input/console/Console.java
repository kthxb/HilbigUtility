package com.ash.input.console;

import java.awt.Point;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ash.util.DebugHelper;
import com.ash.util.Utility;
import com.ash.util.files.EZRGB;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.VerifyEvent;

/**
 * A customized console-application that supports coloring and multiple command modification and addition via {@link CommandParserInterface}s.
 * @author Aaron Hilbig
 *
 */
public class Console implements Runnable {

	protected Shell shlConsole;
	
	public static boolean DEFAULT_SETTINGS = true;
	public static boolean HIDE_OPENING_MESSAGE = false;
	
	public ArrayList<CommandParserInterface> commandparserList = new ArrayList<CommandParserInterface>();
	
	private Color bgc;
	private StyledText consoleText; 
	private String title;
	private Display display;
	private boolean finishedLoading;
	private int width = 700, height = 400;
	public boolean enableTimestamp = false;
	public boolean enableCommandUsage = false;
	private boolean enableDefaultCommands = false;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@SuppressWarnings("unused")
	private static synchronized void fuckingEntryPoint(){
		try {
			Console window = new Console(null);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		Console c = new Console("Console");
		
		c.enableTimestamp = true;
		c.setEnableCommandUsage(true);
		c.setEnableDefaultCommands(true);
		c.commandparserList.add(new ShellCommandParser());
		
		//for(CommandParserInterface i : c.commandparserList)
		//	c.println("Loaded: " + i.getLoadMessage());
		
		/*
		for (int i = 0; i < 5; i++) {
			c.printSameLine("HALLO");
		}
		
		c.println();
		
		//c.setSize(500, 200);
		
		//c.applyStyle(0, 10, true, SWT.COLOR_RED);
		
		
		c.printlnColored(EZRGB.SWT_COLORS.CYAN, "STAAARRRT");
		
		c.printlnColorFormatter("%100,0,0%test und %0,100,100,100,250,0%test");
		
		c.printlnColorFormatter("%133,7,0%1337 %0,0,0,255,255,255%inverrrt");
		*/
		
		/*c.requestInput(new InputAvailableListener(){
			@Override
			public void onInputAvailable(String input) {
				c.printlnColored(EZRGB.SWT_COLORS.YELLOW, input);
			}
		});*/
		
		c.focus();
	}
	
	/**
	 * Create a new instance
	 * @param title
	 */
	public Console(String title) {
		super();
		this.title = title;
		//this.open();
		Thread t = new Thread(this);
		t.start();
		while(!finishedLoading){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
		bgc = new Color(getDisplay(), 0,0,0);
		if(DEFAULT_SETTINGS && !HIDE_OPENING_MESSAGE) printlnColorFormatter("Opened HUtil console: %0,200,0,100,0,100%(c) Aaron Hilbig 2016%reset%");
		if(enableCommandUsage) commandparserList = new ArrayList<CommandParserInterface>();
	}
	
	/**
	 * Enables a loop that continuosly checks for command input.
	 * @param b
	 */
	public void setEnableCommandUsage(boolean b){
		enableCommandUsage = b;
		if(enableCommandUsage) {
			if(DEFAULT_SETTINGS) println("Insert command...");
			requestInput(null);
		}
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		setDisplay(Display.getDefault());
		createContents();
		shlConsole.open();
		shlConsole.layout();
		while (!shlConsole.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlConsole = new Shell();
		shlConsole.setImage(SWTResourceManager.getImage(Console.class, "/com/ash/input/console/console.png"));
		shlConsole.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				shlConsole.setSize(width, height);
				if(consoleText!=null) consoleText.setSize(width - 16, height - 39);
			}
		});
		shlConsole.setSize(width, height);
		shlConsole.setText(title);
		
		consoleText = new StyledText(shlConsole, SWT.NONE);
		consoleText.addVerifyKeyListener(new VerifyKeyListener(){
			@Override
			public void verifyKey(VerifyEvent e) {
				//System.out.println("'" + consoleText.getText().substring(index-1).trim().equals("") + "' " + e.keyCode);
				try {
					//cancel if text empty and backspace pressed
					if(e.keyCode==8 && consoleText.getText().substring(index-1).trim().equals("")){
						e.doit = false;
						//System.out.println("invalid");
					}
					if(e.keyCode==13 && consoleText.getText().substring(index-1).trim().equals(""))
						e.doit = false;
					//handle enter
					if(e.keyCode==13 && waitForInput && !consoleText.getText().substring(index-1).trim().equals("")){
						inputConfirm();
					} else if(e.keyCode==9 && waitForInput){ //Tab, auto-fill
						String[] currentCom = consoleText.getText().substring(index-1).trim().split(" ");
						System.out.println(currentCom[0]);
						for(CommandParserInterface i : commandparserList){
							i.syntax(getConsole(), currentCom[0], currentCom.length - 1);
						}
						e.doit = false;
					} /*else if(consoleText.getText().substring(index-1).trim().equals(""))
						e.doit = false;*/
				} catch(Exception xc){
					if(xc instanceof StringIndexOutOfBoundsException){
						//System.out.println("StringIndexOutOfBoundsException");
						update();
						e.doit = false;
					}
					xc.printStackTrace();
				}
			}
		});
		/*consoleText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				//Enter
				/*
				System.out.println("'" + consoleText.getText().substring(index-1).trim().equals("") + "' " + e.keyCode);
				try {
					if(e.keyCode==8 && consoleText.getText().substring(index-1).trim().equals("")){
						e.doit = false;
						System.out.println("invalid");
					}
					if(e.keyCode==13 && waitForInput && !consoleText.getText().substring(index-1).trim().equals("")){
						inputConfirm();
					} else if(e.keyCode==9 && waitForInput){ //Tab
						String[] currentCom = consoleText.getText().substring(index-1).trim().split(" ");
						System.out.println(currentCom[0]);
						for(CommandParserInterface i : commandparserList){
							i.syntax(getConsole(), currentCom[0], currentCom.length - 1);
						}
						e.doit = false;
					} else if(consoleText.getText().substring(index-1).trim().equals(""))
						e.doit = false;
				} catch(Exception xc){
					if(xc instanceof StringIndexOutOfBoundsException){
						System.out.println("StringIndexOutOfBoundsException");
						update();
						e.doit = false;
					}
					//xc.printStackTrace();
				}
			}
		});*/
		consoleText.setEditable(false);
		consoleText.setSelectionForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		consoleText.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		consoleText.setFont(SWTResourceManager.getFont("System", 12, SWT.BOLD));
		consoleText.setRightMargin(10);
		consoleText.setLeftMargin(10);
		consoleText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		consoleText.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		consoleText.setBounds(0, 0, width - 16, height - 39);
		
		finishedLoading = true;
	}
	
	public void update(){
		consoleText.setSelection(consoleText.getText().trim().length());
	}
	
	/**
	 * Print to the console
	 * @param s
	 */
	public void print(String s){
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				consoleText.append(((DEFAULT_SETTINGS ? "> " : "") + (enableTimestamp ? Utility.timeSec() + ": " : "") + s));
				update();
			}
		});
	}
	
	/**
	 * Append to the same line without header
	 * @param s
	 */
	public void printSameLine(String s){
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				consoleText.append(s);
				update();
			}
		});
	}
	
	/**
	 * Prints a new line
	 */
	public void println(){
		printSameLine("\n");
	}
	
	/**
	 * Prints to the console and adds a "\n"
	 * @param s
	 */
	public void println(String s){
		print(s + "\n");
	}
	
	/**
	 * Prints an error with red color.
	 * @param s
	 */
	public void errln(String s){
		printlnColored(new EZRGB(255,0,0),s + "\n");
	}
	
	/**
	 * Prints the String and replaces color format symbols with colors<br>
	 * Formatting syntax: %r,g,b% or %r,g,b,rbg,gbg,bgb% or %reset
	 * @param s
	 */
	public void printlnColorFormatter(String s){
		s = s.replaceAll("%reset%", "%255,255,255," + bgc.getRed() + "," + bgc.getGreen() + "," + bgc.getBlue() + "," + "%");
		s+= "%";
		String newString = "";
		ArrayList<StyleRange> styles = new ArrayList<>();
		
		boolean command = false;
		String latestCommand = "";
		int latestCommandIndex = 0;
		int selectorLength = 0;
		int i = 0;
		for(char c : s.toCharArray()){
			if(c=='%' && !command){
				latestCommandIndex = i;
				command = true;
				continue;
			}
			if(c=='%' && command){
				command = false;
			
				//System.out.println(latestCommand + " l " + latestCommand.length());
				StyleRange temp = new StyleRange();
				temp.start = latestCommandIndex;
				//temp.length = selectorLength;
				if(styles.size()>0) styles.get(styles.size()-1).length = selectorLength;
				String[] rgb = latestCommand.split(",");
				temp.foreground = new Color(getDisplay(), Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				if(rgb.length==6)
					temp.background = new Color(getDisplay(), Integer.parseInt(rgb[3]), Integer.parseInt(rgb[4]), Integer.parseInt(rgb[5]));
				
				//System.out.println("Adding this style: " + temp.toString());
				
				latestCommand = "";
				latestCommandIndex = 0;
				selectorLength = -1;
				
				styles.add(temp);
			}
			if(command){
				latestCommand += c;
				continue;
			} else {
				selectorLength++;
				newString += c!='%' ? c : "";
			}
			i++;
		}
		styles.get(styles.size()-1).length = selectorLength;
		
		println(newString);
		
		final String co = newString;
		for(StyleRange sr : styles){
			
			//System.out.println("Adding style: " + sr.toString());
			
			getDisplay().syncExec(new Runnable(){
				@Override
				public void run() {
					if(styles.indexOf(sr)+1==styles.size())
						sr.start -= 1;
					sr.start += consoleText.getText().trim().length() + (enableTimestamp ? Utility.timeSec() + ": "  : "1234567890").length() - co.length() - 10;
					consoleText.setStyleRange(sr);
				}
			});
		}
	}
	
	/**
	 * Prints the String and replaces color format symbols with colors asynchrously<br>Might help if console prints output in wrong order.<br>
	 * Formatting syntax: %r,g,b% or %r,g,b,rbg,gbg,bgb% or %reset
	 * @param s
	 */
	public void printlnColorFormatterAsync(String s){
		s = s.replaceAll("%reset%", "%255,255,255," + bgc.getRed() + "," + bgc.getGreen() + "," + bgc.getBlue() + "," + "%");
		s+= "%";
		String newString = "";
		ArrayList<StyleRange> styles = new ArrayList<>();
		
		boolean command = false;
		String latestCommand = "";
		int latestCommandIndex = 0;
		int selectorLength = 0;
		int i = 0;
		for(char c : s.toCharArray()){
			if(c=='%' && !command){
				latestCommandIndex = i;
				command = true;
				continue;
			}
			if(c=='%' && command){
				command = false;
			
				//System.out.println(latestCommand + " l " + latestCommand.length());
				StyleRange temp = new StyleRange();
				temp.start = latestCommandIndex;
				//temp.length = selectorLength;
				if(styles.size()>0) styles.get(styles.size()-1).length = selectorLength;
				String[] rgb = latestCommand.split(",");
				temp.foreground = new Color(getDisplay(), Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				if(rgb.length==6)
					temp.background = new Color(getDisplay(), Integer.parseInt(rgb[3]), Integer.parseInt(rgb[4]), Integer.parseInt(rgb[5]));
				
				//System.out.println("Adding this style: " + temp.toString());
				
				latestCommand = "";
				latestCommandIndex = 0;
				selectorLength = -1;
				
				styles.add(temp);
			}
			if(command){
				latestCommand += c;
				continue;
			} else {
				selectorLength++;
				newString += c!='%' ? c : "";
			}
			i++;
		}
		styles.get(styles.size()-1).length = selectorLength;
		
		println(newString);
		
		final String co = newString;
		for(StyleRange sr : styles){
			
			//System.out.println("Adding style: " + sr.toString());
			
			getDisplay().asyncExec(new Runnable(){
				@Override
				public void run() {
					if(styles.indexOf(sr)+1==styles.size())
						sr.start -= 1;
					sr.start += consoleText.getText().trim().length() + (enableTimestamp ? Utility.timeSec() + ": "  : "1234567890").length() - co.length() - 10;
					consoleText.setStyleRange(sr);
				}
			});
		}
	}
	
	/**
	 * Prints colored text for SWT_Colors {@link EZRGB}
	 * @param color
	 * @param s
	 */
	public void printlnColored(EZRGB.SWT_COLORS color, String s){
		print(s + "\n");
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				applyStyle(consoleText.getText().trim().length() - (s + (enableTimestamp ? Utility.timeSec() + ": "  : "")).length(), (s + (enableTimestamp ? Utility.timeSec() + ": " : "")).length(), false, color.value);
				update();
			}
		});
	}
	
	/**
	 * Prints colored text. {@link EZRGB}
	 * @param color
	 * @param s
	 */
	public void printlnColored(EZRGB color, String s){
		print(s + "\n");
		Color c = new Color(getDisplay(), color.r, color.g, color.b);
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				applyStyle(consoleText.getText().trim().length() - (s + (enableTimestamp ? Utility.timeSec() + ": "  : "")).length(), (s + (enableTimestamp ? Utility.timeSec() + ": " : "")).length(), false, c);
				update();
			}
		});
	}
	
	/**
	 * Prints formatted string.
	 * @param s
	 * @param args
	 */
	public void printf(String s, Object... args){
		print(String.format(s, args));
	}
	
	/**
	 * Prints formatted string by using {@link DebugHelper}.insertAllVariables(...)
	 * @param s
	 * @param from
	 */
	public void printlnInsert(String s, Object from){
		try {
			print(DebugHelper.insertAllVariables(s, from) + "\n");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			print(s + " [Parsing error]\n");
		} catch (IllegalAccessException e) {
			print(s + " [Parsing error]\n");
			e.printStackTrace();
		}
	}
	
	private void applyStyle(int start, int length, boolean bold, int color){
		StyleRange styleRange = new StyleRange();
		styleRange.start = start;
		styleRange.length = length;
		if(bold) styleRange.fontStyle = SWT.BOLD;
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				styleRange.foreground = getDisplay().getSystemColor(color);//display.getSystemColor(SWT.COLOR_BLUE);
				consoleText.setStyleRange(styleRange);
				update();
			}
		});
	}
	
	private void applyStyle(int start, int length, boolean bold, Color color){
		StyleRange styleRange = new StyleRange();
		styleRange.start = start;
		styleRange.length = length;
		if(bold) styleRange.fontStyle = SWT.BOLD;
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				styleRange.foreground = color;//display.getSystemColor(SWT.COLOR_BLUE);
				consoleText.setStyleRange(styleRange);
				update();
			}
		});
	}
	
	
	@Override
	public void run() {
		this.open();
	}
	
	/**
	 * Set the bounds of the console
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
		getDisplay().asyncExec(new Runnable(){

			@Override
			public void run() {
				shlConsole.setSize(width, height);
				consoleText.setSize(width - 16, height - 39);
				update();
			}
			
		});
	}
	
	/**
	 * Brings the console to the front
	 */
	public void focus(){
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				shlConsole.forceFocus();
			}
		});
	}
	
	/**
	 * Sets the font size.
	 */
	public void setFontsize(int i){
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				consoleText.setFont(SWTResourceManager.getFont("System", i, SWT.BOLD));
			}
		});
	}
	
	/**
	 * Sets the background color of the whole console window.
	 * @param color
	 */
	public void setBackground(EZRGB color){
		final Color c = new Color(getDisplay(), color.r, color.g, color.b);
		bgc = c;
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				consoleText.setBackground(c);
			}
		});
	}
	
	private boolean waitForInput;
	private int index;
	private String input;
	private InputAvailableListener listener;
	private String lastCommand = "help";
	
	/**
	 * Wait for a user input. A InputAvailable event will be fired to the given {@link InputAvailableListener} on submission.
	 * <br>Only allows one listener at a time
	 * @param ial
	 */
	public void requestInput(InputAvailableListener ial){
		
		if(waitForInput || listener!=null){
			System.err.println("There is already an input requested!");
			return;
		}
		
		print("");
		
		getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				index = consoleText.getText().length();
				consoleText.setEditable(true);
			}
		});
		
		waitForInput = true;
		
		listener = ial;
	}
	
	/**
	 * Blocks/Cancels the input request. Will reset the listener so it must be reassigned.
	 */
	public void ignoreInput(){
		waitForInput = false;
		listener = null;
		consoleText.setEditable(false);
	}
	
	private void inputConfirm(){
		
		getDisplay().syncExec(new Runnable(){
			@Override
			public void run() {
				consoleText.setEditable(false);
				input = consoleText.getText().substring(index-1).trim();
			}
		});
		
		if(input==null || input.trim().equals("")){
			consoleText.setEditable(true);
			return;
		}
		
		waitForInput = false;
		
		if(listener!=null){
			listener.onInputAvailable(input);
			listener = null;
		}
		if(enableCommandUsage){
			if(input.equals("!!")){
				input = lastCommand;
			} else if(input.startsWith("!!!")){
				input = input.replace("!!!", lastCommand.split(" ")[0]);
			}
			for(CommandParserInterface i : commandparserList){
				String com = input.split(" ")[0];
				String[] args = null;
				if(input.split(" ").length>1)
					args = input.substring(input.indexOf(" ") + 1).split(" ");
				boolean b = i.parseCommand(this, com, args);
				if(!b && (commandparserList.size()<=1 || (commandparserList.get(commandparserList.size()-1).equals(i)))){
					printlnColored(new EZRGB(255,0,0), "Error parsing command...");
				} else if(b){
					break;
				}
			}
			lastCommand = input;
			requestInput(null);
		}
	}
	
	public boolean getEnableDefaultCommands() {
		return enableDefaultCommands;
	}
	
	/**
	 * Add a {@link DefaultCommandParser} to the console
	 * @param enableDefaultCommands
	 */
	public void setEnableDefaultCommands(boolean enableDefaultCommands) {
		this.enableDefaultCommands = enableDefaultCommands;
		if(enableDefaultCommands)
			commandparserList.add(new DefaultCommandParser());
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}
	
	private Console getConsole(){
		return this;
	}
	
	public Point getBounds(){
		return new Point(width,height);
	}
	
	/**
	 * 
	 * @author Aaron Hilbig
	 *
	 */
	public interface InputAvailableListener {
		public void onInputAvailable(String input);
	}
}
