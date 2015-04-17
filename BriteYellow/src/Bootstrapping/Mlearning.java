package Bootstrapping;

import distribution.StatsGenerator;
import dynamodb.DataBaseOperations;
import dynamodb.DataBaseQueries;
import dynamodb.NoSQLDownload;
import dynamodb.ObjectConversion;
import filters.FilterMain;
import maths.DataGetter;
import csvimport.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import objects.PhoneData;
import objects.PhoneDataDB;
import objects.SmartTrackInfo;
import objects.TrackInfo;

import com.sun.org.apache.bcel.internal.generic.IXOR;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import splitting.*;
import graphing.*;



public class Mlearning {
	/*
	public  int[] xbounds = {
		200,850
	};
	public  int[] ybounds = {
		302,364
	};
	*/
	
	private int speed, xkalm, ykalm, Interp;
	
	public FilterMain filtering;
	
	public int[] xbounds = {
		200,330,460,590,720,850
	};
	public int[] ybounds = {
		302,322,344,364
	};
	
	
	public int[] property = {
		StatsGenerator.PATH_LENGTH,	//gettotalaverage
		StatsGenerator.TIME_STOPPED, //gettotalaverage
		StatsGenerator.NO_STOPS, //gettotalaverage
		
		StatsGenerator.TIME_SPENT, //gettotalaverage
		StatsGenerator.INACTIVE_TIME, //gettotalaverage
		StatsGenerator.STHETACHANGE, //gettotalaverage
		StatsGenerator.TIME_PER_STOP, //gettotalaverage
		StatsGenerator.AVERAGE_SPEED, //gettotalaverage
		StatsGenerator.FREQ_IN_AREA, //gettotalaverage
		StatsGenerator.PATH_PER_SHORTEST_PATH,
		StatsGenerator.TIME_PER_SHORTEST_PATH
	};
	
	public double[][] property2 = {
		{ StatsGenerator.AVERAGE_SPEED, 10, Double.POSITIVE_INFINITY},	// Speed > 10 points/sec
		{ StatsGenerator.AVERAGE_SPEED, 0, 3},	// Speed < 3 points/sec
	};
	final  int XSTILL = 30;	//Max x distance for determining whether
									// the person is staying still
	final  int YSTILL = 6;	//Max y distance for determining whether
									// the person is staying still
			
		

	public static void main(String args[]) throws Exception{

		
		Scanner sc = new Scanner(System.in);

		System.out.println("What is the max speed, x-Kalman Factor, y-Kalman Factor, and Interpolation value?");
		int speed = sc.nextInt(), xkalm = sc.nextInt(), ykalm = sc.nextInt(), Interp = sc.nextInt();
		
		Mlearning ml = new Mlearning(speed, xkalm, ykalm, Interp);
		
		ArrayList<TrackInfo> data = ml.queryAndProcess();
		/*
		float[][] x =getCoords(data, SmartTrackInfo.TOTAVRGSPEED);
		float[][] y = getCoords(data, SmartTrackInfo.NO_STOPS);
		
		xyScatterP plot = new xyScatterP();
		plot.plot(x, y, "Total Average Speed vs Number of Stops", "Total Average Speed vs Number of Stops", "Total Average Speed", "Number of Stops", "plot");
		*/
		
		
		ArrayList<TrackInfo> toWrite = ml.queryAndProcess();
		ml.writeToDB(toWrite);
		

				
	}
	
	public Mlearning(int speed, int xkalm, int ykalm, int Interp){
		this.speed = speed;
		this.xkalm = xkalm;
		this.ykalm = ykalm;
		this.Interp = Interp;
		
	}
	
	public ArrayList<PhoneData> queryAndFilter() throws Exception{
		DataBaseQueries DBQ= new DataBaseQueries("3D_Cloud_Pan_Data");
		PhoneNames phoneNames = new PhoneNames();
		ArrayList<PhoneDataDB> outputDB;
		ArrayList<PhoneData> raw = new ArrayList<PhoneData>();
		ArrayList<PhoneData> filtered = new ArrayList<PhoneData>();
		filtering = new FilterMain(speed, xkalm, ykalm, Interp);
		DataGetter newdg;
		PhoneData[] newdata;
		ArrayList<TrackInfo> TrackAnalysis;
		
		for(int i = 1; i < 6; i++){ 
			String PhoneID = phoneNames.numberToName(i); 
			System.out.println("Querying tracks for phone: " + PhoneID); 
			outputDB = DBQ.queryTable(PhoneID, 'a'); 
			System.out.println("Converting tracks for phone: " + PhoneID);
			raw = ObjectConversion.convertFrom(outputDB);
			System.out.println("Processing tracks for phone: " + PhoneID);
			filtered = filtering.FilterTot(raw);
			
		}

		return filtered;
	}
	
	

	public ArrayList<PhoneData> selectPhoneandFilter(String PhoneID) throws Exception{
		DataBaseQueries DBQ= new DataBaseQueries("3D_Cloud_Pan_Data");
		PhoneNames phoneNames = new PhoneNames();
		ArrayList<PhoneDataDB> outputDB;
		ArrayList<PhoneData> raw = new ArrayList<PhoneData>();
		ArrayList<PhoneData> filtered = new ArrayList<PhoneData>();
		filtering = new FilterMain(speed, xkalm, ykalm, Interp);
		DataGetter newdg;
		PhoneData[] newdata;
		ArrayList<TrackInfo> TrackAnalysis;

		 
		System.out.println("Querying tracks for phone: " + PhoneID); 
		outputDB = DBQ.queryTable(PhoneID, 'a'); 
		System.out.println("Converting tracks for phone: " + PhoneID);
		raw = ObjectConversion.convertFrom(outputDB);
		System.out.println("Processing tracks for phone: " + PhoneID);
		filtered = filtering.FilterTot(raw);
		


		return filtered;
	}
	
	
	public ArrayList<TrackInfo> queryAndProcess() throws Exception{
		DataBaseQueries DBQ= new DataBaseQueries("3D_Cloud_Pan_Data");
		PhoneNames phoneNames = new PhoneNames();
		ArrayList<PhoneDataDB> outputDB;
		ArrayList<PhoneData> raw = new ArrayList<PhoneData>();
		ArrayList<PhoneData> filtered = new ArrayList<PhoneData>();
		ArrayList<TrackInfo> trackAnTot = new ArrayList<TrackInfo>();
		filtering = new FilterMain(speed, xkalm, ykalm, Interp);
		DataGetter newdg;
		PhoneData[] newdata;
		ArrayList<TrackInfo> TrackAnalysis;
		
		for(int i = 1; i < 6; i++){ 
			String PhoneID = phoneNames.numberToName(i); 
			System.out.println("Querying tracks for phone: " + PhoneID); 
			outputDB = DBQ.queryTable(PhoneID, 'a'); 
			System.out.println("Converting tracks for phone: " + PhoneID);
			raw = ObjectConversion.convertFrom(outputDB);
			System.out.println("Processing tracks for phone: " + PhoneID);
			filtered = filtering.FilterTot(raw);
			newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
			newdata = newdg.getFullPhoneData();
			TrackAnalysis = writeTrackStats(newdata);
			trackAnTot.addAll(TrackAnalysis);
			System.out.println("Done: "+i+"/5");
			
		}

		return trackAnTot;
	}
	
	public void writeToDB(ArrayList<TrackInfo> TrackAnalysis) throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("The_Big_Track_Analysis");
		DBO.deleteTable();
		DBO.createTracksTable();
		System.out.println("Writing to Database");
		DBO.batchWrite(TrackAnalysis);
		System.out.println("Write complete");
	}
	
	public ArrayList<TrackInfo> writeTrackStats(PhoneData[] newdata) throws ParseException, java.text.ParseException{
		TrackSelect TS = new TrackSelect();
		ArrayList<TrackInfo> totalTI = new ArrayList<TrackInfo>();
		for(int i=1; i<=TS.getTotalTracks(newdata); i++){

			PhoneData[] track = TS.selecter(newdata, i);
			StatsGenerator statGen = new StatsGenerator(track);
			
			if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.AVERAGE_SPEED, 200, 850, 302, 364))
					&& statGen.getTotalAverage(StatsGenerator.AVERAGE_SPEED, 200, 850, 302, 364)< 1000){	

			for(int j = 0; j<xbounds.length-1; j++){
				for(int k =0; k<ybounds.length-1; k++){	
						//Setting of data					
						TrackInfo TI = new TrackInfo();
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.INACTIVE_TIME, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setINACTIVE_TIME(statGen.getTotalAverage(StatsGenerator.INACTIVE_TIME, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setINACTIVE_TIME(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.NO_STOPS, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setNO_STOPS(statGen.getTotalAverage(StatsGenerator.NO_STOPS, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setNO_STOPS(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.PATH_LENGTH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setPATH_LENGTH(statGen.getTotalAverage(StatsGenerator.PATH_LENGTH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setPATH_LENGTH(0);
						TI.setPHONE_ID(track[0].phone_id);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.STHETACHANGE, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setSTHETACHANGE(statGen.getTotalAverage(StatsGenerator.STHETACHANGE, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setSTHETACHANGE(0);
	//					TI.setSTHETAIN(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
	//					TI.setSTHETAOUT(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
	//					TI.setTIME_STOPPED(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
	//					TI.setSTHETAINOUT(statGen.getTotalAverage(StatsGenerator.STHETACHANGE_NO, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.TIME_PER_STOP, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTIMEPERSTOP(statGen.getTotalAverage(StatsGenerator.TIME_PER_STOP, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setTIMEPERSTOP(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.FREQ_IN_AREA, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTIMESSTOPPEDHERE(statGen.getTotalAverage(StatsGenerator.FREQ_IN_AREA, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setTIMESSTOPPEDHERE(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.AVERAGE_SPEED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTOTAVRGSPEED(statGen.getTotalAverage(StatsGenerator.AVERAGE_SPEED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setTOTAVRGSPEED(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.TIME_STOPPED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTIMESSTOPPEDHERE(statGen.getTotalAverage(StatsGenerator.TIME_STOPPED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setTIME_SPENT(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.TIME_SPENT, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTIME_SPENT(statGen.getTotalAverage(StatsGenerator.TIME_SPENT, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setTIME_SPENT(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.TIME_SPENT, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTRACK_NO(i);
						else
							TI.setTRACK_NO(-1);
						//if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.PATH_PER_SHORTEST_PATH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
						//	TI.setPathPerShortest(statGen.getTotalAverage(StatsGenerator.PATH_PER_SHORTEST_PATH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						//else
						//	TI.setPathPerShortest(0);
						if(Double.isFinite(statGen.getTotalAverage(StatsGenerator.SHORTEST_PATH_LENGTH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])))
							TI.setTimePerShortest(statGen.getTotalAverage(StatsGenerator.SHORTEST_PATH_LENGTH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
						else
							TI.setTimePerShortest(0);
						

						double speedlessthan3 = statGen.getTotalFreqAt(StatsGenerator.AVERAGE_SPEED, 2, 3, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]);
						if(Double.isFinite(speedlessthan3))
							TI.setSpeedLessThan3(speedlessthan3);
						else
							TI.setSpeedLessThan3(0);
						
						
						double speedlargerthan10 = statGen.getTotalFreqAt(StatsGenerator.AVERAGE_SPEED, 10, Double.POSITIVE_INFINITY, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]);
						if(Double.isFinite(speedlargerthan10))
							TI.setSpeedLargerThan10(speedlargerthan10);
						else
							TI.setSpeedLargerThan10(0);
						
						TI.setX1(xbounds[j]);
						TI.setX2(xbounds[j+1]);
						TI.setY1(ybounds[k]);
						TI.setY2(ybounds[k+1]);
						TI.setCharacteristic(characteristicType(track[0].phone_id));
						totalTI.add(TI);
					}
				}
			}
		}
		return totalTI;
	}
	
	public int characteristicType(String phoneID){
		int type = 0;
		if(phoneID.contentEquals("HT25TW5055273593c875a9898b00")){
			type = 1;
		}else if(phoneID.contentEquals("ZX1B23QBS53771758c578bbd85")){
			type = 2;
		}else if(phoneID.contentEquals("TA92903URNf067ff16fcf8e045")){
			type = 3;
		}else if(phoneID.contentEquals("YT910K6675876ded0861342065")){
			type = 3;
		}else if(phoneID.contentEquals("ZX1B23QFSP48abead89f52e3bb")){
			type = 1;
		}
		return type;
	}
	
	/**Get coordinates
	 * 
	 * @param TrackAnalysis: TrackInfo in ArrayList
	 * @param property: the attribute name of type double
	 * @return
	 */
	
//////////////////////////////////////////////////////////////////
//						Create Coordinates
//						Name: Alex Kell
//						Date: 02/04/2015
//						Date of Update: 05/04/2015
//	Description: Create coordinates based on characteristic required
////////////////////////////////////////////////////////////////////
	
	public static float[][] getCoords(ArrayList<TrackInfo> TrackAnalysis, int property){
		float result[][] = new float[3][TrackAnalysis.size()];
		for(int i = 0; i<result[0].length; i++){
			// Convert TrackInfo to SmartTrackInfo (Allows for )
			SmartTrackInfo sti = new SmartTrackInfo(TrackAnalysis.get(i));
			if(sti.getInt(SmartTrackInfo.Characteristic)==1){	//Places
				result[0][i] = (float) sti.getDouble(property);	
			}else if(sti.getInt(SmartTrackInfo.Characteristic)==2){
				result[1][i] = (float) sti.getDouble(property);
			}else if(sti.getInt(SmartTrackInfo.Characteristic)==3)
				result[2][i] = (float) sti.getDouble(property);
		}
		return result;
	}
	
}
