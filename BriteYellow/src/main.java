import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import maths.*;
import mysql.*;
import csvimport.*;
import dynamodb.*;
import filters.*;
import filters.jkalman.JKalmanHelper;
import graphing.PlotTracks;
import redundant.DataSorting2;
import splitting.*;

public class main {
	public static void main(String args[]) throws Exception{
		
		System.out.println("Track machine learning 101");
		

		//System.out.println("Enter PhoneID String eg: HT25TW5055273593c875a9898b00");
		//String query = sc.nextLine();
		DataBaseQueries QueryRaw = new DataBaseQueries("3D_Cloud_Pan_Data");
		DataBaseOperations DBO = new DataBaseOperations("Processed_Data");
		
		System.out.println("Querying");
		ArrayList<PhoneDataDB> outputDB = QueryRaw.queryTable("8d32435715629c24a4f3a16b");
		System.out.println("Converting");
		ArrayList<PhoneData> output= DBO.convertFrom(outputDB);
		
		System.out.println("Filtering");
		FilterMain filtering = new FilterMain(200, 5, 5, 3);
		ArrayList<PhoneData> filtered = filtering.FilterTot(output);
		
		// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
		DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
		PhoneData[] newdata = newdg.getFullPhoneData();
		
		DBO.createTable();
		
		ArrayList<PhoneDataDB> pddb = DBO.convertToPhoneDataDB(newdata);
		System.out.println("Saving Data...");
		DBO.batchWrite(pddb);
		System.out.println("Done Saving Data.");
		
		
		//PlotTracks.plotTrack2(output.toArray(new PhoneData[output.size()]),filtered.toArray(new PhoneData[filtered.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);
	}
}
