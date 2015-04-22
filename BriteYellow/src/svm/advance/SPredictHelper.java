package svm.advance;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import svm.libsvm.svm;
import svm.libsvm.svm_model;
import svm.libsvm.svm_node;

public class SPredictHelper{

	private svm_model model;
	private StrNumConverter snc;
	
	private int correct = 0;
	private int total = 0;

	public SPredictHelper(svm_model model, StrNumConverter snc){
		this.model = model;
		this.snc = snc;
	}
	
	public SPredictHelper(String model_file_name, String str_converter_file_name) throws IOException{
		model = loadModel(model_file_name);
		snc = new StrNumConverter(model_file_name, str_converter_file_name);
	}
	

	private static svm_model loadModel(String model_file) throws IOException{
		svm_model model = svm.svm_load_model(model_file);
		if (model == null)
			throw new NullPointerException("Can't open model file "+model_file+". The model seems to be null.");

		return model;
	}

	/** Method for predicting a single point
	 * 
	 * @param point the point in the double format
	 * @return
	 */
	public String predict(double[] point){
		svm_node[] x = new svm_node[point.length];
		for(int j=0;j<point.length;j++){
			x[j] = new svm_node();
			x[j].index = j;
			x[j].value = point[j];
		}

		return snc.getName((int)svm.svm_predict(model,x));
	}
	
	public String predict(double[] point, int... col){
		svm_node[] x = new svm_node[col.length];
		for(int j=0;j<col.length;j++){
			x[j] = new svm_node();
			x[j].index = j;
			x[j].value = point[col[j]];
		}

		return snc.getName((int)svm.svm_predict(model,x));
	}
	
	/** Method for predicting a single point
	 * 
	 * @param x the point in the svm_node[] format
	 * @return
	 */
	public double predict(svm_node[] x){
		return svm.svm_predict(model,x);
	}
	
	public String predict(String expected, double[] point){
		String result = predict(point);
		if(result.equals(expected))
			correct++;
		total++;
		return result;
	}
	public String predict(String expected, double[] point, int... columns){
		String result = predict(point, columns);
		if(result.equals(expected))
			correct++;
		total++;
		return result;
	}
	public float getAcurracy(){
		return (float)correct / (float)total;
	}
	public int getTotal(){
		return total;
	}
	public int getCorrect(){
		return correct;
	}
	
	///////////////////////////////////////////////////////////////////
	// Test bench for the STrainHelper class
	// Name: Chun Bong Cheung
	// Date: 15/04/2015
	// Description: Test some data using the SVM model generated in the
	//              STrainHelperper test bench, and output the accuracy
	//				Please run the test bench in the STrainHelper before
	//				run the test bench below
	///////////////////////////////////////////////////////////////////
	/**Test bench for the SPredictHelper class
	 * Run the test bench in STrainHelper class before running it
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IOException{
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
		
		// File path for the SVM model file and data label file generated in the
		// STrainHelper test bench during training
		String filepath = new File("").getAbsolutePath()+"/src/svm/samples/svm_sample_results.train";
		SPredictHelper sph = new SPredictHelper(filepath+".model", filepath+".snc");
		// Predict the result of the data one by one. Labels are the expected results.
		// This method automatically count the number of correct results
		for(int i=0; i<data.length; i++){
			sph.predict(labels[i], data[i], 0, 2);
		}
		// Show the results in a popup Dialog
		JOptionPane.showMessageDialog(
				null, "Correct/Tested: "+sph.getCorrect()+
				"/"+sph.getTotal()+" \n(Accuracy: "+sph.getAcurracy()*100+"%)",
				"Prediction results", JOptionPane.INFORMATION_MESSAGE
				);
		
	}
	
}
