import distribution.StatsGenerator;
import dynamodb.DataBaseOperations;
import dynamodb.DataBaseQueries;
import dynamodb.NoSQLDownload;
import dynamodb.ObjectConversion;
import filters.FilterMain;
import maths.DataGetter;
import csvimport.*;

import java.util.ArrayList;
import java.util.Scanner;

import objects.PhoneData;
import objects.PhoneDataDB;
import objects.TrackInfo;

import com.sun.org.apache.bcel.internal.generic.IXOR;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

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
		FilterMain filtering = new FilterMain(speed, xkalm, ykalm, Interp);
		ArrayList<PhoneData> filtered = filtering.FilterTot(raw);
		
		// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
		DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
		final PhoneData[] newdata = newdg.getFullPhoneData();
		
		ArrayList<TrackInfo> TrackAnalysis=writeTrackStats(newdata);
		
		DataBaseOperations DBO = new DataBaseOperations("The_Big_Track_Analysis");
		//DBO.createTable();
		//DBO.batchWrite(StatsDB);
		//DBO.

		
	}
	
	public static ArrayList<PhoneData> getAllPhoneDB() throws Exception{
		DataBaseQueries DBQ= new DataBaseQueries("3D_Cloud_Pan_Data");
		PhoneNames phoneNames = new PhoneNames();
		ArrayList<PhoneDataDB> rawTotUn = new ArrayList<PhoneDataDB>();
		for(int i = 1; i < 7; i++){ 
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
	
	public static ArrayList<TrackInfo> writeTrackStats(PhoneData[] newdata) throws ParseException, java.text.ParseException{
		TrackSelect TS = new TrackSelect();
		ArrayList<TrackInfo> totalTI = new ArrayList<TrackInfo>();
		for(int i=1; i<=TS.getTotalTracks(newdata); i++){
			PhoneData[] track = TS.selecter(newdata, i);
			StatsGenerator statGen = new StatsGenerator(track);
			for(int j = 0; j<xbounds.length-1; j++){
				for(int k =0; k<ybounds.length-1; k++){	
					//Setting of data					
					TrackInfo TI = new TrackInfo();
					TI.setINACTIVE_TIME(statGen.getTotalAverage(StatsGenerator.INACTIVE_TIME, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setNO_STOPS(statGen.getTotalAverage(StatsGenerator.NO_STOPS, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setPATH_LENGTH(statGen.getTotalAverage(StatsGenerator.PATH_LENGTH, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setPHONE_ID(newdata[i].phone_id);
					TI.setSTHETACHANGE(statGen.getTotalAverage(StatsGenerator.STHETACHANGE, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
//					TI.setSTHETAIN(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setSTHETAOUT(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setTIME_STOPPED(statGen.getTotalAverage(property[1], 200, 850, 302, 364));
//					TI.setSTHETAINOUT(statGen.getTotalAverage(StatsGenerator.STHETACHANGE_NO, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTIMEPERSTOP(statGen.getTotalAverage(StatsGenerator.TIME_PER_STOP, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTIMESSTOPPEDHERE(statGen.getTotalAverage(StatsGenerator.FREQ_IN_AREA, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTOTAVRGSPEED(statGen.getTotalAverage(StatsGenerator.AVERAGE_SPEED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTIMESSTOPPEDHERE(statGen.getTotalAverage(StatsGenerator.TIME_STOPPED, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTIME_SPENT(statGen.getTotalAverage(StatsGenerator.TIME_SPENT, xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
					TI.setTRACK_NO(i);
					TI.setX1(xbounds[j]);
					TI.setX2(xbounds[j+1]);
					TI.setY1(ybounds[k]);
					TI.setY2(ybounds[k+1]);
					totalTI.add(TI);
				}
			}
		}
		return totalTI;
	}
}
