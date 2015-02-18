

import java.io.FileNotFoundException;
import java.util.Scanner;

import CSVImport.*;

import splitting.*;

public class main {
	public static void main(String args[]){
		
		System.out.println("Track machine learning 101");
		Scanner sc  = new Scanner(System.in);
		
		
		//Select Data
		System.out.println("Enter data file path:");
		String filePath = sc.nextLine();
		System.out.println("Select from the following options:\n1. Print Phone 1 Data\n2. Print Phone 2 Data\n3. Print Phone 3 Data\n4. Print Phone 4 Data\n5. Print Phone 5 Data\n6. Print Phone 6 Data\n7. Print Raw Data\n");
		String phoneOpt = sc.nextLine();
		getArray dat = new getArray();
		String[][] phoneData = dat.csvToArray(filePath, phoneOpt);
		System.out.println(phoneData.length);
		
		
		//Split Data into Individual Tracks
		
		
		
		
	}
}
