import java.util.ArrayList;
import java.util.Random;

import objects.TrackInfo;
import datamaps.TrackInfoToCSV;
import dynamodb.DataBaseOperations;
import dynamodb.DataBaseQueries;


public class sampleData {
	public static void main(String args[]) throws Exception{
	
		String phoneName = "The_Big_Track_Analysis";
		DataBaseQueries dbq = new DataBaseQueries(phoneName);
		ArrayList<TrackInfo> all = new ArrayList<TrackInfo>();

		TrackInfoToCSV csv = new TrackInfoToCSV();
		for(int i = 0; i<11; i++){
			ArrayList<TrackInfo> x = dbq.queryTrackTable("HT25TW5055273593c875a9898b00", randInt(1, 36));
			all.addAll(x);
		}
		for(int i = 0; i<28; i++){
			ArrayList<TrackInfo> x = dbq.queryTrackTable("ZX1B23QFSP48abead89f52e3bb", randInt(1, 92));
			all.addAll(x);
		}
		for(int i = 0; i<11; i++){
			ArrayList<TrackInfo> x = dbq.queryTrackTable("TA92903URNf067ff16fcf8e045", randInt(1, 32));
			all.addAll(x);
		}
		for(int i = 0; i<3; i++){
			ArrayList<TrackInfo> x = dbq.queryTrackTable("YT910K6675876ded0861342065", randInt(1, 10));
			all.addAll(x);
		}
		for(int i = 0; i<7; i++){
			ArrayList<TrackInfo> x = dbq.queryTrackTable("ZX1B23QBS53771758c578bbd85", randInt(1, 22));
			all.addAll(x);
		}
		System.out.println("h");
		writeToDB(all, "Sample_Track_Set");
		
		
	}
	
	public static int randInt(int min, int max){
		Random rand = new Random();
		int randomNum = rand.nextInt((max-min)+1)+min;
		return randomNum;
	}
	
	public static void writeToDB(ArrayList<TrackInfo> TrackAnalysis, String tableN) throws Exception{
		DataBaseOperations DBO = new DataBaseOperations(tableN);
		//DBO.deleteTable();
		DBO.createTracksTable();
		System.out.println("Writing to Database");
		DBO.batchWrite(TrackAnalysis);
		System.out.println("Write complete");
	}
		
		//TrackInfoToCSV write = new TrackInfoToCSV();
}
