import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import maths.*;
import mysql.*;
import csvimport.*;
import distribution.Ratios;
import filters.DistanceVerify;
import filters.jkalman.JKalmanHelper;
import graphing.PlotTracks;
import redundant.DataSorting2;
import splitting.*;

public class RatioMain {
	
	public static PhoneData[] generateData() throws SQLException{
		Scanner sc  = new Scanner(System.in);
		System.out.println("Enter query (eg: x = 156 AND PhoneID = 'HT25TW5055273593c875a9898b00'):");
		String query = sc.nextLine();
				
		ArrayList<PhoneData> output = new insertMySQL().query("Rawdata",query);
		
		return TrackSelect.selecter(output.toArray(new PhoneData[output.size()]), 1);

	}
	public static double sumArrays(double[] input){
		double result = 0;
		for(double k:input)
			result+=k;
		return result;
	}
	public static void main(String args[]) throws ParseException, SQLException{
		Ratios r = new Ratios(generateData());
		double[] res = r.get(Ratios.PATH_LENGTH, 660, 693, 302, 364);
		double result = sumArrays(res);
		System.out.println("Total path length (points) for the selected data / track: "+result);
		
		// The size of the res array (i.e. res.length) represent how many times the person enters the area
		//
		// To get the average speed, you must get the results of total path length and time spent in that area by
		// get(Ratios.PATH_LENGTH, xstart, xend, ystart, yend) and
		// get(Ratios.TIME_SPENT, xstart, xend, ystart, yend)
		// and sum the elements in each results
		//
		// Do not use the get(Ratios.AVERAGE_SPEED, xstart, xend, ystart, yend) as this will give you wrong results
		// This may also apply to STHETAOUT - STHETAIN
		
		
	}
	
}
