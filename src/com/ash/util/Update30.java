package com.ash.util;

import java.util.function.BooleanSupplier;

/**
 * Calls the BooleanSupplier 30 times a second in a thread.
 * @author Aaron
 *
 */
public class Update30 extends Thread {
	
	public boolean run = true, debug = false;
	public BooleanSupplier c;
	
	public Update30(BooleanSupplier c){
		this.c = c;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 16666666.666666666D;
		double delta = 0.0D;
		while (run) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0D) {
				boolean b = c.getAsBoolean();
				if(debug)
					System.out.println("Aaron Hilbig / Update30 Success: " + b);
				delta -= 1.0D;
			}
		}
	}
	
}
