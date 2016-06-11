package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.List;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Drawer {

	ChartPanel panel;
	XYSeriesCollection dataset;
	private Frame frame;
	String title;
	String xAxis;
	String yAxis;
	
	public Drawer(String title,String xAxis,String yAxis){
		this.title = title;
		this.xAxis=xAxis;
		this.yAxis=yAxis;
		
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
                this.title,
                this.xAxis,
                this.yAxis,
                dataset, 
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
        
        
        LogarithmicAxis xAxis = new LogarithmicAxis(this.xAxis);
        LogarithmicAxis yAxis = new LogarithmicAxis(this.yAxis);
        
        xAxis.setAllowNegativesFlag(true);
        yAxis.setAllowNegativesFlag(true);
        xAxis.setRange(new Range(0.01, 90));
        yAxis.setRange(new Range(0.5, 90));
        
        
        chart.getXYPlot().setDomainAxis(xAxis);
        chart.getXYPlot().setRangeAxis(yAxis);
        
        //chart.getXYPlot().setRenderer(new XYSplineRenderer());
        
        chart.getXYPlot().getRenderer().setSeriesStroke(
        	    1, 
        	    new BasicStroke(
        	        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
        	        1.0f, new float[] {6.0f, 6.0f}, 0.0f
        	    ));
        	    
        
        
        ChartPanel chartPanel = new ChartPanel(chart);
        
        if(this.panel != null)
        	this.frame.remove(this.panel);
        
        
        
        this.panel=chartPanel;
        
        this.frame.add(chartPanel);
        chartPanel.setSize(this.frame.getSize());
        chartPanel.setVisible(true);
	}
}
