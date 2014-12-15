import java.io.FileNotFoundException;
import java.util.Scanner;


public class MainTestBench {
	
	public static void main(String[] args) throws FileNotFoundException{
	int option = -1;
	int option2 = 0;
	Scanner ui = new Scanner( System.in );
	System.out.println("Select from the following options:\n1. Print Phone 1 Data\n2. Print Phone 2 Data\n3. Print Phone 3 Data\n4. Print Raw Data\n");
	option = ui.nextInt( );
	System.out.println("Select no. of Lines to Print:\n");
	option2 = ui.nextInt();
	
	String cd[][]=csvreader.wd(option);
	
	for (int k = 0; k < option2; k++){
        for (int l = 0; l < 5; l++) {
            System.out.print(cd[l][k] + " ");
        }
        System.out.print("\n");
    }
	
	
	
	}
}
