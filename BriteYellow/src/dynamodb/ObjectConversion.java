package dynamodb;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import maths.PhoneData;

public class ObjectConversion {
	
	static DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
	
	
	//converts an Arraylist of PhoneData objects to PhoneDataDB objects
	public static ArrayList<PhoneDataDB> convertToPhoneDataDB(ArrayList<PhoneData> pd){
		ArrayList<PhoneDataDB> whole = new ArrayList<PhoneDataDB>();
		PhoneDataDB datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneDataDB();
			datapoint = convertToPhoneDataDB(pd.get(i));
			
			whole.add(datapoint);
			
		}
		
		return whole;
	}
	
	public static ArrayList<PhoneDataDB> convertToPhoneDataDB(PhoneData[] pda){
		ArrayList<PhoneData> pd = new ArrayList<PhoneData>(Arrays.asList(pda));
		ArrayList<PhoneDataDB> whole = new ArrayList<PhoneDataDB>();
		PhoneDataDB datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneDataDB();
			datapoint = convertToPhoneDataDB(pd.get(i));
			
			whole.add(datapoint);
			
			
		}
		
		return whole;
	}
	
	public static PhoneDataDB convertToPhoneDataDB(PhoneData pd){
		
		PhoneDataDB datapoint;
		
		datapoint = new PhoneDataDB();
		datapoint.setXPosition(pd.x);
		datapoint.setYPosition(pd.y);
		datapoint.setZPosition(pd.z);
		datapoint.setTimestampLong(pd.ts.getTime());
		datapoint.setTimeBetween(pd.tb);
		datapoint.setXDisplacement(pd.xdisp);
		datapoint.setYDisplacement(pd.ydisp);
		datapoint.setZDisplacement(pd.zdisp);
		datapoint.setModDisplacement(pd.moddisp);
		datapoint.setXSpeed(pd.rsx);
		datapoint.setYSpeed(pd.rsy);
		datapoint.setZSpeed(pd.rsz);
		datapoint.setModSpeed(pd.modspd);
		datapoint.setSpeedTheta(pd.spdtheta);
		datapoint.setXAcceleration(pd.rax);
		datapoint.setYAcceleration(pd.ray);
		datapoint.setZAcceleration(pd.raz);
		datapoint.setModAcceleration(pd.modacc);
		datapoint.setAccelerationTheta(pd.acctheta);
		datapoint.setPhoneID(pd.phone_id);
		datapoint.setTrackNo(pd.track_no);
		
		return datapoint;
	}
	
	
	public static ArrayList<PhoneData> convertFrom(ArrayList<PhoneDataDB> pd){
		ArrayList<PhoneData> whole = new ArrayList<PhoneData>();
		PhoneData datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneData();
			datapoint = convertFrom(pd.get(i));
			whole.add(datapoint);
			
		}
		
		return whole;
	}
	
	
	public static PhoneData convertFrom(PhoneDataDB pd){
	
		PhoneData datapoint;
		
		datapoint = new PhoneData();
		datapoint.x = pd.getXPosition();
		datapoint.y = pd.getYPosition();
		datapoint.z = pd.getZPosition();
		datapoint.ts = new Timestamp(pd.getTimestampLong());
		datapoint.wholedate = new Date(pd.getTimestampLong());
		datapoint.wholedatestring = df.format(datapoint.wholedate);
		datapoint.tb = pd.getTimeBetween();
		datapoint.xdisp = pd.getXDisplacement();
		datapoint.ydisp = pd.getYDisplacement();
		datapoint.zdisp = pd.getZDisplacement();
		datapoint.moddisp = pd.getModDisplacement();
		datapoint.rsx = pd.getXSpeed();
		datapoint.rsy = pd.getYSpeed();
		datapoint.rsz = pd.getZSpeed();
		datapoint.modspd = pd.getModSpeed();
		datapoint.spdtheta = pd.getSpeedTheta();
		datapoint.rax = pd.getXAcceleration();
		datapoint.ray = pd.getYAcceleration();
		datapoint.raz = pd.getZAcceleration();
		datapoint.modacc = pd.getModAcceleration();
		datapoint.acctheta = pd.getAccelerationTheta();
		datapoint.phone_id = pd.getPhoneID();
		datapoint.track_no = pd.getTrackNo();
	
		
		return datapoint;
	}
	
}
