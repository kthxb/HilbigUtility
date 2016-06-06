package com.ash.util.math;

import java.util.function.Function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * Displays a function in a custom window. Syntax:
 * <pre>SingleFunctionDisplay sfd = new SingleFunctionDisplay("WindowTitle", "ChartTitle", "x-axis-name", "y-axis-name", displayLegend, minX, maxX, stepsize, (x) -> {
			return Math.exp(x); // return the y value for x -- the function
		}, "Function name", windowWidth, windowHeight);</pre><br>It opens automatically.
 * @author Aaron Hilbig
 *
 */
@SuppressWarnings("serial")
public class SingleFunctionDisplay extends ApplicationFrame {
	
	public SingleFunctionDisplay(String applicationTitle, String chartTitle, String xAxis, String yAxis, boolean legend, double minX, double maxX, double stepSize, Function<Double,Double> function, String name, int width, int height) {
		
		super(applicationTitle);
		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle, xAxis, yAxis, createDataset(function, minX, maxX, stepSize, name), PlotOrientation.VERTICAL, legend, true, false);
		
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
		setContentPane(chartPanel);
		
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
		
	}

	private DefaultCategoryDataset createDataset(Function<Double,Double> function, double minX, double maxX, double stepSize, String name) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(double d = minX; d<=maxX; d+= stepSize){
			dataset.addValue(function.apply(d), name, "" + d);
		}
		return dataset;
	}

	public static void main(String[] args) {
		/*SingleFunctionDisplay sfd = new SingleFunctionDisplay("Test", "Test", "x-Achse", "y-Achse", false, 0.0, 1.0, 0.05, (x) -> {
			return EZMath.nonLinearSteep(x);
		}, "Tesfunktion", 400, 300);*/
		
		
		
		
		SingleFunctionDisplay sfd = new SingleFunctionDisplay("Titel", "Testdiagramm", "x-Achse", "y-Achse", true, 0.0, 10.0, 0.5, (x) -> {
			return Math.exp(x);
		}, "E-Funktion", 400, 300);
	}
	
}
