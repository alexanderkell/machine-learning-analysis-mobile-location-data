package graphing;

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
	public static void plot(double[] x1, double[] y1, double[] x2, double[] y2, double[] x3, double[] y3, String Title, String Heading, String xlabel, String ylabel, String Name){
		
		scatterGraph chart = new scatterGraph(x1,y1,x2,y2,x3,y3, Title, Heading, xlabel, ylabel, Name);
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );		
	}
}  