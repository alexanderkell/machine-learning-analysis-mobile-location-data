import java.util.ArrayList;

import objects.TrackInfo;
import csvexport.CSVWriter;
import csvimport.PhoneNames;
import dynamodb.DataBaseQueries;


public class TrackInfoToCSV {

	public static void main(String[] args) throws Exception{
		DataBaseQueries DBQ = new DataBaseQueries("The_Big_Track_Analysis");
		String phone_id;
		PhoneNames pn = new PhoneNames();
		CSVWriter csv;
		ArrayList<TrackInfo> TI = new ArrayList<TrackInfo>();
		ArrayList<String> TIString = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();
		String fileLoc = "trackdatafiles/";

		TIString.add("PHONE_ID ,TRACK_NO,PATH_LENGTH,TIME_STOPPED,NO_STOPS,TIME_SPENT,INACTIVE_TIME,STHETACHANGE,STHETAIN,STHETAOUT,STHETAINOUT,TIMEPERSTOP,TOTAVRGSPEED,TIMESSTOPPEDHERE,X1,Y1,X2,Y2\n");
		String behaviour = "0,0,0\n";
		
		
		for(int i = 1; i < 7; i++){
			
			phone_id = pn.numberToName(i);
			TI = new ArrayList<TrackInfo>();
			TI = DBQ.queryTrackTable(phone_id, 'a');
			
			//Businessman,Shopper,Security
			if(phone_id.equalsIgnoreCase("TA92903URNf067ff16fcf8e045")){
				behaviour = "0,1,0\n";
			}
			else if(phone_id.equalsIgnoreCase("ZX1B23QFSP48abead89f52e3bb")){
				behaviour = "1,0,0\n";
			}
			else if(phone_id.equalsIgnoreCase("HT25TW5055273593c875a9898b00")){
				behaviour = "1,0,0\n";
			}
			else if(phone_id.equalsIgnoreCase("YT910K6675876ded0861342065")){
				behaviour = "0,1,0\n";
			}
			else if(phone_id.equalsIgnoreCase("ZX1B23QBS53771758c578bbd85")){
				behaviour = "0,0,1\n";
			}
			
			for(int j = 0; j < TI.size(); j++){
				
				TIString.add(TI.get(j).toCSV());
				output.add(behaviour);
				
			}
		}
		
		csv = new CSVWriter(fileLoc+"input");
		csv.write(TIString.toArray(new String[TIString.size()]), true);
		csv.finish();
		csv = new CSVWriter(fileLoc+"output");
		csv.write(output.toArray(new String[output.size()]), true);
		csv.finish();
		
	}
	
}
