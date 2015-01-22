package Maths;
import java.util.*;
import java.text.*;
import java.lang.Math;

import CSVImport.CSVReaders;

public class DataFormatOperations{
	
	//initialise all variables
	//time between variable
	private double tb = 0;
	//relative speed
	private double rsx = 0;
	private double rsy = 0;
	private double rsz = 0;
	//relative acceleration
	private double rax = 0;
	private double ray = 0;
	private double raz = 0;
	//time variables
	private double hr = 0;
	private double mn = 0;
	private double sc = 0;
	private double hr2 = 0;
	private double mn2 = 0;
	private double sc2 = 0;
	private double xco = 0;
	private double yco = 0;
	private double zco = 0;
	private double modspd = 0;
	private double modacc = 0;
	private double spdtheta = 0;
	private double acctheta = 0;
	
	

	//sets up the date formats to be used for splitting up the different constituent parts of the date
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
	DateFormat hour = new SimpleDateFormat("HH", Locale.ENGLISH);
	DateFormat min = new SimpleDateFormat("mm", Locale.ENGLISH);
	DateFormat sec = new SimpleDateFormat("ss", Locale.ENGLISH);
	
	
	//class variables
	private static int opt;
	private int length;
	private static String fn = new String("/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
	private String temp = new String();
	//read in data
	private String cdcalc[][];
	private PhoneData cdcalc2[];
	
	public DataFormatOperations(int opt, String fn) throws ParseException{
		
		CSVReaders Read = new CSVReaders(fn);
		cdcalc = Read.myPhone(opt);
		cdcalc2 = new PhoneData[cdcalc[0].length];
		
		
		//create constructor object
		DataFormatOperations.opt = opt;
		DataFormatOperations.fn = fn;
		length = cdcalc[0].length;
		
		cdcalc2 = new PhoneData[length];
		
		// Store the x,y,z speeds
		for(int i=0; i<length; i++){
			cdcalc2[i] = new PhoneData();
			try{
				cdcalc2[i].x = Double.parseDouble(cdcalc[0][i]);
			} catch (NumberFormatException e) {
				cdcalc2[i].x = 0;
				}
			try{
				cdcalc2[i].y = Double.parseDouble(cdcalc[1][i]);
			} catch (NumberFormatException e) {
				cdcalc2[i].y = 0;
				}
			try{
				cdcalc2[i].z = Double.parseDouble(cdcalc[2][i]);
			} catch (NumberFormatException e) {
				cdcalc2[i].z = 0;
				}
			cdcalc2[i].wholedatestring = cdcalc[3][i];
			try{
				cdcalc2[i].wholedate = df.parse(cdcalc[3][i]);
			}catch(ParseException pe){
				
			}
		}
		
		
		//works out the time between each reading based on the time
			for(int y = 0; y<length-1; y++){
				
				Date wholedate1 =  cdcalc2[y].wholedate; 
				Date wholedate2 =  cdcalc2[y+1].wholedate;
				
				if(wholedate1.compareTo(wholedate2)>0){
					System.err.println("Please Put Data in Date and Time Order Before Running!");
					break;
				}else{
					temp = hour.format(wholedate1);
					hr =Double.parseDouble(temp);
					temp = min.format(wholedate1);
					mn =Double.parseDouble(temp);
					temp = sec.format(wholedate1);
					sc =Double.parseDouble(temp); 
					temp = hour.format(wholedate2);
					hr2 =Double.parseDouble(temp);
					temp = min.format(wholedate2);
					mn2 =Double.parseDouble(temp);
					temp = sec.format(wholedate2);
					sc2 =Double.parseDouble(temp);
					tb = (hr2 - hr)*60*60 + (mn2 - mn)*60 + (sc2 - sc);
					cdcalc2[y+1].tb = tb;
				}
						
	        }
			
			for (int k = 1; k < length; k++){//working out relative speeds in all directions (velocity)
				
				xco = cdcalc2[k].x - cdcalc2[k-1].x;
				yco = cdcalc2[k].y - cdcalc2[k-1].y;
				zco = cdcalc2[k].z - cdcalc2[k-1].z;

				if(cdcalc2[k].tb == 0){
					// If the time difference is 0, assume time difference is 1 second and take distance / 1 second
					cdcalc2[k].rsx = rsx = xco;
					cdcalc2[k].rsy = rsy = yco;
					cdcalc2[k].rsz = rsz = zco;
				} else{
					// Calculate Speeds (distance / time)
					cdcalc2[k].rsx = rsx = xco/cdcalc2[k].tb;
					cdcalc2[k].rsy = rsy = yco/cdcalc2[k].tb;
					cdcalc2[k].rsz = rsz = zco/cdcalc2[k].tb;
	
					cdcalc2[k].spdtheta = Math.atan(rsy/rsx);
					
					cdcalc2[k].modspd = Math.sqrt(rsx*rsx + rsy*rsy);
				}
			}
			
			//working out relative acceleration in all directions
			for(int l=2; l<length; l++){
				
				rax = (cdcalc2[l].rsx - cdcalc2[l-1].rsx);
				ray = (cdcalc2[l].rsy - cdcalc2[l-1].rsy);
				raz = (cdcalc2[l].rsz - cdcalc2[l-1].rsz);
				if(cdcalc2[l].tb == 0){
					// If the time difference is 0, assume time difference is 1 second and take distance / 1 second
					cdcalc2[l].rax = rax;
					cdcalc2[l].ray = ray;
					cdcalc2[l].raz = raz;
				} else {
					cdcalc2[l].rax = rax/ cdcalc2[l].tb;
					cdcalc2[l].ray = ray/ cdcalc2[l].tb;
					cdcalc2[l].raz = raz/ cdcalc2[l].tb;
				}
				cdcalc2[l].acctheta = Math.atan(ray/rax);
				
				cdcalc2[l].modacc = Math.sqrt(rax*rax + ray*ray);
				
			}
				
	}
	
	public DataFormatOperations() throws ParseException{
		
		this(opt, fn);
				
	}
	
	public int getLength(){
		return length;
	}
	
	public void setPhone(int opt){
		DataFormatOperations.opt = opt;
	}
	
	public void setFileName(String fn){
		DataFormatOperations.fn = fn;
	}
	
	public String[][] getFull(){
		return cdcalc;
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
		String date = new String();
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			date = hour.format(id);
			hr =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return hr;
	}
	
	public double getMin(int index){
		String date = new String();
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			date = min.format(id);
			mn =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return mn;
	}
	
	public double getSec(int index){
		String date = new String();
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			date = sec.format(id);
			sc =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return sc;
	}
	
	public double getTimeBetweenValue(int index){
		
		return cdcalc2[index].tb;
		
	}
	
	public double[] getXYZSpeedValue(int index){

		return new double[]{
			cdcalc2[index].rsx,
			cdcalc2[index].rsy,
			cdcalc2[index].rsz
		};
		
	}
	
	public double[] getXYZAccelerationValue(int index){
		return new double[]{
				cdcalc2[index].rax,
				cdcalc2[index].ray,
				cdcalc2[index].raz
			};
	}
	
	public double getModSValue(int index){
		return cdcalc2[index].modspd;
		
	}
	
	public double getModAValue(int index){
		return cdcalc2[index].modacc;
		
	}
	
	public double getSThetaValue(int index){
		return cdcalc2[index].spdtheta;
		
	}
	
	public double getAThetaValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than 2");
		
		return cdcalc2[index].acctheta;
		
	}

	private class PhoneData{
		//position x,y,z
		double x, y, z;
		
		//whole date in Data and String format
		Date wholedate;
		String wholedatestring;
		
		//time between current position and the previous position
		double tb;
		
		//relative speeds in x,y,z and modulus direction, and angle
		double rsx, rsy, rsz, modspd, spdtheta;
		
		//relative accelerations in x,y,z and modulus direction
		double rax, ray, raz, modacc, acctheta;
	}
}