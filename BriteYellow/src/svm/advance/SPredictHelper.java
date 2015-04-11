package svm.advance;

import java.io.IOException;

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
	public float getAcurracy(){
		return (float)correct / (float)total;
	}
	public int getTotal(){
		return total;
	}
	public int getCorrect(){
		return correct;
	}
	
}
