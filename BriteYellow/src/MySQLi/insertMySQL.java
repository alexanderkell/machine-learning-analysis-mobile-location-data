package MySQLi;

import Maths.*;

public class insertMySQL {

	public void insertXDisp(DataGetter Data){
		for(int i = 0; i<Data.getLength(); i++){
			String insert = "INSERT INTO AnalysedTracks ("
					+ "x,y,z,Timestamp,Phone ID, XDisp, YDisp, ZDisp, DispModulus, TimeBetween, XSpeed, YSpeed, ZSpeed, ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			PreparedStatement st = connection.
			
			Data.getDistanceBetween(i);
		}
	}
}
