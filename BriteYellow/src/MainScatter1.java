import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import csvexport.CSVWriter;
import filters.DistanceVerify;
import mysql.insertMySQL;
import Distribution.Ratios;
//for whole divided area
import Objects.PhoneData;

public class MainScatter1 {
	final static String ph1n = "HT25TW5055273593c875a9898b00";//variables denoting phone IDs
    final static String ph2n = "ZX1B23QBS53771758c578bbd85";
    final static String ph3n = "TA92903URNf067ff16fcf8e045";
    final static String ph4n = "YT910K6675876ded0861342065";
    final static String ph5n = "ZX1B23QFSP48abead89f52e3bb";
    final static String ph6n = "8d32435715629c24a4f3a16b";
    
	final static String[] phin = new String[]{
			ph1n,
			ph5n,
			ph2n,
			ph3n,
			ph4n,
		};
	
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ParseException, SQLException {
		// TODO Auto-generated method stub
		
		ArrayList<PhoneData>[] output = new ArrayList[5];
		ArrayList<PhoneData>[] reana = new ArrayList[5];
		for(int i = 0 ; i<5; i++){
			output[i] = insertMySQL.query("PhoneID = '"+phin[i]+"'");
			
			// For the verifying the modulus distance part
			DistanceVerify cutBig = new DistanceVerify(output[i],144);
			cutBig.check();
			reana[i] = cutBig.getFull();
		}
		/* REMEMBER TO CHANGE LINE 103 which looks like the following
		 * change output to reana if working with DistanceVerify
		* Ratios ratios = new Ratios(output[i].toArray(new PhoneData[output[i].size()]));
		*/
		
		/*
		 * x range: 87 to 1044 (main corridor from 87 to 659 and from 694 to 1044)
		 * y range: 2 to 364 (main corridor from 302 to 364)
		 */
		
		final int XINTERVAL = 4; //4
		final int YINTERVAL = 1; //3
		final double XSTART = 250;
		final double YSTART = 300;
		final double XEND = 950;
		final double YEND = 380;
		
		final int PORPERTYX = Ratios.PATH_LENGTH;
		final int PORPERTYY = Ratios.TIME_SPENT;
		
		
		// No need to the change the following 5 lines
		final String XAXIS = Ratios.getAxisName(PORPERTYX)+" ("+Ratios.getAxisUnit(PORPERTYX)+")";
		final String YAXIS = Ratios.getAxisName(PORPERTYY)+" ("+Ratios.getAxisUnit(PORPERTYY)+")";

		float xstep = (float)(XEND - XSTART)/XINTERVAL;
		float ystep = (float)(YEND - YSTART)/YINTERVAL;
		
		double xstart, xend, ystart, yend;
		
		
		CSVWriter writer = null;
		
		for(int a=0; a<XINTERVAL; a++){
			xstart = XSTART + xstep*a;
			xend = xstart+xstep;
			for(int b=0; b<YINTERVAL; b++){
				
				// Calculate xstart, xend, ystart, yend
				ystart = YSTART + ystep*b;
				yend = ystart+ystep;

				File directory = new File("src/Distribution/Ratios/Ratios");
				if(! directory.isDirectory())
					directory.mkdirs();
				
				String CHARTTITLE = Ratios.getAxisName(PORPERTYY)+" vs. "+Ratios.getAxisName(PORPERTYX)+" from ("+Math.round((float)xstart)+", "+Math.round((float)ystart)+") to ("+Math.round((float)xend)+", "+Math.round((float)yend)+")";
				try {
					writer = new CSVWriter(directory+CHARTTITLE+".csv");
					writer.write(new String[]{CHARTTITLE});
					writer.write(new String[]{XAXIS});
					writer.write(new String[]{YAXIS});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				long lines = 0;

				for(int i = 0; i<5; i++){
					Ratios ratios = new Ratios(output[i].toArray(new PhoneData[output[i].size()]));
					int phone_no = 0;
					if(phin[i].equals(ph1n))
						phone_no = 1;
					else if(phin[i].equals(ph2n))
						phone_no = 2;
					else if(phin[i].equals(ph3n))
						phone_no = 3;
					else if(phin[i].equals(ph4n))
						phone_no = 4;
					else if(phin[i].equals(ph5n))
						phone_no = 5;

					
					double[] result1 = ratios.get(PORPERTYX, xstart, xend, ystart, yend);
					double[] result2 = ratios.get(PORPERTYY, xstart, xend, ystart, yend);

					if(result1 == null)
						continue;
							
							
					for(int j=0; j<result1.length; j++){
						try{
							writer.write(new float[]{ (float) result1[j], (float) result2[j], phone_no});
							lines ++;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
				}
				try {
					writer.finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// If the results were empty, delete the file
				if(lines == 0){
					try {
						if(! writer.delete())
							System.err.println("Failed to delete \""+writer.getFileName()+"\"");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}	
	}
}
