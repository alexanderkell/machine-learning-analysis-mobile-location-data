package svm.advance;

import svm.libsvm.svm_parameter;

public class SParam extends svm_parameter{
	
	public final static int _s = 0;
	public final static int _t = 1;
	public final static int _d = 2;
	public final static int _g = 3;	// 1/num_features
	public final static int _r = 4;
	public final static int _n = 5;
	public final static int _m = 6;
	public final static int _c = 7;
	public final static int _e = 8;
	public final static int _p = 9;
	public final static int _h = 10;
	public final static int _b = 11;
	public final static int _w = 12;
	
	private svm_parameter param;
	public SParam(){
		param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
	}
	public SParam(svm_parameter param){
		this.param = param;
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
	/**Get the whole svm parameter
	 * 
	 * @return
	 */
	public svm_parameter getParam(){
		return param;
	}
}
