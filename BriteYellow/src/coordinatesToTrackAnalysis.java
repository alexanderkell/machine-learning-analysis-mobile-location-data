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

public class coordinatesToTrackAnalysis {
	public static void main(String args[]) throws Exception{
		System.out.println("Converting from coordinate data to track data");
		
		DataBaseQueries QueryProcessed = new DataBaseQueries("Processed_Data");
		char a = a;
		System.out.println("Querying procesed data");
		ArrayList<PhoneDataDB> queriedData = QueryProcessed.queryTable("HT25TW5055273593c875a9898b00", a);
		
		
		
	}
}
