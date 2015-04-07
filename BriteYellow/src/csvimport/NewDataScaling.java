package csvimport;

import filters.FilterMain;
import graphing.PlotTracks;
import graphing.TrackChangeListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import csvexport.CSVWriter;

import splitting.TrackSelect;

import maths.DataGetter;
import maths.PhoneData;

public class NewDataScaling {
	
	public static ArrayList<PhoneData> scaleAndCalcNewData(ArrayList<PhoneData> INPUT) throws ParseException{
		ArrayList<PhoneData> OUTPUT = new ArrayList<PhoneData>();
		ArrayList<PhoneData>  final_OUTPUT = new ArrayList<PhoneData>(); 
		PhoneData datapoint;
		
		for(int i = 0; i < INPUT.size(); i++){
			datapoint = new PhoneData();;
			datapoint.x = (INPUT.get(i).x/1.36)-586;
			datapoint.y = (INPUT.get(i).y/1.36)-3.88;
			datapoint.z = INPUT.get(i).z;
			datapoint.ts = INPUT.get(i).ts;
			datapoint.wholedate = INPUT.get(i).wholedate;
			datapoint.wholedatestring = INPUT.get(i).wholedatestring;
			datapoint.tb = INPUT.get(i).tb;
			datapoint.phone_id = INPUT.get(i).phone_id;
			OUTPUT.add(datapoint);
//			System.out.println(datapoint.x+" "+datapoint.y);
		}
		
		DataGetter DG = new DataGetter(OUTPUT.toArray(new PhoneData[OUTPUT.size()]));
		final_OUTPUT = new ArrayList<PhoneData>(Arrays.asList(DG.getFullPhoneData()));
		
		return final_OUTPUT;
	}
	
	public static void main(String args[]) throws Exception{
		final String filename = "HT25TW5055273593c875a9898b00.csv";
		ArrayList<PhoneData> pd1_list = new CSVReaders(filename).getUncategorisedDataObjectList();
		ArrayList<PhoneData> pd2_list = scaleAndCalcNewData(pd1_list);
		
		
		FilterMain filtering = new FilterMain(200, 11, 13);
		ArrayList<PhoneData> filtered = filtering.FilterTot(pd2_list);
		
		// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
		DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
		final PhoneData[] newdata = newdg.getFullPhoneData();
	

		
//		final PhoneData[] pd2 = pd2_list.toArray(new PhoneData[pd2_list.size()]);
		int max_tracks = TrackSelect.getTotalTracks(newdata);
		TrackChangeListener tcl = new TrackChangeListener(){

			@Override
			public PhoneData[] setTrack(int index) {
				// TODO Auto-generated method stub
				return TrackSelect.selecter(newdata, index);
			}
			
		};
		PlotTracks.plotTrack2(PlotTracks.X, PlotTracks.Y, 0.1f, tcl, max_tracks);
	}
	
}
