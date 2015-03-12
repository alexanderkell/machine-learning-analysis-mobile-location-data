package filters;

import java.text.ParseException;
import java.util.ArrayList;

import filters.jkalman.JKalmanHelper;
import maths.DataGetter;
import maths.PhoneData;

public class FilterMain {
	int maxSpeed=200;
	int xMesNoise=11;
	int yMesNoise=13;
	
	public FilterMain(){

	}
	
	public FilterMain(int xMesNoise, int yMesNoise){
		this.xMesNoise = xMesNoise;
		this.yMesNoise = yMesNoise;
	}
	
	
	public FilterMain(int maxSpeed, int xMesNoise, int yMesNoise){
		this.maxSpeed = maxSpeed;
		this.xMesNoise = xMesNoise;
		this.yMesNoise = yMesNoise;

	}	
	
	public ArrayList<PhoneData> FilterTot(ArrayList<PhoneData> output) throws Exception{
		//Cut big speeds
	
		DistanceVerify cutBig = new DistanceVerify(output,maxSpeed);
		cutBig.check();
		ArrayList<PhoneData> reana = cutBig.getFull();
	
		
		// Kalman filter
		JKalmanHelper jkh = new JKalmanHelper(reana, xMesNoise, yMesNoise);
		while(!jkh.isEndReached())
			jkh.processData();
		
		reana = jkh.getFullResult();
		
		return reana;
			
	}
	
	
}
