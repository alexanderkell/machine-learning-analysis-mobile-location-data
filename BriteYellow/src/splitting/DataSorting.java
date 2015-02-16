package splitting;

import java.text.ParseException;

import Maths.DataFormatOperations;

public class DataSorting {
	String[][] newdat;
	static int opt = 1;
	static String fn = new String();
	
	public DataSorting(int opt, String fn) throws ParseException {
				this.opt = opt;
				this.fn = fn;
				//create an instance of the DataFormatOperations class
				DataFormatOperations data = new DataFormatOperations(this.opt, this.fn);
				
				//gets the full matrix
				newdat = data.getFull();
				
				/*//selects how many lines to print
				int length = 100;
				
				//prints the array
				for (int k = 0; k < length; k++){
				for (int l = 0; l < 16; l++) {
					System.out.print(newdat[l][k] + " || ");
				}
					System.out.print("\n");
				}*/
		
	}
	
	public DataSorting() throws ParseException{
		this(opt, fn);
		
	}
	
	
	
}

