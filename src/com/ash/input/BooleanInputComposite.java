package com.ash.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;

/**
 * @author Aaron Hilbig
 *
 */
class BooleanInputComposite extends Composite implements CompositeInterface<Boolean> {

	private Button btnInput;
	public Button getBtnInput() {
		return btnInput;
	}
	public void setBtnInput(Button btnInput) {
		this.btnInput = btnInput;
	}

	private String objectName;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BooleanInputComposite(Composite parent, String name) {
		super(parent, SWT.NONE);
		this.setSize(350, 50);
		setLayout(null);
		
		btnInput = new Button(this, SWT.CHECK);
		btnInput.setBounds(104, 17, 333, 16);
		btnInput.setText(name);
		btnInput.setToolTipText(name);
		
		objectName = name;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public Boolean getInput() {
		return this.btnInput.getSelection();
	}
	
	@Override
	public String getObjectName() {
		return objectName;
	}
}
