import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import CSVImport.*;
import Filters.DistanceVerify;
import Graphing.PlotTracks;
import redundant.DataSorting2;
import splitting.*;
import Maths.*;
import MySQL.*;

public class main {
	public static void main(String args[]) throws Exception{
		
		System.out.println("Track machine learning 101");
		Scanner sc  = new Scanner(System.in);

		System.out.println("Enter data file path:");
		String filePath = sc.nextLine();
		insertMySQL.insertXDisp(filePath);

		
		/*
		System.out.println("Enter query (eg: x = 156 AND PhoneID = 'HT25TW5055273593c875a9898b00'):");
		String query = sc.nextLine();
		
		ArrayList<PhoneData> output = insertMySQL.query(query);
				
//		DataGetter reAn = new DataGetter(output);
		
		DistanceVerify cutBig = new DistanceVerify(output,150);
		cutBig.check();
		ArrayList<PhoneData> reana = cutBig.getFull();
		
		sc.close();
		
		//PlotTracks.plotTrack2(reana, PlotTracks.X, PlotTracks.Y, 0.1f);
		PlotTracks.plotTrack2(reana.toArray(new PhoneData[reana.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);
		*/
	}
}
