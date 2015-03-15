
import java.util.ArrayList;

import maths.*;
import autoregression.*;

public class AutoRegTestB1 {
	
	public static void main(String[] args) throws Exception{
		
		int opt = 1;
		String fn = new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		PhoneData PD[];
		ArrayList<PhoneData> PDAL = new ArrayList<PhoneData>();
		double[] r;
		int order = 2;
		
		DataGetter DG = new DataGetter(opt, fn);
		int length = DG.getLength();
		ARDataFeed ARDF = new ARDataFeed();
		PD = DG.getFullPhoneData();
		int i = 0;
		
		while(i<length){
			PDAL.add(PD[i]);
			i++;
		}
		
		ARDF.setData(PDAL);
		ARDF.setMeasuredParameter('s', 'x');
		ARDF.setOrder(order);
		r = ARDF.runAR();
		
		for(int j = 0; j<order;j++){
		
			System.out.println(r[j]);
		
		}
	}
}
