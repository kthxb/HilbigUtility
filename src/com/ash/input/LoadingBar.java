package com.ash.input;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * 
 * Simple preset for a loading bar
 * 
 * @author Aaron Hilbig
 *
 */
public class LoadingBar {
	
	String title, text;
	int max;
	private JProgressBar bar;
	private JLabel label;
	private JDialog dlg;
	
	/**
	 * Create a new instance
	 * @param title
	 * @param text
	 * @param max
	 */
	public LoadingBar(String title, String text, int max){
		this.title = title;
		this.text = text;
		this.max = max;
		label = new JLabel(text);
		bar = new JProgressBar(0, max);
	}
	
	/**
	 * Opens the instance in a new thread.<br>
	 * Use setValue to mimic progress.
	 */
	public void open(){
		
	    Thread t = new Thread(new Runnable() {
	      @Override
		public void run() {
	    	dlg = new JDialog(new JFrame(), title, true);
	  	    dlg.add(BorderLayout.CENTER, bar);
	  	    dlg.add(BorderLayout.NORTH, label);
	  	    dlg.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	  	    dlg.setSize(300, 75);
	  	    dlg.setLocationRelativeTo(null);
	        dlg.setVisible(true);
	      }
	    });
	    t.start();
	}
	
	public void close(){
		dlg.setVisible(false);
	}
	
	public void setValue(int value){
		bar.setValue(value);
	}
	
	public void setText(String text){
		label.setText(text);
	}

}
