package MySQL;

import java.sql.*;
import java.util.ArrayList;

import Maths.*;
//import Maths.DataFormatOperations.PhoneData;

public class insertMySQL {
	
	public static void main(String args[]){
		try {
			insertMySQL.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	//public void insertXDisp(DataGetter Data) throws Exception{
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

			String insert = "INSERT INTO AnalysedTracks "
					+ "(x,y,z,PhoneID,XDisp,YDisp,ZDisp,DispModulus,TimeBetween,XSpeed,YSpeed,ZSpeed,ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try{
				preparedStatement = conn.prepareStatement(insert);
				preparedStatement.
				preparedStatement.setDouble(1, (DG.getX(i)));
				preparedStatement.setDouble(2, (DG.getY(i)));
				preparedStatement.setDouble(3, (DG.getZ(i)));
				preparedStatement.setString(4, (DG.getPhoneID(i)));
				if(i==0){
					for(int zz = 5; zz<=18; zz++){
						preparedStatement.setInt(zz, 0);
					}
					continue;
				} 
				preparedStatement.setDouble(5, (DG.getXYZDistanceBetween(i)[0]));
				preparedStatement.setDouble(6, (DG.getXYZDistanceBetween(i)[1]));
				preparedStatement.setDouble(7, (DG.getXYZDistanceBetween(i)[2]));
				preparedStatement.setDouble(8, (DG.getDistanceBetween(i)));
				preparedStatement.setDouble(9, (DG.getTimeBetweenValue(i)));
				preparedStatement.setDouble(10, (DG.getXYZSpeedValue(i)[0]));
				preparedStatement.setDouble(11, (DG.getXYZSpeedValue(i)[1]));
				preparedStatement.setDouble(12, (DG.getXYZSpeedValue(i)[2]));
				preparedStatement.setDouble(13, (DG.getModSValue(i)));
				preparedStatement.setDouble(14, (DG.getSThetaValue(i)));
				
				if(i==1){
					for(int zz = 15; zz<=18; zz++){
						preparedStatement.setInt(zz, 0);
					}
					continue;
				} 
				preparedStatement.setDouble(15, (DG.getXYZAccelerationValue(i)[0]));
				preparedStatement.setDouble(16, (DG.getXYZAccelerationValue(i)[1]));
				preparedStatement.setDouble(17, (DG.getXYZAccelerationValue(i)[2]));
				preparedStatement.setDouble(18, (DG.getAThetaValue(i)));

				
				preparedStatement.executeUpdate();
				System.out.println("Record inserted");
			}catch(SQLException e){
				System.out.println(e.getMessage());
			}
			

		}
	}
	public static PhoneData[] query() throws SQLException{
		
		ArrayList<PhoneData> pdd2 = new ArrayList<PhoneData>();
//		PhoneData[] pdd = new PhoneData[];
	 
		
		try{
			Connection conn = connection.Connect();
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT * FROM AnalysedTracks WHERE y = 13";
			ResultSet rs = stmt.executeQuery(sql);		
			
			while(rs.next()){			
// "(x,y,z,PhoneID,XDisp,YDisp,ZDisp,DispModulus,TimeBetween,XSpeed,YSpeed,ZSpeed,ModSpeed, STheta, XAcc, YAcc, ZAcc, ATheta) "

				PhoneData newData = new PhoneData();
				newData.x = rs.getInt("x");
				newData.y = rs.getInt("x");
				newData.z = rs.getInt("x");
				newData.phone_id = rs.getString("PhoneID");
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
				
				pdd2.add(newData);
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		
		}
		
		PhoneData[] result = pdd2.toArray(new PhoneData[pdd2.size()]);
		return result;
	}
	
}
