package com.ash.util.math;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 * Can perform gaussian elimination on any value set and calculate functions of first, second, third degree through any given points.<br>
 * @author Aaron Hilbig
 *
 */
public class GaussianElimination {

	private static final double EPSILON = 1e-10;

	private static Logger logger = Logger.getLogger("GaussianElimination");

	static {
		logger.setLevel(Level.OFF);
	}

	public static void log(boolean b) {
		if (b)
			logger.setLevel(Level.FINE);
		else
			logger.setLevel(Level.OFF);
	}

	private static double[] lsolve(double[][] A, double[] b) {
		int N = b.length;

		for (int p = 0; p < N; p++) {

			int max = p;
			for (int i = p + 1; i < N; i++) {
				if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
					max = i;
				}
			}
			double[] temp = A[p];
			A[p] = A[max];
			A[max] = temp;
			double t = b[p];
			b[p] = b[max];
			b[max] = t;

			if (Math.abs(A[p][p]) <= EPSILON) {
				throw new RuntimeException("Matrix is singular or nearly singular");
			}

			for (int i = p + 1; i < N; i++) {
				double alpha = A[i][p] / A[p][p];
				b[i] -= alpha * b[p];
				for (int j = p; j < N; j++) {
					A[i][j] -= alpha * A[p][j];
				}
			}
		}

		// back substitution
		double[] x = new double[N];
		for (int i = N - 1; i >= 0; i--) {
			double sum = 0.0;
			for (int j = i + 1; j < N; j++) {
				sum += A[i][j] * x[j];
			}
			x[i] = (b[i] - sum) / A[i][i];
		}
		return x;
	}

	/**
	 * Gibt eine Funktion ersten Grades (der Geraden) durch den Ursprung und den
	 * angegebenen Punkt als String zurück<br>
	 * f(x) = ax + c
	 * 
	 * @param p
	 * @param q
	 * @return
	 */
	public static String functionThroughPoints(Point p) {
		return functionThroughPoints(new Point(0, 0), p);
	}

	/**
	 * Gibt eine Funktion ersten Grades (der Geraden) durch die angegebenen
	 * Punkte als String zurück<br>
	 * f(x) = ax + c
	 * 
	 * @param p
	 * @param q
	 * @return
	 */
	public static String functionThroughPoints(Point p, Point q) {
		// ax²+bx+c
		// p.y = a(p.x)² + b*(p.x) + c

		Point left = p.x < q.x ? p : q;
		Point right = left.equals(p) ? q : p;

		double diffX = right.x - left.x;
		double diffY = right.y - left.y;
		double a = diffY / diffX;
		double c = left.y - (a * left.x);

		//logger.info("y = " + a + "x + " + c);
		
		String function = new FunctionGenerator("f(x) = ", FunctionGenerator.PARAM_FUNCTION, a, c).function;
		logger.info(function);
		
		/*return "f(x) = " + (a != 0 || a == 1 ? (" + " + (a != 1 ? a : "") + "x") : "")
				+ (c != 0 && c > 0 ? (" + " + c) : c != 0 ? (" " + c) : "");*/
		return function;
	}

	/**
	 * Gibt eine Funktion zweiten Grades durch die angegebenen Punkte als String
	 * zurück<br>
	 * f(x) = ax²+bx+c
	 * 
	 * @param p
	 * @param q
	 * @param r
	 * @return
	 */
	public static String functionThroughPoints(Point p, Point q, Point r) {
		// ax²+bx+c
		// p.y = a(p.x)² + b*(p.x) + c

		double[][] A = { { Math.pow(p.x, 2), p.x, 1 }, { Math.pow(q.x, 2), q.x, 1 }, { Math.pow(r.x, 2), r.x, 1 } };

		double[] solve = { p.y, q.y, r.y };
		double[] x = lsolve(A, solve);

		for (int i = 0; i < x.length; i++) {
			logger.fine("" + x[i]);
		}

		double a = x[0], b = x[1], c = x[2];

		/*String function = "f(x) = " + (a != 0 ? ((a != 1 ? a : "") + "x²") : "")
				+ (b != 0 || b == 1 ? (" + " + (b != 1 ? b : "") + "x") : "") + (c != 0 ? (" + " + c) : "");*/
		
		String function = new FunctionGenerator("f(x) = ", FunctionGenerator.PARAM_SECOND_DEGREE_FUNCTION, a, b, c).function;

		logger.info(function);

		return function;
	}

	/**
	 * Gibt eine Funktion dritten Grades durch die angegebenen Punkte als String
	 * zurück<br>
	 * Werte unter 2^-12 werden auf null abgerundet, da es sich bei diesen meist
	 * um<br>
	 * Rundungsfehler handelt.
	 * 
	 * @param p
	 * @param q
	 * @param r
	 * @param s
	 * @return
	 */
	public static String functionThroughPoints(Point p, Point q, Point r, Point s) {
		// ax²+bx+c
		// p.y = a(p.x)² + b*(p.x) + c

		double[][] A = { { Math.pow(p.x, 3), Math.pow(p.x, 2), p.x, 1 }, { Math.pow(q.x, 3), Math.pow(q.x, 2), q.x, 1 },
				{ Math.pow(r.x, 3), Math.pow(r.x, 2), r.x, 1 }, { Math.pow(s.x, 3), Math.pow(s.x, 2), s.x, 1 } };

		double[] solve = { p.y, q.y, r.y, s.y };
		double[] x = lsolve(A, solve);

		for (int i = 0; i < x.length; i++) {
			logger.fine("" + x[i]);
		}

		double a = x[0], b = x[1], c = x[2], d = x[3];

		a = a < Math.pow(2, -12) ? 0 : a;
		b = b < Math.pow(2, -12) ? 0 : b;
		c = c < Math.pow(2, -12) ? 0 : c;
		d = d < Math.pow(2, -12) ? 0 : d;

		/*String function = "f(x) = " + (a != 0 ? ((a != 1 ? a : "") + "x³") : "")
				+ (b != 0 ? (" + " + (b != 1 ? b : "") + "x²") : "")
				+ (c != 0 || c == 1 ? (" + " + (c != 1 ? c : "") + "x") : "") + (d != 0 ? (" + " + d) : "");
		*/
		String function = new FunctionGenerator("f(x) = ", FunctionGenerator.PARAM_CUBIC_FUNCTION, a, b, c, d).function;

		logger.info(function);

		return function;
	}

	public static Point midPoint(Point... points) {
		Point[] important = importantPoints(points);
		Point avrg = new Point(0, 0);
		int div = 0;
		for (Point p : important) {
			div++;
			avrg.x += p.x;
			avrg.y += p.y;
		}
		avrg.x /= div;
		avrg.y /= div;
		return avrg;
	}

	public static Point[] importantPoints(Point... points) {
		Point farLeft = null;
		Point farRight = null;
		Point highest = null;
		Point lowest = null;
		for (Point p : points) {
			if (farLeft == null || p.x < farLeft.x)
				farLeft = p;
			if (farRight == null || p.x > farRight.x)
				farRight = p;
			if (highest == null || p.y > highest.y)
				highest = p;
			if (lowest == null || p.y < lowest.y)
				lowest = p;
		}
		Point[] p = new Point[4];
		p[0] = farLeft;
		p[1] = farRight;
		p[2] = highest;
		p[3] = lowest;
		String info = "farLeft: " + farLeft.toString().replace("java.awt.Point", "").replace("x=", "").replace("y=", "")
				+ ", farRight: " + farRight.toString().replace("java.awt.Point", "").replace("x=", "").replace("y=", "")
				+ ", highest: " + highest.toString().replace("java.awt.Point", "").replace("x=", "").replace("y=", "")
				+ ", lowest: " + lowest.toString().replace("java.awt.Point", "").replace("x=", "").replace("y=", "");
		logger.info(info);
		return p;
	}

	public static int numberOfIndividualPoints(Point... points) {
		ArrayList<Point> checked = new ArrayList<Point>();
		int ret = 0;
		for (Point p : points)
			if (!checked.contains(p)) {
				ret++;
				checked.add(p);
			}
		return ret;
	}

	public static void main(String[] args) {
		/*
		 * int N = 4; double[][] A = { { 27, 9, 3, 1 }, { 64, 16, 4, 1 }, { 48,
		 * 8, 1, 0}, { -27, 9, -3, 1 } }; double[] b = { 7, 1, 0 , 8}; double[]
		 * x = lsolve(A, b);
		 * 
		 * 
		 * // print results for (int i = 0; i < N; i++) {
		 * System.out.println(x[i]); }
		 */

		log(true);

		functionThroughPoints(new Point(1, 1));

		functionThroughPoints(new Point(1, 1), new Point(3, 3.5D));

		functionThroughPoints(new Point(1, 1), new Point(-1, 1), new Point(2, 4));

		// functionThroughPoints(new Point(-2, 8), new Point(0, 2), new Point(2,
		// 8));

		functionThroughPoints(new Point(-3, -54), new Point(-2, -16), new Point(0, 0), new Point(2, 16));

		// functionThroughPoints(new Point(-3,-44), new Point(1,4), new
		// Point(-1,0), new Point(3,64));

		// importantPoints(new Point(-3,-44), new Point(1,4), new Point(-1,0),
		// new Point(3,64));

		// logger.info("MidPoint: " + midPoint(importantPoints(new
		// Point(-3,-44), new Point(1,4), new Point(-1,0), new Point(3,64))));

		// logger.info("" + numberOfIndividualPoints(importantPoints(new
		// Point(-3,-44), new Point(1,4), new Point(-1,0), new Point(3,64))));

		functionThroughPoints(new Point(1, 1));

		// logger.info(hex(91));
		// logger.info(time() + " " + date());

		//System.out.println(toFunction("x²,x, ", -1.0D, -2.5D, 0.0D));
	}

	public static class Point {
		public double x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "P[" + x + ", " + y + "]";
		}
	}
	
/*
	public static String toFunction(String params, double... x) {
		String[] param = params.split(",");
		String ret = "f(x) = ";
		int num = 0;
		for (double x0 : x) {
			if (x0 != 0 && x0 > 0 && num != 0) {
				ret += " + " + x0 + param[num];
			} else if (x0 != 0 && x0 > 0 && num == 0) {
				ret += x0 + param[num];
			} else if (x0 != 0 && x0 < 0 && num != 0) {
				ret += " - " + Math.abs(x0) + param[num];
			} else if (x0 != 0 && x0 < 0 && num == 0) {
				ret += "- " + Math.abs(x0) + param[num];
			}
			num++;
		}
		return ret;
	}*/

}