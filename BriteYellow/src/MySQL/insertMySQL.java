package MySQL;

import java.sql.*;

import Maths.*;

public class insertMySQL {

	public static void main(String args[]) throws Exception{
		insertMySQL.insertXDisp();
	}
	
	//public void insertXDisp(DataGetter Data) throws Exception{
	public static void insertXDisp() throws Exception{
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement preparedStatement = null;
		
		conn = connection.Connect();
		
		for(int i = 0; i<Data.getLength(); i++){

			String insert = "INSERT INTO AnalysedTracks "
					+ "(x,y,z,PhoneID,XDisp,YDisp,ZDisp,DispModulus,TimeBetween,XSpeed,YSpeed,ZSpeed,ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try{
				preparedStatement = conn.prepareStatement(insert);
				preparedStatement.setInt(1, 11);
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
				
				preparedStatement.executeUpdate();
				System.out.println("Record inserted");
			}catch(SQLException e){
				System.out.println(e.getMessage());
			}
			

		}
	}
}
