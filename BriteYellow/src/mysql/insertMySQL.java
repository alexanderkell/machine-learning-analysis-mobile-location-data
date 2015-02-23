package mysql;

import java.sql.*;
import java.util.ArrayList;

import maths.*;

public class insertMySQL {

	

	public static void insertXDisp(String fn) throws Exception{

		Connection conn = null;
		Statement stmt = null;
		PreparedStatement preparedStatement = null;
		int opt = 7;
		System.out.println("Getter");
		DataGetter DG = new DataGetter(opt, fn);
		System.out.println("Connector");
		conn = connection.Connect();
		
		for(int i = 0; i<DG.getLength(); i++){

			String insert = "INSERT INTO RawData "
					+ "(x,y,z,PhoneID, Timestamp,TrackNo,XDisp,YDisp,ZDisp,DispModulus,TimeBetween,XSpeed,YSpeed,ZSpeed,ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try{
				preparedStatement = conn.prepareStatement(insert);
				preparedStatement.setDouble(1, (DG.getX(i)));
				preparedStatement.setDouble(2, (DG.getY(i)));
				preparedStatement.setDouble(3, (DG.getZ(i)));
				preparedStatement.setString(4, (DG.getPhoneID(i)));
				preparedStatement.setTimestamp(5, (DG.getTimestamp(i)));
				preparedStatement.setInt(6, (DG.getTrackNo(i)));
				
				if(i==0){
					for(int zz = 7; zz<=20; zz++){
						preparedStatement.setDouble(zz, 0);
					}
					continue;
				} 
				preparedStatement.setDouble(7, (DG.getXYZDistanceBetween(i)[0]));
				preparedStatement.setDouble(8, (DG.getXYZDistanceBetween(i)[1]));
				preparedStatement.setDouble(9, (DG.getXYZDistanceBetween(i)[2]));
				preparedStatement.setDouble(10, (DG.getDistanceBetween(i)));
				preparedStatement.setDouble(11, (DG.getTimeBetweenValue(i)));
				preparedStatement.setDouble(12, (DG.getXYZSpeedValue(i)[0]));
				preparedStatement.setDouble(13, (DG.getXYZSpeedValue(i)[1]));
				preparedStatement.setDouble(14, (DG.getXYZSpeedValue(i)[2]));
				preparedStatement.setDouble(15, (DG.getModSValue(i)));
				preparedStatement.setDouble(16, (DG.getSThetaValue(i)));
				
				if(i==1){
					for(int zz = 17; zz<=20; zz++){
						preparedStatement.setDouble(zz, 0);
					}
					continue;
				} 
				preparedStatement.setDouble(17, (DG.getXYZAccelerationValue(i)[0]));
				preparedStatement.setDouble(18, (DG.getXYZAccelerationValue(i)[1]));
				preparedStatement.setDouble(19, (DG.getXYZAccelerationValue(i)[2]));
				preparedStatement.setDouble(20, (DG.getAThetaValue(i)));
				

				
				preparedStatement.executeUpdate();
			}catch(SQLException e){
				System.out.println(e.getMessage());
			}
			

		}
		System.out.println("Records inserted");
	}
	public static ArrayList<PhoneData> query(String query) throws SQLException{
		
		ArrayList<PhoneData> pdd2 = new ArrayList<PhoneData>();
		int x = 0;
		try{
			Connection conn = connection.Connect();
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT * FROM RawData WHERE "+query;
			ResultSet rs = stmt.executeQuery(sql);		
			
			while(rs.next()){			
// "(x,y,z,PhoneID,XDisp,YDisp,ZDisp,DispModulus,TimeBetween,XSpeed,YSpeed,ZSpeed,ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "

				PhoneData newData = new PhoneData();
				newData.x = rs.getDouble("x");
				newData.y = rs.getDouble("y");
				newData.z = rs.getDouble("z");
				newData.phone_id = rs.getString("PhoneID");
				newData.ts = rs.getTimestamp("Timestamp");
				newData.xdisp = rs.getDouble("XDisp");
				newData.ydisp = rs.getDouble("YDisp");
				newData.zdisp = rs.getDouble("ZDisp");
				newData.moddisp = rs.getDouble("DispModulus");
				newData.tb = rs.getDouble("TimeBetween");
				newData.rsx = rs.getDouble("XSpeed");
				newData.rsy = rs.getDouble("YSpeed");
				newData.rsz = rs.getDouble("ZSpeed");
				newData.modspd = rs.getDouble("ModSpeed");
				newData.spdtheta = rs.getDouble("STheta");
				newData.rax = rs.getDouble("XAcc");
				newData.ray = rs.getDouble("YAcc");
				newData.raz = rs.getDouble("ZAcc");
				newData.acctheta = rs.getDouble("ATheta");
				newData.track_no = rs.getInt("TrackNo");
				x++;
				pdd2.add(newData);
				
			}
			System.out.println("Queried and retrieved "+ x+" values");
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		
		}
		
//		PhoneData[] result = pdd2.toArray(new PhoneData[pdd2.size()]);
		return pdd2;
	}
	
}
