package interpolation;

import graphing.PlotHelper;
import graphing.PlotTracks;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import maths.DataGetter;
import maths.PhoneData;
import splitting.TrackSelect;

public class CHSpline {
	
	/**
	 * 
	 * Method taken from http://www.sciencedirect.com/science/article/pii/S0165783609002604 Section 2.3
	 * 
	 * @param t A value between 0 and 1
	 * @param point0	x y coordinate of the starting point
	 * @param modspd0	modulus speed of the starting point
	 * @param heading0	speed angle of the starting point
	 * @param point1	x y coordinate of the next point
	 * @param modspd1	modulus speed of the next point
	 * @param heading1  speed angle of the next point
	 * @return the x y coordinate correspond to t
	 */
	public static double[] cHs(float t, double[] point0, double modspd0, double heading0, double[] point1, double modspd1, double heading1){

		return cHs(
				t,
				point0,
				new double[]{modspd0*Math.cos(heading0), modspd0*Math.sin(heading0)},
				point1,
				new double[]{modspd1*Math.cos(heading1), modspd1*Math.sin(heading1)}
				);
		
	}
	
	/**
	 * 
	 * @param t A value between 0 and 1
	 * @param point0 coordinate of the starting point
	 * @param speed0 speed of the starting point
	 * @param point1 coordinate of the end point
	 * @param speed1 speed of the end point
	 * @return
	 */
	public static double[] cHs(float t, double[] point0, double[] speed0, double[] point1, double[] speed1){

		// Calculate the point 
		double[] result = new double[point0.length];
		for(int i = 0; i < result.length; i++){
			result[i] = (2*t*t*t - 3*t*t + 1) * point0[i] + (t*t*t - 2*t*t + t)*speed0[i] + (-2*t*t*t + 3*t*t)*point1[i] + (t*t*t-t*t)*speed1[i];
		}

		return result;
	}
	
	/**Test bench for the CHS
	 * 
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String args[]) throws ParseException{
		
		
		DataGetter dg = new DataGetter(2, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\24th Sept ORDERED.csv");

		// Select track 1
//		PhoneData[] pd = TrackSelect.selecter(dg.getFullPhoneData(), 1);
		PhoneData[] pd = dg.getFullPhoneData();
		final String[] labels = new String[]{"SamplePoints", "SampleResults"};

		PlotHelper plot = new PlotHelper("Demo", "X", "Y", labels);
		
		ArrayList <PhoneData> before = new ArrayList <PhoneData>();
		ArrayList <PhoneData> after = new ArrayList <PhoneData>();
		
		for(int h = 49; h<61; h++){
			if(h == 49)
				before.add(pd[h]);
			before.add(pd[h+1]);
			
			final double[] point0 = new double[]{ pd[h].x, pd[h].y };
			final double[] point1 = new double[]{ pd[h+1].x, pd[h+1].y  };
			
//			System.out.println(pd[h].x+" "+ pd[h].y );
//			System.out.println(pd[h+1].x+" "+ pd[h].y);
			final double modspeed0 = pd[h].modspd;
			final double heading0 = pd[h].spdtheta;
	
			final double modspeed1 = pd[h+1].modspd; 
			final double heading1 = pd[h+1].spdtheta;
			
			final float step = 0.1f;
			//======================================================
			
			final int steps = (int) (1/step);
			
			final double[][] f = new double[steps+1][2];
			
/*			double xdispt0 = Math.abs(pd[h].xdisp) + Math.abs(pd[h+1].xdisp);
			double xdispt1 = Math.abs(pd[h+1].xdisp) + Math.abs(pd[h+2].xdisp);
			double ydispt0 = Math.abs(pd[h].ydisp) + Math.abs(pd[h+1].ydisp);
			double ydispt1 = Math.abs(pd[h+1].ydisp) + Math.abs(pd[h+2].ydisp);
			
			double[] speed0 = new double[]{
					pd[h].rsx*Math.abs(pd[h+1].xdisp)/xdispt0 + pd[h+1].rsx*Math.abs(pd[h].xdisp)/xdispt0 ,
					pd[h].rsy*Math.abs(pd[h+1].ydisp)/ydispt0 + pd[h+1].rsy*Math.abs(pd[h].ydisp)/ydispt0 ,
			};
			double[] speed1 = new double[]{
					pd[h+1].rsx*Math.abs(pd[h+2].xdisp)/xdispt1 + pd[h+2].rsx*Math.abs(pd[h+1].xdisp)/xdispt1 ,
					pd[h+1].rsy*Math.abs(pd[h+2].ydisp)/ydispt1 + pd[h+2].rsy*Math.abs(pd[h+1].ydisp)/ydispt1 ,
			};
*/			
			// Calculate the average speed between the current and the next point
			double[] speed0 = new double[]{
					(pd[h].rsx + pd[h+1].rsx)/2,
					(pd[h].rsy + pd[h+1].rsy)/2
			};
			// Calculate the average speed between the next point and the point after
			double[] speed1;
			if(h+2 >= pd.length){
				speed1 = new double[]{
						pd[h+1].rsx,
						pd[h+1].rsy
					};
			} else {
				speed1 = new double[]{
					(pd[h+1].rsx + pd[h+2].rsx)/2,
					(pd[h+1].rsy + pd[h+2].rsy)/2
				};
			}
			for(int i = 0; i<=steps; i++){
//				f[i] = cHs(i*step, point0, modspeed0, heading0, point1, modspeed1, heading1);
//				f[i] = cHs(i*step, point0, new double[]{pd[h].rsx, pd[h].rsy}, point1, new double[]{pd[h+1].rsx, pd[h+1].rsy});
				f[i] = cHs(i*step, point0, speed0, point1, speed1);

				
				PhoneData newdata = new PhoneData();
				newdata.x = f[i][0];
				newdata.y = f[i][1];
				newdata.ts = new Timestamp(pd[h].ts.getTime()+(long)((pd[h+1].ts.getTime()-pd[h].ts.getTime())*i*step));
				after.add(newdata);
			}
//			plot.addData(labels[0], point0[0], point0[1]);
//			plot.addData(labels[0], point1[0], point1[1]);
//			plot.addData(labels[1], f);

		}


//		plot.showDialog();
		PlotTracks.plotTrack2(before.toArray(new PhoneData[before.size()]),after.toArray(new PhoneData[after.size()]), PlotTracks.X, PlotTracks.Y, 1f);
	}
}
