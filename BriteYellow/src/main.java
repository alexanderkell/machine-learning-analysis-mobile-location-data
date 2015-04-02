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
		Scanner sc  = new Scanner(System.in);

		//System.out.println("Enter PhoneID String eg: HT25TW5055273593c875a9898b00");
		//String query = sc.nextLine();
		DataBaseOperations DBO = new DataBaseOperations();
		
		dynamodb.DataBaseQueries QueryRaw = new dynamodb.DataBaseQueries();
		System.out.println("Querying");
		ArrayList<PhoneDataDB> outputDB = QueryRaw.queryRawTable("ZX1B23QFSP48abead89f52e3bb");
		System.out.println("Converting");
		ArrayList<PhoneData> output= DBO.convertFromPhoneData(outputDB);

		sc.close();
		for(int i=0; i<20; i++){
		//	System.out.println(output.get(i).ts.getTime()+" ");
			System.out.println("n :"+output.get(i).ts+" ");
		}
		System.out.println("Filtering");
		FilterMain filtering = new FilterMain(200, 5, 5, 3);
		ArrayList<PhoneData> filtered = filtering.FilterTot(output);
		
		// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
		DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
		PhoneData[] newdata = newdg.getFullPhoneData();
	
		
		System.out.println(newdata.length);
		
		ArrayList<ProcessedDataDB> newdataDB = DBO.convertToProcessedDataDB(newdata);
		
		DBO.makeProcessedTable(newdata);
		
		PlotTracks.plotTrack2(output.toArray(new PhoneData[output.size()]),filtered.toArray(new PhoneData[filtered.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);
	}
}
