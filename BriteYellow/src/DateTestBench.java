
import java.io.FileNotFoundException;
import java.text.ParseException;

import maths.DataGetter;


public class DateTestBench {
//tests the DataFormatOperations Class
	
	public static void main(String[] args) throws ParseException{
		//this is the phone option you wish to print
		int opt = 4;
		
		//this is the filename
		//HT25TW5055273593c875a9898b00.csv
		//24th Sept ORDERED.csv
		String fn =  new String("26th Sept Ordered.csv");
		
		//create an instance of the DataFormatOperations class
		DataGetter data = new DataGetter(opt, fn);
		
		//gets the full matrix
		String[][] newdat = data.getFull();
		
		//selects how many lines to print
		int length = data.getLength();
		
		//prints the array
		/*for (int k = 0; k < length; k++){
		for (int l = 0; l < 18; l++) {
			System.out.print(newdat[l][k] + " ");
		}
			System.out.print("\n");
		}*/
		
		for (int k = 0; k < length; k++){
			System.out.println(newdat[3][k]+" "+newdat[16][k]);
		}
		
		
	}
	
}
