import java.io.FileNotFoundException;
import java.text.ParseException;

import org.jfree.ui.RefineryUtilities;

import Graphing.*;


public class DateTestBench {

	public static void main(String[] args) throws FileNotFoundException, ParseException{
		
		String[][] newdat = dataFormat.datcalc(1);
		int length = 0;
		while (newdat[0][length] != null){
			length++;
		}
		double[] x = new double[length];
		double[] y = new double[length];
		
		for(int i = 0; i<length; i++){
			
			x[i] = Double.parseDouble(newdat[0][i]);			
			y[i] = Double.parseDouble(newdat[1][i]);
			System.out.println();
			System.out.println(x[i] + " " + y[i]);
		}
		
		
		scatterGraph chart = new scatterGraph("Plot", "XCo Vs YCo",x,y);
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );
		
		
	}
	
}
