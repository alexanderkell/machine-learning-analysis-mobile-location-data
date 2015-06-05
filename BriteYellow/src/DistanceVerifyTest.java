import java.text.ParseException;
import java.util.ArrayList;

import maths.DataGetter;


import objects.PhoneData;
import splitting.TrackSelect;
import filters.DistanceVerify;
import filters.FilterMain;
import filters.SpeedVerify;
import filters.jkalman.JKalmanHelper;
import graphing.PlotTracks;


public class DistanceVerifyTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DataGetter dfo = new DataGetter(1,"24th Sept ORDERED.csv");
//		ArrayList<PhoneData> unfiltered = dfo.getFullPhoneDataList();
		ArrayList<PhoneData> unfiltered = TrackSelect.selecter(dfo.getFullPhoneDataList(), 19);
		DistanceVerify cutBig = new DistanceVerify(unfiltered,138);
		cutBig.check();
		ArrayList<PhoneData> reana = cutBig.getFull();
		
		// Distance Verified result
//		PlotTracks.plotTrack2(unfiltered.toArray(new PhoneData[unfiltered.size()]), reana.toArray(new PhoneData[reana.size()]),PlotTracks.X, PlotTracks.Y, 1f);
//		PlotTracks.plotTrack2(reana.toArray(new PhoneData[reana.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);
		
		
		JKalmanHelper jkh = new JKalmanHelper(reana, 11, 13);
		while(!jkh.isEndReached())
			jkh.processData();
		
		ArrayList<PhoneData> reana2 = jkh.getFullResult();
		// Kalman filter result
//		PlotTracks.plotTrack2(reana.toArray(new PhoneData[reana.size()]), reana2.toArray(new PhoneData[reana2.size()]),PlotTracks.X, PlotTracks.Y, 1f);

		ArrayList<PhoneData> interpolated = FilterMain.interpolate(3, reana2);
		// Interpolation filter result
//		PlotTracks.plotTrack2(reana2.toArray(new PhoneData[reana2.size()]),interpolated.toArray(new PhoneData[interpolated.size()]),PlotTracks.X, PlotTracks.Y, 1f);
		
		PlotTracks.plotTrack2(unfiltered.toArray(new PhoneData[unfiltered.size()]),interpolated.toArray(new PhoneData[interpolated.size()]),PlotTracks.X, PlotTracks.Y, 1f);
	}

}
