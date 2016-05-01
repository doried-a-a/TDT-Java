package gui;

import java.awt.Frame;
import java.awt.List;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Drawer {
	
	double x[]; double y[];

	ChartPanel panel;
	XYSeriesCollection dataset;
	
	private Frame frame;
	public Drawer(){
		
		frame = new Frame();
		frame.setSize(600, 400);
		frame.setVisible(true);
	
	}
	
	public void draw(ArrayList<Double> x,ArrayList<Double> y,String title){
		if(x.size() != y.size())
			throw new RuntimeException("Drawer: Lists x and y don't have the same size.");
		
		XYSeries series = new XYSeries(title);
		for(int i=0;i<x.size();i++)
			series.add(x.get(i), y.get(i));
		
		
		if(this.dataset==null)
			 this.dataset = new XYSeriesCollection();
		
		XYSeriesCollection dataset = this.dataset;
		
        dataset.addSeries(series);
        
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Chart",
                "x-axis",
                "y-axis",
                dataset, 
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
        
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        ChartPanel chartPanel = new ChartPanel(chart);
        
        if(this.panel != null)
        	this.frame.remove(this.panel);
        this.panel=chartPanel;
        this.frame.add(chartPanel);
        chartPanel.setSize(this.frame.getSize());
        chartPanel.setVisible(true);
	}
}
