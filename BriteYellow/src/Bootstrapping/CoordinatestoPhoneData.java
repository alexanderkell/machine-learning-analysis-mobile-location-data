package Bootstrapping;

import java.util.ArrayList;

import objects.PhoneData;

public class CoordinatestoPhoneData {
	public ArrayList<PhoneData> convertToPhoneData(ArrayList<Coordinates> co, String phoneID){
		ArrayList<PhoneData> pd = new ArrayList<PhoneData>();
		for(int i = 0; i<co.size(); i++){
			PhoneData pdOne = new PhoneData();
			pdOne.x = co.get(i).x;
			pdOne.y = co.get(i).y;
			pdOne.z = 0;
			pdOne.ts = co.get(i).timestamp;
			pdOne.phone_id = phoneID;
			pd.add(pdOne);
		}
		return pd;
	}
}
