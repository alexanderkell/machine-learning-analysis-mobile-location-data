package Graphing;
import java.awt.Color; 
import java.awt.BasicStroke; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//Need to import JFreeChart library 


public class scatterGraph extends ApplicationFrame{
   
	public void print(){
		System.out.println("hey yo!");
	}
	
	public scatterGraph( String applicationTitle , String chartTitle, double[] xval, double[] yval)
	{
		
		super(applicationTitle);
		
		XYDataset dataSet = createDataset(xval, yval); //First create the dataset
        JFreeChart xylineChart = createChart(dataSet, chartTitle);
	      
	      ChartPanel chartPanel = new ChartPanel( xylineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
	      final XYPlot plot = xylineChart.getXYPlot( );
	      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	      renderer.setSeriesPaint( 0 , Color.RED );
	      renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	      plot.setRenderer( renderer ); 
	      setContentPane( chartPanel ); 
	
	}

	
	private XYDataset createDataset(double[] x, double[] y ){	//Creates the dataset
		final XYSeries scatter1 = new XYSeries( "Data" );          
		for(int i=0; i<x.length; i++){
			scatter1.add( x[i] , y[i] );    
		}
		    
		final XYSeriesCollection dataset = new XYSeriesCollection( );          
	    dataset.addSeries( scatter1 );  
		return dataset;
	}
	
	
	private JFreeChart createChart(XYDataset dataset, String title){
		
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
   	         title ,
   	         "Category" ,
   	         "Score" ,
   	         dataset ,
   	         PlotOrientation.VERTICAL ,
   	         true , true , false);//Creates the line chart based upon dataSet	
	
		
		return xylineChart;
		/*
		scatterGraph chart = new scatterGraph("Browser Usage Statistics", "Which Browser are you using?");
	      chart.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart );          
	      chart.setVisible( true );
		*/
	}
}
	
	