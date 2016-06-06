package com.ash.util;

/**
 * A simple integrated stopwatch for time measurement
 * @author Aaron Hilbig
 *
 */
public class Stopwatch {
	
	public static void main(String[] args) throws InterruptedException {
		Stopwatch stw = new Stopwatch();
		stw.start();
		Thread.sleep(1000);
		stw.stop();
		System.out.println(stw.getTimeInSeconds());
	}
	
	private double startTimeD, stopTimeD;
	boolean running = false;
	
	public Stopwatch(){
		
	}
	
	public double currentTimeInMillis(){
		if(running){
			return stopTimeD - System.currentTimeMillis();
		} else 
			return 0;
	}
	
	public double currentTimeInSeconds(){
		return currentTimeInMillis() / 1000;
	}
	
	public void start(){
		//startTime = new Date();
		startTimeD = System.currentTimeMillis();
		running = true;
	}
	
	public void stop(){
		if(running){
			//stopTime = new Date();
			stopTimeD = System.currentTimeMillis();
			running = false;
		} else
			throw new Error("Stopwatch wasn´t started before stopping");
	}
	
	public double getTimeInMillis(){
		if(running){
			throw new Error("Stopwatch is still running");
		} else {
			return stopTimeD - startTimeD;
		}
	}
	
	public double getTimeInSeconds(){
		if(running){
			throw new Error("Stopwatch is still running");
		} else {
			return (stopTimeD - startTimeD) / 1000;
		}
	}
	
}
