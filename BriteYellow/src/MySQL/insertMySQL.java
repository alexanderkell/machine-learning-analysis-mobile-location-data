package MySQL;

import java.sql.*;

import Maths.*;
import Maths.DataFormatOperations.PhoneData;

public class insertMySQL {

	public static void main(String args[]) throws Exception{
		int opt = 7;
		String fn = new String("C:/Users/Fezan/Documents/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		insertMySQL.insertXDisp(opt, fn);
	}
	
	//public void insertXDisp(DataGetter Data) throws Exception{
	public static void insertXDisp(int opt, String fn) throws Exception{
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement preparedStatement = null;
		
		
		DataGetter DG = new DataGetter(opt, fn);
		conn = connection.Connect();
		
		for(int i = 0; i<DG.getLength(); i++){

			String insert = "INSERT INTO AnalysedTracks "
					+ "(x,y,z,PhoneID,XDisp,YDisp,ZDisp,DispModulus,TimeBetween,XSpeed,YSpeed,ZSpeed,ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try{
				preparedStatement = conn.prepareStatement(insert);
				preparedStatement.setInt(1, (int) (DG.getX(i)));
				preparedStatement.setInt(2, (int) (DG.getY(i)));
				preparedStatement.setInt(3, (int) (DG.getZ(i)));
				preparedStatement.setString(4, (DG.getPhoneID(i)));
				preparedStatement.setInt(5, (int) (DG.getXYZDistanceBetween(i)[0]));
				preparedStatement.setInt(6, (int) (DG.getXYZDistanceBetween(i)[1]));
				preparedStatement.setInt(7, (int) (DG.getXYZDistanceBetween(i)[2]));
				preparedStatement.setInt(8, (int) (DG.getDistanceBetween(i)));
				preparedStatement.setInt(9, (int) (DG.getTimeBetweenValue(i)));
				preparedStatement.setInt(10, (int) (DG.getXYZSpeedValue(i)[0]));
				preparedStatement.setInt(11, (int) (DG.getXYZSpeedValue(i)[1]));
				preparedStatement.setInt(12, (int) (DG.getXYZSpeedValue(i)[2]));
				preparedStatement.setInt(13, (int) (DG.getModSValue(i)));
				preparedStatement.setInt(14, (int) (DG.getSThetaValue(i)));
				preparedStatement.setInt(15, (int) (DG.getXYZAccelerationValue(i)[0]));
				preparedStatement.setInt(16, (int) (DG.getXYZAccelerationValue(i)[1]));
				preparedStatement.setInt(17, (int) (DG.getXYZAccelerationValue(i)[2]));
				preparedStatement.setInt(18, (int) (DG.getAThetaValue(i)));
				
				preparedStatement.executeUpdate();
				System.out.println("Record inserted");
			}catch(SQLException e){
				System.out.println(e.getMessage());
			}
			

		}
	}
}
