package svm.advance;

import graphing.PlotHelper;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;

import svm.libsvm.svm;
import svm.libsvm.svm_model;
import svm.libsvm.svm_node;
import svm.libsvm.svm_problem;

public class STrainHelper extends SParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3443334455284060103L;
	final static String AREA = " area";
	final static String POINTS = " points";
	
	private final String model_file_ext = ".train.model";
	private final String snc_file_ext = ".train.snc";
	
	private double[][] train;
	private svm_problem prob;
	private svm_model model;
	
	private double[] maxbounds = null;
	private double[] minbounds = null;
	private int total_dots = 0;
	private int dots_drawn = 0;
	private String[] str_labels;
	private double[] num_labels;
	
	// String to number converter for converting String to numbers for
	// svm training and predicting
	private StrNumConverter snc;
	private String file_name = new File("").getAbsolutePath()+"/src/svm/models/new";
	private int[] columns = null;
	private CustomChartProgressListener ccpl = null;
	
	public STrainHelper(String[] str_labels, double[][] train){
		this(str_labels,train,null);
	}
	public STrainHelper(String[] str_labels, double[][] train, CustomChartProgressListener ccpl){
		super();
		this.ccpl = ccpl;
		log("Initialising STrainHelper...");
		
		this.str_labels = str_labels;
		num_labels = new double[str_labels.length];
		snc = new StrNumConverter();
		for(int i = 0; i < str_labels.length; i++){
			num_labels[i] = snc.getIndex(str_labels[i]);
		}

        this.train = train;	
	}
	
	public String[] getLabels(){
		return snc.getLabels();
	}
	public StrNumConverter getStrNumConverter(){
		return snc;
	}
	
	public svm_model getModel(){
		return model;
	}
	public svm_model svmTrain(int... columns) throws IOException {
		if(columns.length == 0)
			throw new IllegalArgumentException("Input arguments cannot be null");
		
		this.columns = columns;
	    prob = new svm_problem();
	    int dataCount = train.length;
	    total_dots += dataCount;
	    prob.y = new double[dataCount];
	    prob.l = dataCount;
	    prob.x = new svm_node[dataCount][];     

	    maxbounds = new double[columns.length];
	    minbounds = new double[columns.length];
	    

	    for (int i = 0; i < dataCount; i++){
	    	prob.x[i] = new svm_node[columns.length];
	    	for (int j = 0; j < columns.length; j++){
	    		svm_node node = new svm_node();
	            node.index = j;
	            node.value = train[i][columns[j]];
	            prob.x[i][j] = node;
	            
	         // Set the maximum and minimum bounds
	    	    if(i == 0){
	    	    	maxbounds[j] = minbounds[j] = node.value;
	    	    } else {
	    	    	// Find Bounds
	    			if(node.value > maxbounds[j])
	    				maxbounds[j] = node.value;
	    			else if(node.value < minbounds[j])
	    				minbounds[j] = node.value;
	    	    }
	        }
	        prob.y[i] = num_labels[i];
	    }

	    log("Training "+ dataCount+" records...");
	    svm.svm_set_print_string_function(ccpl);
	    model = svm.svm_train(prob, super.getParam());
	    
	    File parent = new File(file_name).getParentFile();
	    if(!parent.exists()){
	    	parent.mkdirs();
	    }
	    log("Filepath for model files: \""+parent.getAbsolutePath()+"\"");
	    svm.svm_save_model(file_name+model_file_ext,model);
	    snc.save2File(file_name+model_file_ext, file_name+snc_file_ext);
	    return model;
	}
	
	public double[][] getTrainedData(){
		if(columns == null || columns.length == 0){
			throw new NullPointerException("Read training data first using svm_train");
		}
		double[][] result = new double[train.length][columns.length];
		for(int i=0; i<result.length; i++){
			for(int j=0; j<columns.length; j++){
				result[i][j] = train[i][columns[j]];
			}
		}
		return result;
	}
	public void setChartProgressListener(CustomChartProgressListener ccpl){
		this.ccpl = ccpl;
	}
	public double[][] getBounds(double percent_ext){
		
		
		if(maxbounds == null)
			throw new NullPointerException("Read training data first using svm_train");

		double[] extension = {
				percent_ext/100*(maxbounds[0]-minbounds[0]),
				percent_ext/100*(maxbounds[1]-minbounds[1])
				};
		
		
		// Adjust the bounds
		for(int i = 0; i<2; i++){
		maxbounds[i] += extension[i];
		minbounds[i] -= extension[i];
		}
		
		return new double[][]{
				{minbounds[0], maxbounds[0]},
				{minbounds[1], maxbounds[1]}
		};
	}
	/** Generate dots
	 * 
	 * @param x_dimen Number of dots in the x axis
	 * @param y_dimen Number of dots in the y axis
	 * @return
	 */
	private double[][] generateDots(int x_dimen, int y_dimen){
		if(columns == null)
			throw new NullPointerException("Read training data first, " +
					"see readTrainData(String input_file_name)");

		double xmax = maxbounds[0];
		double xmin = minbounds[0];
		double ymax = maxbounds[1];
		double ymin = minbounds[1];
		
		double xinc = (xmax-xmin)/(double)x_dimen;
		double yinc = (ymax-ymin)/(double)y_dimen;
		
		double xcur = xmin;
		double ycur = ymax;
		
		double[][] result = new double[x_dimen*y_dimen][2];
		for(int i=0; i<x_dimen; i++){
			for(int j=0; j<y_dimen; j++){
				result[i*y_dimen +j ][0] = xcur;
				result[i*y_dimen +j ][1] = ycur;
				ycur-=yinc;	//decrement y
			}
			xcur+=xinc;	//increment x
			ycur = ymax;	//reset y
		}
		
		// Total dots to draw
		total_dots += x_dimen*y_dimen;
		
		return result;
	}
	public double[][] getSV(){
		svm_node[][] node = model.SV;
		
		int m=2;
		double[][] resultSV = new double[node.length][m];
		for(int i=0;i<node.length;i++)
		{
			for(int j=0;j<m;j++)
				resultSV[i][j] = node[i][j].value;
			
		}
		
		total_dots += node.length;
		return resultSV;
	}
	
	public void plot_graph(final String title, final String axis_name1, final String axis_name2){
		
		if(columns.length != 2)
			throw new IllegalArgumentException("This plot graph only works with data with 2 axis");
		log("Plotting graph...");
		final JLabel error_label = new JLabel();
		final Thread b = new Thread(){
			final JFrame load_frame = new JFrame("Plotting graph...");
			private void setError(String error_msg){
				error_label.setText("<html>The following error has occurred. Press \"cancel\" to abort this process:"+
						"<br><font color = \"red\">"+error_msg+"</font></html>");
				load_frame.validate();
				load_frame.repaint();
				load_frame.pack();
				log("<html><font color = \"red\">Oops :( An error has occurred. </font></html>");
			}
			public void run(){
				// For the loading dialog
				JPanel load_panel = new JPanel();
				load_panel.setLayout(new BorderLayout());
				load_frame.setUndecorated(true);
				final JProgressBar pbar = new JProgressBar();
				pbar.setStringPainted(true);
				pbar.setString("Loading ...");
				final JButton load_button = new JButton("Cancel");
				
				// Set up the loading frame
				load_panel.add(pbar, BorderLayout.CENTER);
				load_panel.add(new JLabel("Plotting graph..."), BorderLayout.NORTH);
				load_panel.add(load_button, BorderLayout.EAST);
				load_panel.add(error_label, BorderLayout.SOUTH);
				load_panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

				load_frame.add(load_panel);
				load_frame.pack();
				load_frame.setLocationRelativeTo(null);
				load_frame.setVisible(true);
				load_frame.setAlwaysOnTop(true);
				
				try{
					TimerTask timertask = new TimerTask(){
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(dots_drawn < total_dots && total_dots!=0){
								int value = total_dots == 0 ? 0 : 100*dots_drawn/total_dots;
								setProgress(value);
								pbar.setValue(value);
							}
						}
					};	// end new TimerTask
					
					
					final JFrame dia2 = new JFrame();
					dia2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					dia2.setTitle("Plotting \""+title+"\". Please wait ...");
					dia2.setLayout(new BorderLayout());
					
					
					final Timer timer = new Timer();
					timer.scheduleAtFixedRate(timertask, 0, 100);
					load_button.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							timer.cancel();
							load_frame.dispose();
							dia2.dispose();
							if(ccpl != null)
								ccpl.onAbort();
							System.err.println("User aborted plotting graph");
						}
					});
					
									
					final SPredictHelper pp = new SPredictHelper(model, snc);
					
					double[][] xymm = getBounds(2);
					
	//				System.out.println("x max: "+xymm[0]+"\nx min: "+xymm[1]+"\ny max: "+xymm[2]+"\ny min: "+xymm[3]);
					
	
				
					int[] num_label = model.label;
					String[] s_label = new String[num_label.length*2+1];
					for(int i=0; i<num_label.length; i++){
						s_label[i] = snc.getName(i)+ POINTS;
						s_label[i+num_label.length] = snc.getName(i)+ AREA;
					}
					
					s_label[s_label.length-1] = "Support Vectors";
					
					final PlotHelper demo = new PlotHelper(title, axis_name1, axis_name2, s_label);
					
					final ChartPanel cp = demo.getChartPanel();
					
					dia2.add(cp, BorderLayout.CENTER);
					dia2.pack();
					dia2.setLocationRelativeTo(null);
					dia2.setVisible(true);
					
					cp.setVisible(false);
					
					demo.setAxisRange(xymm[0][0],xymm[0][1],xymm[1][0],xymm[1][1]);
					Shape shapeCir  = new Ellipse2D.Double(-2.5,-2.5,5,5);
					    				
				    Shape shapeCirStroke = new BasicStroke(0.1f).createStrokedShape(shapeCir);
	
					Shape shapeRect  = new Rectangle2D.Double(-1.5,-1.5,3,3);
					
					demo.setSeriesPaint(s_label[s_label.length-1], new Color(0,0,0));
					demo.setSeriesShape(s_label[s_label.length-1], shapeCirStroke);
					
					Color color[][] = new Color[5][2];
					color[0][0] = new Color(0,0,255);
					color[0][1] = new Color(0,0,255,20);
					color[1][0] = new Color(255,0,0);
					color[1][1] = new Color(255,0,0,20);
					color[2][0] = new Color(0,255,0);
					color[2][1] = new Color(0,255,0,20);
					color[3][0] = new Color(255,177,18);
					color[3][1] = new Color(255,177,18,20);
					color[4][0] = new Color(249,44,255);
					color[4][1] = new Color(249,44,255,20);
					
					for(int i=0; i<num_label.length; i++){
						demo.setSeriesPaint(s_label[i], color[i%color.length][0]);
						demo.setSeriesPaint(s_label[i+num_label.length], color[i%color.length][1]);
					}
					
	
					for(int i=0; i<num_label.length; i++){
						demo.setSeriesShape(s_label[i], shapeRect);
						demo.setSeriesShape(s_label[i+num_label.length], shapeCir);
					}
	
					dots_drawn = 0;
					
					for(int i=0; i<train.length; i++){
						demo.addData(str_labels[i]+ POINTS, train[i][columns[0]], train[i][columns[1]]);
						dots_drawn ++;
					}
					
					double[][] dots = generateDots(180, 180);
					
					for(int i=0; i<dots.length; i++){
						String result = pp.predict(dots[i]);
						
						demo.addData(result +  AREA, dots[i][0], dots[i][1]);
						dots_drawn ++;
					}
					
					
					double[][] SVs = getSV();
					for(int i=0; i<SVs.length; i++){
						demo.addData(s_label[s_label.length-1], SVs[i][0], SVs[i][1]);
						dots_drawn ++;
					}
	
				    pbar.setString("Almost done...");
				    pbar.setValue(100);
				    
				    ChartProgressListener progress_listener2 = new ChartProgressListener(){
	
				    	private boolean first_time = true;
				    	@Override
						public void chartProgress(ChartProgressEvent arg0) {
							// TODO Auto-generated method stub
	
							if(first_time && arg0.getType() == ChartProgressEvent.DRAWING_FINISHED){
								first_time = false;
								timer.cancel();
								dia2.setTitle(title);
								load_frame.setVisible(false);
								log("Done plotting graph");
								finish();
							}
						}
				    };
				    demo.addProgressListener(progress_listener2);
				    
				    if(ccpl != null)
				    	demo.addProgressListener(ccpl);
				    
				    
				   	cp.setVisible(true);
				} catch(Exception e){
					setError(e.toString());
					e.printStackTrace();
				}
			}
		};
		b.start();
	}
	public void setSaveFileName(String file_name){
		this.file_name = file_name;
	}
	
	public String getModelFileName(){
		return file_name+model_file_ext;
	}
	public String getSNCFileName(){
		return file_name+snc_file_ext;
	}
	
	///////////////////////////////////////////////////////////////////
	// Test bench for the STrainHelper class
	// Name: Chun Bong Cheung
	// Date: 15/04/2015
	// Description: Generates some test data, perform SVM analysis, and
	// 				plot the resultant hyperplane
	///////////////////////////////////////////////////////////////////
	/**Test bench for the STrainHelper class
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// The test data labels (3 types with names "A", "B", "C")
		String[] labels = new String[]{
				"A", "A", "A", "A","A", 
				"A", "A", "B", "B", "B",
				"B", "B", "B", "B", "B", 
				"C", "C", "C", "C", "C"
				};
		
		/* The test data with 3 columns with corresponding labels to the
		 * above String array e.g. for {2, 2, 0} - label = "A", 
		 * column0 = 2, column1 = 2, column2 = 0
		 */
		double[][] data = new double[][]{
				{1.8, 1.8, 0}, {1.8, 1.8, 1}, {2.2, 2.2, 2}, {2, 2, 5},	{2.3, 2.3, 4},
				{2, 2, 2.5}, {2, 2, 3.5}, {0.8, 0.8, 3}, {1, 1, 5}, {1, 1, 4}, 
				{1.2, 1.2, 2}, {0.8, 0.8, 3}, {1, 1, 5}, {1, 1, 4}, {1.2, 1.2, 2},
				{3, 3, 5}, {3, 3, 0}, {3, 3, 2}, {3.5, 3.5, 4},	{2.7, 2.7, 6}
		};
		
		final SVMProgressDialog svmpd = new SVMProgressDialog("Training in progress");
		svmpd.updateProgress("Training "+data.length+" records...");
		// Initalise STrainHelper with the data labels and test data
		STrainHelper t = new STrainHelper(labels, data);
		t.setSaveFileName("src/svm/samples/svm_sample_results");
		t.setChartProgressListener(new CustomChartProgressListener(){

			@Override
			public void chartProgress(ChartProgressEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void print(String s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAbort() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void finish() {
				// TODO Auto-generated method stub
				svmpd.finish();
			}

			@Override
			public void progressUpdated(int percent) {
				// TODO Auto-generated method stub
				svmpd.updateProgress(percent);
			}
			
		});
		// Set SVM parameters
		t.setParam(_t, 0);	// Set kernal to be linear (_t = change kernel, 
							// 0 = to linear)
		// Train data columns0 and columns 2
		t.svmTrain(0,2);	
		// Plot the hyperplanes
		svmpd.updateProgress("Plotting graph");
		t.plot_graph("Sample Plot","Axis 1", "Axis 2");
		
		
		
		// Predict the result of a point (the 5th point is used as an example below
		SPredictHelper h = new SPredictHelper(t.getModelFileName(), t.getSNCFileName());
		// Nothing will be printed if the below assertion statement is correct
		// (i.e. predicting {2,2,3,5} would give "A"
		assert h.predict(data[4]).equals(labels[4]): "Result should be :"+labels[4];
		
	}

	private void log(String msg){
		if(ccpl != null){
			ccpl.print(msg+"\n");
		}
		System.out.println(msg);
	}
	private void finish(){
		if(ccpl != null){
			ccpl.finish();
		}
	}
	private void setProgress(int percent){
		if(ccpl != null){
			ccpl.progressUpdated(percent);
		}
	}
}
