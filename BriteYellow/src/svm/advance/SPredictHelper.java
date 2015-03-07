package svm.advance;

import java.io.IOException;

import svm.libsvm.svm;
import svm.libsvm.svm_model;
import svm.libsvm.svm_node;
import svm.libsvm.svm_problem;

public class SPredictHelper{

	private svm_model model;
	
	public SPredictHelper(svm_model model){
		this.model = model;
	}
	
	public SPredictHelper(String model_file_name) throws IOException{
		model = loadModel(model_file_name);	
	}
	
	private static svm_model loadModel(String model_file) throws IOException{
		svm_model model = svm.svm_load_model(model_file);
		if (model == null)
		{
			System.err.print("can't open model file "+model_file+"\n");
			System.exit(1);
		}
		return model;
	}

	/** Method for predicting a single point
	 * 
	 * @param point the point in the double format
	 * @return
	 */
	public double predict(double[] point){
		
		svm_node[] x = new svm_node[point.length];
		for(int j=0;j<point.length;j++){
			x[j] = new svm_node();
			x[j].index = j;
			x[j].value = point[j];
		}

		return svm.svm_predict(model,x);
	}
	
	/** Method for predicting a single point
	 * 
	 * @param x the point in the svm_node[] format
	 * @return
	 */
	public double predict(svm_node[] x){
		return svm.svm_predict(model,x);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
