package Autoregression;
import java.text.ParseException;

import Maths.*;

public class ARCalculations {
	
	String fn =new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
	private int opt = 0;
	
	public ARCalculations() throws ParseException{
		DataFormatOperations DFO = new DataFormatOperations(opt,fn);
		int length = DFO.getLength();
		double[][] data = new double[4][length];
		double[] xyz = new double[3]; 
		double tbm1 = 0;
		double tb = 0;
		for(int i=0; i<length; i++){
			xyz = DFO.getXYZValue(i);
			
			data[0][i] = xyz[0];
			data[1][i] = xyz[1];
			data[2][i] = xyz[2];
			
			if(i>1){
				tbm1 = data[3][i-1];
			}
			tb = DFO.getTimeBetweenValue(i);
			data[3][i] = tbm1 + tb;
			
			
 		}
		
		for (int k = 0; k < length; k++){
		for (int l = 0; l < 4; l++) {
			System.out.print(data[l][k] + " ");
		}
			System.out.print("\n");
		}
		
		
		
	}
	
	public ARCalculations(int opt, String fn) throws ParseException{
		this.opt = opt;
		this.fn = fn;
		DataFormatOperations DFO = new DataFormatOperations(opt,fn);
		int length = DFO.getLength();
		double[][] data = new double[4][length];
		double[] xyz = new double[3]; 
		double tbm1 = 0;
		double tb = 0;
		for(int i=0; i<length; i++){
			xyz = DFO.getXYZValue(i);
			
			data[0][i] = xyz[0];
			data[1][i] = xyz[1];
			data[2][i] = xyz[2];
			
			if(i>1){
				tbm1 = data[3][i-1];
			}
			tb = DFO.getTimeBetweenValue(i);
			data[3][i] = tbm1 + tb;
			
			
 		}
		
		for (int k = 0; k < length; k++){
		for (int l = 0; l < 4; l++) {
			System.out.print(data[l][k] + " ");
		}
			System.out.print("\n");
		}
		
		
		
	}
	
	public void setPhone(int opt){
		this.opt = opt;
	}
	
	public void setFileName(String fn){
		this.fn = fn;
	}
	
	

}
