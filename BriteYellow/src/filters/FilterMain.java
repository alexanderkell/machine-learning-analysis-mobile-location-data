package filters;

import java.text.ParseException;
import java.util.ArrayList;

import maths.DataGetter;
import maths.PhoneData;

public class FilterMain {
	public FilterMain(ArrayList<PhoneData> output) throws ParseException{
	
		lessThanFilter filt = new lessThanFilter(output);
		filt.filter(lessThanFilter.MDISP, 0, 150);
		ArrayList<PhoneData> underDisp = filt.getFullPhoneData();
		
		//ReAnalyse Data
		DataGetter reAn = new DataGetter(underDisp.toArray(new PhoneData[underDisp.size()]));
		PhoneData[] reana = reAn.getFullPhoneData();
		
		lessThanFilter filt = new lessThanFilter(output);
		filt.filter(lessThanFilter.MSpeed, 0, 100);
		ArrayList<PhoneData> underSpeed = filt.getFullPhoneData();
		
		//ReAnalyse Data
		DataGetter reAn = new DataGetter(underDisp.toArray(new PhoneData[underDisp.size()]));
		PhoneData[] reana = reAn.getFullPhoneData();
		
		
	}
	
	
}
