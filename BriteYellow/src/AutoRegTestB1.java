import autoregression.*;
import Maths.*;

public class AutoRegTestB1 {
	
	public static void main(String[] args) throws Exception{
		
		AutoregressionCalc ARC = new AutoregressionCalc();
		int opt = 1;
		String fn = new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		
		DataGetter DG = new DataGetter(opt, fn);
		int length = DG.getLength();
		double[] t = new double[length];
		
		int i = 0;
		
		while(i<length){
			
			t[i] = DG.getX(i);
			i++;
		}
		
		
		double[] r = ARC.calculateARCoefficients(t, 2, false);
		
		System.out.println(r[0]);
		System.out.println(r[1]);
		//System.out.println(r[2]);
		
	}
}
