package com.ash.input;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ash.input.InputInterfaceXml.BooleanInput;
import com.ash.input.InputInterfaceXml.FormatStringInput;
import com.ash.input.InputInterfaceXml.IntegerInput;
import com.ash.input.InputInterfaceXml.LimitedIntegerInput;
import com.ash.input.InputInterfaceXml.MultiInput;
import com.ash.input.InputInterfaceXml.StringInput;
import com.ash.input.StringInputComposite.FormatException;
import com.ash.util.files.SWTLoader;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

/**
 * <br>
 * Quickly create a GUI with few commands.<br><br>
 * Example:<br><br>
 * 
 * InputInterface i = new InputInterface("Title","Please enter your adress:");<br>
 * i.addStringInput("City");<br>
 * i.addStringInput("Street");<br>
 * i.addIntegerInput("Hs No.:",0,5000);<br>
 * ...<br>
 * i.open(); //Open the GUI<br>
 * if(i.result.get("City").equals("Stuttgart")) ...<br>
 * String xml = i.toXml(); //Convert to xml...<br><br>
 * 
 * @author Aaron Hilbig
 *
 */
@SuppressWarnings("rawtypes")
public class InputInterface {

	protected Shell shell;
	
	private boolean open = true;
	private Button btnAccept;
	private String buttonText = "Accept";
	private String title, desc;
	private int adjustSize = 0;
	public HashMap<String, Object> result;
	Label lbl;

	public ArrayList<CompositeInterface> allInputs;
	
	/**
	 * Create a new instance
	 * Use of InputInterface(String title, String description) is recommended.
	 */
	public InputInterface(){
		shell = new Shell();
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		shell.setText("Input");
		
		this.title = "Input";
		this.desc = "";
		
		result = new HashMap<String, Object>();
		allInputs = new ArrayList<CompositeInterface>();
	}
	
	/**
	 * Create a new instance with set title and description
	 * @param title
	 * @param description
	 */
	public InputInterface(String title, String description){
		
		shell = new Shell();
		setIcon(InputInterface.class, "/com/ash/input/inputinterface.png");
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		shell.setText(title);
		
		this.title = title;
		this.desc = description;
		
		if(description!=null){
			lbl = new Label(shell, SWT.NONE);
			lbl.setBounds(0, 0, 55, 15);
			lbl.setText(description);
			lbl.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		}
		
		result = new HashMap<String, Object>();
		allInputs = new ArrayList<CompositeInterface>();
	}
	
	/**
	 * Manually adjust the height of the window before opening
	 * @param pxl
	 */
	public void adjustSize(int pxl){
		adjustSize = pxl;
	}
	
	/**
	 * Set the image icon
	 * @param c
	 * @param path
	 */
	public void setIcon(Class<?> c, String path){
		shell.setImage(SWTResourceManager.getImage(c, path));
	}
	
	/**
	 * Set the text of the accept button
	 * @param s
	 */
	public void setButtonText(String s){
		if(btnAccept!=null) btnAccept.setText(s);
		buttonText = s;
	}
	
	/**
	 * Add an input for numeric values (unlimited)
	 * @param name
	 */
	public void addIntegerInput(String name){
		result.put(name, 0);
		IntegerInputComposite integerInputComposite = new IntegerInputComposite(shell, name);
		allInputs.add(integerInputComposite);
	}
	
	/**
	 * Add an input for boolean values
	 * @param name
	 */
	public void addBooleanInput(String name){
		result.put(name, 0);
		BooleanInputComposite booleanInputComposite = new BooleanInputComposite(shell, name);
		allInputs.add(booleanInputComposite);
	}
	
	/**
	 * Add an input for numeric values between min and max
	 * @param name
	 * @param min
	 * @param max
	 */
	public void addIntegerInput(String name, int min, int max){
		result.put(name, 0);
		allInputs.add(new IntegerInputComposite(shell, name, min, max));
	}
	
	/**
	 * Add a text input
	 * @param name
	 */
	public void addStringInput(String name){
		result.put(name, "");
		StringInputComposite stringInputComposite = new StringInputComposite(shell, name);
		allInputs.add(stringInputComposite);
	}
	
	/**
	 * Add a text input with given format. See {@link com.ash.util.StringFormatCheck}
	 * @param name
	 * @param format
	 */
	public void addStringInput(String name, String format){
		result.put(name, "");
		StringInputComposite stringInputComposite = new StringInputComposite(shell, name, format);
		allInputs.add(stringInputComposite);
	}
	
	/**
	 * Add an input with multiple given choices
	 * @param name
	 * @param options
	 */
	public void addMultiInput(String name, String... options){
		result.put(name, "");
		MultiInputComposite stringInputComposite = new MultiInputComposite(shell, name, options);
		stringInputComposite.setBounds(3, 117, 340, 38);
		allInputs.add(stringInputComposite);
	}

	/**
	 * Launch the application.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		SWTLoader.loadSWT();
		
		InputInterface i = new InputInterface("Titel","Geben sie hier ihre Daten ein");
		
		i.addStringInput("Name","§§.$$$--§§§§§§§§§§§§");
		i.addStringInput("Vorname");
		i.addIntegerInput("Alter");
		i.addIntegerInput("Finger",0,10);
		i.addBooleanInput("Männlich?");
		i.addBooleanInput("Weiblich?");
		i.addMultiInput("Lieblingsfarbe","Rot","Blau","Grün");
		i.addMultiInput("Lieblingstier","Hund","Katze","Maus");
		i.setButtonText("ok");
		
		i.open();
		
		//System.out.println(i.open().get("Name"));
		
		//i.open();
		
		//InputInterfaceXml xml = i.toXml();//InputInterfaceXml.openFromFile();
		//xml.print();
		//xml.toInputInterface().open();
		
		//xml.saveAsFile();
		
		/*try {
			InputInterface window = new InputInterface();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Open the window. Returns a map with all submitted values. <br>Should be called last and after all adjustments.<br>Does not create a new thread.
	 * @throws Exception 
	 */
	public HashMap<String, Object> open() throws Exception {
		
		if(shell==null || result==null)
			throw new Exception("You cannot call the default constructor on InputInterface (unless loading XML)");
		
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		shell.forceActive();
		while (!shell.isDisposed() && open) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		if(open && shell.isDisposed())
			return null;
		
		
		for(CompositeInterface c : allInputs){
			result.put(c.getObjectName(), c.getInput());
		}
		
		shell.dispose();
		return result;
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell.setSize(368, getSize());
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent arg0) {
				shell.setSize(368, getSize());
			}
		});
		
		Button plh = new Button(shell, SWT.NONE);
		plh.setLayoutData(new RowData(354, SWT.DEFAULT));
		plh.setVisible(false);
		
		btnAccept = new Button(shell, SWT.NONE);
		btnAccept.setLayoutData(new RowData(347, SWT.DEFAULT));
		btnAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean allInputsValid = true;
				for(CompositeInterface c : allInputs){
					if(c instanceof StringInputComposite){
						StringInputComposite sic = (StringInputComposite) c;
						try {
							sic.validFormat();
							if(!sic.format.equals("") && sic.getInput().equals("")){
								JOptionPane.showMessageDialog(null, sic.getObjectName() + " can't be empty", "Wrong Format", JOptionPane.ERROR_MESSAGE);
								allInputsValid = false;
							}
						} catch (FormatException e) {
							allInputsValid = false;
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "In field: " + sic.getObjectName() + "\n" + e.getMessage() + "\nFormat for this field (example): " + StringInputComposite.toReadableFormat(sic.format), "Wrong Format", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				if(allInputsValid)
					open = false;
			}
		});
		btnAccept.setText(buttonText);
		
		//shell.setLayout(new AbsoluteLayout());
		//shell.setSize(368, btnAccept.getBounds().y + btnAccept.getBounds().height + 3);
		
		//shell.pack(true);
	}
	
	public int getSize(){
		int size = allInputs.size() * 40;
		size += 41 + 3 ; //Top-Margin
		size += 49; //Accept-Button
		size += lbl == null ? 0 : (lbl.getText().split("\n").length+1) * 11;
		size += adjustSize;
		return size;
	}
	
	public static boolean debugXMLConversion = true;
	/**
	 * @return Current instance as Xml-instance which can be saved to a xml-file.
	 */
	public InputInterfaceXml toXml(){
		
		if(debugXMLConversion) System.out.println("Marshalling:");
		
		ArrayList<IntegerInput> ii = new ArrayList<IntegerInput>();
		ArrayList<LimitedIntegerInput> lii = new ArrayList<LimitedIntegerInput>();
		ArrayList<StringInput> si = new ArrayList<StringInput>();
		ArrayList<MultiInput> mi = new ArrayList<MultiInput>();
		ArrayList<BooleanInput> bi = new ArrayList<BooleanInput>();
		ArrayList<FormatStringInput> fsi = new ArrayList<FormatStringInput>();
		
		for(CompositeInterface ci : allInputs){
			if(debugXMLConversion) System.out.print("CompositeInterface \"" + ci.getObjectName() + "\"\t");
			if(ci instanceof IntegerInputComposite && ((IntegerInputComposite) ci).min == Integer.MIN_VALUE){
				if(debugXMLConversion) System.out.println("ci instanceof IntegerInputComposite");
				ii.add(new IntegerInput(ci.getObjectName()));
				continue;
			} 
			if(ci instanceof IntegerInputComposite){
				if(debugXMLConversion) System.out.println("ci instanceof LimitedIntegerInput");
				IntegerInputComposite ci0 = (IntegerInputComposite) ci;
				lii.add(new LimitedIntegerInput(ci.getObjectName(), ci0.min, ci0.max));
				continue;
			} 
			if(ci instanceof StringInputComposite && !((StringInputComposite) ci).format.equals("")){
				if(debugXMLConversion) System.out.println("ci instanceof FormatStringInput");
				fsi.add(new FormatStringInput(ci.getObjectName(), ((StringInputComposite) ci).format));
				continue;
			} 
			if(ci instanceof StringInputComposite){
				if(debugXMLConversion) System.out.println("ci instanceof StringInput");
				si.add(new StringInput(ci.getObjectName()));
				continue;
			} 
			if(ci instanceof MultiInputComposite){
				if(debugXMLConversion) System.out.println("ci instanceof MultiInput");
				mi.add(new MultiInput(ci.getObjectName(), ((MultiInputComposite) ci).options));
				continue;
			} 
			if(ci instanceof BooleanInputComposite){
				if(debugXMLConversion) System.out.println("ci instanceof BooleanInput");
				bi.add(new BooleanInput(ci.getObjectName()));
			}
		}
		IntegerInput[] integerInputs = null;
		LimitedIntegerInput[] limitedIntegerInputs = null;
		StringInput[] stringInputs = null;
		MultiInput[] multiInputs = null;
		BooleanInput[] booleanInputs = null;
		FormatStringInput[] formatStringInputs = null;
		
		if(ii.size()>0){
			if(debugXMLConversion) System.out.println("IntegerInput:");
			integerInputs = new IntegerInput[ii.size()];
			for(int i=0; i<ii.size(); i++){
				integerInputs[i] = ii.get(i);
				if(debugXMLConversion) System.out.println("\tPut: " + ii.get(i).toString() + " into " + integerInputs[i].toString());
			}
		}
		if(lii.size()>0){
			if(debugXMLConversion) System.out.println("LimitedIntegerInput:");
			limitedIntegerInputs = new LimitedIntegerInput[lii.size()];
			for(int i=0; i<lii.size(); i++){
				limitedIntegerInputs[i] = lii.get(i);
				if(debugXMLConversion) System.out.println("\tPut: " + lii.get(i).toString() + " into " + limitedIntegerInputs[i].toString());
			}
		}
		if(si.size()>0){
			if(debugXMLConversion) System.out.println("StringInput:");
			stringInputs = new StringInput[si.size()];
			for(int i=0; i<si.size(); i++){
				stringInputs[i] = si.get(i);
				if(debugXMLConversion) System.out.println("\tPut: " + si.get(i).toString() + " into " + stringInputs[i].toString());
			}
		}
		if(mi.size()>0){
			if(debugXMLConversion) System.out.println("MultiInput:");
			multiInputs = new MultiInput[mi.size()];
			for(int i=0; i<mi.size(); i++){
				multiInputs[i] = mi.get(i);
				if(debugXMLConversion) System.out.println("\tPut: " + mi.get(i).toString() + " into " + multiInputs[i].toString());
			}
		}
		if(bi.size()>0){
			if(debugXMLConversion) System.out.println("BooleanInput:");
			booleanInputs = new BooleanInput[bi.size()];
			for(int i=0; i<bi.size(); i++){
				booleanInputs[i] = bi.get(i);
				if(debugXMLConversion) System.out.println("\tPut: " + bi.get(i).toString() + " into " + booleanInputs[i].toString());
			}
		}
		if(fsi.size()>0){
			if(debugXMLConversion) System.out.println("FormatStringInput:");
			formatStringInputs = new FormatStringInput[bi.size()];
			for(int i=0; i<bi.size(); i++){
				formatStringInputs[i] = fsi.get(i);
				if(debugXMLConversion) System.out.println("\tPut: " + fsi.get(i).toString() + " into " + formatStringInputs[i].toString());
			}
		}
		
		InputInterfaceXml iix = new InputInterfaceXml(title, desc, buttonText, integerInputs, limitedIntegerInputs, stringInputs, multiInputs, booleanInputs, formatStringInputs);
		
		return iix;
	}
	
	/**
	 * MSGTYPEs for message box
	 */
	public static enum MSGTYPE {
		ERROR, WARNING, INFO, QUESTION;
	}
	
	/**
	 * Shows a message box
	 * @param message
	 * @param title
	 * @param type
	 */
	public static void message(String message, String title, MSGTYPE type){
		int i = type == MSGTYPE.ERROR ? JOptionPane.ERROR_MESSAGE : type == MSGTYPE.INFO ? JOptionPane.INFORMATION_MESSAGE : type == MSGTYPE.WARNING ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE;
		JOptionPane.showMessageDialog(null, message, title, i);
	}
	
	/**
	 * Shows a color chooser widget
	 * @param title
	 * @param startColor
	 * @return the chosen color
	 */
	public static Color colorChooser(String title, Color startColor){
		return JColorChooser.showDialog(null, "Choose a Color", startColor);
	}
	
	/**
	 * Shows a color chooser widget
	 * @param title
	 * @return the chosen color
	 */
	public static Color colorChooser(String title){
		return JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
	}
	
	/**
	 * Used to check the input of the InputInterface instance after it was closed <br>(can only be used after calling open())<br>
	 * Checks if the parameter submitted for s (parameter-name) equals c.
	 * @param s
	 * @param c
	 * @return
	 */
	public boolean equals(String s, String c){
		return this.result.get(s).toString().equals(c);
	}
	
	/**
	 * Converts xml-String to InputInterface instance by using the InputInterfaceXml class.
	 * @param xml
	 * @return
	 * @throws JAXBException
	 */
	public static InputInterface fromXml(String xml) throws JAXBException{
		return InputInterfaceXml.interfaceFromXml(xml);
	}
	
	/**
	 * Converts xml-file to InputInterface instance by using the InputInterfaceXml class.
	 * @param xml
	 * @return
	 * @throws JAXBException
	 */
	public static InputInterface fromXml(File xml){
		return InputInterfaceXml.interfaceFromXml(xml);
	}
	
	/*
	public void allCheckbuttonsAreRadiobuttons(boolean b){
		if(b){
			for(CompositeInterface ci : allInputs){
				if(ci instanceof BooleanInputComposite){
					BooleanInputComposite bic = (BooleanInputComposite) ci;
					String name = bic.getBtnInput().getText();
					String toolTip = bic.getBtnInput().getToolTipText();
					org.eclipse.swt.graphics.Rectangle bounds = bic.getBtnInput().getBounds();
					
					Button newButton = new Button(bic, SWT.RADIO);
					newButton.setText(name);
					newButton.setToolTipText(toolTip);
					newButton.setBounds(bounds);
					bic.setBtnInput(newButton);
				}
			}
		}
	}*/
}
