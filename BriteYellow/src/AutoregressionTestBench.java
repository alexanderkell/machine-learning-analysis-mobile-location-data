import java.text.ParseException;

import Autoregression.ARCalculations;


public class AutoregressionTestBench {

	public static void main(String[] args) throws ParseException{
		String fn =new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		int opt = 1;
		
		ARCalculations ARC = new ARCalculations(opt, fn);
		double[] xyzarc = ARC.xyzARCoefficients(10,3);
		
		for(int i = 0; i<6; i++){
			System.out.println(xyzarc[i]);
		}
	}
	
}
