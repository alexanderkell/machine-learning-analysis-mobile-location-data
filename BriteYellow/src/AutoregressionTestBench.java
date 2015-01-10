import java.text.ParseException;

import Autoregression.ARCalculations;


public class AutoregressionTestBench {

	public static void main(String[] args) throws ParseException{
		String fn =new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		int opt = 1;
		
		ARCalculations ARC = new ARCalculations(opt, fn);
		ARC.delayData(1);
		
	}
	
}
