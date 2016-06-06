package com.ash.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;

/**
 * @author Aaron Hilbig
 *
 */
class MultiInputComposite extends Composite implements CompositeInterface<String> {
	
	private String objectName;
	private Combo comboInput;
	public String[] options;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MultiInputComposite(Composite parent, String name, String... options) {
		super(parent, SWT.NONE);
		this.setSize(350, 50);
		setLayout(null);
		
		this.options = options;
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(7, 17, 91, 15);
		lblName.setText(name);
		lblName.setToolTipText(name);
		
		comboInput = new Combo(this, SWT.NONE);
		comboInput.setItems(options);
		comboInput.setBounds(104, 15, 236, 21);
		comboInput.select(0);
		comboInput.setToolTipText(name);
		
		objectName = name;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public String getInput() {
		return this.comboInput.getItem(comboInput.getSelectionIndex());
	}

	@Override
	public String getObjectName() {
		return objectName;
	}
}
