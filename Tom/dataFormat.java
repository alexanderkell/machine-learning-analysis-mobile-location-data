import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;

public class dataFormat{
		
	public static double[][][][] d4d (int opt, int length) throws FileNotFoundException, ParseException{
		
		double[][][][] datset = new double[length][length][length][length];
		String cd[][]=csvReader.wd(opt);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		String temp = cd[3][3];
		Date result =  df.parse(temp);  
	    System.out.println(result);
		
		

		return datset;	
	}

}
