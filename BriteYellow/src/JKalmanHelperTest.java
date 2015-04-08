import java.util.ArrayList;
import java.util.Scanner;

import Objects.PhoneData;
import filters.jkalman.JKalmanHelper;
import graphing.PlotTracks;
import mysql.insertMySQL;


public class JKalmanHelperTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Scanner sc  = new Scanner(System.in);	
		
		System.out.println("Enter query (eg: x = 156 AND PhoneID = 'HT25TW5055273593c875a9898b00'):");
		String query = sc.nextLine();
		sc.close();
		ArrayList<PhoneData> output = insertMySQL.query(query);
		
		ArrayList<PhoneData> output2 = new ArrayList<PhoneData>();
		for(int i=1; i<50; i++)
			output2.add(output.get(i));
		
//		for(int i=0; i<10; i++)
//			System.out.println(output2.get(i).x+" "+output2.get(i).y+" "+output2.get(i).ts.toString());
		
		
		JKalmanHelper jkh = new JKalmanHelper(output2, 11, 13);
		while(!jkh.isEndReached())
			jkh.processData();
		
		ArrayList<PhoneData> result = jkh.getFullResult();
		for(int i=0; i<result.size(); i++)
			System.out.println(output2.get(i).x+" "+output2.get(i).y+" -- "+result.get(i).x+" "+result.get(i).y+" "+result.get(i).ts.toString());

		PlotTracks.plotTrack2(output2.toArray(new PhoneData[output2.size()]), result.toArray(new PhoneData[result.size()]), PlotTracks.X, PlotTracks.Y, 0.5f);
	}
	
}
