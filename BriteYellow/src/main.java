
import java.sql.Connection;
import java.util.ArrayList;

import dynamodb.*;
import maths.PhoneData;
import mysql.*;

public class main {
	public static void main(String args[]) throws Exception{
		
		System.out.println("Track machine learning 101");
		connection con = new connection();
		Connection cond = con.Connect();
	
		insertMySQL Query = new insertMySQL();
		
		ArrayList<PhoneData> filtered = Query.query("FilteredData", "ZX1B23QFSP48abead89f52e3bb");
		
		System.out.println(filtered.get(1).x);
		
		//CommonOperations.reWriteProcessedData();
		
		//PlotTracks.plotTrack2(output.toArray(new PhoneData[output.size()]),filtered.toArray(new PhoneData[filtered.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);
	}
}
