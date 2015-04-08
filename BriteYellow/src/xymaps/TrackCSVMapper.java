package xymaps;

import heatmap.HeatChart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import objects.PhoneData;
import objects.PhoneDataDB;
import csvexport.CSVWriter;
import dynamodb.DataBaseQueries;
import dynamodb.ObjectConversion;

public class TrackCSVMapper {
	
	
	String PHONE_ID;
	int[] TRACK_NOs;
	DataBaseQueries DBQ = new DataBaseQueries("Processed_Data");
	final String folderName = "XYMaps/";
	final String heatFolder = "heatMaps/";
	int SHORTEN_X_BY = 0;
	int SHORTEN_Y_BY = 0;
	
	public static void main(String[] args) throws Exception{
		
		/*int[] test = {1 ,2, 3,4,5,6,13,14,17,19,21,22,23,24,25,26,27,28,30,31,32,36,37};
		String phone = "HT25TW5055273593c875a9898b00";*/
		
		/*int[] test = {4,5,7,9,12,13,15,18,20,22,2,3,11,19};
		String phone = "ZX1B23QBS53771758c578bbd85";*/
		
		/*int[] test = {4, 8, 11, 12, 15, 17, 18, 19, 21, 22, 26, 28, 30, 31, 32, 33, 34, 39, 41, 42,
				43, 44, 45, 48, 49, 50, 51, 53, 54, 57, 59, 63, 65, 66, 67, 68, 69, 71, 72, 73,
				76, 77, 78, 79, 80, 81, 86, 88, 89, 90, 94, 95, 96, 98, 99};
		String phone = "ZX1B23QFSP48abead89f52e3bb";*/
		
		/*int[] test = {1, 3, 4, 7, 10};
		String phone = "YT910K6675876ded0861342065";*/
		
		int[] test = {3,6,9,22,24,28,32,33,36};
		String phone = "YT910K6675876ded0861342065";
	
		
		TrackCSVMapper TCM = new TrackCSVMapper(phone, test, 200, 267);
		TCM.writeSpeedMap();
		TCM.writeOnesMap();
		//TCM.makeSpeedHeatMap();
		
	}
	
	public TrackCSVMapper(String PHONE_ID, int[] TRACK_NOs, int SHORTEN_X_BY, int SHORTEN_Y_BY) throws Exception{
		this.PHONE_ID = PHONE_ID;
		this.TRACK_NOs = TRACK_NOs;
		this.SHORTEN_X_BY = SHORTEN_X_BY;
		this.SHORTEN_Y_BY = SHORTEN_Y_BY;
		
	}
	
	
	public void writeOnesMap() throws ParseException{
		ArrayList<PhoneDataDB> input1;
		ArrayList<PhoneData> input2;
		int maxtrack = 0;
		int temp;
		
		for(int i = 0; i < TRACK_NOs.length; i++){
			temp = TRACK_NOs[i];
			if(temp>maxtrack){
				maxtrack = temp;
				
			}
		}
		
		
		for(int i = 1; i <= maxtrack; i++){
			input1 = DBQ.queryTable(PHONE_ID, i);
			input2 = ObjectConversion.convertFrom(input1);
			onesPositionWriter(input2);
		}
			
			
		
	}
	
	public void writeSpeedMap() throws ParseException{
		ArrayList<PhoneDataDB> input1;
		ArrayList<PhoneData> input2;
		int maxtrack = 0;
		int temp;
		
		for(int i = 0; i < TRACK_NOs.length; i++){
			temp = TRACK_NOs[i];
			if(temp>maxtrack){
				maxtrack = temp;
				
			}
		}
		
		
		for(int i = 1; i <= maxtrack; i++){
			input1 = DBQ.queryTable(PHONE_ID, i);
			input2 = ObjectConversion.convertFrom(input1);
			speedPositionWriter(input2);
		}
			
			
		
	}
	
	public void makeSpeedHeatMap() throws ParseException, IOException{
		ArrayList<PhoneDataDB> input1;
		ArrayList<PhoneData> input2;
		int temp;
		input1 = DBQ.queryTable(PHONE_ID, 'a');
		input2 = ObjectConversion.convertFrom(input1);
		double[][] input = speedPositionPopulator(input2);
		writeSpeedHeatMap(input);
		
	}
	
	private void speedPositionWriter(ArrayList<PhoneData> TRACK_DATA) throws ParseException{
		String title = folderName+"/Speed/"+TRACK_DATA.get(0).phone_id+"/TRACK"+TRACK_DATA.get(0).track_no;
		int errors = 0;
		int xval;
		int yval;
		int zval;
		double mv;
		
		double[][] output = new double[850-SHORTEN_X_BY][375-SHORTEN_Y_BY];
		
		for(int x=0; x<TRACK_DATA.size(); x++){
			
			xval = (int) TRACK_DATA.get(x).x-SHORTEN_X_BY;
		
			yval = (int) TRACK_DATA.get(x).y-SHORTEN_Y_BY;
			
			zval = (int) TRACK_DATA.get(x).z;
			
			mv = TRACK_DATA.get(x).modspd;

			try{
				output[xval][yval] = mv;
			}catch(ArrayIndexOutOfBoundsException e){
				errors++;
				System.err.println("Error "+ errors + " For Track " + TRACK_DATA.get(x).track_no +" Discounted Speed points: x = " + xval + " y = " + yval);
			}
		
		}
		
		writeArray(output, title);
	}
	
	private void onesPositionWriter(ArrayList<PhoneData> TRACK_DATA) throws ParseException{
		String title = folderName+"/Ones/"+TRACK_DATA.get(0).phone_id+"/TRACK"+TRACK_DATA.get(0).track_no;
		int errors = 0;
		int xval;
		int yval;
		int zval;
		
		
		double[][] output = new double[850-SHORTEN_X_BY][375-SHORTEN_Y_BY];
		
		for(int x=0; x<TRACK_DATA.size(); x++){
			
			xval = (int) TRACK_DATA.get(x).x-SHORTEN_X_BY;
		
			yval = (int) TRACK_DATA.get(x).y-SHORTEN_Y_BY;
			
			zval = (int) TRACK_DATA.get(x).z;
			

			try{
				output[xval][yval] = 1;
			}catch(ArrayIndexOutOfBoundsException e){
				errors++;
				System.err.println("Error "+ errors + " For Track " + TRACK_DATA.get(x).track_no +" Discounted Ones points: x = " + xval + " y = " + yval);
			}
		
		}
		
		writeArray(output, title);
	}
	
	private double[][] speedPositionPopulator(ArrayList<PhoneData> PHONE_DATA) throws ParseException{
		String title = folderName+"/Speed/"+PHONE_DATA.get(0).phone_id+"/TRACK"+PHONE_DATA.get(0).track_no;
		int errors = 0;
		int xval;
		int yval;
		int zval;
		double mv;
		
		double[][] output = new double[850-SHORTEN_X_BY][375-SHORTEN_Y_BY];
		
		for(int x=0; x<PHONE_DATA.size(); x++){
			
			xval = (int) PHONE_DATA.get(x).x-SHORTEN_X_BY;
		
			yval = (int) PHONE_DATA.get(x).y-SHORTEN_Y_BY;
			
			zval = (int) PHONE_DATA.get(x).z;
			
			mv = PHONE_DATA.get(x).modspd;

			try{
				output[xval][yval] += mv;
			}catch(ArrayIndexOutOfBoundsException e){
				errors++;
				System.err.println("Error "+ errors + " For Track " + PHONE_DATA.get(x).track_no +" Discounted Speed points: x = " + xval + " y = " + yval);
			}
		
		}
		
		return output;
	}
	
	public void writeSpeedHeatMap(double[][] zhs) throws IOException{
		
		HeatChart map = new HeatChart(zhs);

		// Step 2: Customise the chart.
		map.setTitle("Speed");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(2);
		map.setCellHeight(3);
		System.setProperty("Axis", "#FF1493");
		map.setAxisColour(Color.getColor("Axis"));
		map.setShowXAxisValues(false);
		map.setShowYAxisValues(false);
		System.setProperty("LowVal", "#AFEEEE");
		System.setProperty("HighVal", "#191970");
		map.setHighValueColour(Color.getColor("HighVal"));
		map.setLowValueColour(Color.getColor("LowVal"));
		
		
		String filename = heatFolder + "Speed Heatmap for Phone " + PHONE_ID + ".png";
		
		File parent = new File(filename).getParentFile();
		if( !parent.exists())
			parent.mkdirs();		
		File file = new File(filename);
		map.saveToFile(file);
		
	}
	

	private static void writeArray(double[][] input, String title){
		try {
			CSVWriter CSVW = new CSVWriter(title);
			CSVW.write(input);
			CSVW.finish();
		} catch (IOException e) {
		System.err.println("IOException");
		}
	}
	
	
	private static double maxXVal(ArrayList<PhoneData> TRACK_DATA){
		double temp = 0;
		double max = 0;
		for(int i = 0; i < TRACK_DATA.size(); i++){
			temp = TRACK_DATA.get(i).x;
			if(temp>max){
				max = temp;
			}
		}
		return max;
	}
	
	private static double maxYVal(ArrayList<PhoneData> TRACK_DATA){
		double temp = 0;
		double max = 0;
		for(int i = 0; i < TRACK_DATA.size(); i++){
			temp = TRACK_DATA.get(i).y;
			if(temp>max){
				max = temp;
			}
		}
		return max;
	}
	
	
	
}
