package crosscorrelation;

import java.util.ArrayList;

import csvimport.PhoneNames;
import dynamodb.DataBaseQueries;
import objects.PhoneData;
import objects.TrackInfo;
import csvexport.CSVWriter;

public class AutoCorrelator {
	
	final static String TABLE_NAME = "Generated_Track_Store_Whole_Corridor";
	private ArrayList<TrackInfo> ti;
	private double[] track;
	
	
	public static void main(String[] args) throws Exception{
		ArrayList<TrackInfo> tifull = new ArrayList<TrackInfo>();
		ArrayList<TrackInfo> tifuller = new ArrayList<TrackInfo>();
		DataBaseQueries DBQ = new DataBaseQueries(TABLE_NAME);
		String phone_id;
		PhoneNames pn = new PhoneNames();
		CSVWriter csv = new CSVWriter(TABLE_NAME+"/"+"Autocorr");
		
		
		
		for(int i = 1; i < 7; i++){
			
				phone_id = pn.numberToName(i);
				System.out.println("Querying phone: "+ phone_id);
				tifull.addAll(DBQ.queryTrackTable(phone_id, 'a'));
		}
		
		AutoCorrelator AC = new AutoCorrelator(tifull);
		
		AC.makeAR();
		tifuller = AC.getData();
		
		for(TrackInfo elem : tifuller){
			csv.write(elem.getAutocorrelation());
		}
		
		csv.finish();
		
		
		System.out.println("Written AC");
	}
	
	public AutoCorrelator(ArrayList<TrackInfo> ti){
		
		this.ti = ti;
		
	}
	
	private double[] setupArray(TrackInfo tr){
		
		track = new double[22];
		track[0] = tr.getAngleLargerThan10();
		track[1] = tr.getAngleLargerThan15();
		track[2] = tr.getAngleLargerThan20();
		track[3] = tr.getAngleLargerThan5();
		track[4] = tr.getINACTIVE_TIME();
		track[5] = tr.getNO_STOPS();
		track[6] = tr.getPATH_LENGTH();
		track[7] = tr.getPathPerShortest();
		track[8] = tr.getSpeedLargerThan10();
		track[9] = tr.getSpeedLessThan1();
		track[10] = tr.getSpeedLessThan2();
		track[11] = tr.getSpeedLessThan3();
		track[12] = tr.getSTHETACHANGE();
		track[13] = tr.getSTHETAIN();
		track[14] = tr.getSTHETAINOUT();
		track[15] = tr.getSTHETAOUT();
		track[16] = tr.getTIME_SPENT();
		track[17] = tr.getTIME_STOPPED();
		track[18] = tr.getTimePerShortest();
		track[19] = tr.getTIMEPERSTOP();
		track[20] = tr.getTIMESSTOPPEDHERE();
		track[21] = tr.getTOTAVRGSPEED();
		
		return track;
		
	}
	
	public void makeAR(){
		
		for(TrackInfo elem : ti){
			setupArray(elem);
			elem.setAutocorrelation(DSP.xcorr(track));
		}
		
	}
	
	public ArrayList<TrackInfo> getData(){
		
		return ti;
	}

	
	
	
	
}
