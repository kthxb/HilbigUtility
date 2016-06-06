package com.ash.util.math;

public class Point<T extends Number> {
	
	public T x, y;
	
	public Point(T x, T y){
		this.x = x;
		this.y = y;
	}
	
	public java.awt.Point toPoint(){
		return new java.awt.Point((int)x, (int)y);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setLocation(Point p){
		x = (T) p.x;
		y = (T) p.y;
	}
	
	public void setLocation(T x, T y){
		this.x = (T) x;
		this.y = (T) y;
	}
	
	/*
	private Class getType(){
		if(x instanceof Integer){
			return Integer.class;
		}
		return Integer.class;
	}*/

}
