import Filters.*;
import Graphing.*;

//import javax.swing.JPanel;

public class main {
	public static void main(String[] args){

		wienerFilter obj2 = new wienerFilter();
		double input[] = {1,1,1,1,1,1,1,1,2,1,1,1,1,1,0.5,1,1,1,1,-3,1,2,1,1,1,1,2,1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		double reference[] = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		
		double[] output = obj2.wFilter(input, reference);
		
		double in1[] = new double[output.length];
		for(int i=0; i<output.length; i++){
			in1[i] = i+1;
		}
				
		XYPlot plotObj = new XYPlot();
		plotObj.plot(in1, output, in1, reference, in1, input, "Title", "Heading", "Y-Axis", "X-Axis", "Saved53245");
		
		
	}
}