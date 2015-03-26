import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import Distribution.StatsGenerator;

import maths.PhoneData;
import mysql.insertMySQL;


public class StatGeneratorMain {

	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
        "TA92903URNf067ff16fcf8e045",
        "YT910K6675876ded0861342065",
        "ZX1B23QFSP48abead89f52e3bb",
	};

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		System.out.println("Track machine learning 101");

/*		System.out.println("Enter data file path:");
		String filePath = sc.nextLine();
		insertMySQL.insertXDisp(filePath);
*/
		System.out.println("Enter query (PhoneID = 'HT25TW5055273593c875a9898b00' AND TrackNo = 3):");
		
		for(int i = 0; i<phones.length; i++){
			int track = 1;
			
			while(true){
				String query = "PhoneID = '"+phones[i]+"' AND TrackNo = "+String.valueOf(track);
				ArrayList<PhoneData> filtered;
				try{
					filtered = insertMySQL.query("FilteredData", query);
				} catch (SQLException e){
					System.out.println("PhoneID "+phones[i]+" has "+(track-1)+" tracks");
					break;	// Exit this while loop
				}
				
				
				// Generate stats
				StatsGenerator sg = new StatsGenerator(filtered);
				
				
				
				track++;
			}
		}
		
	}

}
