package com.ash.util.math;

/**
 * Supplier of chaos. Random. Entropy.
 * @author Aaron Hilbig
 *
 */
public class Random {
	
	public static int getRandom(int min, int max){
		java.util.Random rand = new java.util.Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Returns if value i is percent% near to 'to'.
	 * @param i
	 * @param to
	 * @param percent
	 * @return
	 */
	public static boolean isNear(int i, int to, float percent){
		float oneperc = to / 100;
		float percof = i / oneperc;
		if(percent*100<=percof) return true;
		return false;
	}
	
	/**
	 * Returns randomly true, by a chance of percent%.
	 * 100%
	 * @param percent
	 * @return
	 */
	public static boolean chance(float percent){
		double f = getRandom(0,100);
		if(f<=(percent*100))
			return true;
		return false;
	}
	
	/**
	 * Returns randomly true, by a chance of percent%.<br>
	 * 100.00%
	 * @param percent
	 * @return
	 */
	public static boolean exactChance(float percent){
		double f = getRandom(0,10000);
		if(f<=(percent*10000))
			return true;
		return false;
	}
	
}
