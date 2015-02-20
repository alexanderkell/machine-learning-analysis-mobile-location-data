package Maths;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

import Maths.DataFormatOperations.PhoneData;

public class DataGetter extends DataFormatOperations{

	public DataGetter(int opt, String fn) throws ParseException {
		super(opt, fn);
		
	}
	
	public int getLength(){
		return length;
	}
	
	
	
	public String[][] getFull(){
		if(cdcalc[5][0] == null){	// Just to check if the full String is already created, if yes, no need to build full String[][] again
			// Build the full String[][]
			for(int i=0; i<cdcalc[0].length; i++){
				cdcalc[5][i] = String.valueOf(cdcalc2[i].tb);
				cdcalc[6][i] = String.valueOf(cdcalc2[i].rsx);
				cdcalc[7][i] = String.valueOf(cdcalc2[i].rsy);
				cdcalc[8][i] = String.valueOf(cdcalc2[i].rsz);
				cdcalc[9][i] = String.valueOf(cdcalc2[i].rax);
				cdcalc[10][i] = String.valueOf(cdcalc2[i].ray);
				cdcalc[11][i] = String.valueOf(cdcalc2[i].raz);
				cdcalc[12][i] = String.valueOf(cdcalc2[i].modspd);
				cdcalc[13][i] = String.valueOf(cdcalc2[i].modacc);
				cdcalc[14][i] = String.valueOf(cdcalc2[i].spdtheta);
				cdcalc[15][i] = String.valueOf(cdcalc2[i].acctheta);
				
			}
		}
		return cdcalc;
	}
	public PhoneData[] getFullPhoneData(){
		return cdcalc2;
	}
	
	public double[] getXYZValue(int index){
		return new double[]{
				cdcalc2[index].x,
				cdcalc2[index].y,
				cdcalc2[index].z
		};
			
	}
	
	public String getDateTimeString(int index){
		
		return cdcalc2[index].wholedatestring;
		
	}
		
	public double getHour(int index){
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			String date = hour.format(id);
			hr =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return hr;
	}
	
	public double getMin(int index){
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			String date = min.format(id);
			mn =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return mn;
	}
	
	public double getSec(int index){
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			String date = sec.format(id);
			sc =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return sc;
	}
	
	public double getTimeBetweenValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");

		return cdcalc2[index].tb;
		
	}
	
	// Not to be confused with getModSValue(int index)
	public double getDistanceBetween(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");
		return cdcalc2[index].moddisp;
	}
	
	public double getX(int index){
		return cdcalc2[index].x;
	}
	public double getY(int index){
		return cdcalc2[index].y;
	}
	public double getZ(int index){
		return cdcalc2[index].z;
	}
	public double[] getXYZDistanceBetween(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");
		return new double[]{
				cdcalc2[index].xdisp, cdcalc2[index].ydisp, cdcalc2[index].zdisp
			};
	}
	public double[] getXYZSpeedValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Speed index must be greater than or equal to 1");

		return new double[]{
			cdcalc2[index].rsx,
			cdcalc2[index].rsy,
			cdcalc2[index].rsz
		};
		
	}
	
	public double[] getXYZAccelerationValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than or equal to 2");

		return new double[]{
				cdcalc2[index].rax,
				cdcalc2[index].ray,
				cdcalc2[index].raz
			};
	}
	
	// Not to be confused with getDistanceBetween(int index)
	public double getModSValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Speed index must be greater than or equal to 1");

		return cdcalc2[index].modspd;
		
	}
	
	public double getModAValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than or equal to 2");

		return cdcalc2[index].modacc;
		
	}
	
	public double getSThetaValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Speed index must be greater than or equal to 1");

		return cdcalc2[index].spdtheta;
		
	}
	
	public double getAThetaValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than or equal to 2");
		
		return cdcalc2[index].acctheta;
		
	}
	public double getSThetaChange(int index){
		if(index<2)
			throw new IllegalArgumentException("Speed theta change index must be greater than or equal to 2");

		return cdcalc2[index].spdtheta - cdcalc2[index-1].spdtheta;
		
	}
	public double getAThetaChange(int index){
		if(index<3)
			throw new IllegalArgumentException("Acceleration theta change index must be greater than or equal to 3");
		
		return cdcalc2[index].acctheta - cdcalc2[index-1].acctheta;
		
	}
	
	public boolean isStandingStill(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");
		// Return true if the modulus speed is less than 1 point/sec, or false if not
		return cdcalc2[index].modspd ==0;
	}
	
	public Timestamp getTimestamp(int index){
		return cdcalc2[index].ts;
	}
	
	public String getPhoneID(int index){
		String[][] full = getFull();
		String pid = full[5][index];
		return pid;
	}
	

}
