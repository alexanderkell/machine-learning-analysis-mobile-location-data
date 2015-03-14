package filters;

import interpolation.CHSpline;

import java.sql.Timestamp;
import java.util.ArrayList;

import maths.PhoneData;
	
public class Interpolation{
	final static int tstep = 20;
	/**The interpolation method
	 * 
	 * @param input The ArrayList of PhoneData
	 * @return The interpolated ArrayList of PhoneData
	 */
	public ArrayList<PhoneData> interpolate(ArrayList<PhoneData> input){
		// The process cannot continue if result.size() <= 1
		if(input.size() <= 1){
			System.err.println("Interpolation is not carried out as the size of the input is less than 2");
			return input;
		}
		
		// Otherwise
		ArrayList<PhoneData> result = new ArrayList<PhoneData>();
		result.add(input.get(0));
		for(int i = 1; i < input.size(); i++){
			PhoneData p0 = input.get(i-1);
			PhoneData p1 = input.get(i);
			double[] point0 = new double[]{
					p0.x, p0.y, p0.z
			};
			double[] speed0 = new double[]{
					(p0.rsx+p1.rsx)/2, (p0.rsy+p1.rsy)/2, (p0.rsz+p1.rsz)/2
			};
			double[] point1 = new double[]{
					p1.x, p1.y, p1.z
			};
			double[] speed1;
			if(i<input.size()-1){
				PhoneData p2 = input.get(i+1);
				speed1 = new double[]{
						(p1.rsx+p2.rsx)/2, (p1.rsy+p2.rsy)/2, (p1.rsz+p2.rsz)/2
				};
			} else{
				speed1 = new double[]{
						p1.rsx, p1.rsy, p1.rsz
				};
			}
					
			// Calculate the time between 2 successive points
			long time0 = p0.ts.getTime();
			long time_diff = p1.ts.getTime() - time0;
			
			
			for(int j = 1; j <tstep; j++){
				float t = (float)j/(float)tstep;
				double[] newpoint = CHSpline.cHs(t, point0, speed0, point1, speed1);
				PhoneData newdata = new PhoneData();
				switch(newpoint.length){
				case 3:
					newdata.z = newpoint[2];
				case 2:
					newdata.y = newpoint[1];
				case 1:
					newdata.x = newpoint[0];
					break;
				}
				// Calculate and store the time
				newdata.ts = new Timestamp(time0+ (long)(time_diff*t));
				result.add(newdata);
			}
			result.add(input.get(i));
		}
		return result;
	}
}
