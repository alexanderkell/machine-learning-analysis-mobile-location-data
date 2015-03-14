import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import maths.*;
import mysql.*;
import csvimport.*;
import filters.*;
import filters.DistanceVerify;
import filters.FilterMain;
import filters.jkalman.JKalmanHelper;
import graphing.PlotTracks;
import redundant.DataSorting2;
import splitting.*;

public class main {
	public static void main(String args[]) throws Exception{
		
		System.out.println("Track machine learning 101");
		Scanner sc  = new Scanner(System.in);

/*		System.out.println("Enter data file path:");
		String filePath = sc.nextLine();
		insertMySQL.insertXDisp(filePath);
*/
		System.out.println("Enter query (eg: x = 01 AND PhoneID = 'HT25TW5055273593c875a9898b00'):");
		String query = sc.nextLine();
		
		ArrayList<PhoneData> output = insertMySQL.query(query);
		sc.close();

		FilterMain filtering = new FilterMain(200, 11, 13);
		ArrayList<PhoneData> filtered = filtering.FilterTot(output);
		
		// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
		DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
		PhoneData[] newdata = newdg.getFullPhoneData();
		
		PlotTracks.plotTrack2(output.toArray(new PhoneData[output.size()]),filtered.toArray(new PhoneData[filtered.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);
	}
}
