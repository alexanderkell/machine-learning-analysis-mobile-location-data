package Maths;
import java.io.FileNotFoundException;
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
	
	//power variables
	private double p = 2;
	private double sqrt = 0.5;
	
	//class variables
	private int opt = 0;
	private int length = 20000;
	private String fn = new String("/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
	private String temp = new String();
	//read in data
	String cdcalc[][] = new String[20][length];
	
	public DataFormatOperations() throws ParseException{
		
		CSVReaders Read = new CSVReaders(fn);
		cdcalc = Read.myPhone(opt);
		//create constructor object
		length = 0;
		int wk = 0;
		
		try{
			while(cdcalc[0][wk] != null){
				length++;
				wk++;
			}
		}catch(ArrayIndexOutOfBoundsException aiob){
				
			
			}
		
		
		//works out the time between each reading based on the time
			for(int y = 0; y<length-1; y++){
				try{
					
					Date wholedate1 =  df.parse(cdcalc[3][y]); 
					Date wholedate2 =  df.parse(cdcalc[3][y+1]);
					
					if(wholedate1.compareTo(wholedate2)>0){
						System.out.println("Please Put Data in Date and Time Order Before Running!");
						y=length;
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
						cdcalc[5][y+1] = String.valueOf(tb);
					}
				}catch(ParseException pe){
					
				}
						
	        }
			
			
			for (int k = 1; k < length; k++){//working out relative speeds in all directions (velocity)
						
						try{
							xco = Double.parseDouble(cdcalc[0][k]) - Double.parseDouble(cdcalc[0][k-1]);
						} catch (NumberFormatException e) {
							xco = 0;
							}
						try{
							yco = Double.parseDouble(cdcalc[1][k]) - Double.parseDouble(cdcalc[2][k-1]);
						} catch (NumberFormatException e) {
							yco = 0;
							}
						try{
							zco = Double.parseDouble(cdcalc[2][k]) - Double.parseDouble(cdcalc[2][k-1]);
						} catch (NumberFormatException e) {
							zco = 0;
							}

						
						rsx = xco/tb;
						rsy = yco/tb;
						rsz = zco/tb;
						rax = xco/(tb*tb);
						ray = yco/(tb*tb);
						raz = zco/(tb*tb);
						spdtheta = Math.atan(rsy/rsx);
						acctheta = Math.atan(ray/rax);
						
						modspd = Math.pow(rsx,p)+Math.pow(rsy,p);
						modspd = Math.pow(modspd,sqrt);
						modacc = Math.pow(rax,p)+Math.pow(ray,p);
						modacc = Math.pow(modacc,sqrt);
						

						if(tb != 0){
							cdcalc[6][k] = String.valueOf(rsx);
							cdcalc[7][k] = String.valueOf(rsy);
							cdcalc[8][k] = String.valueOf(rsz);
							cdcalc[9][k] = String.valueOf(rax);
							cdcalc[10][k] = String.valueOf(ray);
							cdcalc[11][k] = String.valueOf(raz);
							cdcalc[12][k] = String.valueOf(modspd);
							cdcalc[13][k] = String.valueOf(modacc);
							cdcalc[14][k] = String.valueOf(spdtheta);
							cdcalc[15][k] = String.valueOf(acctheta);
						}else{
							cdcalc[6][k] = String.valueOf(0);
							cdcalc[7][k] = String.valueOf(0);
							cdcalc[8][k] = String.valueOf(0);
							cdcalc[9][k] = String.valueOf(0);
							cdcalc[10][k] = String.valueOf(0);
							cdcalc[11][k] = String.valueOf(0);
							cdcalc[12][k] = String.valueOf(0);
							cdcalc[13][k] = String.valueOf(0);
							cdcalc[14][k] = String.valueOf(0);
							cdcalc[15][k] = String.valueOf(0);
						}
						
					
				}
				
			
			/*for (int k = 0; k < length; k++){
			for (int l = 0; l < 16; l++) {
				System.out.print(cdcalc[l][k] + " ");
			}
				System.out.print("\n");
			}*/
				
	}
	
	
	public DataFormatOperations(int opt, String fn) throws ParseException{
		
		CSVReaders Read = new CSVReaders(fn);
		cdcalc = Read.myPhone(opt);
		
		//create constructor object
		this.opt = opt;
		this.fn = fn;
		length = 0;
		int wk = 0;
		try{
			while(cdcalc[0][wk] != null){
				length++;
				wk++;
			}
		}catch(ArrayIndexOutOfBoundsException aiob){
				
			
			}
		
		
		//works out the time between each reading based on the time
			for(int y = 0; y<length-1; y++){
				try{
					
					Date wholedate1 =  df.parse(cdcalc[3][y]); 
					Date wholedate2 =  df.parse(cdcalc[3][y+1]);
					
					if(wholedate1.compareTo(wholedate2)>0){
						System.out.println("Please Put Data in Date and Time Order Before Running!");
						y=length;
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
						cdcalc[5][y+1] = String.valueOf(tb);
					}
				}catch(ParseException pe){
					
				}
						
	        }
			
			
			for (int k = 1; k < length; k++){//working out relative speeds in all directions (velocity)
						
						try{
							xco = Double.parseDouble(cdcalc[0][k]) - Double.parseDouble(cdcalc[0][k-1]);
						} catch (NumberFormatException e) {
							xco = 0;
							}
						try{
							yco = Double.parseDouble(cdcalc[1][k]) - Double.parseDouble(cdcalc[2][k-1]);
						} catch (NumberFormatException e) {
							yco = 0;
							}
						try{
							zco = Double.parseDouble(cdcalc[2][k]) - Double.parseDouble(cdcalc[2][k-1]);
						} catch (NumberFormatException e) {
							zco = 0;
							}

						
						rsx = xco/tb;
						rsy = yco/tb;
						rsz = zco/tb;
						rax = xco/(tb*tb);
						ray = yco/(tb*tb);
						raz = zco/(tb*tb);
						spdtheta = Math.atan(rsy/rsx);
						acctheta = Math.atan(ray/rax);
						
						modspd = Math.pow(rsx,p)+Math.pow(rsy,p);
						modspd = Math.pow(modspd,sqrt);
						modacc = Math.pow(rax,p)+Math.pow(ray,p);
						modacc = Math.pow(modacc,sqrt);
						

						if(tb != 0){
							cdcalc[6][k] = String.valueOf(rsx);
							cdcalc[7][k] = String.valueOf(rsy);
							cdcalc[8][k] = String.valueOf(rsz);
							cdcalc[9][k] = String.valueOf(rax);
							cdcalc[10][k] = String.valueOf(ray);
							cdcalc[11][k] = String.valueOf(raz);
							cdcalc[12][k] = String.valueOf(modspd);
							cdcalc[13][k] = String.valueOf(modacc);
							cdcalc[14][k] = String.valueOf(spdtheta);
							cdcalc[15][k] = String.valueOf(acctheta);
						}else{
							cdcalc[6][k] = String.valueOf(0);
							cdcalc[7][k] = String.valueOf(0);
							cdcalc[8][k] = String.valueOf(0);
							cdcalc[9][k] = String.valueOf(0);
							cdcalc[10][k] = String.valueOf(0);
							cdcalc[11][k] = String.valueOf(0);
							cdcalc[12][k] = String.valueOf(0);
							cdcalc[13][k] = String.valueOf(0);
							cdcalc[14][k] = String.valueOf(0);
							cdcalc[15][k] = String.valueOf(0);
						}
						
					
				}
				
			
			/*for (int k = 0; k < length; k++){
			for (int l = 0; l < 16; l++) {
				System.out.print(cdcalc[l][k] + " ");
			}
				System.out.print("\n");
			}*/
				
	}
	
	public int getLength(){
		length = 0;
		int wk = 0;
		try{
			while(cdcalc[0][wk] != null){
				length++;
				wk++;
			}
		}catch(ArrayIndexOutOfBoundsException aiob){
				
			
			}
		return length;
	}
	
	public void setPhone(int opt){
		this.opt = opt;
	}
	
	public void setFileName(String fn){
		this.fn = fn;
	}
	
	public String[][] getFull(){
		return cdcalc;
	}
	
	public double[] getXYZValue(int index){
		double[] val = new double[3];
		try{
			
			String vals1 = cdcalc[0][index];
			String vals2 = cdcalc[1][index];
			String vals3 = cdcalc[2][index];
			val[0] = Double.parseDouble(vals1);
			val[1] = Double.parseDouble(vals2);
			val[2] = Double.parseDouble(vals3);
			
		}catch(NumberFormatException nfe){
			val[0] = 0;
			val[1] = 0;
			val[2] = 0;
			
		}
		return val;
		
	}
	
	public String getDateTimeString(int index){
		String date = new String();
		
		date = cdcalc[3][index];
		
	return date;
	}
		
	public double getHour(int index){
		String date = new String();
		try {
			Date id =  df.parse(cdcalc[3][index]);
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
			Date id =  df.parse(cdcalc[3][index]);
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
			Date id =  df.parse(cdcalc[3][index]);
			date = sec.format(id);
			sc =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return sc;
	}
	
	public double getTimeBetweenValue(int index){
		double val = 0;
		String vals = cdcalc[5][index];
		val = Double.parseDouble(vals);
	return val;	
	}
	
	public double[] getXYZSpeedValue(int index){
		double[] val = new double[3];
		String vals1 = cdcalc[6][index];
		String vals2 = cdcalc[7][index];
		String vals3 = cdcalc[8][index];
		val[0] = Double.parseDouble(vals1);
		val[1] = Double.parseDouble(vals2);
		val[2] = Double.parseDouble(vals3);
		
	return val;
	}
	
	public double[] getXYZAccelerationValue(int index){
		double[] val = new double[3];
		String vals1 = cdcalc[9][index];
		String vals2 = cdcalc[10][index];
		String vals3 = cdcalc[11][index];
		val[0] = Double.parseDouble(vals1);
		val[1] = Double.parseDouble(vals2);
		val[2] = Double.parseDouble(vals3);
		
	return val;
	}
	
	public double getModSValue(int index){
		double val = 0;
		String vals = cdcalc[12][index];
		val = Double.parseDouble(vals);
		
	return val;
	}
	
	public double getModAValue(int index){
		double val = 0;
		String vals = cdcalc[13][index];
		val = Double.parseDouble(vals);
		
	return val;
	}
	
	public double getSThetaValue(int index){
		double val = 0;
		String vals = cdcalc[14][index];
		val = Double.parseDouble(vals);
		
	return val;
	}
	
	public double getAThetaValue(int index){
		double val = 0;
		String vals = cdcalc[15][index];
		val = Double.parseDouble(vals);
		
	return val;
	}
	
	
	
	

}
