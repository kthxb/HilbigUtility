package com.ash.input;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import com.ash.util.math.Comparison;

/**
 * @author Aaron Hilbig
 *
 */
class StringInputComposite extends Composite implements CompositeInterface<String> {
	
	private Text textInput;
	private String objectName;
	String format = "";

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StringInputComposite(Composite parent, String name) {
		super(parent, SWT.NONE);
		this.setSize(350, 50);
		setLayout(null);
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(7, 17, 91, 15);
		lblName.setText(name);
		lblName.setToolTipText(name);
		
		textInput = new Text(this, SWT.BORDER);
		textInput.setBounds(104, 15, 236, 21);
		textInput.setToolTipText(name);
		
		this.format = "";
		
		objectName = name;
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StringInputComposite(Composite parent, String name,String format) {
		super(parent, SWT.NONE);
		this.setSize(350, 50);
		setLayout(null);
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(7, 17, 91, 15);
		lblName.setText(name);
		lblName.setToolTipText(name);
		
		textInput = new Text(this, SWT.BORDER);
		textInput.setBounds(104, 15, 236, 21);
		textInput.setToolTipText(name + " - Format: " + toReadableFormat(format));
		
		this.format = format;
		
		objectName = name;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public String getInput() {
		return this.textInput.getText();
	}

	@Override
	public String getObjectName() {
		return objectName;
	}
	
	public boolean hasFormat(){
		return !format.equals("");
	}
	
	public boolean validFormat() throws FormatException {
		return validFormatException(getInput(), format);
	}
	
	/**
	 * Checks if String s is formatted like format<br>
	 * Valid formatting:<br>
	 * '@': any letter or number<br>
	 * '§': only letters<br>
	 * '$': only numbers
	 * 
	 * @param s
	 * @param format
	 * @return
	 */
	public static boolean validFormat(String s, String format){
		boolean valid = true;
		for(int i = 0; i<s.length(); i++){
			String org = "" + s.charAt(i);
			String form;
			try {
				form = "" + format.charAt(i);
			} catch(StringIndexOutOfBoundsException e){
				return true;
			}
			if(form.equals("@"))
				valid = true;
			else if(form.equals("§") && !Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals("$") && Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals(org))
				valid = true;
			else return false;
		}
		return valid;
	}
	
	/**
	 * Checks if String s is formatted like format<br>
	 * Valid formatting:<br>
	 * '@': any letter or number<br>
	 * '§': only letters<br>
	 * '$': only numbers
	 * 
	 * @param s
	 * @param format
	 * @return
	 * @throws FormatException 
	 */
	public static boolean validFormatException(String s, String format) throws FormatException{
		boolean valid = true;
		for(int i = 0; i<s.length(); i++){
			String org = "" + s.charAt(i);
			String form;
			try {
				form = "" + format.charAt(i);
			} catch(StringIndexOutOfBoundsException e){
				return true;
			}
			if(form.equals("@"))
				valid = true;
			else if(form.equals("§") && !Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals("$") && Comparison.equalsOr(org, "0","1","2","3","4","5","6","7","8","9"))
				valid = true;
			else if(form.equals(org))
				valid = true;
			else
				throw new FormatException("Invalid format at index " + i + ": Allowed format: " + form + " (" + (form.equals("§") ? "Any letter, no numbers" : form.equals("$") ? "Only numbers" : ("Can only be '" + form + "'")) + ")");
		}
		return valid;
	}
	
	public static class FormatException extends Exception {
		private static final long serialVersionUID = 1L;
		public FormatException(String s){
			super(s);
		}
	}
	
	public static String toReadableFormat(String format){
		String ret = "";
		int number = 0;
		for(int i = 0; i<format.length(); i++){
			String form = "" + format.charAt(i);
			if(form.equals("@")){
				String a = Comparison.alphabet[i*2];
				ret += a;
			} else if(form.equals("§")){
				String a = Comparison.alphabet[i*2+1];
				ret += a;
			}
			else if(form.equals("$")){
				number++;
				if(number==10)
					number = 1;
				ret += number;
			}
			else ret += form;
		}
		return ret;
	}
}
