import java.io.FileNotFoundException;
import java.text.ParseException;

import Maths.DataFormatOperations;


public class DateTestBench {

	public static void main(String[] args) throws ParseException{
		int opt = 1;
		String fn =  new String("/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
		DataFormatOperations data = new DataFormatOperations(opt, fn);
		String[][] newdat = data.getFull();
		
	}
	
}
