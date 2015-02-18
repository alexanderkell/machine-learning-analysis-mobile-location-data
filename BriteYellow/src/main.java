

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import CSVImport.*;
import splitting.*;
import Maths.*;

public class main {
	public static void main(String args[]) throws ParseException{
		
		System.out.println("Track machine learning 101");
		Scanner sc  = new Scanner(System.in);
		
		
		//Select Data
		System.out.println("Enter data file path:");
		String filePath = sc.nextLine();
		System.out.println("Select from the following options:\n1. Print Phone 1 Data\n2. Print Phone 2 Data\n3. Print Phone 3 Data\n4. Print Phone 4 Data\n5. Print Phone 5 Data\n6. Print Phone 6 Data\n7. Print Raw Data\n");
		int phoneOpt = sc.nextInt();
		
		DataFormatOperations dfo = new DataFormatOperations(phoneOpt,filePath); //Data Processing
		

		//Split Data into Individual Tracks
		DataSorting split = new DataSorting(phoneOpt, filePath);
		String[][] split1 = split.getSort();
		
		String[][] split2 = TrackSelect.selecter(split1, 1);
		
		//Print data
		DataFormatOperations.plotTrack2(split2, 0, 1, 0.1f);
		
		
	}
}
