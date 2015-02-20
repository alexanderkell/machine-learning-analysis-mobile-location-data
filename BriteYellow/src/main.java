import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import CSVImport.*;
import redundant.DataSorting2;
import splitting.*;
import Maths.*;
import MySQL.*;

public class main {
	public static void main(String args[]) throws Exception{
		
		System.out.println("Track machine learning 101");
		Scanner sc  = new Scanner(System.in);
		
		//Select Data
		System.out.println("Enter data file path:");
		String filePath = sc.nextLine();
		//System.out.println("Select from the following options:\n1. Print Phone 1 Data\n2. Print Phone 2 Data\n3. Print Phone 3 Data\n4. Print Phone 4 Data\n5. Print Phone 5 Data\n6. Print Phone 6 Data\n7. Print Raw Data\n");
		//int phoneOpt = sc.nextInt();
		
		
		//Split Data into Individual Tracks
		//DataSorting2 split = new DataSorting2(phoneOpt, filePath);
		//PhoneData[] split1 = split.getSort();
		//System.out.println("Total tracks: "+TrackSelect.getTotalTracks(split1));

		insertMySQL.insertXDisp(filePath);
		
		//PhoneData[] split2 = TrackSelect.selecter(split1, 1);
		
		
		
		// Plot graph
		//DataFormatOperations.plotTrack2(split2, DataFormatOperations.X, DataFormatOperations.Y, 0.2f);
		
		sc.close();
	}
}
