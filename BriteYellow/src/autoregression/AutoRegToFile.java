package autoregression;

import java.util.ArrayList;

import csvexport.CSVWriter;
import csvimport.PhoneNames;
import dynamodb.DataBaseQueries;
import dynamodb.ObjectConversion;
import objects.PhoneData;
import splitting.TrackSelect;
import maths.*;
import autoregression.*;

public class AutoRegToFile {
	
	public static void main(String[] args) throws Exception{
		
		/*ArrayList<PhoneData> PDAL = new ArrayList<PhoneData>();
		int opt = 1;
		String fn = new String("24th Sept ORDERED.csv");
		PhoneData PD[];
		DataGetter DG = new DataGetter(opt, fn);
		int length = DG.getLength();
		PD = DG.getFullPhoneData();
		int i = 0;
		while(i<length){
			PDAL.add(PD[i]);
			i++;
		}*/
		CSVWriter CSV;
		double[] r;
		final int ORDER = 10;
		ArrayList<PhoneData> PDAL;
		ArrayList<PhoneData> PDAL2;
		DataBaseQueries DBQ = new DataBaseQueries("Processed_Data");
		String phone_id;
		PhoneNames PN = new PhoneNames();
		int max;
		String coefficients = new String();
		String attributes = new String();
		ArrayList<String> att = new ArrayList<String>();
		final char P1 = 'p';
		final char P2 = 'x';
		String behaviour = "0,0,0\n";
		ArrayList<String> output = new ArrayList<String>();
		final String FILE_LOCATION = "AutoregCSV/";
		
		
		for(int x = 1; x < 7; x++){
			phone_id = PN.numberToName(x);
			PDAL = ObjectConversion.convertFrom(DBQ.queryTable(phone_id, 'a'));
			max = DBQ.findMaxTrackNo(phone_id);
			
			//Businessman,Shopper,Security
			if(phone_id.equalsIgnoreCase("TA92903URNf067ff16fcf8e045")){
				behaviour = "0,1,0\n";
			}
			else if(phone_id.equalsIgnoreCase("ZX1B23QFSP48abead89f52e3bb")){
				behaviour = "1,0,0\n";
			}
			else if(phone_id.equalsIgnoreCase("HT25TW5055273593c875a9898b00")){
				behaviour = "1,0,0\n";
			}
			else if(phone_id.equalsIgnoreCase("YT910K6675876ded0861342065")){
				behaviour = "0,1,0\n";
			}
			else if(phone_id.equalsIgnoreCase("ZX1B23QBS53771758c578bbd85")){
				behaviour = "0,0,1\n";
			}
			
			for(int i = 1; i < max; i++){
				
				PDAL2 = TrackSelect.selecter(PDAL, i);
				
				ARDataFeed ARDF = new ARDataFeed();
				
				
				ARDF.setData(PDAL2);
				ARDF.setMeasuredParameter(P1, P2);
				ARDF.setOrder(ORDER);
				r = ARDF.runAR();
				try{
					coefficients = ","+r[0]; 
					for(int j = 1; j<ORDER; j++){
						coefficients += ","+r[j];
					}
					attributes = phone_id + "," + i + coefficients + "\n";
					att.add(attributes);
					output.add(behaviour);
					
				}catch(NullPointerException e){
					System.err.println("Track Too Short");
				}
				System.out.println("Processing: "+"Phone: " +phone_id + " Track: " +i);
				/*try{
					
					for(int j = 0; j<order;j++){
					
						System.out.println(r[j]);
		
					}
				}catch(NullPointerException e){
					System.err.println("Track Too Short");
				}
				System.out.println();*/
			}
			
		}
		
		for (String value : att) {
		    System.out.print(value);
		}
		
		CSV = new CSVWriter(FILE_LOCATION + "measured parameter= " + P1 + " in the= " + P2 + " order= " + ORDER);
		CSV.write(att.toArray(new String[att.size()]), true);
		CSV.finish();
		CSV = new CSVWriter(FILE_LOCATION + "output" + " measured parameter= " + P1 + " in the= " + P2  + " order= " + ORDER);
		CSV.write(output.toArray(new String[output.size()]), true);
		CSV.finish();
		System.out.println("Finished Writing.");
	}
}
