import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

import filters.KalmanFilter2;

import maths.PhoneData;
import mysql.insertMySQL;


public class KalmanFilter2Test {

	/**
	 * @param args
	 * @throws SQLException 
	 */
/*	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("Track machine learning 101");
		Scanner sc  = new Scanner(System.in);

		
		System.out.println("Enter query (eg: x = 156 AND PhoneID = 'HT25TW5055273593c875a9898b00'):");
		String query = sc.nextLine();
		
		ArrayList<PhoneData> output = insertMySQL.query(query);
		
		ArrayList<PhoneData> output2 = new ArrayList<PhoneData>();
		for(int i = 0; i<10; i++){
			output2.add(output.get(i));
		}
		
		KalmanFilter2 kf2 = new KalmanFilter2(KalmanFilter2.u, KalmanFilter2.HexAccel_noise_mag, KalmanFilter2.tkn_x, KalmanFilter2.tkn_y, output2);
		System.out.println(output2.get(0).x+" "+output2.get(0).y);
		for(int i = 1; i<10; i++){
			kf2.update();
			System.out.println(output2.get(i).x+" "+output2.get(i).y+" -- "+kf2.getEstimatedPosXAt(i-1)+" "+kf2.getEstimatedPosYAt(i-1));
		}
		
	}
*/
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		
		ArrayList<PhoneData> output2 = new ArrayList<PhoneData>();
		PhoneData ph = new PhoneData();
		ph.x = 2; ph.y = 1; ph.ts = new Timestamp(0);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 1; ph.y = 2; ph.ts = new Timestamp(1000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 1; ph.y = 1; ph.ts = new Timestamp(2000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 2; ph.y = 2; ph.ts = new Timestamp(3000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 2; ph.y = 2; ph.ts = new Timestamp(4000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 2; ph.y = 2; ph.ts = new Timestamp(5000);
		ph = new PhoneData();
		output2.add(ph);
		ph.x = 3; ph.y = 3; ph.ts = new Timestamp(6000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 3; ph.y = 3; ph.ts = new Timestamp(7000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 4; ph.y = 4; ph.ts = new Timestamp(8000);
		output2.add(ph);
		
		KalmanFilter2 kf2 = new KalmanFilter2(KalmanFilter2.u, KalmanFilter2.HexAccel_noise_mag, KalmanFilter2.tkn_x, KalmanFilter2.tkn_y, output2);
		System.out.println(output2.get(0).x+" "+output2.get(0).y);
		for(int i = 1; i<8; i++){
			kf2.update();
			System.out.println(output2.get(i).x+" "+output2.get(i).y+" -- "+kf2.getEstimatedPosXAt(i-1)+" "+kf2.getEstimatedPosYAt(i-1));
		}
	}
}
