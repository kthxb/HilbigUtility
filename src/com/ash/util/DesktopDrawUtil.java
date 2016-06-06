package com.ash.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.event.TargetEvent;
import org.sikuli.api.event.TargetEventListener;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;

/**
 * Allows for drawing of Rectangles and Labels on the whole Desktop (w/out GUI)<br>
 * All entities must be added manually by using boxList.add() for rectangles, labeledBoxList.add() for labeled rectangles and msgList.add() for labels;<br><br>
 * Also has some image detection and other static functionality
 * 
 * @author Aaron Hilbig
 *
 */
public class DesktopDrawUtil {
	
	public ArrayList<Rectangle> boxList;
	public ArrayList<LabeledRectangle> labeledBoxList;
	public ArrayList<Message> msgList;
	public int duration = 5;
	
	public static class Message {
		public String s;
		public int x, y;
		
		public Message(String s, int x, int y) {
			this.s = s;
			this.x = x;
			this.y = y;
		}

	}
	
	public static class LabeledRectangle {
		public String s;
		public Rectangle r;
		
		public LabeledRectangle(String s, Rectangle r) {
			this.s = s;
			this.r = r;
		}
		
		public LabeledRectangle(String s, int x, int y, int w, int h) {
			this.s = s;
			r = new Rectangle(x,y,w,h);
		}
	}
	
	/**
	 * Creates new instance. Sets the time for how long the entities will be shown.
	 * @param duration
	 */
	public DesktopDrawUtil(int duration){
		labeledBoxList = new ArrayList<>();
		boxList = new ArrayList<>();
		msgList = new ArrayList<>();
		this.duration = duration;
		new DesktopScreenRegion();
	}
	
	/**
	 * Draw all entities for duration time
	 */
	public void display(){
		Canvas canvas = new DesktopCanvas();
		for(Rectangle r : boxList){
			ScreenRegion temp = new DesktopScreenRegion();
			temp.setBounds(r);
			canvas.addBox(temp);
		}
		for(LabeledRectangle r : labeledBoxList){
			ScreenRegion temp = new DesktopScreenRegion();
			temp.setBounds(r.r);
			canvas.addBox(temp);
			r.r.setBounds((int) r.r.getCenterX(),(int) r.r.getCenterY(), 1, 1);
			temp.setBounds(r.r);
			canvas.addLabel(temp, r.s);
		}
		for(Message s : msgList){
			ScreenRegion temp = new DesktopScreenRegion();
			temp.setBounds(new Rectangle(s.x, s.y, 1,1));
			canvas.addLabel(temp, s.s);
		}
		canvas.display(duration);
	}
	
	/*
	private void quickdrawBox(Rectangle r){
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				ScreenRegion temp = new DesktopScreenRegion();
				temp.setBounds(r);
				System.out.println(r);
				
				
				//canvas.display(duration);
			}
		});
		t.start();
	}
	
	private void quickdrawMessage(String s){
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				ScreenRegion temp = new DesktopScreenRegion();
				//temp.setBounds(10,10,10,10);
				Canvas canvas = new DesktopCanvas();
				canvas.addLabel(temp, s);
				canvas.display(duration);
			}
		});
		t.start();
	}*/
	
	public static void main(String[] args) {
		DesktopDrawUtil ddu = new DesktopDrawUtil(5);
		ddu.labeledBoxList.add(new LabeledRectangle("karte 1", 788, 862, 86, 107));
		ddu.labeledBoxList.add(new LabeledRectangle("karte 2", 895, 863, 86, 108)); //karte 2
		ddu.labeledBoxList.add(new LabeledRectangle("karte 3", 1000, 861, 89, 111)); //karte 3
		ddu.labeledBoxList.add(new LabeledRectangle("karte 4", 1107, 862, 85, 107)); //karte 4
		ddu.labeledBoxList.add(new LabeledRectangle("elixirleiste", 809, 991, 393, 24)); //elixirleiste
		ddu.msgList.add(new Message("hallo",70,70));
		ddu.display();
	}
	
	/**
	 * Finds an image on the (given region of the) screen and returns its prosition
	 * @param region
	 * @param img
	 * @return
	 */
	public static Rectangle findImageInDesktopRegion(Rectangle region, BufferedImage img){
		ScreenRegion s = new DesktopScreenRegion();
		s.setBounds(region);
		Target target = new ImageTarget(img);
		ScreenRegion r = s.find(target);
		return new Rectangle(r.getBounds().x,r.getBounds().y,r.getBounds().width,r.getBounds().height);
	}
	
	/**
	 * Finds an image on the (given region of the) screen and returns its prosition
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param img
	 * @return
	 */
	public static Rectangle findImageInDesktopRegion(int x, int y, int w, int h, BufferedImage img){
		ScreenRegion s = new DesktopScreenRegion();
		s.setBounds(new Rectangle(x,y,w,h));
		Target target = new ImageTarget(img);
		ScreenRegion r = s.find(target);
		return new Rectangle(r.getBounds().x,r.getBounds().y,r.getBounds().width,r.getBounds().height);
	}
	
	/**
	 * Fires event when image is detected on the (given region of the) screen.<br>Untested/Unverified so deprectated
	 * @param region
	 * @param img
	 * @param c
	 */
	@Deprecated
	public static void onImageDetection(Rectangle region, BufferedImage img, Consumer<TargetEvent> c){
		ScreenRegion s = new DesktopScreenRegion();
		s.setBounds(region);
		Target target = new ImageTarget(img);
		s.addTargetEventListener(target, new TargetEventListener(){
			@Override
			public void targetAppeared(TargetEvent e) {
				c.accept(e);
			}
			@Override
			public void targetMoved(TargetEvent e) {
			}
			@Override
			public void targetVanished(TargetEvent e) {
			}
		});
	}
	
	
	@Deprecated
	public static void drawBox(Rectangle r){
		ScreenRegion def = new DesktopScreenRegion();
		def.setBounds(r);
		Canvas canvas = new DesktopCanvas();
		canvas.addBox(def);
		canvas.display(5);
	}
	
	@Deprecated
	public static void drawMessage(String s, int x, int y){
		ScreenRegion def = new DesktopScreenRegion();
		Rectangle rect_ = new Rectangle();
		rect_.setBounds(x, y, 10, 10);
		def.setBounds(rect_);
		Canvas canvas = new DesktopCanvas();
		canvas.addLabel(def, s);
		canvas.display(5);
	}
	
	@Deprecated
	public static void drawMessage(String s){
		int x = 10;
		int y = 10;
		ScreenRegion def = new DesktopScreenRegion();
		Rectangle rect_ = new Rectangle();
		rect_.setBounds(x, y, 10, 10);
		def.setBounds(rect_);
		Canvas canvas = new DesktopCanvas();
		canvas.addLabel(def, s);
		canvas.display(5);
	}
	
	@Deprecated
	public static void drawMessage(String s, int time){
		int x = 10;
		int y = 10;
		ScreenRegion def = new DesktopScreenRegion();
		Rectangle rect_ = new Rectangle();
		rect_.setBounds(x, y, 10, 10);
		def.setBounds(rect_);
		Canvas canvas = new DesktopCanvas();
		canvas.addLabel(def, s);
		canvas.display(time);
	}
	
	/**
	 * Draws a message onto the screen. Should be deprecated but is actually quite useful
	 * @param s
	 * @param time
	 * @param x
	 * @param y
	 */
	public static void drawMessage(String s, int time, int x, int y){
		ScreenRegion def = new DesktopScreenRegion();
		Rectangle rect_ = new Rectangle();
		rect_.setBounds(x, y, 10, 10);
		def.setBounds(rect_);
		Canvas canvas = new DesktopCanvas();
		canvas.addLabel(def, s);
		canvas.display(time);
	}
	
}
