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
import objects.SmartTrackInfo;
import objects.TrackInfo;

import com.sun.org.apache.bcel.internal.generic.IXOR;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import splitting.*;
import graphing.*;



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
		//double[][] path_length = new double[TrackAnalysis.size()][TrackAnalysis.];
		

		/*float[][] y = getYCoords(TrackAnalysis);
		float[][] x = getXCoords(TrackAnalysis);
				
		xyScatterP plotObj = new xyScatterP();
		plotObj.plot(x,y, "test", "test","test","test","test");
		plotObj.plot(x, y, "Number of stops Vs Path Length", "Compare", "Number of Stops", "Path Length", "Comparison");*/
		writeToDB(TrackAnalysis);
	}
	
	public static ArrayList<PhoneData> getAllPhoneDB() throws Exception{
		DataBaseQueries DBQ= new DataBaseQueries("3D_Cloud_Pan_Data");
		PhoneNames phoneNames = new PhoneNames();
		ArrayList<PhoneDataDB> rawTotUn = new ArrayList<PhoneDataDB>();
		for(int i = 1; i < 6; i++){ 
			String PhoneID = phoneNames.numberToName(i); 
			System.out.println("Querying tracks for phone: " + PhoneID); 
			ArrayList<PhoneDataDB> outputDB = DBQ.queryTable(PhoneID, 'd'); 
			System.out.println("Converting tracks for phone: " + PhoneID); 
			rawTotUn.addAll(outputDB);
			System.out.println("Converted: "+i+"/5");
		}
		ArrayList<PhoneDataDB> rawTot = new ArrayList<PhoneDataDB>();
		rawTot=ObjectConversion.listSorter(rawTotUn);
		ArrayList<PhoneData> raw = ObjectConversion.convertFrom(rawTot);
		return raw;
	}
	
	public static void writeToDB(ArrayList<TrackInfo> TrackAnalysis) throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("The_Big_Track_Analysis");
		DBO.createTracksTable();
		System.out.println(DBO.toString());
		System.out.println("Writing to Database");
		DBO.batchWrite(TrackAnalysis);
		System.out.println("Write complete");
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
					TI.setX1(xbounds[j]);
					TI.setX2(xbounds[j+1]);
					TI.setY1(ybounds[k]);
					TI.setY2(ybounds[k+1]);
					TI.setCharacteristic(characteristicType(track[0].phone_id));
					totalTI.add(TI);
				}
			}
		}
		return totalTI;
	}
	
	public static int characteristicType(String phoneID){
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
	public static float[][] getCoords(ArrayList<TrackInfo> TrackAnalysis, int property){
		float result[][] = new float[3][TrackAnalysis.size()];
		for(int i = 0; i<result[0].length; i++){
			// Convert TrackInfo to SmartTrackInfo
			SmartTrackInfo sti = new SmartTrackInfo(TrackAnalysis.get(i));
			if(sti.getInt(SmartTrackInfo.Characteristic)==1){
				result[0][i] = (float) sti.getDouble(property);
			}else if(sti.getInt(SmartTrackInfo.Characteristic)==2){
				result[1][i] = (float) sti.getDouble(property);
			}else if(sti.getInt(SmartTrackInfo.Characteristic)==3)
				result[2][i] = (float) sti.getDouble(property);
		}
		return result;
	}
	
}
