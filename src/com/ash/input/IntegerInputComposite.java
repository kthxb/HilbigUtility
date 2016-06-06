package com.ash.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Aaron Hilbig
 *
 */
class IntegerInputComposite extends Composite implements CompositeInterface<Integer> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	private Spinner spinner;
	private String objectName;
	public int min, max;
	
	public IntegerInputComposite(Composite parent, String name) {
		this(parent, name, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */
	public IntegerInputComposite(Composite parent, String name, int min, int max) {
		super(parent, SWT.NONE);
		this.setSize(350, 50);
		setLayout(null);
		
		this.min = min;
		this.max = max;
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setBounds(7, 17, 91, 15);
		lblName.setText(name);
		lblName.setToolTipText(name);
		
		spinner = new Spinner(this, SWT.BORDER);
		spinner.setBounds(104, 15, 236, 21);
		spinner.setMinimum(min);
		spinner.setMaximum(max);
		spinner.setToolTipText(name + (min!=Integer.MIN_VALUE ? (" (from min. " + min + " to max. " + max + ")") : ""));
		
		this.objectName = name;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public Integer getInput() {
		return spinner.getSelection();
	}

	@Override
	public String getObjectName() {
		return objectName;
	}
}
