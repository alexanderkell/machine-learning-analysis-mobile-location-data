import java.io.FileNotFoundException;
import java.text.ParseException;

import Maths.DataFormatOperations;


public class DateTestBench {

	public static void main(String[] args) throws FileNotFoundException, ParseException{
		int opt = 1;
		String fn =  new String("/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
		DataFormatOperations data = new DataFormatOperations(opt, fn);
		String[][] newdat = data.calcData();
		int length = 0;
		while (newdat[0][length] != null){
			length++;
		}
		/*double[] x = new double[length];
		double[] y = new double[length];
		double[] z = new double[length];
		
		for(int i = 0; i<length; i++){
			
			x[i] = Double.parseDouble(newdat[0][i]);			
			y[i] = Double.parseDouble(newdat[1][i]);
			z[i] = Double.parseDouble(newdat[2][i]);
			System.out.println();
			System.out.println(x[i] + " " + y[i] + " " + z[i]);
		}*/

		
		
	}
	
}
