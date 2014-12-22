package Graphing;

import java.io.File;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
	
public class XYPlot{
	public static void plot(double[] xval, double[] yval, String Title, String Heading, String xlabel, String ylabel, String Name){
	
		double[] gain = new double[xval.length];
		for(int i=0; i<xval.length; i++){
			gain[i] = 20*Math.log10(yval[i]/xval[i]);
		}
		 
		scatterGraph chart = new scatterGraph(xval,yval, Title, Heading, xlabel, ylabel, Name);
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );
		
	}

}  