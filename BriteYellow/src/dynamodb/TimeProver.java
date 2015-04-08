package dynamodb;

import java.util.ArrayList;

import Objects.PhoneData;
import Objects.PhoneDataDB;

public class TimeProver {
	
	
	public static void main (String[] args) throws Exception{
		
		Printer("HT25TW5055273593c875a9898b00", 1);
		
		
		
	}
	
	public static void Printer(String PHONE_ID, int Track_No) throws Exception{
		
		DataBaseQueries Queries = new DataBaseQueries("Processed_Data");
		ArrayList<PhoneDataDB> PDDB =
					Queries.queryTable(PHONE_ID, Track_No);
		ArrayList<PhoneData> PD = ObjectConversion.convertFrom(PDDB);
		
		
		for(int i = 0; i < PD.size(); i++){
			
			System.out.println(PD.get(i).toString());
			
			
		}
		
		
	}
}
