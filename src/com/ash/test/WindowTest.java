package com.ash.test;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import com.ash.input.hggui.HButton;
import com.ash.input.hggui.HWindow;
import com.ash.util.files.EZRGB;

@Deprecated
public class WindowTest extends HWindow {

	public WindowTest(String title, Color backgroundcolor) {
		super(title, backgroundcolor);
	}

	@Override
	public void createContent() {
		HButton b = new HButton("hallo!",150, 50, Color.RED, new Font("Arial",1,15),this);
		b.setHoverColor(new EZRGB(255,100,100).toColor());
		//b.addTo(getSurface());
		b.addTo(getSurface());
		
		HButton b1 = new HButton("Tschüss!",150, 50, Color.BLUE, new Font("Arial",1,15),this);
		b1.setHoverColor(new EZRGB(100,100,255).toColor());
		b1.addTo(getSurface());
		
		HButton b2 = new HButton("3",150, 50, Color.GREEN, new Font("Arial",1,15),this);
		b2.setHoverColor(new EZRGB(255,255,100).toColor());
		//b2.setEnabled(false);
		//surface.add(b2.drawable);
		b2.addTo(getSurface());
		//getSurface().add(b2);
	}
	
	public static void main(String[] args) throws AWTException {
		WindowTest w = new WindowTest("Test", Color.WHITE);
		w.open();
	}

	@Override
	public void onButtonClick(String btnName, MouseEvent e) {
		System.out.println(btnName);
	}

}
