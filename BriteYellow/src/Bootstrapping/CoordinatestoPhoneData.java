package Bootstrapping;

import java.sql.Timestamp;
import java.util.ArrayList;

import maths.DataFormatOperations;
import maths.DataGetter;
import objects.PhoneData;

public class CoordinatestoPhoneData {
	public PhoneData[] convertToPhoneData(ArrayList<Coordinates> co, String phoneID){
		PhoneData pd[] = new PhoneData[co.size()];
		for(int i = 0; i<co.size(); i++){
			pd[i] = new PhoneData();
//			pd[i].ts = co.get(i).getTimestamp();
			System.out.println(co.get(i).x);
			pd[i].x = co.get(i).x;
			pd[i].y = co.get(i).y;
			pd[i].z = 0;
			pd[i].phone_id = phoneID;
			pd[i].ts = co.get(i).timestamp;
			pd[i].track_no = co.get(i).track;
		}
		return pd;
	}
	
}
