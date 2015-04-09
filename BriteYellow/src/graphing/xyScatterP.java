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
	
public class xyScatterP{
	public void plot(float[][] x, float[][] y, String Title, String Heading, String xlabel, String ylabel, String Name){
		
		scatterPlot chart = new scatterPlot(x,y, Title, Heading, xlabel, ylabel, Name);
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );		
	}
}  