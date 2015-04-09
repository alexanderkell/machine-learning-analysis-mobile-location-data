package graphing;
import java.io.*;
import java.awt.Color; 
import java.awt.BasicStroke; 
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtilities; 
//Need to import JFreeChart library 


public class scatterPlot extends ApplicationFrame{
	
	public scatterPlot(float[][] x1,  float[][] y, String applicationTitle , String chartTitle, String xlabel, String ylabel, String Name)
	{
		super(applicationTitle);
		JPanel chartPanel = createDemoPanel(x1, y, applicationTitle, chartTitle, xlabel, ylabel, Name);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}
		private static JFreeChart createChart(XYDataset dataset, String applicationTitle, String chartTitle, String xlabel, String ylabel, String name) {
	        JFreeChart chart = ChartFactory.createScatterPlot(chartTitle, xlabel, ylabel, dataset, PlotOrientation.VERTICAL, true, false, false);

	        XYPlot plot = (XYPlot) chart.getPlot();
	        plot.setNoDataMessage("NO DATA");
	        plot.setDomainZeroBaselineVisible(true);
	        plot.setRangeZeroBaselineVisible(true);
	        
	        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
	        renderer.setSeriesOutlinePaint(0, Color.black);
	        renderer.setUseOutlinePaint(true);
	        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
	        domainAxis.setAutoRangeIncludesZero(false);
	        domainAxis.setTickMarkInsideLength(2.0f);
	        domainAxis.setTickMarkOutsideLength(0.0f);
	        
	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setTickMarkInsideLength(2.0f);
	        rangeAxis.setTickMarkOutsideLength(0.0f);
	        
	        return chart;
	        
		}
      /*  
		File XYChart = new File( Name+".jpeg" ); 
		try {
			ChartUtilities.saveChartAsJPEG( XYChart, chart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/


	
	private static XYDataset createDataset(float[][] x1,float[][] y){	//Creates the dataset
		final XYSeries scatter1 = new XYSeries("Business Man");          
		for(int i=0; i<x1[0].length; i++){
			scatter1.add( x1[0][i] , y[0][i] );    
		}
		final XYSeries scatter2 = new XYSeries("Security");          
		for(int i=0; i<x1[1].length; i++){
			scatter2.add( x1[1][i] , y[1][i] );    
		}
		final XYSeries scatter3 = new XYSeries("Shopper");          
		for(int i=0; i<x1[2].length; i++){
			scatter3.add( x1[2][i] , y[2][i] );    
		}
		
		    
		final XYSeriesCollection dataset = new XYSeriesCollection( );          
	    dataset.addSeries(scatter3);
	    dataset.addSeries(scatter2);
	    dataset.addSeries(scatter1); 
		return dataset;
	}
	
	
	public static JPanel createDemoPanel(float[][] x1, float[][] y, String applicationTitle, String chartTitle, String xlabel, String ylabel, String name) {

		
		XYDataset x = createDataset(x1, y);
		JFreeChart chart = createChart(x, applicationTitle, chartTitle, xlabel, ylabel, name);
		ChartPanel chartPanel = new ChartPanel(chart);
		//chartPanel.setVerticalAxisTrace(true);
		//chartPanel.setHorizontalAxisTrace(true);
		// popup menu conflicts with axis trace
		chartPanel.setPopupMenu(null);
        
		chartPanel.setDomainZoomable(true);
		chartPanel.setRangeZoomable(true);
		return chartPanel;
    }
}
	
	