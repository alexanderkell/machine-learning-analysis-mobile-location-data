import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;

import CSVImport.*;

//tests csvreader
public class MainTestBench {
	
	public static void main(String[] args){
	Scanner ui = new Scanner( System.in );//state the scanner
	int out = 0;
	String exit = new String();
		//while(out != 1){
			int option = -1;
			int option2 = 0;
			String fn = new String();
			System.out.println("First Enter the Full Address of the Data File You Wish to Input:");  
			System.out.println("(for example: '/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv')");
			fn = ui.nextLine();
			CSVReaders Read = new CSVReaders(fn);
			System.out.println("Set as: " + Read.getFileName() + "\n");
			System.out.println("Select from the following options:\n1. Print Phone 1 Data\n2. Print Phone 2 Data\n3. Print Phone 3 Data\n4. Print Phone 4 Data\n5. Print Phone 5 Data\n6. Print Phone 6 Data\n7. Print Raw Data\n");
			option = ui.nextInt( );//chooses which phone data to print
			System.out.println("Select no. of Lines to Print:\n");
			option2 = ui.nextInt();
	
			String cd[][]=Read.myPhone(option);
			
			for (int k = 0; k < option2; k++){
				for (int l = 0; l < 5; l++) {
				System.out.print(cd[l][k] + " ");
				}
			System.out.print("\n");
			}
			System.out.print("\n\n\n\n");
		//}
			System.exit(1);
	}
}
