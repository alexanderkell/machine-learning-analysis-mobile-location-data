package csvimport;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import maths.DataGetter;
import maths.PhoneData;

public class NewDataScaling {
	
	public static ArrayList<PhoneData> scaleAndCalcNewData(ArrayList<PhoneData> INPUT) throws ParseException{
		ArrayList<PhoneData> OUTPUT = new ArrayList<PhoneData>();
		ArrayList<PhoneData>  final_OUTPUT = new ArrayList<PhoneData>(); 
		PhoneData datapoint;
		
		for(int i = 0; i < INPUT.size(); i++){
			datapoint = new PhoneData();;
			datapoint.x = (INPUT.get(i).x/1.36)-586;
			datapoint.y = (INPUT.get(i).y/1.36)-3.88;
			datapoint.z = INPUT.get(i).z;
			datapoint.ts = INPUT.get(i).ts;
			datapoint.wholedate = INPUT.get(i).wholedate;
			datapoint.wholedatestring = INPUT.get(i).wholedatestring;
			datapoint.tb = INPUT.get(i).tb;
			datapoint.phone_id = INPUT.get(i).phone_id;
			OUTPUT.add(datapoint);
		}
		
		DataGetter DG = new DataGetter(OUTPUT.toArray(new PhoneData[OUTPUT.size()]));
		final_OUTPUT = new ArrayList<PhoneData>(Arrays.asList(DG.getFullPhoneData()));
		
		return final_OUTPUT;
	}
	
	
}
