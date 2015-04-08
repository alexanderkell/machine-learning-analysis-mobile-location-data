package maths;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;

import objects.PhoneData;
import csvimport.CSVReaders;

public class DataFormatOperations{
		
	//initialise all variables
	//relative speed
	private double rsx = 0;
	private double rsy = 0;
	private double rsz = 0;
	//relative acceleration
	private double rax = 0;
	private double ray = 0;
	private double raz = 0;
	//time variables
	protected double hr = 0;
	protected double mn = 0;
	protected double sc = 0;
	private double xco = 0;
	private double yco = 0;
	private double zco = 0;
	
	
	

	//sets up the date formats to be used for splitting up the different constituent parts of the date
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
	DateFormat df2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
	DateFormat hour = new SimpleDateFormat("HH", Locale.ENGLISH);
	DateFormat min = new SimpleDateFormat("mm", Locale.ENGLISH);
	DateFormat sec = new SimpleDateFormat("ss", Locale.ENGLISH);
	
	
	//class variables
	private CSVReaders Read;
	protected int length;
	//read in data
	protected String cdcalc[][];
	protected PhoneData cdcalc2[];
	
	public DataFormatOperations(int opt, String fn) throws ParseException{
		
		//Read and store the phone data
		Read = new CSVReaders(fn);
		cdcalc = Read.myPhone(opt);
		processData1();
		makeTimeStamp();
		processData2();
		getSort();
		makeXYZDistanceBetween();
		makeDistanceBetween();
	}
	
	public DataFormatOperations(PhoneData[] ph) throws ParseException{
		//Read and store the phone data
		length = ph.length;
		cdcalc2 = ph;
		cdcalc = new String[20][length];
		if(cdcalc2[0].ts == null){
			makeTimeStamp();
		}
		processData2();
		makeXYZDistanceBetween();
		makeDistanceBetween();
		getSort();
	}
	
	/**This method allows the phone to be changed without the need to
	 * read data from the CSVReaders again
	 * 
	 * @param opt phone number 1 to 6 or 7 for all phones
	 */
	public void changePhone(int opt){
		cdcalc = Read.myPhone(opt);
		length = cdcalc[0].length;
		processData1();
		if(cdcalc2[0].ts == null){
			makeTimeStamp();
		}
		processData2();
		getSort();
		makeTimeStamp();
		makeXYZDistanceBetween();
		makeDistanceBetween();
	}
	
	
	private void processData1(){
		
//		this.opt = opt;
		//create constructor object
		length = cdcalc[0].length;
		
		cdcalc2 = new PhoneData[length];
		
		// Store the x,y,z speeds
		for(int i=0; i<length; i++){
			cdcalc2[i] = new PhoneData();
			cdcalc2[i].phone_id = cdcalc[4][i];
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
				try{
					cdcalc2[i].wholedate = df2.parse(cdcalc[3][i]);
					
				}catch(ParseException pe2){
					System.err.println("Problem Passing Date, Please Check Format");
				}
			}
		}
		
	}
	private void processData2(){
		
		if(cdcalc2[0].wholedate == null && cdcalc2[0].ts == null){
			for(int i=0; i<length; i++){
				try{
					cdcalc2[i].wholedate = df.parse(cdcalc2[i].wholedatestring);
				}catch(ParseException pe){
					try{
						cdcalc2[i].wholedate = df2.parse(cdcalc2[i].wholedatestring);
						
					}catch(ParseException pe2){
						System.err.println("Problem Passing Date, Please Check Format");
					}
				}
				
			}
			
		}
		
		//works out the time between each reading based on the time
			for(int y = 0; y<length-1; y++){
				
				Timestamp wholedate1 = cdcalc2[y].ts;
				Timestamp wholedate2 = cdcalc2[y+1].ts;
				
					// If the 2 successive points are not in the same day, make tb = 0
					if(wholedate1.getDate() - wholedate2.getDate() != 0){
						cdcalc2[y+1].tb = 0;
						continue;
					}
				
				
				if(wholedate1.compareTo(wholedate2)>0){
					throw new IllegalArgumentException("Please Put Data in Date and Time Order Before Running!");
				}else{
					cdcalc2[y+1].tb = (wholedate2.getTime() - wholedate1.getTime())/1000;
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
					cdcalc2[k].rsz = zco;
				} else{
					// Calculate Speeds (distance / time)
					cdcalc2[k].rsx = rsx = xco/cdcalc2[k].tb;
					cdcalc2[k].rsy = rsy = yco/cdcalc2[k].tb;
					cdcalc2[k].rsz = zco/cdcalc2[k].tb;
				}
				
				// Calculate the angle
				// If the person is not moving (zero speed), assume the angle is
				// as the previous angle
				if(rsx == 0 && rsy == 0)
					cdcalc2[k].spdtheta = cdcalc2[k-1].spdtheta;
				else
					cdcalc2[k].spdtheta = Math.atan(rsy/rsx);
				
				
				cdcalc2[k].modspd = Math.sqrt(rsx*rsx + rsy*rsy +rsz*rsz);
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
					cdcalc2[l].modacc = (cdcalc2[l].modspd - cdcalc2[l-1].modspd);

				} else {
					cdcalc2[l].rax = rax/ cdcalc2[l].tb;
					cdcalc2[l].ray = ray/ cdcalc2[l].tb;
					cdcalc2[l].raz = raz/ cdcalc2[l].tb;
					cdcalc2[l].modacc = (cdcalc2[l].modspd - cdcalc2[l-1].modspd) / cdcalc2[l].tb;

				}
				
				// If the person is not moving (zero speed), assume the angle is
				// as the previous angle
				if(rax == 0 && ray == 0){
					cdcalc2[l].acctheta = cdcalc2[l-1].acctheta;

				}else{
					cdcalc2[l].acctheta = Math.atan(ray/rax);
				}
				
			}
			
	}
	

	
	private void getSort(){
		int r = 0,s = 0; 
		int track = 1; 
		for(int i = 0; i<length; i++){ 
			double x = cdcalc2[i].x;
			if(x<200){ 
				s = 1;
				if(r == 1){ 
					r = 0; 
					track++;
				} 
			cdcalc2[i].track_no = -1; 
			} else if(x>850){
				r = 1; 
				if(s == 1){ 
					s = 0; 
					track++; 
				} 
				cdcalc2[i].track_no = -1; 
			}else{ 
				cdcalc2[i].track_no = track; 
				cdcalc[16][i] = String.valueOf(track); 
			} 
		}
	}
	
	private void makeTimeStamp(){

		for(int i = 0; i<length; i++){
			//calculates the mysql timestamp
			Date wholedate =  cdcalc2[i].wholedate;
			// If the data doesn't have the wholedate attribute in Date format, create it then
			if(wholedate == null){
				try{
					wholedate = df.parse(cdcalc2[i].wholedatestring);
				}catch(ParseException pe){
					try{
						wholedate = df2.parse(cdcalc2[i].wholedatestring);
						
					}catch(ParseException pe2){
						System.err.println("Problem Passing Date, Please Check Format");
					}
				}
			}
			Timestamp ts = new Timestamp(wholedate.getTime());
			cdcalc2[i].ts = ts;
/*			try{
				cdcalc[17][i] = String.valueOf(ts);
			}catch(NullPointerException npe){
					
			}
*/		}
	}
	
	private void makeXYZDistanceBetween(){
		// Calculate different between 2 recorded position
		int i = 1;
		while(i<length){	
			cdcalc2[i].xdisp = cdcalc2[i].x - cdcalc2[i-1].x;
			cdcalc2[i].ydisp = cdcalc2[i].y - cdcalc2[i-1].y;
			cdcalc2[i].zdisp = cdcalc2[i].z - cdcalc2[i-1].z;
			i++;
		}
		
		
	}
	
	private void makeDistanceBetween(){
		int i = 1;
		while(i<length){
			// Calculate different between 2 recorded position
			double xdiff = cdcalc2[i].x - cdcalc2[i-1].x;
			double ydiff = cdcalc2[i].y - cdcalc2[i-1].y;
			cdcalc2[i].moddisp = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
			i++;
		}
	}
}

