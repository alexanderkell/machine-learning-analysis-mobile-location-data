package dynamodb;

import java.util.ArrayList;

import filters.FilterMain;
import maths.DataGetter;
import maths.PhoneData;
import csvimport.PhoneNames;

public class CommonOperations {
	
	public static void reWriteRawData() throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("3D_Cloud_Pan_Data");
		DBO.deleteTable();
		DBO.createTable();
		
		for(int i = 0; i < 6; i++){
			DataGetter DG = new DataGetter(i, "24th Sept ORDERED.CSV");
			ArrayList<PhoneData> pd24 = DG.getFullPhoneDataList();
			ArrayList<PhoneDataDB> pddb24 = ObjectConversion.convertToPhoneDataDB(pd24);
			System.out.println("Writing 24th Septh Values for phone " + i + "... ");
			DBO.batchWrite(pddb24);
		}
		
		for(int i = 1; i < 7; i++){
			DataGetter DG = new DataGetter(i, "26th Sept ORDERED.CSV");
			ArrayList<PhoneData> pd24 = DG.getFullPhoneDataList();
			ArrayList<PhoneDataDB> pddb24 = ObjectConversion.convertToPhoneDataDB(pd24);
			System.out.println("Writing 26th Septh Values for phone " + i + "... ");
			DBO.batchWrite(pddb24);
		}
		
	}
	
	public static void reWriteProcessedData() throws Exception{
		PhoneNames phoneNames = new PhoneNames();
		String PhoneID;
		DataBaseQueries QueryRaw = new DataBaseQueries("3D_Cloud_Pan_Data");
		DataBaseOperations DBO = new DataBaseOperations("Processed_Data");
		DBO.deleteTable();
		DBO.createTable();
		
		for(int i = 1; i < 7; i++){
			
			PhoneID = phoneNames.numberToName(i);
			System.out.println("Querying tracks for phone: " + PhoneID);
			ArrayList<PhoneDataDB> outputDB = QueryRaw.queryTable(PhoneID, 'a');
			System.out.println("Converting tracks for phone: " + PhoneID);
			ArrayList<PhoneData> output= ObjectConversion.convertFrom(outputDB);
		
			System.out.println("Filtering tracks for phone: " + PhoneID);
			FilterMain filtering = new FilterMain(200, 5, 5, 3);
			ArrayList<PhoneData> filtered = filtering.FilterTot(output);
		
			// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
			DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
			PhoneData[] newdata = newdg.getFullPhoneData();
		
			ArrayList<PhoneDataDB> pddb = ObjectConversion.convertToPhoneDataDB(newdata);
			System.out.println("Saving data for phone: " + PhoneID + "...");
			DBO.batchWrite(pddb);
			System.out.println("Done saving data for phone: " + PhoneID + ".");
		}
		
		
		
	}
	
	
}
