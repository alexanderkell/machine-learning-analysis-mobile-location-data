import java.text.ParseException;
import java.util.ArrayList;

import maths.DataFormatOperations;
import maths.DataGetter;

import csvimport.CSVReaders;

import objects.PhoneData;
import splitting.TrackSelect;
import filters.DistanceVerify;
import graphing.PlotTracks;


public class DistanceVerifyTest {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		DataGetter dfo = new DataGetter(1,"24th Sept ORDERED.csv");
//		ArrayList<PhoneData> unfiltered = dfo.getFullPhoneDataList();
		ArrayList<PhoneData> unfiltered = TrackSelect.selecter(dfo.getFullPhoneDataList(), 7);
		DistanceVerify cutBig = new DistanceVerify(unfiltered,200);
		cutBig.check();
		ArrayList<PhoneData> reana = cutBig.getFull();
		PlotTracks.plotTrack2(unfiltered.toArray(new PhoneData[unfiltered.size()]),reana.toArray(new PhoneData[reana.size()]), PlotTracks.X, PlotTracks.Y, 1f);
//		PlotTracks.plotTrack2(reana.toArray(new PhoneData[reana.size()]), PlotTracks.X, PlotTracks.Y, 0.1f);

	}

}
