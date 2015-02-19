import java.text.ParseException;

import redundant.DataSorting;
import splitting.*;


public class DataSortingTestBench {
	
	public static void main(String[] args) throws ParseException{
		
		//this is the phone option you wish to print
				int opt = 1;
				
				//this is the filename
				String fn =  new String("C:\\Users\\Fezan\\Documents\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
				DataSorting ds = new DataSorting(opt, fn);
				ds.getSort();
				System.out.println("");
				System.out.println("DS testBench Ran");
				System.out.println("");
				
	}

}
