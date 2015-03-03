package svm.advance;

import java.util.ArrayList;

import svm.libsvm.svm_model;
import svm.libsvm.svm_parameter;
import svm.libsvm.svm_problem;

import maths.PhoneData;

public class STrainHelper extends SParam{

	private ArrayList<PhoneData> data;
	private svm_parameter param;
	
	
	public STrainHelper(double[] array1, double[] array2){
		super();
		this.data = data;
		
	}
	
	
	private svm_model svmTrain() {
	    svm_problem prob = new svm_problem();
	    int dataCount = train.length;
	    prob.y = new double[dataCount];
	    prob.l = dataCount;
	    prob.x = new svm_node[dataCount][];     

	    for (int i = 0; i < dataCount; i++){            
	        double[] features = train[i];
	        prob.x[i] = new svm_node[features.length-1];
	        for (int j = 1; j < features.length; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = features[j];
	            prob.x[i][j-1] = node;
	        }           
	        prob.y[i] = features[0];
	    }                

	    System.out.println("Training...");
	    svm_model model = svm.svm_train(super.getParam(), param);

	    return model;
	}
	
	
	public void setParam(int parameter, double value){
		if(parameter == _s)
			param.svm_type = (int) value;
		if(parameter == _t)
			param.kernel_type = (int) value;
		if(parameter == _d)
			param.degree = (int) value;
		if(parameter == _g)
			param.gamma = value;
		if(parameter == _r)
			param.coef0 = value;
		if(parameter == _n)
			param.nu = value;
		if(parameter == _m)
			param.cache_size = value;
		if(parameter == _c)
			param.C = value;
		if(parameter == _e)
			param.eps = value;
		if(parameter == _p)
			param.p = value;
		if(parameter == _h)
			param.shrinking = (int) value;
		if(parameter == _b)
			param.probability = (int) value;
		if(parameter == _w)
			param.nr_weight = (int) value;

	}
	public double getParam(int parameter){
		if(parameter == _s)
			return param.svm_type;
		if(parameter == _t)
			return param.kernel_type;
		if(parameter == _d)
			return param.degree;
		if(parameter == _g)
			return param.gamma;
		if(parameter == _r)
			return param.coef0;
		if(parameter == _n)
			return param.nu;
		if(parameter == _m)
			return param.cache_size;
		if(parameter == _c)
			return param.C;
		if(parameter == _e)
			return param.eps;
		if(parameter == _p)
			return param.p;
		if(parameter == _h)
			return param.shrinking;
		if(parameter == _b)
			return param.probability;
		throw new IllegalArgumentException("Wrong Argument!");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
