package dynamodb;

import java.util.ArrayList;

import maths.PhoneData;

public class TimeProver {
	
	
	public static void main (String[] args) throws Exception{
		
		Printer("HT25TW5055273593c875a9898b00");
		
		
		
	}
	
	public static void Printer(String PHONE_ID) throws Exception{
		
		DataBaseQueries Queries = new DataBaseQueries("Processed_Data");
		ArrayList<PhoneDataDB> PDDB =
					Queries.queryTable(PHONE_ID,'a');
		ArrayList<PhoneData> PD = ObjectConversion.convertFrom(PDDB);
		
		
		for(int i = 0; i < PD.size(); i++){
			
			System.out.println(PD.get(i).toString());
			
			
		}
		
		
	}
}
