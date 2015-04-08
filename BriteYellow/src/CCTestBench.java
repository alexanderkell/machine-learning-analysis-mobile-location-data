
import java.util.ArrayList;

import Objects.PhoneData;
import maths.DataGetter;
import crosscorrelation.*;


public class CCTestBench {
	
	public static void main(String[] args) throws Exception{
		
		
		int opt = 1;
		String fn = new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		PhoneData PD[];
		PhoneData PD2[];
		ArrayList<PhoneData> PDAL = new ArrayList<PhoneData>();
		ArrayList<PhoneData> PDAL2 = new ArrayList<PhoneData>();
		
		DataGetter DG = new DataGetter(opt, fn);
		int length = DG.getLength();
		CCData CCD = new CCData();
		PD = DG.getFullPhoneData();
		PD2 = DG.getFullPhoneData();
		int i = 0;
		double[] result;
		
		while(i<length){
			PDAL.add(PD[i]);
			i++;
		}
		
		while(i<length){
			PDAL2.add(PD2[i]);
			i++;
		}
		
		CCD.setData1(PDAL);
		CCD.setData2(PDAL2);
		CCD.setMeasuredParameter('p', 'x');
		
		result = CCD.getComputedVariables();
		int rs = result.length;
		
		for(i = 0; i<rs; i++){
			System.out.println("xcor = " + result[i]);

		}
		
	}
}
