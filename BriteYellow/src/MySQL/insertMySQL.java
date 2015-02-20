package MySQL;

import java.sql.*;

import Maths.*;
import Maths.DataFormatOperations.PhoneData;

public class insertMySQL {
<<<<<<< HEAD
	
	public static void main(String args[]){
		try {
			insertMySQL.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertXDisp(DataGetter Data) throws Exception{
=======

	public static void main(String args[]) throws Exception{
		int opt = 7;
		String fn = new String("C:/Users/Fezan/Documents/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		insertMySQL.insertXDisp(opt, fn);
	}
	
	//public void insertXDisp(DataGetter Data) throws Exception{
	public static void insertXDisp(int opt, String fn) throws Exception{
>>>>>>> 1c0106acb8a88fbf889d0ac8a7d56531b8a41b2b
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
<<<<<<< HEAD
				preparedStatement.setInt(1, 12);
				preparedStatement.setInt(2, 12);
				preparedStatement.setInt(3, 13);
				preparedStatement.setInt(4, 11);
				preparedStatement.setInt(5, 12);
				preparedStatement.setInt(6, 13);
				preparedStatement.setInt(7, 11);
				preparedStatement.setInt(8, 12);
				preparedStatement.setInt(9, 13);
				preparedStatement.setInt(10, 11);
				preparedStatement.setInt(11, 12);
				preparedStatement.setInt(12, 13);
				preparedStatement.setInt(13, 11);
				preparedStatement.setInt(14, 12);
				preparedStatement.setInt(15, 13);
				preparedStatement.setInt(16, 11);
				preparedStatement.setInt(17, 12);
				preparedStatement.setInt(18, 13);
=======
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
>>>>>>> 1c0106acb8a88fbf889d0ac8a7d56531b8a41b2b
				
				preparedStatement.executeUpdate();
				System.out.println("Record inserted");
			}catch(SQLException e){
				System.out.println(e.getMessage());
			}
			

		}
	}
	public static void query() throws SQLException{
		try{
			Connection conn = connection.Connect();
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT x, y FROM AnalysedTracks WHERE y = 13";
			ResultSet rs = stmt.executeQuery(sql);		
			
			while(rs.next()){
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				
				System.out.println("x: "+x);
				System.out.println("y: "+y);
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		
		}
	}
	
}
