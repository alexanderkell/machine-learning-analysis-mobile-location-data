import java.text.ParseException;
import java.util.ArrayList;

import csvimport.CSVReaders;
import maths.DataGetter;
import maths.PhoneData;
import graphing.PlotTracks;


public class TrackPlotter3000 {
	public static void main(String[] args) throws ParseException{
		String fn = "HT25TW5055273593c875a9898b00.csv";
		CSVReaders CSV = new CSVReaders(fn);
		PhoneData[] PD = CSV.getUncategorisedDataObject();
		DataGetter DG = new DataGetter(PD);
		PlotTracks.plotTrack3(DG.getFullPhoneData(), PlotTracks.X, PlotTracks.Y, 0.1f);;
		
	}
}
