package Maths;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;

import CSVImport.CSVReaders;

public class DataFormatOperations{
	

	public final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", "Yacc", "Zacc", "ModAcc", "ATheta"	

	};
	public final static int X = 0;
	public final static int Y = 1;
	public final static int Z = 2;
	public final static int WholeDate = 3;
	public final static int PhoneId = 4;
	public final static int TimeBetween = 5;
	public final static int XSpeed = 6;
	public final static int YSpeed = 7;
	public final static int ZSpeed = 8;
	public final static int MSpeed = 9;
	public final static int STheta = 10;
	public final static int XAcc = 11;
	public final static int YAcc = 12;
	public final static int ZAcc = 13;
	public final static int MAcc = 14;
	public final static int ATheta = 15;
	public final static int Track = 16;
	public final static int Timestamp = 17;
	
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
	protected double hr = 0;
	protected double mn = 0;
	protected double sc = 0;
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
	private CSVReaders Read;
	private int opt;
	protected int length;
	private String fn;
	private String temp = new String();
	//read in data
	protected String cdcalc[][];
	protected PhoneData cdcalc2[];
	
	public DataFormatOperations(int opt, String fn) throws ParseException{
		
		//Read and store the phone data
		Read = new CSVReaders(fn);

		cdcalc = Read.myPhone(opt);
		processData1();
		processData2();
	}
	public void reanalyse(String[][] cdcalc){
		this.cdcalc = cdcalc;
		processData1();
		processData2();
	}
	public void reanalyse(PhoneData[] cdcalc2){
		this.cdcalc2 = cdcalc2;
		processData2();
	}
	public void processData1(){
		
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
				
			}
		}
		
	}
	public void processData2(){
		
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
				
				
				cdcalc2[k].modspd = Math.sqrt(rsx*rsx + rsy*rsy);
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
				
				// If the person is not moving (zero speed), assume the angle is
				// as the previous angle
				if(rax == 0 && ray == 0){
					cdcalc2[l].acctheta = cdcalc2[l-1].acctheta;

				}else{
					cdcalc2[l].acctheta = Math.atan(ray/rax);
				}
				cdcalc2[l].modacc = (cdcalc2[l].modspd - cdcalc2[l-1].modspd) / cdcalc2[l].tb;
				

			}
			
			getSort();
	}
	
	public class PhoneData{
		//position x,y,z
		public double x, y, z;
		
		//whole date in Data and String format
		public Date wholedate;
		public String wholedatestring;
		
		//time between current position and the previous position
		public double tb;
		
		//relative speeds in x,y,z and modulus direction, and angle
		public double rsx, rsy, rsz, modspd, spdtheta;
		
		//relative accelerations in x,y,z and modulus direction
		public double rax, ray, raz, modacc, acctheta;
		
		public String phone_id;
		
		public int track_no;
		
		public Timestamp ts;
	}
	
	public void getSort(){
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		while(i<length){
			
			x = cdcalc2[i].x;
			if(x>200 && x<850){
				cdcalc2[i].track_no = track;
				cdcalc[16][i] = String.valueOf(track);
			}
			else if(x>850){
				r=1;
				s=0;
				cdcalc2[i].track_no = -1;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
				cdcalc2[i].track_no = -1;
			}
			
			
			else if(x<200){s=1;r=0;
				cdcalc2[i].track_no = -1;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
				cdcalc2[i].track_no = -1;
			}
			
			//System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
		}
	}
	
	public void makeTimeStamp(){
		int i = 0;
		
		while(i<length){
			//calculates the mysql timestamp
			Date wholedate =  cdcalc2[i].wholedate;
			//System.out.println(wholedate);
			Timestamp ts = new Timestamp(wholedate.getTime());
			cdcalc2[i].ts = ts;
			cdcalc[17][i] = String.valueOf(ts);
			i++;
		}
		
	}
	
	public static void main(String[] args) throws ParseException{
		DataFormatOperations DFO = new DataFormatOperations(1 , "/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		DFO.makeTimeStamp();
		
	}
	

}