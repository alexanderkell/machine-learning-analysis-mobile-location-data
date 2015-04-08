import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import csvexport.CSVWriter;
import filters.DistanceVerify;
import maths.DataGetter;
import mysql.insertMySQL;
import Distribution.ProbabilityList;
import Distribution.ProbabilityList.Labels;
import Objects.PhoneData;


public class main2 {
	final String ph1n = "HT25TW5055273593c875a9898b00";//variables denoting phone IDs
    final String ph2n = "ZX1B23QBS53771758c578bbd85";
    final String ph3n = "TA92903URNf067ff16fcf8e045";
    final String ph4n = "YT910K6675876ded0861342065";
    final String ph5n = "ZX1B23QFSP48abead89f52e3bb";
    final String ph6n = "8d32435715629c24a4f3a16b";
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws SQLException, ParseException {
		// TODO Auto-generated method stub
		Scanner sc  = new Scanner(System.in);
		
		
		System.out.println("Enter query (eg: x = 156 AND PhoneID = 'HT25TW5055273593c875a9898b00'):");
		String query = sc.nextLine();
		
		ArrayList<PhoneData> output = insertMySQL.query(query);
				
//		DataGetter reAn = new DataGetter(output);
		
		
		// For the verifying the modulus distance part
		DistanceVerify cutBig = new DistanceVerify(output,150);
		cutBig.check();
		ArrayList<PhoneData> reana = cutBig.getFull();
		
		
		
		sc.close();
//		ProbabilityList pl = new ProbabilityList(reana.toArray(new PhoneData[reana.size()]));
		ProbabilityList pl = new ProbabilityList(output.toArray(new PhoneData[output.size()]));

//		System.out.println("Total time: "+pl.getTotal(ProbabilityList.TIME));
		
		
		
/*		final String[] slabels = {
				"1:2", "2:3", "3:4", "4:5", "6:7", "7:8", "8:9", "9:10", "10:11", "12:13", "13:14", ">14"
			};
*/		final String[] slabels = {
				"<0.01", "0.01:10", "10:20", "20:30", "30:40", "40:50", "50:60", "60:70", "70:80", "80:90", "90:100", ">100"
			};
/*		final String[] slabels = {
				"1:4", "4:8", "8:12", "12:16", "16:20", "20:24", "24:28", "28:32", "32:40", "40:44", "44:48", ">48"
			};

		final String[] slabels = {
			"-72:-54", "-54:-36","-36:-18","-18:-1","-1:1","1:18", "18:36","36:54", "54:72"

		};

		final String[] slabels = {
				"60-120", "120-180","180-240","240-300","300-360","360-420","420-480", "480-540", "540-600", "600-660", "660-720", "720-800", "800-880", "880-960", ">960"
			};

		final String[] slabels = {
			"60-240", "240-480", "480-720", "720-960", "960-1200", "1200-1440", "1440-1680", "1680-1920", "1920-2160", "2160-2400", "2400-2640", "2640-2880", ">2880"

		};
		final String[] slabels = {
			"<0.01", "0.01-10", "10-20", "20-30", "30-40", "40-50", "50-60", "60-70", "70-80", "80-90", "90-100", ">100"
		};
*/		final float lower_bound = 1;
		final float higher_bound = 100000;
		final boolean within_bounds = true;
		final double xstart = 100, xend = 1020, ystart = 300, yend = 375;
		final int compare_pro = ProbabilityList.MODSPD;
		final int result_pro = ProbabilityList.TIME;
		

		
		// Parse the string labels
		Labels[] labels = new Labels[slabels.length];
		for(int i=0; i<slabels.length; i++){
			labels[i] = new Labels();
			String[] s = slabels[i].split(":|<|>");
			if(slabels[i].contains(":")){
				labels[i].low = Float.parseFloat(s[0]);
				labels[i].low = ProbabilityList.processNumbers(compare_pro, labels[i].low);
				labels[i].high = Float.parseFloat(s[1]);
				labels[i].high = ProbabilityList.processNumbers(compare_pro, labels[i].high);
			}
			else if(slabels[i].contains("<")){
				labels[i].high = Float.parseFloat(s[1]);
				labels[i].high = ProbabilityList.processNumbers(compare_pro, labels[i].high);
				labels[i].lorh = -1;
			} else if(slabels[i].contains(">")){
				labels[i].low = Float.parseFloat(s[1]);
				labels[i].high = ProbabilityList.processNumbers(compare_pro, labels[i].high);
				labels[i].lorh = 1;
			}
//			System.out.println(labels[i].low+" "+labels[i].high);
		}

		


			

		double denominator = pl.get(lower_bound,higher_bound,xstart, xend, ystart, yend, compare_pro, result_pro);
//			double denominator2 = pl.get(step,100000,xstart, xend, ystart, yend, compare_pro, result_pro);
		double denominator2 = pl.getTotal(xstart, xend, ystart, yend, result_pro);
		System.out.println("Average Speed (points/sec): "+(float)(pl.getTotal(xstart, xend, ystart, yend,ProbabilityList.DIST) / pl.getTotal(xstart, xend, ystart, yend,ProbabilityList.TIME)));

		float[] result = new float[slabels.length];
		for(int i=0; i<slabels.length; i++){
			if(labels[i].lorh == 0)
				result[i] = (float)pl.get(labels[i].low, labels[i].high, xstart, xend, ystart, yend, compare_pro, result_pro);

			else if(labels[i].lorh == -1)
				result[i] = (float)pl.get(Float.NEGATIVE_INFINITY, labels[i].high, xstart, xend, ystart, yend, compare_pro, result_pro);
			
			else if(labels[i].lorh == 1)
				result[i] = (float)pl.get(labels[i].low, Float.POSITIVE_INFINITY, xstart, xend, ystart, yend, compare_pro, result_pro);
				
			else 
				throw new IllegalArgumentException ("\"labels["+i+"].lorh\" must be -1 or 0 or 1");
			
			if (within_bounds)
				result[i] = result[i] /(float) denominator;
			else
				result[i] = result[i] /(float) denominator2;
			
		}
				
		for(int i = 0; i<result.length; i++){
			System.out.print(result[i]+" ");
		}
		
		
		
	}

}
