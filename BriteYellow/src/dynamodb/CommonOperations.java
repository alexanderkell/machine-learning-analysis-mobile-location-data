package dynamodb;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import objects.*;
import filters.FilterMain;
import maths.DataGetter;
import csvimport.CSVReaders;
import csvimport.PhoneNames;
import csvimport.NewDataScaling;

public class CommonOperations {
	
	public static void reWriteNewData() throws Exception{
		PhoneNames phoneNames = new PhoneNames();
		DataBaseOperations DBO = new DataBaseOperations("March_Data");
		DBO.deleteTable();
		DBO.createTable();
		String PhoneID;
		
		for(int i = 1; i < 6; i++){
			
			if(i!=4){
				PhoneID = phoneNames.numberToName(i);
				final String filename = PhoneID +".csv";
				System.out.println("Getting Data From "+ filename);
				ArrayList<PhoneData> pd1_list = new CSVReaders(filename).getUncategorisedDataObjectList();
				System.out.println("Scaling Data From "+ filename);
				ArrayList<PhoneData> pd2_list = NewDataScaling.scaleAndCalcNewData(pd1_list);
				System.out.println("Converting Data From "+ filename);
				ArrayList<PhoneDataDB> pd3_list = ObjectConversion.convertToPhoneDataDB(pd2_list);
				System.out.println("Writing Data From "+ filename +"...");
				DBO.batchWrite(pd3_list);
				System.out.println("Finished Writing Data From "+ filename + ".");
			}
		}
	}
	
	public static void reWriteNewProcessedData() throws Exception{
		PhoneNames phoneNames = new PhoneNames();
		String PhoneID;
		DataBaseQueries QueryRaw = new DataBaseQueries("March_Data");
		DataBaseOperations DBO = new DataBaseOperations("March_Processed_Data");
		DBO.deleteTable();
		DBO.createTable();
		
		for(int i = 1; i < 6; i++){
			
			if(i!=4){
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
	
	public static void reWriteRawData() throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("3D_Cloud_Pan_Data");
		DBO.deleteTable();
		DBO.createTable();
		
		for(int i = 1; i < 6; i++){
			DataGetter DG = new DataGetter(i, "24th Sept ORDERED.CSV");
			ArrayList<PhoneData> pd24 = DG.getFullPhoneDataList();
			ArrayList<PhoneDataDB> pddb24 = ObjectConversion.convertToPhoneDataDB(pd24);
			System.out.println("Writing 24th Sept Values for phone " + i + "... ");
			DBO.batchWrite(pddb24);
		}
		
		for(int i = 1; i < 7; i++){
			DataGetter DG = new DataGetter(i, "26th Sept ORDERED.CSV");
			ArrayList<PhoneData> pd24 = DG.getFullPhoneDataList();
			ArrayList<PhoneDataDB> pddb24 = ObjectConversion.convertToPhoneDataDB(pd24);
			System.out.println("Writing 26th Sept Values for phone " + i + "... ");
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
