package xymaps;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import maths.*;
import csvexport.*;


public class DoubleCSVMaker {
	
	static int opt = 1;
	static String fn = new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
	
	public static void main(String[] args) throws ParseException{
		speedWriter();
		speedPositionWriter();
		
	}
	
	public static void speedWriter() throws ParseException{
		int length;
		DataGetter DG = new DataGetter(opt,fn);
		length = DG.getLength();
		PhoneData[] PD = DG.getFullPhoneData();
		int maxtn = PD[length-1].track_no;
		int tn = 1;
		double [] temp1 = new double[5];
		ArrayList<double[]> temp2 = new ArrayList<double[]>();
		double[][] temp3 = new double [temp2.size()][];
		String title = new String();
		
			for(int j = 0; j<length; j++){
			
				if(tn == PD[j].track_no){
					temp1[0] = PD[j].x;
					temp1[1] = PD[j].y;
					temp1[2] = PD[j].z;
					temp1[3] = PD[j].ts.getTime();
					temp1[4] = PD[j].modspd;
					temp2.add(temp1);
					temp1 = new double[5];
					
				}
				else if(tn == PD[j].track_no-1){
					
					temp3 = toArray(temp2);
					title = String.format("Phone No. %d track no %d", opt, tn);
					writeArray(temp3, title);
					temp3 = new double [temp2.size()][];
					tn++;
				}
				
				else if(PD[j].track_no==-1){
					//System.out.printf("Row: %d not included as out of range\n", j);
				}
				
				else{
					System.out.printf("Error, some unknown values were found in the Track Number Postion in Row: %d of value: %d", j , PD[j].track_no);
				}
				
			}	
			temp3 = toArray(temp2);
			title = String.format("Phone No. %d track no %d", opt, tn);
			writeArray(temp3, title);
			temp3 = new double [temp2.size()][];
		
		
	}
	
	public static void speedPositionWriter() throws ParseException{
		int length;
		DataGetter DG = new DataGetter(opt,fn);
		length = DG.getLength();
		PhoneData[] PD = DG.getFullPhoneData();
		int maxtn = PD[length-1].track_no;
		int tn = 1;
		double [] temp1 = new double[5];
		ArrayList<double[]> temp2 = new ArrayList<double[]>();
		double[][] temp3 = new double [temp2.size()][];
		String title = new String();
		
			for(int j = 0; j<length; j++){
			
				if(tn == PD[j].track_no){
					temp1[0] = PD[j].x;
					temp1[1] = PD[j].y;
					temp1[2] = PD[j].z;
					temp1[3] = PD[j].ts.getTime();
					temp1[4] = PD[j].modspd;
					temp2.add(temp1);
					temp1 = new double[5];
					
				}
				else if(tn == PD[j].track_no-1){
					
					temp3 = toArray(temp2);
					title = String.format("Speed Map of Phone No. %d track no %d", opt, tn);
					double[][] pmat = toPositionMap(temp3);
					writeArray(pmat, title);
					temp3 = new double [temp2.size()][];
					
					tn++;
				}
				
				else if(PD[j].track_no==-1){
					//System.out.printf("Row: %d not included as out of range\n", j);
				}
				
				else{
					System.out.printf("Error, some unknown values were found in the Track Number Postion in Row: %d of value: %d", j , PD[j].track_no);
				}
				
			}	
			temp3 = toArray(temp2);
			title = String.format("Speed Map of Phone No. %d track no %d", opt, tn);
			double[][] pmat = toPositionMap(temp3);
			writeArray(pmat, title);
			temp3 = new double [temp2.size()][];
		
		
	}
	
	
	private static void writeArray(double[][] input, String title){
		try {
			CSVWriter CSVW = new CSVWriter(title);
			CSVW.write(input);
			CSVW.finish();
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}
	
	private static double[][] toArray(ArrayList<double[]> list){
		double[][] result = new double[5][list.size()];
		for(int i = 0; i < list.size(); i++) {
			for(int k = 0; k < 5; k++){
				result[k][i] = list.get(i)[k];
				
			}
		}
		return result;
	}
	
	
	private static double[][] toPositionMap(double[][] input){
		int xval;
		int yval;
		int zval;
		double mv;
		
		double[][] output = new double[(int)maxColVal(input,0)+1][(int)maxColVal(input,1)+1];
		
		for(int x=0; x<input[0].length; x++){
			
			xval = (int) input[0][x];
		
			yval = (int) input[1][x];
			
			zval = (int) input[2][x];
			
			mv = input[4][x];

			
			output[xval][yval] = mv;
		
		}
		
		return output;
	}
	
	private static double maxColVal(double[][] data, int col){
		double temp = 0;
		double max = 0;
		for(int i = 0; i < data[0].length; i++){
			temp = data[col][i];
			if(temp>max){
				max = temp;
			}
		}
		return max;
	}
	

	
}
