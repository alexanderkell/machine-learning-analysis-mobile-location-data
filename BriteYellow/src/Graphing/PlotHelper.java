/**
 * 
 */
package Graphing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Helper class for plotting scatter graphs for Support Vector Machine (SVM)
 * 
 * Modified from:
 * http://stackoverflow.com/questions/13792917/jfreechart-scatter-plot-moving-data-between-series
 * 
 * @see http://stackoverflow.com/a/13794076/230513
 * @see http://stackoverflow.com/questions/8430747
 * @see http://stackoverflow.com/questions/8048652
 * @see http://stackoverflow.com/questions/7231824
 * @see http://stackoverflow.com/questions/7205742
 * @see http://stackoverflow.com/questions/7208657
 * @see http://stackoverflow.com/questions/7071057
 * @see http://stackoverflow.com/questions/8736553
 */
public class PlotHelper extends JFrame {

	public static final String SV = "SVECTOR";
	public static final boolean AREA = false;
	public static final boolean DATA = true;
    public static final int DEFAULT_CHART_WIDTH = 960;
    public static final int DEFAULT_CHART_HEIGHT = 540;

    private XYPlot xyPlot;
	private final XYSeries[] series;
	
	private ChartPanel chartPanel;
	private JFreeChart jfreechart;
	private XYSeriesCollection xySeriesCollection;
	private String[] str_labels;

    /**Construct a new scatter chart
     * 
     * @param title
     * Chart title
     * @param str_labels
     * Labels for the series
     */
    public PlotHelper(String title, String xaxis, String yaxis ,String[] str_labels, int chart_width, int chart_height) {
        super(title);
        this.str_labels = str_labels;

        
        series = new XYSeries[str_labels.length];
        
        for(int i=0; i<str_labels.length; i++){
        	series[i] = new XYSeries(str_labels[i], false);
        }
        
        chartPanel = createDemoPanel(title, xaxis, yaxis);
        chartPanel.setPreferredSize(new Dimension(chart_width, chart_height));
    }

    public PlotHelper(String title, String xaxis, String yaxis, String[] str_labels) {
        this(title, xaxis, yaxis, str_labels, DEFAULT_CHART_WIDTH, DEFAULT_CHART_HEIGHT);
    }
    /**Show an external dialog container the graph if you don't want to create one yourself
     * 
     * @param close_option 
     * Close option for the dialog 
     * e.g. JFrame.EXIT_ON_CLOSE, JFrame.DISPOSE_ON_CLOSE, JFRAME.HIDE_ON_CLOSE, JFRAME.DO_NOTHING_ON_CLOSE
     */
    public void showDialog(int close_option){
        
        setDefaultCloseOperation(close_option);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        

        jfreechart.addChangeListener(new ChartChangeListener(){

			public void chartChanged(ChartChangeEvent arg0) {
				// TODO Auto-generated method stub
			
				String title = arg0.getChart().getTitle().getText();
				if(title!=null)
					setTitle(title);
			}
        	
        });

/*        
        chartPanel.addPropertyChangeListener(new PropertyChangeListener(){
        	public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                System.out.println(propertyName);
        	}
        });
*/      
        pack();
        setVisible(true);
        
    }
    public void showDialog(){
    	showDialog(JFrame.EXIT_ON_CLOSE);
    }
    
    /** Method for getting chart panel if you want to integrate the chart
     *  into your own dialog
     *  
     *  @return the chart
     */
    public ChartPanel getChartPanel(){
    	return chartPanel;
    }
    private ChartPanel createDemoPanel(final String title, final String xaxis, final String yaxis) {
 
        jfreechart = ChartFactory.createScatterPlot(
            title, xaxis, yaxis, createSeries(),
            PlotOrientation.VERTICAL, true, true, false);
        xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setBackgroundPaint(Color.white);

        return new ChartPanel(jfreechart);
    }

    /**Add a ChartProgressListener to your chart
     * 
     * @param chartprogress
     */
    public void addProgressListener(ChartProgressListener chartprogress){
    	jfreechart.addProgressListener(chartprogress);
    }
    public XYPlot getXYPlot(){
    	return xyPlot;
    }

    /**Method for setting chart title
     * 
     */
    public void setTitle(String newtitle){
    	jfreechart.setTitle(newtitle);
    }
    /** Method for setting dot color:
     *  
     *  @param label
     *  Label for the series you want to set dot color
     *  @param data_Or_Area
     *  True for adding data. False for adding area
     *  @param color Color
     */
    public void setSeriesPaint(String label, Color color){
    	XYItemRenderer renderer = xyPlot.getRenderer();
    	
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			renderer.setSeriesPaint(i, color);
    			break;
    		}
    	}
    }
    
    /** Method for setting dot shape:
     *  For setting dot shape for support vector, use one of the following:
     *  	addData(svm_plot_helper.SV, false, double x, double y)
     *  	addData(svm_plot_helper.SV, double x, double y)
     *  
     *  @param label
     *  Label for the series you want to set dot shape
     *  @param data_Or_Area
     *  True for data series. False for area series
     *  @param shape Shape
     */
    public void setSeriesShape(String label, Shape shape){
    	XYItemRenderer renderer = xyPlot.getRenderer();
    	
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			renderer.setSeriesShape(i, shape);
    			break;
    		}
    	}
    }
 
    public void setSeriesLinesVisble(String label, boolean visible){
    	XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
    	
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			renderer.setSeriesLinesVisible(i, visible);
    			break;
    		}
    	}
    }
    /** Method for setting dot stroke:
     *  
     *  @param label
     *  Label for the series you want to set dot stroke
     *  @param data_Or_Area
     *  True for data series. False for area series
     *  @param stroke Stroke
     */
    public void setSeriesStroke(String label, Stroke stroke){
    	XYItemRenderer renderer = xyPlot.getRenderer();
    	
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			renderer.setSeriesStroke(i, stroke);
    			break;
    		}
    	}
    	
    }
    
    
    /** Method for adjusting axis / bounds
     *   
     *  @param minX
     *  X minimum
     *  @param maxX
     *  X maximum
     *  @param minY 
     *  Y minimum
     *  @param maxY 
     *  Y maximum
     */
    public void setAxisRange(double minX, double maxX, double minY, double maxY){
    	xyPlot.getDomainAxis().setRange(minX, maxX);
    	xyPlot.getRangeAxis().setRange(minY, maxY);
    }
    
    public void setDomainAxisInverted(boolean invert){
    	xyPlot.getDomainAxis().setInverted(invert);
    }
    public void setRangeAxisInverted(boolean invert){
    	xyPlot.getRangeAxis().setInverted(invert);
    }

    @SuppressWarnings("unused")
	private void setAxisRange(NumberAxis axis, boolean vertical) {
        axis.setRange(-6.0, 6.0);
        axis.setTickUnit(new NumberTickUnit(0.5));
        axis.setVerticalTickLabels(vertical);
    }

    private XYDataset createSeries() {
        xySeriesCollection = new XYSeriesCollection();
        for(int i=0; i<series.length; i++)
        	xySeriesCollection.addSeries(series[i]);
		return xySeriesCollection;
    }
   
    /** Method for adding a single point to a series
     *  
     *  @param label
     *  Label for the series you want to add data
     *  @param x x coordinate
     *  @param y y coordinate
     */
    public void addData(String label, int x, int y){

    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			series[i].add(x,y);
    			break;
    		}
    	}
    }
    
    /** Method for adding data
     *  
     *  @param label
     *  Label for the series you want to add data
     *  @param x x array
     *  @param y y array
     *
     *	x array length must be the same as y array length
     */
    public void addData(String label, int[] x, int[] y){
    	if (x.length != y.length)
    		throw new ArrayIndexOutOfBoundsException("x array length differs for y array length");

    	addData(label, x, y, 0, x.length-1);
    }
    public void addData(String label, int[] x, int[] y, int lowerlimit, int higherlimit){
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			for(int j=lowerlimit; j<=higherlimit; j++)
    				series[i].add(x[j],y[j]);
    			break;
    		}
    	}
    }
    
    
    /** Method for adding a single point to a series
     *  
     *  @param label
     *  Label for the series you want to add data
     *  @param x x coordinate
     *  @param y y coordinate
     */
    public void addData(String label, double x, double y){

    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			series[i].add(x,y);
    			break;
    		}
    	}
    }
    
    /** Method for adding data
     *  
     *  @param label
     *  Label for the series you want to add data
     *  @param x x array
     *  @param y y array
     *
     *	x array length must be the same as y array length
     */
    public void addData(String label, double[] x, double[] y){
    	if (x.length != y.length)
    		throw new ArrayIndexOutOfBoundsException("x array length differs for y array length");

    	addData(label, x, y, 0, x.length-1);
    }
    public void addData(String label, double[] x, double[] y, int lowerlimit, int higherlimit){
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			for(int j=lowerlimit; j<=higherlimit; j++)
    				series[i].add(x[j],y[j]);
    			break;
    		}
    	}
    }
    
    public void addData(String label, double[][] xy){
    	addData(label, xy,0, xy.length-1);
    }
    public void addData(String label, double[][] xy, int lowerlimit, int higherlimit){
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			for(int j=lowerlimit; j<=higherlimit; j++)
    				series[i].add(xy[j][0],xy[j][1]);
    			break;
    		}
    	}
    }
    
    
    
    /** Method for adding data
     *  
     *  @param label
     *  Label for the series you want to add data
     *  @param x x array
     *  @param y y array
     *
     *	x array length must be the same as y array length
     */
    public void addData(String label, String[] x, String[] y){
    	if (x.length != y.length)
    		throw new ArrayIndexOutOfBoundsException("x array length differs for y array length");

    	addData(label, x, y, 0, x.length-1);
    }
    public void addData(String label, String[] x, String[] y, int lowerlimit, int higherlimit){
    	
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			for(int j=lowerlimit; j<=higherlimit; j++){
    				try{
    					series[i].add(Integer.parseInt(x[j]),Integer.parseInt(y[j]));
    				} catch (NumberFormatException e){
    					System.err.println(e.toString()+" at line "+j);
    				}
    			}
    			break;
    		}
    	}
    }
    
    /**Method for clearing all data of a series
     * 
     * @param label
     * Label for the series you want to clear its data
     */
    public void clearData(String label){
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			series[i].clear();
    			break;
    		}
    	}
    }
    
    /**Method for changing name for a particular series
     * 
     * @param oldlabel	Old name
     * @param newlabel	New name
     */
    public void changeSeriesName(String oldlabel, String newlabel){
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(oldlabel)){
    			series[i].setKey(newlabel);
    		}
    	}
    }
    /**Method for retrieving the series you want so you can do more with it
     * 
     * @param label
     * Label for the series you want to retrieve
     * @return the XYSeries
     */
    public XYSeries getSeries(String label){
    	for(int i=0; i<str_labels.length; i++){
    		if(str_labels[i].equals(label)){
    			return series[i];
    		}
    	}
    	return null;
    }
    
}