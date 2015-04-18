package datamaps;
import java.util.ArrayList;

import objects.TrackInfo;
import csvexport.CSVWriter;
import csvimport.PhoneNames;
import dynamodb.DataBaseQueries;


public class TrackInfoToCSV {
	
	final static String TABLE_NAME = "The_Big_Track_Analysis";
	final static String FILE_LOCATION = "trackdatanew";
	
	public static void main(String[] args) throws Exception{
		writeCSV();
		writeZonedCSV();
		writeZonedCSVNoXY();
		
	}
	
	
	public static void writeCSV() throws Exception{
		DataBaseQueries DBQ = new DataBaseQueries(TABLE_NAME);
		String phone_id;
		PhoneNames pn = new PhoneNames();
		CSVWriter csv;
		ArrayList<TrackInfo> TI = new ArrayList<TrackInfo>();
		ArrayList<String> TIString = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();

		TIString.add("phone_id,track_no,pathLength,timeStopped,noStops,timeSpent,inactiveTime,sThetaChange,sThetaIn,sThetaOut,sThetaInOut,timePerStop,totAvrgSpeed,timesStoppedHere,pathPerShortest,timePerShortest,speedLessThan3,speedLessThan2,speedLessThan1,anglelargerthan5,anglelargerthan10,anglelargerthan15,anglelargerthan20,speedLargerThan10,x1,y1,x2,y2,characteristic\n");
		output.add("Businessman,Shopper,Security"+"\n");
		String behaviour = "0,0,0\n";
		
		
		for(int i = 1; i < 7; i++){
			
			phone_id = pn.numberToName(i);
			TI = new ArrayList<TrackInfo>();
			System.out.println("Querying phone: "+ phone_id);
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
				
				TIString.add(TI.get(j).toCSV()+"\n");
				output.add(behaviour);
				
			}
			System.out.println("Done phone: "+ phone_id+".");
		}
		
		csv = new CSVWriter(TABLE_NAME+"/"+FILE_LOCATION+ "/" +"input");
		csv.write(TIString.toArray(new String[TIString.size()]), true);
		csv.finish();
		csv = new CSVWriter(TABLE_NAME+"/"+FILE_LOCATION+ "/" +"output");
		csv.write(output.toArray(new String[output.size()]), true);
		csv.finish();
		System.out.println("Stored in: " + TABLE_NAME+"/"+FILE_LOCATION);
		System.out.println("Finished Writing.");
	}
	
	public static void writeZonedCSV() throws Exception{
		DataBaseQueries DBQ = new DataBaseQueries(TABLE_NAME);
		String phone_id;
		PhoneNames pn = new PhoneNames();
		CSVWriter csv;
		ArrayList<TrackInfo> TI = new ArrayList<TrackInfo>();
		ArrayList<String> TIString = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();
		String title = "phone_id,track_no,pathLength,timeStopped,noStops,timeSpent,inactiveTime,sThetaChange,sThetaIn,sThetaOut,sThetaInOut,timePerStop,totAvrgSpeed,timesStoppedHere,pathPerShortest,timePerShortest,speedLessThan3,speedLessThan2,speedLessThan1,anglelargerthan5,anglelargerthan10,anglelargerthan15,anglelargerthan20,speedLargerThan10,x1,y1,x2,y2,characteristic";
		String titlewithout = ",pathLength,timeStopped,noStops,timeSpent,inactiveTime,sThetaChange,sThetaIn,sThetaOut,sThetaInOut,timePerStop,totAvrgSpeed,timesStoppedHere,pathPerShortest,timePerShortest,speedLessThan3,speedLessThan2,speedLessThan1,anglelargerthan5,anglelargerthan10,anglelargerthan15,anglelargerthan20,speedLargerThan10,x1,y1,x2,y2,characteristic";
		
		for(int i = 0; i < 14; i++){
			title += titlewithout;
		}
		
		TIString.add(title + "\n");
		output.add("Businessman,Shopper,Security"+"\n");
		
		String behaviour = "0,0,0\n";
		String temp = new String();
		
		for(int i = 1; i < 7; i++){
			
			phone_id = pn.numberToName(i);
			TI = new ArrayList<TrackInfo>();
			System.out.println("Querying phone: "+ phone_id);
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
				
				if(j==0){
					temp = TI.get(j).toCSV();
				}
				else{
					boolean samephone = TI.get(j).getPHONE_ID().equalsIgnoreCase(TI.get(j-1).getPHONE_ID());
				
					if(TI.get(j).getTRACK_NO() == TI.get(j-1).getTRACK_NO() && samephone == true){
						temp += "," + TI.get(j).toCSVNoRef();
					}else{
						temp += "\n";
						TIString.add(temp);
						output.add(behaviour);
						temp = TI.get(j).toCSV();
					}
				}
				
				
			}
			System.out.println("Done phone: "+ phone_id+".");

		}
		
		csv = new CSVWriter(TABLE_NAME+"/"+FILE_LOCATION+ "/" +"zonedinput");
		csv.write(TIString.toArray(new String[TIString.size()]), true);
		csv.finish();
		csv = new CSVWriter(TABLE_NAME+"/"+FILE_LOCATION+ "/" +"zonedoutput");
		csv.write(output.toArray(new String[output.size()]), true);
		csv.finish();
		System.out.println("Stored in: " + TABLE_NAME+"/"+FILE_LOCATION);
		System.out.println("Finished Writing.");
	}
	
	public static void writeZonedCSVNoXY() throws Exception{
		DataBaseQueries DBQ = new DataBaseQueries(TABLE_NAME);
		String phone_id;
		PhoneNames pn = new PhoneNames();
		CSVWriter csv;
		ArrayList<TrackInfo> TI = new ArrayList<TrackInfo>();
		ArrayList<String> TIString = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();
		String title = "phone_id,track_no,pathLength,timeStopped,noStops,timeSpent,inactiveTime,sThetaChange,sThetaIn,sThetaOut,sThetaInOut,timePerStop,totAvrgSpeed,timesStoppedHere,pathPerShortest,timePerShortest,speedLessThan3,speedLessThan2,speedLessThan1,anglelargerthan5,anglelargerthan10,anglelargerthan15,anglelargerthan20,speedLargerThan10,characteristic";
		String titlewithout = ",pathLength,timeStopped,noStops,timeSpent,inactiveTime,sThetaChange,sThetaIn,sThetaOut,sThetaInOut,timePerStop,totAvrgSpeed,timesStoppedHere,pathPerShortest,timePerShortest,speedLessThan3,speedLessThan2,speedLessThan1,anglelargerthan5,anglelargerthan10,anglelargerthan15,anglelargerthan20,speedLargerThan10,characteristic";
		
		for(int i = 0; i < 14; i++){
			title += titlewithout;
		}
		
		TIString.add(title + "\n");
		output.add("Businessman,Shopper,Security"+"\n");
		
		String behaviour = "0,0,0\n";
		String temp = new String();
		
		for(int i = 1; i < 7; i++){
			
			phone_id = pn.numberToName(i);
			TI = new ArrayList<TrackInfo>();
			System.out.println("Querying phone: "+ phone_id);
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
				
				if(j==0){
					temp = TI.get(j).toCSVNoXY();
				}
				else{
					boolean samephone = TI.get(j).getPHONE_ID().equalsIgnoreCase(TI.get(j-1).getPHONE_ID());
				
					if(TI.get(j).getTRACK_NO() == TI.get(j-1).getTRACK_NO() && samephone == true){
						temp += "," + TI.get(j).toCSVNoXYNoRef();
					}else{
						temp += "\n";
						TIString.add(temp);
						output.add(behaviour);
						temp = TI.get(j).toCSVNoXY();
					}
				}
				
				
			}
			System.out.println("Done phone: "+ phone_id+".");

		}
		
		csv = new CSVWriter(TABLE_NAME+"/"+FILE_LOCATION+ "/" +"zonedinput noXY");
		csv.write(TIString.toArray(new String[TIString.size()]), true);
		csv.finish();
		csv = new CSVWriter(TABLE_NAME+"/"+FILE_LOCATION+ "/" +"zonedoutput noXY");
		csv.write(output.toArray(new String[output.size()]), true);
		csv.finish();
		System.out.println("Stored in: " + TABLE_NAME+"/"+FILE_LOCATION);
		System.out.println("Finished Writing.");
	}
	
}
	



