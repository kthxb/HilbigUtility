package com.ash.util.math;

import java.util.ArrayList;
import java.util.function.Function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

@SuppressWarnings("serial")
public class MultiFunctionDisplay  extends ApplicationFrame {
	
	public MultiFunctionDisplay(String applicationTitle, String chartTitle, String xAxis, String yAxis, boolean legend, double minX, double maxX, double stepSize, ArrayList<Function<Double,Double>> functions, ArrayList<String> names, int width, int height) {
		
		super(applicationTitle);
		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle, xAxis, yAxis, createDataset(functions, minX, maxX, stepSize, names), PlotOrientation.VERTICAL, legend, true, false);
		
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
		setContentPane(chartPanel);
		
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

	private DefaultCategoryDataset createDataset(ArrayList<Function<Double, Double>> functions, double minX, double maxX, double stepSize, ArrayList<String> names) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(double d = minX; d<=maxX; d+= stepSize){
			for(Function<Double,Double> f : functions)
				dataset.addValue((double) f.apply(d), names.get(functions.indexOf(f)), "" + d);
		}
		return dataset;
	}

	public static void main(String[] args) {
		
		ArrayList<Function<Double, Double>> functions = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();
		
		functions.add((x) -> {
			return EZMath.nonLinearFlat(x);
		});
		
		functions.add((x) -> {
			return EZMath.nonLinearSteep(x);
		});
		
		names.add("Flat");
		names.add("Steep");
		
		MultiFunctionDisplay sfd = new MultiFunctionDisplay("Test", "Test", "x-Achse", "y-Achse", true, 0.0, 1.0, 0.02, functions, names, 400, 300);
		
		
	}
	
}
