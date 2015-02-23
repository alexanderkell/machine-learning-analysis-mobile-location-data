package autoregression;
import java.text.ParseException;

import Maths.*;

public class ARCalculations {
	
	public static void main(String[] args) throws Exception{
		ARCalculations ARC= new ARCalculations();
		double[] r = ARC.secondOrder(20);
		System.out.println(r[0] + " " + r[1]);
		
	}
	
	static String fn =new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
	private static int opt = 1;
	double[][] data = new double[4][];
	int length = 0;
	double[] arc = new double[2];
	double[] xyzarc = new double[6];
	
	//constructor imports the data array containing the analysed data
	public ARCalculations() throws ParseException{
		this(opt,fn);
	}
	
	public ARCalculations(int opt, String fn) throws ParseException{
		this.opt = opt;
		this.fn = fn;
		DataGetter DFO = new DataGetter(opt,fn);
		int length = DFO.getLength();
		this.length = length;
		double[][] data = new double[4][length];
		double[] xyz = new double[3]; 
		double tbm1 = 0;
		double tb = 0;
		for(int i=0; i<length; i++){
			xyz = DFO.getXYZValue(i);
			
			for(int h=0; h<3; h++){
				data[h][i] = xyz[h];
				}
			
			if(i>1){
				tbm1 = data[3][i-1];
			}
			if(i == 0){
				tb = 0;
			}
			else{
				tb = DFO.getTimeBetweenValue(i);
			}
			data[3][i] = tbm1 + tb;
			
			
 		}
		
		this.data = data;
		
		/*for (int k = 0; k < length; k++){
		for (int l = 0; l < 4; l++) {
			System.out.print(this.data[l][k] + " ");
		}
			System.out.print("\n");
		}*/
		
		
		
	}
	
	public void setPhone(int opt){
		this.opt = opt;
	}
	
	public void setFileName(String fn){
		this.fn = fn;
	}
	
	public double[][] getXYZTimeReference(){
		return data;
	}
	
	public double[][] reverseDataOrder(){
		double[][] revdata = new double[4][length];
		for(int i = 0; i<length; i++){
			for(int h = 0; h<=3; h++){
				revdata[h][length-1-i] = data[h][i];
			}
		}
		
		return revdata;
	}
	
	//class delays the data by a step in the matrix
	public double[][] delayData(int delay){
		double[][] data1 = new double [4][];
		double[][] revdata = reverseDataOrder();
		if(data[3][0] > data[3][1]){
			data1 = data;
		}
		else if(data[3][0] < data[3][1]){
			data1 = revdata;
		}
		double [][] deldata = new double[4][length+delay];
		for(int i=0; i<length; i++){
			for(int h=0; h<4; h++){
			deldata[h][i+delay] = data1[h][i];
			}
			
		}
		
		
		return deldata;
	}
	
	
	
	//method works out the regression coefficients of each coordinate, coordinate is set by the 'co' variable, order of autoreg is set by 'order', 'lb' sets the amount of look backs through the data
	public double[] regressionCoefficients(int lb, int delay1, int delay2, int co, int order){
		double[][] deldata1 = delayData(delay1);
		double[][] deldata2 = delayData(delay2);
		double xsum = 0;
		double ysum = 0;
		double x2;
		double y2;
		double xy;
		double x2sum = 0;
		double y2sum = 0;
		double xysum = 0;
		double xbar;
		double ybar;
		double x2bar;
		double y2bar;
		double xybar;
		int readings = order+lb;
		double m;
		double b;
		
		for(int i=order; i<readings; i++){
			xsum += deldata2[co][i];
			ysum += deldata1[co][i];
			x2 = deldata2[co][i]*deldata2[co][i];
			y2 = deldata1[co][i]*deldata1[co][i];
			xy = deldata2[co][i]*deldata1[co][i];
			x2sum += x2;
			y2sum += y2;
			xysum += xy;
			
		}
		
		xbar = xsum/lb;
		ybar = ysum/lb;
		x2bar = x2sum/lb;
		y2bar = y2sum/lb;
		xybar = xysum/lb;
	
		m = ((xbar*ybar) - xybar)/(xbar*xbar-x2bar);
		b = ybar - m*xbar;
		arc[0] = m;
		arc[1] = b;
		
		
		return arc;
	}
	
	public double calcMean(int delay, int lb, int co, int order){
		double[][] deldata = delayData(delay);
		int readings = order+lb;
		double sum = 0;
		double N = lb;
		double mean;
		
		for(int i=order; i<readings; i++){
			sum += deldata[co][i];
		}
		mean = sum / N;
		
		return mean;
	}
	
	public double standardDeviation(int delay, int lb, int co, int order){
		double[][] deldata = delayData(delay);
		int readings = order+lb;
		double sd = 0;
		double sum = 0;
		double N = lb;
		double mean = calcMean(delay, lb, co, order);
		double VAR;
		double x;
		double ad;
		double sq;
		double temp = 0;
		
		for(int i=order; i<readings; i++){
			x= deldata[co][i];
			ad = (x - mean);
			sq = ad*ad;
			temp += sq;
		}
		
		VAR = temp/(N-1);
		sd = Math.sqrt(VAR);
		
		return sd;
	}
	
	public double[] secondOrder(int lb){
		double beta1;
		double beta2;
		double a;
		double[] arc = new double[9];
		double ryx1[];
		double ryx2[];
		double rx1x2[];
		double sdy;
		double sdx1;
		double sdx2;
		
		//for x plane
		
		ryx1 = regressionCoefficients(lb, 0, 1, 0, 2);
		ryx2 = regressionCoefficients(lb, 0, 2, 0, 2);
		rx1x2 = regressionCoefficients(lb, 1, 2, 0, 2);
		sdy = standardDeviation(0, lb, 0, 2);
		sdx1 = standardDeviation(1, lb, 0, 2);
		sdx2 = standardDeviation(2, lb, 0, 2);
		
		beta1 = (ryx1[0]-ryx2[0]*rx1x2[0])/(1-rx1x2[0]*rx1x2[0]);
		beta1 *= sdy/sdx1;
		beta2 = (ryx2[0]-ryx1[0]*rx1x2[0])/(1-rx1x2[0]*rx1x2[0]);
		beta2 *= sdy/sdx2;
		
		arc[0] = beta1;
		arc[1] = beta2;
		
		return arc;
	}
	
	//this method puts each coefficient from each coordinate into a matrix
	/*public double[] xyzARCoefficients(int lb, int order){
		double[] temparc = new double[2];
		
		temparc = arCoefficients(lb, order, 0);
		xyzarc[0] = temparc[0];
		xyzarc[1] = temparc[1];
		
		temparc = arCoefficients(lb, order, 1);
		xyzarc[2] = temparc[0];
		xyzarc[3] = temparc[1];
		
		temparc = arCoefficients(lb, order, 3);
		xyzarc[4] = temparc[0];
		xyzarc[5] = temparc[1];
		
		
		return xyzarc;
	}*/
	
	

}


