package com.ash.input.console;

/**
 * Provides a interface used to supply the {@link Console} with additional command-"plugins"
 * @author Aaron Hilbig
 * 
 *
 */
public interface CommandParserInterface {
	
	/**
	 * 
	 * insert code @ start:
	 * boolean executeWithoutError = false;<br>
		String errorMsg = "Unknown command / No error message available.";<br>
		int offset = 0;<br>
		if(args!=null)<br>
			for(int i=0;i<args.length;i++){<br>
				if(i + offset >= args.length)<br>
					break;<br>
				args[i] = args[i + offset].trim();<br>
				if(args[i + offset].equals("")){<br>
					offset++;<br>
					System.out.println("Empty args, offset = " + offset);<br>
					args[i] = args[i + offset].trim();<br>
				}<br>
			}<br><br><br>@end: 
	
	 * <code>if(!executeWithoutError){<br>c.println();<br>if(c.commandparserList.size()<=1 || (c.commandparserList.get(c.commandparserList.size()-1) instanceof DefaultCommandParser)) c.printlnColored(new EZRGB(255,0,0), errorMsg);<br>} else {<br> c.println();<br>}</code>
	 * @param c
	 * @param com
	 * @param args
	 * @return
	 */
	public boolean parseCommand(Console c, String com, String[] args);
	
	/**
	 * auto-filling if user presses tab.
	 * use c.printSameLine(...) to print text.
	 * @param c
	 * @param com
	 * @param argNum
	 * @return
	 */
	public boolean syntax(Console c, String com, int argNum);
	
	/**
	 * @return the name of the plugin
	 */
	public String getLoadMessage();
	
	/**
	 * @return help message
	 */
	public String getHelpMessage();
}
