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
		speedMapper();
		
	}
	
	public static void speedWriter() throws ParseException{
		int length;
		DataGetter DG = new DataGetter(opt,fn);
		length = DG.getLength();
		PhoneData[] PD = DG.getFullPhoneData();
		int maxtn = PD[length-1].track_no;
		int tn = 1;
		double [] temp1 = new double[5];
		ArrayList<double[]> temp2 = new ArrayList<double[]>();;
		double[][] temp3;
			for(int j = 0; j<length; j++){
				
				
				if(tn == PD[j].track_no){
					temp1[0] = PD[j].x;
					temp1[1] = PD[j].y;
					temp1[2] = PD[j].z;
					temp1[3] = PD[j].ts.getTime();
					temp1[4] = PD[j].modspd;
					temp2.add(temp1);
					
				}
				else if(tn == PD[j].track_no-1){
					try {
						temp3 = toArray(temp2);
						CSVWriter CSVW = new CSVWriter(String.format("Phone No. %d track no %d", opt, tn));
						CSVW.write(temp3);
						CSVW.finish();
					} catch (IOException e) {
						System.out.println("IOException");
					}
					tn++;
				}
				
				else if(PD[j].track_no==-1){
					//System.out.printf("Row: %d not included as out of range\n", j);
				}
				
				else{
					System.out.printf("Error, some unknown values were found in the Track Number Postion in Row: %d of value: %d", j , PD[j].track_no);
				}
				
			}	
			
			try {
				temp3 = toArray(temp2);
				CSVWriter CSVW = new CSVWriter(String.format("Phone No. %d track no %d", opt, tn));
				CSVW.write(temp3);
				CSVW.finish();
			} catch (IOException e) {
				System.out.println("IOException");
			}
		
		
	}
	
	public static void speedMapper() throws ParseException{
		int length;
		DataGetter DG = new DataGetter(opt,fn);
		length = DG.getLength();
		PhoneData[] PD = DG.getFullPhoneData();
		int maxtn = PD[length-1].track_no;
		int tn = 1;
		double [] temp1 = new double[5];
		ArrayList<double[]> temp2 = new ArrayList<double[]>();;
		double[][] temp3;
		double[][] pm;
			for(int j = 0; j<length; j++){
				
				
				if(tn == PD[j].track_no){
					temp1[0] = PD[j].x;
					temp1[1] = PD[j].y;
					temp1[2] = PD[j].z;
					temp1[3] = PD[j].ts.getTime();
					temp1[4] = PD[j].modspd;
					temp2.add(temp1);
					
				}
				else if(tn == PD[j].track_no-1){
					try {
						temp3 = toArray(temp2);
						pm = toPositionMap(temp3);
						CSVWriter CSVW = new CSVWriter(String.format("Position Speed Map for Track No %d", tn));
						CSVW.write(pm);
						CSVW.finish();
					} catch (IOException e) {
						System.out.println("IOException");
					}
					tn++;
				}
				
				else if(PD[j].track_no==-1){
					//System.out.printf("Row: %d not included as out of range\n", j);
				}
				
				else{
					System.out.printf("Error, some unknown values were found in the Track Number Postion in Row: %d of value: %d", j , PD[j].track_no);
				}
				
			}	
			
			try {
				temp3 = toArray(temp2);
				pm = toPositionMap(temp3);
				CSVWriter CSVW = new CSVWriter(String.format("Position Speed Map for Track No %d", tn));
				CSVW.write(pm);
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
			System.out.println(output[xval][yval]);
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
	
	
	/*for(int i = 0; i < temp3[0].length; i++) {
		for(int k = 0; k < 5; k++){
			System.out.print(temp3[k][i]+" ");
			
		}
		System.out.println();
	}*/
	


/*for(int i = 0; i < temp2.size(); i++) {
	for(int k = 0; k < 5; k++){
		System.out.print(" "+temp2.get(i)[k]);
		
	}
	System.out.println();
}*/	
	
}
