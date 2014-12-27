import java.io.FileNotFoundException;
import java.text.ParseException;

import Maths.DataFormatOperations;


public class DateTestBench {
//tests the DataFormatOperations Class
	
	public static void main(String[] args) throws ParseException{
		//this is the phone option you wish to print
		int opt = 7;
		
		//this is the filename
		String fn =  new String("/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
		
		//create an instance of the DataFormatOperations class
		DataFormatOperations data = new DataFormatOperations(opt, fn);
		
		//gets the full matrix
		String[][] newdat = data.getFull();
		
		//selects how many lines to print
		int length = 8;
		
		//prints the array
		for (int k = 0; k < length; k++){
		for (int l = 0; l < 16; l++) {
			System.out.print(newdat[l][k] + " ");
		}
			System.out.print("\n");
		}
		
	}
	
}
