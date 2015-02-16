package splitting;

import java.text.ParseException;

import Maths.DataFormatOperations;

public class DataSorting {
	String[][] newdat;
	
	public DataSorting(int opt, String fn) throws ParseException {
				//create an instance of the DataFormatOperations class
				DataFormatOperations data = new DataFormatOperations(opt, fn);
				
				//gets the full matrix
				newdat = data.getFull();
				
		
	}
}

