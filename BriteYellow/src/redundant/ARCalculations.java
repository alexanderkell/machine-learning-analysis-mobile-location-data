package redundant;
import java.text.ParseException;

import Maths.*;

public class ARCalculations {
	
	public static void main(String[] args) throws ParseException{
		String fn =new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		int opt = 1;
		
		ARCalculations ARC = new ARCalculations(opt, fn);
		double[] xyzarc = ARC.xyzARCoefficients(10,3);
		
		for(int i = 0; i<6; i++){
			System.out.println(xyzarc[i]);
		}
	}
	
	static String fn =new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
	private static int opt = 0;
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
		for(int i=1; i<length; i++){
			xyz = DFO.getXYZValue(i);
			
			for(int h=0; h<3; h++){
				data[h][i] = xyz[h];
				}
			
			if(i>1){
				tbm1 = data[3][i-1];
			}
			tb = DFO.getTimeBetweenValue(i);
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
		/*for (int k = 0; k < length; k++){
			for (int l = 0; l < 4; l++) {
				System.out.print(revdata[l][k] + " ");
			}
				System.out.print("\n");
			}*/
		
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
		
		/*for (int k = 0; k < 12; k++){
			for (int l = 0; l < 4; l++) {
				System.out.print(deldata[l][k] + " ");
			}
				System.out.print("\n");
			}*/
		
		return deldata;
	}
	
	
	
	//method works out the regression coefficients of each coordinate, coordinate is set by the 'co' variable, order of autoreg is set by 'order', 'lb' sets the amount of look backs through the data
	public double[] arCoefficients(int lb, int order, int co){
		
		double[][] deldata = delayData(order);
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
		double[][] data1 = new double [4][];
		double[][] revdata = reverseDataOrder();
		
		if(data[3][0] > data[3][1]){
			data1 = data;
		}
		else if(data[3][0] < data[3][1]){
			data1 = revdata;
		}
		
		for(int i=order; i<readings; i++){
			xsum += deldata[co][i];
			ysum += data1[co][i];
			x2 = deldata[co][i]*deldata[co][i];
			y2 = data1[co][i]*data1[co][i];
			xy = deldata[co][i]*data1[co][i];
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
	
	//this method puts each coefficient from each coordinate into a matrix
	public double[] xyzARCoefficients(int lb, int order){
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
	}
	
	

}


