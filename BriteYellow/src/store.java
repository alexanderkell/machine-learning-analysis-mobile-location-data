import distribution.StatsGenerator;
import dynamodb.DataBaseOperations;
import dynamodb.DataBaseQueries;
import dynamodb.NoSQLDownload;
import dynamodb.ObjectConversion;
import dynamodb.PhoneDataDB;
import dynamodb.TrackInfo;
import filters.FilterMain;
import maths.DataGetter;
import maths.PhoneData;
import csvimport.*;

import java.util.ArrayList;
import java.util.Scanner;

import com.sun.org.apache.bcel.internal.generic.IXOR;

import splitting.*;

public class mLearningOptimisation {
/*	
	public static int[] xbounds = {
		200,850
	};
	public static int[] ybounds = {
		302,364
	};
*/	
	
	public static int[] xbounds = {
		200,330,460,590,720,850
	};
	public static int[] ybounds = {
		302,322,344,364
	};
	
	
	public static int[] property = {
		StatsGenerator.PATH_LENGTH,	//gettotalaverage
		StatsGenerator.TIME_STOPPED, //gettotalaverage
		StatsGenerator.NO_STOPS, //gettotalaverage
		
		StatsGenerator.TIME_SPENT, //gettotalaverage
		StatsGenerator.INACTIVE_TIME, //gettotalaverage
		StatsGenerator.STHETACHANGE, //gettotalaverage
		StatsGenerator.TIME_PER_STOP, //gettotalaverage
		StatsGenerator.AVERAGE_SPEED, //gettotalaverage
		StatsGenerator.FREQ_IN_AREA, //gettotalaverage
	};
	final static int XSTILL = 30;	//Max x distance for determining whether
									// the person is staying still
	final static int YSTILL = 6;	//Max y distance for determining whether
									// the person is staying still
			
		

	public static void main(String args[]) throws Exception{
		
		ArrayList<PhoneData> raw = getAllPhoneDB();
		
		Scanner sc = new Scanner(System.in);

		System.out.println("What is the max speed, x-Kalman Factor, y-Kalman Factor, and Interpolation value?");
		int speed = sc.nextInt(), xkalm = sc.nextInt(), ykalm = sc.nextInt(), Interp = sc.nextInt();
		
		//int maxSpeed, int xMesNoise, int yMesNoise, int interpolNo
		FilterMain filtering = new FilterMain(speed, xkalm, ykalm, Interp);
		ArrayList<PhoneData> filtered = filtering.FilterTot(raw);
		
		// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
		DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
		final PhoneData[] newdata = newdg.getFullPhoneData();
		
		ObjectConversion OC = new ObjectConversion();
		System.out.println("Converting to DB Format");
		ArrayList<PhoneDataDB> pdb = OC.convertToPhoneDataDB(newdata);
		DataBaseOperations DBO = new DataBaseOperations("Machine_Learning_Filtered_D");
		System.out.println("Writing to Machine Learning Database");
		DBO.batchWrite(pdb);
		
		DataBaseQueries DBQ = new DataBaseQueries("Machine_Learning_Filtered_D");
		PhoneNames phoneNames = new PhoneNames();
		String PhoneID = phoneNames.numberToName(1); 
		System.out.println("Querying for track");
		ArrayList<PhoneDataDB> track = DBQ.queryTable(PhoneID, 2);
		System.out.println(track.size());
		System.out.println("Finding maximum track number");
		int maxTNo = DBQ.findMaxTrackNo(PhoneID);
		System.out.println(maxTNo);
		/*
		
		TrackSelect TS = new TrackSelect();
		
		ArrayList<TrackInfo> totalTI = new ArrayList<TrackInfo>();
		
		System.out.println(TS.getTotalTracks(newdata));

		
		
		for(int j = 0; j<xbounds.length-1; j++){
			for(int k =0; k<ybounds.length-1; k++){	
				for(int i=1; i<=TS.getTotalTracks(newdata); i++){
					PhoneData[] track = TS.selecter(newdata, i);
					System.out.println("track length = "+track.length);
					System.out.println("i = "+i);
					
					StatsGenerator statGen = new StatsGenerator(track);
					
					System.out.println(statGen.getTotalAverage(StatsGenerator.INACTIVE_TIME, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1])+": "+xbounds[j]+", "+xbounds[j+1]+", "+ybounds[k]+", "+ybounds[k+1]);
/*				
					//Setting of data
					TrackInfo TI = new TrackInfo();
					TI.setINACTIVE_TIME(statGen.getTotalAverage(StatsGenerator.INACTIVE_TIME, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setNO_STOPS(statGen.getTotalAverage(StatsGenerator.NO_STOPS, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setPATH_LENGTH(statGen.getTotalAverage(StatsGenerator.PATH_LENGTH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setPHONE_ID(newdata[i].phone_id);
					TI.setSTHETACHANGE(statGen.getTotalAverage(StatsGenerator.STHETACHANGE, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
//					TI.setSTHETAIN(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setSTHETAINOUT(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setSTHETAOUT(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setTIME_SPENT(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setTIME_STOPPED(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
					TI.setTIMEPERSTOP(statGen.getTotalAverage(StatsGenerator.TIME_PER_STOP, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTIMESSTOPPEDHERE(statGen.getTotalAverage(StatsGenerator.FREQ_IN_AREA, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTOTAVRGSPEED(statGen.getTotalAverage(StatsGenerator.AVERAGE_SPEED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTRACK_NO(i);
					TI.setX1(xbounds[j]);
					TI.setX2(xbounds[j+1]);
					TI.setY1(ybounds[k]);
					TI.setY2(ybounds[k+1]);
		
				}
			}
		}
		
		
*/
		
		
		
/*
		double[] x = statGen.get(100, 200, 330, 302, 322);
		statGen.getPhoneID(1);
		for(int i=0; i<x.length; i++){
			System.out.printf();
		}
*/
/*

	
//		public int phoneindexcount = 0;
		

	*/		
		
		/*
		
		for(int j = 0; j < xbounds.length-1; j++){
			for(int k = 0; k < ybounds.length-1; k++){
				int index = j*(ybounds.length-1) + k;
				
				String[] results = new String[property.length+property2.length+2];
				results[0] = phones[i];
				results[1] = String.valueOf(track);
				for(int l = 0; l < property.length; l++){
					results[l+2] = String.valueOf(sg.getTotalAverage(property[l], xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
				}
				for(int m = 0; m < property2.length; m++){
					results[m+property.length+2] = String.valueOf(sg.getTotalFreqAt(property2[m][0], property2[m][1],property2[m][2],xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
				}
				// Write the stats
				cw[index].write(results);
			}
		}
		
		*/
	}
	
	public static ArrayList<PhoneData> getAllPhoneDB() throws Exception{
		DataBaseQueries DBQ= new DataBaseQueries("3D_Cloud_Pan_Data");
		PhoneNames phoneNames = new PhoneNames();
		ArrayList<PhoneDataDB> rawTotUn = new ArrayList<PhoneDataDB>();
		for(int i = 1; i < 2; i++){ 
			String PhoneID = phoneNames.numberToName(i); 
			System.out.println("Querying tracks for phone: " + PhoneID); 
			ArrayList<PhoneDataDB> outputDB = DBQ.queryTable(PhoneID, 'd'); 
			System.out.println("Converting tracks for phone: " + PhoneID); 
			//ArrayList<PhoneData> raw = ObjectConversion.convertFrom(outputDB);
			rawTotUn.addAll(outputDB);
			System.out.println("Converted: "+i+"/6");
		}
		ArrayList<PhoneDataDB> rawTot = new ArrayList<PhoneDataDB>();
		rawTot=ObjectConversion.listSorter(rawTotUn);
		ArrayList<PhoneData> raw = ObjectConversion.convertFrom(rawTot);
		return raw;
	}
}
