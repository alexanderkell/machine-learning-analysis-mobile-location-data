import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;

public class dataFormat{
				
	public static String[][] datcalc (int opt) throws FileNotFoundException, ParseException{
		double tb = 0;
		String cd[][]=csvReader.wd(opt);
		int length = 0;
		while (cd[0][length] != null){
			length++;
		}
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		DateFormat hour = new SimpleDateFormat("HH", Locale.ENGLISH);
		DateFormat min = new SimpleDateFormat("mm", Locale.ENGLISH);
		DateFormat sec = new SimpleDateFormat("ss", Locale.ENGLISH);
		
		//for(int x = 0; x<5; x++){
			for(int y = 0; y<length-1; y++){
				Date wholedate1 =  df.parse(cd[3][y]); 
				double hr =Double.parseDouble(hour.format(wholedate1));
				double mn =Double.parseDouble(min.format(wholedate1));
				double sc =Double.parseDouble(sec.format(wholedate1)); 
				Date wholedate2 =  df.parse(cd[3][y+1]); 
				double hr2 =Double.parseDouble(hour.format(wholedate2));
				double mn2 =Double.parseDouble(min.format(wholedate2));
				double sc2 =Double.parseDouble(sec.format(wholedate2));
				tb = (hr2 - hr)*60*60 + (mn2 - mn)*60 + (sc2 - sc);
				cd[5][y+1] = String.valueOf(tb);
			}
			
			
		//}
		
	    //System.out.println(hour.format(wholedate1));
	    //System.out.println(cd[5][1]);
		

		return cd;	
	}

}
