import java.text.ParseException;
import java.util.ArrayList;

import maths.PhoneData;
import csvimport.CSVReaders;
import csvimport.NewDataScaling;


public class NewDataLook {
	
	public static void main(String[] args) throws ParseException{
		
		
		CSVReaders CSV = new CSVReaders("HT25TW5055273593c875a9898b00.csv");
		ArrayList<PhoneData> PD = CSV.getUncategorisedDataObjectList();
		ArrayList<PhoneData> PD2 = NewDataScaling.scaleAndCalcNewData(PD);
		
		for(int i = 0; i < PD2.size(); i++){
			
			System.out.println(PD2.get(i).toString());
			
		}

	}
	
	
}
