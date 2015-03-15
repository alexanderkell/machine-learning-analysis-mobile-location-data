import java.util.ArrayList;
import java.util.Arrays;

import csvexport.CSVWriter;

import filters.FilterMain;
import maths.DataGetter;
import maths.PhoneData;
import mysql.insertMySQL;


public class AttributesPrinter {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		DataGetter dfo = new DataGetter(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\26th Sept ORDERED.csv");
		for (int i = 1; i<=5; i++){
			dfo.changePhone(i);
			FilterMain filtering = new FilterMain(200, 11, 13);
			ArrayList<PhoneData> filtered = filtering.FilterTot(dfo.getFullPhoneDataList());
			
			// Reanalyse the filtered data using DataGetter and store the result in the "newdata" variable
			DataGetter newdg = new DataGetter(filtered.toArray(new PhoneData[filtered.size()]));
			PhoneData[] newdata = newdg.getFullPhoneData();
			CSVWriter ce = new CSVWriter("Attributes/26th Sept Phone "+i);
			ce.write(new String[]{
					"X","Y","Z","TimeStamp",
					"PhoneID","Tb","XDISP","YDISP",
					"ZDISP","MODDISP","RSX","RSY",
					"RSZ","RAX","RAY","RAZ",
					"MODSPD","MODACC","SPDTHETA","ACCTHETA",
					"TRACK", "Interpolated"
			});
			for(int j = 0; j <newdata.length; j++){
				ce.write(new String[]{
						String.valueOf(newdata[j].x), String.valueOf(newdata[j].y), String.valueOf(newdata[j].z), newdata[j].ts.toString(),
						newdata[j].phone_id, String.valueOf(newdata[j].tb), String.valueOf(newdata[j].xdisp), String.valueOf(newdata[j].ydisp),
						String.valueOf(newdata[j].zdisp),String.valueOf(newdata[j].moddisp), String.valueOf(newdata[j].rsx), String.valueOf(newdata[j].rsy),
						String.valueOf(newdata[j].rsz), String.valueOf(newdata[j].rax), String.valueOf(newdata[j].ray), String.valueOf(newdata[j].raz),
						String.valueOf(newdata[j].modspd), String.valueOf(newdata[j].modacc), String.valueOf(newdata[j].spdtheta), String.valueOf(newdata[j].acctheta),
						String.valueOf(newdata[j].track_no), String.valueOf(newdata[j].interpolated)
				});
			}
			ce.finish();
			
		}
	}

}
