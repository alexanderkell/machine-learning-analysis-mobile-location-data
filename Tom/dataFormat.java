import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;
import java.lang.Math;

public class dataFormat{
				
	public static String[][] datcalc (int opt) throws FileNotFoundException, ParseException{
		double tb = 0;//initialise time between variable
		double rsx = 0;//initialise relative speed x variable
		double rsy = 0;//initialise relative speed y variable
		double rsz = 0;//initialise relative speed z variable
		double rax = 0;
		double ray = 0;
		double raz = 0;
		double hr = 0;//initialise first hour variable
		double mn = 0;//initialise first min variable
		double sc = 0; //initialise first sec variable
		double hr2 = 0;//initialise second hour variable
		double mn2 = 0;//initialise second min variable
		double sc2 = 0;//initialise second sec variable
		double xco = 0;
		double yco = 0;
		double zco = 0;
		double modspd = 0;
		double modacc = 0;
		double p = 2;
		double sqrt = 0.5;
		
		String cd[][]=csvReader.wd(opt);//pull through raw matrix of specific phone
		cd[5][0]="0";
		cd[6][0]="0";
		int length = 0;
		while (cd[0][length] != null){
			length++;
		}
		
		
		System.out.print("\n\n\n\n");
		//sets up the date formats to be used for splitting up the different constituent parts of the date
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		DateFormat hour = new SimpleDateFormat("HH", Locale.ENGLISH);
		DateFormat min = new SimpleDateFormat("mm", Locale.ENGLISH);
		DateFormat sec = new SimpleDateFormat("ss", Locale.ENGLISH);
		
		//works out the time between each reading based on the time
			for(int y = 0; y<length-1; y++){
				Date wholedate1 =  df.parse(cd[3][y]); 
				Date wholedate2 =  df.parse(cd[3][y+1]);
				//System.out.println(wholedate1);
				//System.out.println(wholedate2);
				if(wholedate1.compareTo(wholedate2)>0){
	        		System.out.println("Please Put Data in Date and Time Order Before Running!");
	        		y=length;
	        	}else{
				hr =Double.parseDouble(hour.format(wholedate1));
				mn =Double.parseDouble(min.format(wholedate1));
				sc =Double.parseDouble(sec.format(wholedate1)); 
				hr2 =Double.parseDouble(hour.format(wholedate2));
				mn2 =Double.parseDouble(min.format(wholedate2));
				sc2 =Double.parseDouble(sec.format(wholedate2));
				tb = (hr2 - hr)*60*60 + (mn2 - mn)*60 + (sc2 - sc);
				cd[5][y+1] = String.valueOf(tb);
				//System.out.println(tb);
	        }
				}
			
			for (int k = 1; k < length; k++){//working out relative speeds in all directions (velocity)
						xco = Double.parseDouble(cd[0][k])-Double.parseDouble(cd[0][k-1]);
						yco = Double.parseDouble(cd[1][k])-Double.parseDouble(cd[1][k-1]);
						zco = Double.parseDouble(cd[2][k])-Double.parseDouble(cd[2][k-1]);
						tb = Double.parseDouble(cd[5][k]);
						
						rsx = xco/tb;
						rsy = yco/tb;
						rsz = zco/tb;
						rax = xco/(tb*tb);
						ray = yco/(tb*tb);
						raz = zco/(tb*tb);
						
						
						modspd = Math.pow(rsx,p)+Math.pow(rsy,p)+Math.pow(rsz,p);
						modspd = Math.pow(modspd,sqrt);
						modacc = Math.pow(rax,p)+Math.pow(ray,p)+Math.pow(raz,p);
						modacc = Math.pow(modacc,sqrt);
						if(tb != 0){
							cd[6][k] = String.valueOf(rsx);
							cd[7][k] = String.valueOf(rsy);
							cd[8][k] = String.valueOf(rsz);
							cd[9][k] = String.valueOf(rax);
							cd[10][k] = String.valueOf(ray);
							cd[11][k] = String.valueOf(raz);
							cd[12][k] = String.valueOf(modspd);
							cd[13][k] = String.valueOf(modacc);
						}else{
							cd[6][k] = String.valueOf(0);
							cd[7][k] = String.valueOf(0);
							cd[8][k] = String.valueOf(0);
							cd[9][k] = String.valueOf(0);
							cd[10][k] = String.valueOf(0);
							cd[11][k] = String.valueOf(0);
							cd[12][k] = String.valueOf(0);
							cd[13][k] = String.valueOf(0);
						}
						
					
				}
				
			
			/*for (int k = 0; k < length; k++){
			for (int l = 0; l < 14; l++) {
				System.out.print(cd[l][k] + " ");
			}
				System.out.print("\n");
			}*/
			
			
			

	    //System.out.println(hour.format(wholedate1));
	    //System.out.println(cd[5][1]);
		

		return cd;	
	}
	
	

}
