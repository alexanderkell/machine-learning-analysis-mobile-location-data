import java.io.FileNotFoundException;
import java.util.Scanner;


public class MainTestBench {
	
	public static void main(String[] args) throws FileNotFoundException{
	Scanner ui = new Scanner( System.in );//state the scanner
	int out = 0;
	String exit = new String();
		while(out != 1){
			int option = -1;
			int option2 = 0;
			String filenom = new String();
		
			System.out.println("First Enter the Full Address of the Data File You Wish to Input:");  
			System.out.println("(for example: 'C:/Users/Thomas/Documents/4th-year-project/Tom/24th Sept.csv')");
			filenom = ui.nextLine();	
			System.out.println("Set as: " + csvReader.fSet(filenom) + "\n");
			System.out.println("Select from the following options:\n1. Print Phone 1 Data\n2. Print Phone 2 Data\n3. Print Phone 3 Data\n4. Print Raw Data\n");
			option = ui.nextInt( );//chooses which phone data to print
			System.out.println("Select no. of Lines to Print:\n");
			option2 = ui.nextInt();
	
			String cd[][]=csvReader.wd(option);
			
			for (int k = 0; k < option2; k++){
				for (int l = 0; l < 5; l++) {
				System.out.print(cd[l][k] + " ");
				}
			System.out.print("\n");
			}
			System.out.print("\n\n\n\n");
				
		}
			System.exit(1);
	}
}
