package com.ash.util.math;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * Some useful math functions.
 * @author Aaron Hilbig
 *
 */
public class EZMath {
	
	public static void main(String[] args) {
		
		GrowthChart.main(null);
		
//		System.out.println(nonLinearSteep(0.0D) + "\t" + nonLinearFlat(0.0D));
//		System.out.println(nonLinearSteep(0.1D) + "\t" + nonLinearFlat(0.1D));
//		System.out.println(nonLinearSteep(0.2D) + "\t" + nonLinearFlat(0.2D));
//		System.out.println(nonLinearSteep(0.3D) + "\t" + nonLinearFlat(0.3D));
//		System.out.println(nonLinearSteep(0.4D) + "\t" + nonLinearFlat(0.4D));
//		System.out.println(nonLinearSteep(0.5D) + "\t" + nonLinearFlat(0.5D));
//		System.out.println(nonLinearSteep(0.6D) + "\t" + nonLinearFlat(0.6D));
//		System.out.println(nonLinearSteep(0.7D) + "\t" + nonLinearFlat(0.7D));
//		System.out.println(nonLinearSteep(0.8D) + "\t" + nonLinearFlat(0.8D));
//		System.out.println(nonLinearSteep(0.9D) + "\t" + nonLinearFlat(0.9D));
//		System.out.println(nonLinearSteep(1.0D) + "\t" + nonLinearFlat(1.0D));
		
		
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
	 * Use Math.abs() instead
	 * @param i
	 * @return Betrag von i
	 */
	public static int betrag(int i){
		return i > 0 ? i : (i*(-1));
	}
	
	/**
	 * Use Math.min() instead
	 * Makes all values<0 return 0;
	 * -> prevents negative values.
	 * @param i
	 * @return
	 */
	public static int cantBeNegative(int i){
		return i<0 ? 0 : i;
	}
	
	/**
	 * Checks if i is in range of compare
	 * @param i
	 * @param compare
	 * @param range
	 * @return
	 */
	public static boolean inRange(int i, int compare, int range){
		return i>=compare-range && i<=compare+range;
	}
	
	/**
	 * Converts i to hex
	 * @param i
	 * @return
	 */
	public static String hex(int i) {
		return String.format("%x", i);
	}
	
	/**
	 * Returns an exponantial growing rate in range 0.0 - 1.0 (flat)
	 * @param d
	 * @return
	 */
	public static double nonLinearFlat(double d){
		Preconditions.checkArgument(d <= 1.0 && d>=0, "argument must be within 0.0 and 1.0 (" + d + ")");
		return Math.expm1(d) * (1 / 1.718281828459045);
	}
	
	/**
	 * Returns an exponantial growing rate in range 0.0 - 1.0 (steep)
	 * @param d
	 * @return
	 */
	public static double nonLinearSteep(double d){
		Preconditions.checkArgument(d <= 1.0 && d>=0, "argument must be within 0.0 and 1.0");
		return Math.sqrt(d);
	}
	
	private static class GrowthChart extends ApplicationFrame {
		
		public GrowthChart(String applicationTitle, String chartTitle) {
			
			super(applicationTitle);
			JFreeChart lineChart = ChartFactory.createLineChart(chartTitle, "X", "Y", createDataset(), PlotOrientation.VERTICAL, true, true, false);

			ChartPanel chartPanel = new ChartPanel(lineChart);
			chartPanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
			setContentPane(chartPanel);
		}

		private DefaultCategoryDataset createDataset() {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for(double d = 0; d<=1.0D; d+= 0.05D){
				dataset.addValue(nonLinearFlat(d), "flat", "" + d);
				dataset.addValue(nonLinearSteep(d), "steep", "" + d);
			}
			
//			dataset.addValue(30, "schools", "1980");
//			dataset.addValue(60, "schools", "1990");
//			dataset.addValue(120, "schools", "2000");
//			dataset.addValue(240, "schools", "2010");
//			dataset.addValue(300, "schools", "2014");
			return dataset;
		}

		public static void main(String[] args) {
			GrowthChart chart = new GrowthChart("Growth", "...");
			chart.pack();
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		}
		
	}
}
