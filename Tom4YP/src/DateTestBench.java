import java.io.FileNotFoundException;
import java.text.ParseException;


public class DateTestBench {

	public static void main(String[] args) throws FileNotFoundException, ParseException{
		
		String[][] newdat = dataFormat.datcalc(1);
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
