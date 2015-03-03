import java.io.FileNotFoundException;
import java.text.ParseException;

import maths.DataGetter;


public class DateTestBench {
//tests the DataFormatOperations Class
	
	public static void main(String[] args) throws ParseException{
		//this is the phone option you wish to print
		int opt = 1;
		
		//this is the filename
		String fn =  new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		
		//create an instance of the DataFormatOperations class
		DataGetter data = new DataGetter(opt, fn);
		
		//gets the full matrix
		String[][] newdat = data.getFull();
		
		//selects how many lines to print
		int length = data.getLength();
		
		//prints the array
		for (int k = 0; k < length; k++){
		for (int l = 0; l < 18; l++) {
			System.out.print(newdat[l][k] + " ");
		}
			System.out.print("\n");
		}
		
	}
	
}
