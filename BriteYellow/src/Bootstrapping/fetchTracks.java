package Bootstrapping;

import java.util.ArrayList;

import objects.PhoneDataDB;
import dynamodb.DataBaseQueries;

public class fetchTracks {
	public static void main(String args[]) throws Exception{
		DataBaseQueries dbq = new DataBaseQueries("Processed_Data");
		System.out.println("Querying table");
		ArrayList<PhoneDataDB> x = dbq.queryTable("TA92903URNf067ff16fcf8e045", 'a');
		for(int i=0; i<x.size(); i++){
			System.out.println(x.get(i).getTimestampLong()+" "+x.get(i).getXPosition()+" "+x.get(i).getYPosition());
		}
	}
}
