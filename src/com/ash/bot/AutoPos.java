package com.ash.bot;

import java.awt.AWTException;
import java.awt.Point;
import java.util.ArrayList;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * @author Aaron Hilbig
 * 
 * Little program for non-API usage.<br>
 * Used for capturing screen coordinates / rectangles
 *
 */
public class AutoPos {
	
	private Bot b;
	
	private ArrayList<Point> pointList;
	
	public String result;
	
	public boolean done = false;
	
	public static void main(String[] args) throws AWTException {
		AutoPos ap = new AutoPos();
	}
	
	public AutoPos() throws AWTException{
		b = new Bot();
		pointList = new ArrayList<>();
		b.setHotkey(NativeKeyEvent.VC_1, (o) -> {
			addPosition(b.getMousePos().x,b.getMousePos().y);
		});
		b.setHotkey(NativeKeyEvent.VC_ESCAPE, (o) -> {
			showResult();
			b.deactivateListener();
			done = true;
		});
		b.activateListener();
	}

	public String showResult() {
		String ret = "";
		if(pointList.size()%2==0){
			Point lastPoint = null;
			int i = 0;
			for(Point p : pointList){
				if(lastPoint == null){
					lastPoint = p;
				} else {
					i++;
					System.out.println("Rectangle #" + i + ": new Rectangle(" + lastPoint.x + ", " + lastPoint.y + ", " + (p.x - lastPoint.x) + ", " + (p.y - lastPoint.y) + ");");
					ret += "Rectangle #" + i + ": new Rectangle(" + lastPoint.x + ", " + lastPoint.y + ", " + (p.x - lastPoint.x) + ", " + (p.y - lastPoint.y) + ");\n";
					lastPoint = null;
				}
			}
		} else if(pointList.size()==1){
			System.out.println("Point: new Point(" + pointList.get(0).x + ", " +  pointList.get(0).y + ");");
			ret+="Point: new Point(" + pointList.get(0).x + ", " +  pointList.get(0).y + ");\n";
		}
		result = ret;
		done = true;
		return ret;
	}

	public void addPosition(int x, int y) {
		pointList.add(new Point(x,y));
	}
	
}
