package com.ash.input.hggui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.ash.bot.Bot;
import com.ash.util.files.FileManager;

public abstract class HWindow extends JFrame {
	
	private Container surface;
	
	private Bot b;
	private boolean listenerActive;
	
	private ArrayList<Component> allComponents;
	
	private TrayIcon trayIcon;
	
	/**
	 * Creates a new HWindow, a subclass of JFrame with extra functionality. Optimized for use with {@link HButton}s
	 * @param title
	 * @param backgroundcolor
	 */
	public HWindow(String title, Color backgroundcolor){
		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setBackground(backgroundcolor);
		setBounds(100, 100, 400, 300);
		
		setSurface(getContentPane());
		getSurface().setBackground(backgroundcolor);
		
		allComponents = new ArrayList<>();
		createContent();
		
		getSurface().setCursor(getToolkit().createCustomCursor(FileManager.getImageFromSourceFolder("com/ash/input/hggui/cursor.png"), new Point(0,0), "Cursor"));
		
		SystemTray tray = SystemTray.getSystemTray();
		PopupMenu p = new PopupMenu();
		MenuItem open = new MenuItem("Open");
		open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(true);
				tray.remove(trayIcon);
			}
		});
		p.add(open);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Image i = null;
		try {
			i = ImageIO.read(loader.getResource("com/ash/input/inputinterface.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		trayIcon = new TrayIcon(i, title, p);
	}
	
	/**
	 * @param i NativeKeyEvent-KeyCode
	 * @param c
	 */
	public void addHotkey(int i, Consumer<Object> c){
		b.setHotkey(i, c);
		if(!listenerActive){
			listenerActive = true;
			b.activateListener();
		}
	}
	
	public void disableHotkeys(){
		listenerActive = false;
		b.deactivateListener();
	}
	
	/**
	 * All components should be initialized here and added to the surface by using<br><pre>surface.add(comp);</pre><br>HButton-Objects should
	 * be added by using <br><pre>hbutton.addTo(surface);</pre><br>Also, all Components and <b>at least all HButtons</b> should be added to the allComponents-list<br>
	 * by using<br><pre>allComponents.add(hbutton);</pre>
	 */
	public abstract void createContent();
	
	/**
	 * Works only for HButtons that have this window as parent.
	 * @param btnName
	 * @param e
	 */
	public abstract void onButtonClick(String btnName, MouseEvent e);
	
	/*public void createContent(){/*
		/*Canvas c = new Canvas();
		c.setBounds(10, 10, 100, 100);
		c.setBackground(Color.black);
		surface.add(c);
		
		Canvas c1 = new Canvas();
		c1.setBounds(10, 10, 100, 100);
		c1.setBackground(Color.red);
		surface.add(c1);
		*//*
		
		HButton b = new HButton("hallo!",150, 50, Color.RED, new Font("Arial",1,15));
		b.setHoverColor(new EZRGB(255,100,100).toColor());
		surface.add(b.drawable);
		
		HButton b1 = new HButton("Tschüss!",150, 50, Color.BLUE, new Font("Arial",1,15));
		b1.setHoverColor(new EZRGB(100,100,255).toColor());
		surface.add(b1.drawable);
		
		HButton b2 = new HButton("3",150, 50, Color.GREEN, new Font("Arial",1,15));
		b2.setHoverColor(new EZRGB(255,255,100).toColor());
		//b2.setEnabled(false);
		//surface.add(b2.drawable);
		b2.addTo(surface);
		
		allComponents.add(b);
		allComponents.add(b1);
		allComponents.add(b2);
	}*/
	
	@Override
	public void validate(){
		super.validate();
		for(Component c : allComponents){
			if(c instanceof HButton){
				((HButton) c).resetHover();
				((HButton) c).updateCanvas();
			}
		}
	}
	
	public void open(){
		setVisible(true);
	}
	
	public void centerOnScreen(){
		//TODO
	}
	
	public void hideToSystemTray(Image trayIcon) throws AWTException{
		setVisible(false);
		if(trayIcon!=null){
			this.trayIcon.setImage(trayIcon);
		}
		SystemTray tray = SystemTray.getSystemTray();
		tray.add(this.trayIcon);
	}
	
	public void setCursor(BufferedImage img, Point hotspot){
		getSurface().setCursor(getToolkit().createCustomCursor(img, hotspot, "Cursor"));
	}

	public Container getSurface() {
		return surface;
	}

	public void setSurface(Container surface) {
		this.surface = surface;
	}
	
	/*
	public static void main(String[] args) throws IOException, InterruptedException {
		/*EZRGB red = new EZRGB(255,0,50);
		EZRGB other = new EZRGB(40,255,220);
		/*for(float f = 0; f<1.05F; f+=0.05F){
			red.fadeColorHue(other, f).display(true, true, (int) (f * 500), 200);
			red.fadeColorRGB(other, f).display(true, true, (int) (f * 500), 100);
		}*//*
		red.display(true,true,100,100);
		other.display(true,true,100,200);
		red.mixRGB(other).display(true,true,100,300);
		red.mixHSB(other).display(true,true,100,400);*//*
		//System.exit(0);
		
		//System.out.println(FileManager.getTreeVision(FileManager.getDirectoryWithDialog("", "", null, null)));
		
		Window w = new Window("Title", Color.WHITE);
		w.open();
	}*/

}
