/**
 * 
 */
package Distribution;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import csvexport.CSVWriter;
import maths.DataFormatOperations;
import maths.DataGetter;
import maths.PhoneData;

/**
 *
 */
public class ProbabilityList extends DataGetter{

	// variable secret codes
//	final static int POSITION = 10;	//X Y Z position
	public final static int STILL = 14;	//Amount of time standing still
	public final static int DISAPPEAR = 15;  //Amount of time disappear from the tracking system

	public final static int MODSPD = 20;  // Time travelling at modulus speed
	public final static int XSPD = 21;  // X Speed
	public final static int YSPD = 22;  // Y Speed
	public final static int ZSPD = 23;  // Z Speed
	public final static int STHETA = 24;  // Speed theta
	public final static int STHETACHANGE = 25;  // Speed theta
	
	public final static int MODACC = 30;  // Modulus acceleration
	public final static int XACC = 31;  // X acceleration
	public final static int YACC = 32;  // Y acceleration
	public final static int ZACC = 33;  // Z acceleration
	public final static int ATHETA = 34;  // Acceleration theta
	public final static int ATHETACHANGE = 35;  // Acceleration theta
		
	public final static int TIME = 90; // Amount of time
	public final static int DIST = 91;	// Distance travelled
	public final static int XDIST = 92;	// X Distance travelled
	public final static int YDIST = 93;	// Y Distance travelled
	public final static int ZDIST = 94;	// Z Distance travelled
	public final static int NUMBER = 95; // Number of times occurring
	
	private int length;	// length of the data
	
	/** Constructor to load the data
	 * 
	 * @param fn filename
	 * @param opt option
	 * @throws ParseException 
	 */
	public ProbabilityList(int opt, String fn) throws ParseException{
		super(opt, fn);
		length = getLength();
	}
	
	public ProbabilityList(PhoneData[] ph) throws ParseException{
		super(ph);
		length = getLength();
	}
	
	/** Constructor to load the data
	 * 
	 * @param fn filename
	 * @param opt option
	 * @throws ParseException 
	 */
	public ProbabilityList(String fn, int opt) throws ParseException{
		super(opt, fn);
	}
	
	public void changePhone(int opt){
		super.changePhone(opt);
		length = getLength();
	}
	
	public double getTotal(int property){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i=getStartingIndex(property); i<length; i++)
			indices.add(i);
		
		return getTotal(indices, property);
	}
	public double getTotal(double xcenter, double ycenter, int property){
		ArrayList<Integer> indices = getIndicesWithinRange(xcenter, ycenter, 0, property);
		return getTotal(indices, property);
	}
	public double getTotal(double xcenter, double ycenter, double radius, int property){
		ArrayList<Integer> indices = getIndicesWithinRange(xcenter, ycenter, radius, property);
		return getTotal(indices, property);
	}
	public double getTotal(double xstart, double xend, double ystart, double yend, int property){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, property);
		return getTotal(indices, property);
	}
	
	
	private double getTotal(ArrayList<Integer> indices, int result_property){
		double result = 0;
		
		for(int i=0; i<indices.size(); i++){
//			if(dfo.getTimeBetweenValue(indices.get(i))> 20)
//				continue;
			if(result_property == DIST){
				result += getDistanceBetween(indices.get(i));
			} else if(result_property == XDIST){
				result += getXYZDistanceBetween(indices.get(i))[0];
			} else if(result_property == YDIST){
				result += getXYZDistanceBetween(indices.get(i))[1];
			} else if(result_property == ZDIST){
				result += getXYZDistanceBetween(indices.get(i))[2];
			} else if(result_property == TIME){
				result += getTimeBetweenValue(indices.get(i));
				
	//				System.out.println(getTimeBetweenValue(indices.get(i)));
			} else if(result_property == NUMBER){
				result++;
			} else if(result_property == STHETACHANGE){
				result += Math.abs(getSThetaChange(indices.get(i)));
			} else
				throw new IllegalArgumentException("You might be using the wrong argument for \"result_property\".");
		}
		return result;
	}
	
	
	public double get(double low, double high, final int compare_property, final int result_property){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i=getStartingIndex(compare_property); i<length; i++){
			indices.add(i);
		}
		return get(low, high, indices, compare_property, result_property);
	}
	public double get(double low, double high, double xcenter, double ycenter, double radius, final int compare_property, final int result_property){
		ArrayList<Integer> indices = getIndicesWithinRange(xcenter, ycenter, radius, compare_property);
		return get(low, high, indices, compare_property, result_property);
	}
	
	public double get(double low, double high, double xstart, double xend, double ystart, double yend, final int compare_property, final int result_property){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, compare_property);
		return get(low, high, indices, compare_property, result_property);
	}
	
	/**
	 * Get total value of the result_property that matches the criteria of the compare_property specified by low and high
	 * @param low
	 * @param high
	 * @param indices
	 * @param compare_property
	 * @param result_property
	 * @throws IllegalArgumentException if the double "low" is equal or higher than the double "high"
	 * 		if user passes the wrong argument in the "compare_pro" or "result_pro" field
	 * @return total value of the result_property
	 */
	private double get(final double low, final double high, final ArrayList<Integer> indices, final int compare_property, final int result_property){
		if (low >= high)
			throw new IllegalArgumentException(
				"Lower limit \"low\" must be smaller than the higher limit \"high\""
				);
		
		// value for storing the result of the compare property
		double value = 0;
		// ArrayList for storing the indices of the data which matches the criteria
		ArrayList<Integer> result_indices = new ArrayList<Integer>();
		// variable for indicating the person is standing still
		boolean was_still = false;
		for(int i=0; i<indices.size(); i++){
			
			if(compare_property == MODSPD)
				value = getModSValue(indices.get(i));
			else if(compare_property == XSPD)
				value = Math.abs(getXYZSpeedValue(indices.get(i))[0]);
			else if(compare_property == YSPD)
				value = Math.abs(getXYZSpeedValue(indices.get(i))[1]);
			else if(compare_property == ZSPD)
				value = Math.abs(getXYZSpeedValue(indices.get(i))[2]);
			else if(compare_property == MODACC)
				value = getModAValue(indices.get(i));
			else if(compare_property == XACC)
				value = Math.abs(getXYZAccelerationValue(indices.get(i))[0]);
			else if(compare_property == YACC)
				value = Math.abs(getXYZAccelerationValue(indices.get(i))[1]);
			else if(compare_property == ZACC)
				value = Math.abs(getXYZAccelerationValue(indices.get(i))[2]);
			else if(compare_property == STHETA)
				value = getSThetaValue(indices.get(i));
			else if(compare_property == ATHETA)
				value = getAThetaValue(indices.get(i));
			else if(compare_property == STHETACHANGE){
				value = getSThetaChange(indices.get(i));
			}else if(compare_property == ATHETACHANGE){
				value = getAThetaChange(indices.get(i));
			}else if(compare_property == STILL){
				if(isStandingStill(indices.get(i))){
					if(!was_still)
						value = 0;
					value += getTimeBetweenValue(indices.get(i));
					was_still = true;
					if(i<indices.size()-1)
						continue;
				} else {
					was_still = false;
				}
			}else if(compare_property == DISAPPEAR){
				value = getTimeBetweenValue(indices.get(i));
			}else
				throw new IllegalArgumentException("You might have place the wrong arguments in \"compare_property\"");
			

			if(( value >= low && value < high ) && ! was_still){
				result_indices.add(indices.get(i));
			}
		}
		return getTotal(result_indices, result_property);
		
	}

	/**
	 * 
	 * @param xcoordinate
	 * @param ycoordinate
	 * @param radius
	 * @param type	Position = 0, Speed = 1, Acceleration = 2
	 * @return
	 */
	protected ArrayList<Integer> getIndicesWithinRange(double xcoordinate, double ycoordinate, double radius, int property){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = new ArrayList<Integer>(); 
		
		// The starting index
		int start = getStartingIndex(property);
		
		for(int i=start; i<length; i++){
			// Calculate the distance between the user's point and the points in the data list
			double[] b = new double[]{	xcoordinate, ycoordinate, 0	};
			
			if(getLengthBetween(getXYZValue(i),b) <= radius){
				indices.add(i);
			}
		}
		return indices;
	}
	
	/**
	 * 
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @param type Position = 0, Speed = 1, Acceleration = 2
	 * @return
	 */
	protected ArrayList<Integer> getIndicesWithinRange(double xstart, double xend, double ystart, double yend, int property){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = new ArrayList<Integer>(); 
		
		// The starting index
		int start = getStartingIndex(property);

		boolean match = false;
		for(int i=start; i<length; i++){
			// Calculate the distance between the user's point and the points in the data list
			double[] point = getXYZValue(i);
			if(point[0]>=xstart && point[0]<=xend && point[1]>=ystart && point[1]<=yend){
				if(match)
					indices.add(i);
				else
					match = true;
			} else 
				match = false;
					
		}
		return indices;
	}
	
	private double getLengthBetween(double ax, double ay, double bx, double by){
		return Math.sqrt((ax-bx)*(ax-bx) + (ay-by)*(ay-by));
	}
	private double getLengthBetween(double[] a, double[] b){
		if( (a.length == b.length) && a.length == 3 )
			return getLengthBetween(a[0], a[1],b[0], b[1]);
		throw new IllegalArgumentException("The 2 arrays must both have length of 3");
	}
	protected int getStartingIndex(final int property){
		// If it is related to speed or difference in positions or time between the points, the starting index is 1
		if(property == MODSPD|| property == XSPD ||property == YSPD ||property == ZSPD || property == STHETA ||
				property == STILL || property == DISAPPEAR || property == TIME || property == DIST)
			return 1;
		
		// If it is related to acceleration or difference in speeds, the starting index is 2
		else if(property == MODACC ||property == XACC||property == YACC||property == ZACC|| property == ATHETA ||
				property == STHETACHANGE)
			return 2;
		
		// If it is related to change in acceleration, the starting index is 3
		else if(property == ATHETACHANGE)
			return 3;
		
		// It is is related to positions, the starting index is 0
		else 
			return 0;
	}
	
	/**Find the maximum of a set of data
	 * 
	 * @param data The array of data to be compared
	 * @return the index of the element with maximum value
	 */
	public static int getMax(float[] data){
		// temporary set the first element to the maximum, so index = 0
		int mindex = 0;
		for(int i=1; i<data.length; i++){
			if(data[i]> data[mindex]){
				mindex = i;
			}
		}
		return mindex;
	}
	/**Find the minimum of a set of data
	 * 
	 * @param data The array of data to be compared
	 * @return the index of the element with minimum value
	 */
	public static int getMin(float[] data){
		// temporary set the first element to the minimum, so index = 0
		int mindex = 0;
		// Find the minimum
		for(int i=1; i<data.length; i++){
			if(data[i]< data[mindex]){
				mindex = i;
			}
		}
		return mindex;
	}
/*	
	public double getMaxSpeed(){
		double max_speed = getSThetaValue(2);
		for(int i=2; i<length; i++){
			if(max_speed < getSThetaValue(i)){
				max_speed = getSThetaValue(i);
			}
		}
		return max_speed;
	}
	public double getMinSpeed(){
		
		double min_speed = getSThetaValue(2);
		for(int i=2; i<length; i++){
			if(min_speed > getSThetaValue(i)){
				min_speed = getSThetaValue(i);
			}
		}
		return min_speed;
	}
	
	public double getSpeed(double low, double high){
		
		int count = 0;
		for(int i=2; i<length; i++){
			if(getModSValue(i) >= low && getModSValue(i)<high){
				count++;
			}
		}
		return count;
	}
	
*/	
	
	public static String getAxisName(int property){
		if(property == STILL)
			return "Amount of time remained still";
		if(property == DISAPPEAR)
			return "Amount of time disappeared from the tracking system";
		if(property == MODSPD)
			return "Modulus speed";
		if(property == XSPD)
			return "Speed in X direction";
		if(property == YSPD)
			return "Speed in Y direction";
		if(property == ZSPD)
			return "Speed in Z direction";
		if(property == STHETA)
			return "Speed angle";
		if(property == STHETACHANGE)
			return "Speed angle changed";
		if(property == MODACC)
			return "Modulus acceleration";
		if(property == XACC)
			return "Acceleration in X direction";
		if(property == YACC)
			return "Acceleration in Y direction";
		if(property == ZACC)
			return "Acceleration in Z direction";
		if(property == ATHETA)
			return "Acceleration angle";
		if(property == ATHETACHANGE)
			return "Acceleration angle changed";
		if(property == DIST)
			return "distance travelled";
		if(property == XDIST)
			return "distance travelled in x direction";
		if(property == YDIST)
			return "distance travelled in y direction";
		if(property == ZDIST)
			return "distance travelled in z direction";
		if(property == TIME)
			return "time";
		if(property == NUMBER)
			return "no. of times";
		if(property == STHETACHANGE)
			return "change in speed angles";
		return null;
	}
	public static String getAxisUnit(int property){
		if(property == STILL)
			return "sec";
		if(property == DISAPPEAR)
			return "sec";
		if(property == MODSPD)
			return "points/sec";
		if(property == XSPD)
			return "points/sec";
		if(property == YSPD)
			return "points/sec";
		if(property == ZSPD)
			return "points/sec";
		if(property == STHETA)
			return "radians";
		if(property == STHETACHANGE)
			return "radians";
		if(property == MODACC)
			return "points/sec^2";
		if(property == XACC)
			return "points/sec^2";
		if(property == YACC)
			return "points/sec^2";
		if(property == ZACC)
			return "points/sec^2";
		if(property == ATHETA)
			return "radians";
		if(property == ATHETACHANGE)
			return "radians";
		return null;
	}
	
	public static float parseNumbers(String s){
		boolean hasParens = false;
        s = s.replaceAll(",","");

        if(s.contains("(")) {
            s = s.replaceAll("[()]","");
            hasParens = true;
        }

        int number = Integer.parseInt(s);

        if(hasParens) {
            number = -number;
        }
	    
        return number;
		
	}
	public static float processNumbers(int property, float n){
		if(property == STHETA || property == STHETACHANGE || property == ATHETA || property == ATHETACHANGE)
			n = n*(float)Math.PI/180;
		return n;
	}
	/** Test bench
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		final String[] slabels = {
				"1:4", "4:8", "8:12", "12:16", "16:20", "20:24", "24:28", "28:32", "32:40", "40:44", "44:48", ">48"
			};
/*
		final String[] slabels = {
			"-72:-54", "-54:-36","-36:-18","-18:-1","-1:1","1:18", "18:36","36:54", "54:72"

		};

		final String[] slabels = {
				"60-120", "120-180","180-240","240-300","300-360","360-420","420-480", "480-540", "540-600", "600-660", "660-720", "720-800", "800-880", "880-960", ">960"
			};

		final String[] slabels = {
			"60-240", "240-480", "480-720", "720-960", "960-1200", "1200-1440", "1440-1680", "1680-1920", "1920-2160", "2160-2400", "2400-2640", "2640-2880", ">2880"

		};
		final String[] slabels = {
			"<0.01", "0.01-10", "10-20", "20-30", "30-40", "40-50", "50-60", "60-70", "70-80", "80-90", "90-100", ">100"
		};
*/		final float lower_bound = 1;
		final float higher_bound = 100000;
		final boolean within_bounds = true;
		final double xstart = 100, xend = 1020, ystart = 300, yend = 375;
		final int compare_pro = STILL;
		final int result_pro = NUMBER;
		// Output difference with respect to a reference phone instead of their normal values
		// 0 for the phones normal value, and 1 to 5 for phone 1 to 5
		// or -1 for referencing to the maximum value, -2 for referencing to the minimum value
		final int ref_phone = -2;
		final boolean in_percent = false;
		
		final String CHARTTITLE = getAxisName(compare_pro)+" probabilities";
		final String XAXIS = getAxisName(compare_pro)+" ("+getAxisUnit(compare_pro)+")";
		String YAXIS = "Fraction of total "+getAxisName(result_pro);
		
		if(compare_pro == STILL)
			YAXIS += " remain static";
		else if(compare_pro == DISAPPEAR)
			YAXIS += " disconnect from WiFi";
			
		if(ref_phone > 0){
			YAXIS += " with respect to phone "+ref_phone;
		} else if(ref_phone == -1){
			YAXIS += " with respect to maximum value";
		} else if(ref_phone == -2){
			YAXIS += " with respect to minimum value";
		}
		
		if (in_percent && ref_phone !=0)
			YAXIS.replaceFirst("Fraction of", "% difference in");
		

		
		// Parse the string labels
		Labels[] labels = new Labels[slabels.length];
		for(int i=0; i<slabels.length; i++){
			labels[i] = new Labels();
			String[] s = slabels[i].split(":|<|>");
			if(slabels[i].contains(":")){
				labels[i].low = Float.parseFloat(s[0]);
				labels[i].low = processNumbers(STHETACHANGE, labels[i].low);
				labels[i].high = Float.parseFloat(s[1]);
				labels[i].high = processNumbers(STHETACHANGE, labels[i].high);
			}
			else if(slabels[i].contains("<")){
				labels[i].high = Float.parseFloat(s[1]);
				labels[i].high = processNumbers(STHETACHANGE, labels[i].high);
				labels[i].lorh = -1;
			} else if(slabels[i].contains(">")){
				labels[i].low = Float.parseFloat(s[1]);
				labels[i].high = processNumbers(STHETACHANGE, labels[i].high);
				labels[i].lorh = 1;
			}
//			System.out.println(labels[i].low+" "+labels[i].high);
		}
		
		final String[] filenames = {
				"C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/24th Sept ORDERED.csv",
				"C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/26th Sept ORDERED.csv"
		};
		


		
		CSVWriter writer = null;
		try {
			writer = new CSVWriter("src/Distribution/"+CHARTTITLE+".csv");
			writer.write(new String[]{CHARTTITLE});
			writer.write(new String[]{XAXIS});
			writer.write(new String[]{YAXIS});
			for(int i=0; i<slabels.length; i++)
				slabels[i] = slabels[i].replace(':','-');
			writer.write(slabels);
			writer.write(new String[]{"Phone 1: Business","Phone 2: Security", "Phone 3: Shopper", "Phone 4: Shopper","Phone 5: Business"});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		float[][] result = new float[filenames.length*5][slabels.length];
		for(int g=0; g<filenames.length; g++){
			ProbabilityList pl = new ProbabilityList(1, filenames[g]);
			for(int h=1; h<=5; h++){
				if(h>1)
					pl.changePhone(h);
				double denominator = pl.get(lower_bound,higher_bound,xstart, xend, ystart, yend, compare_pro, result_pro);
	//			double denominator2 = pl.get(step,100000,xstart, xend, ystart, yend, compare_pro, result_pro);
				double denominator2 = pl.getTotal(xstart, xend, ystart, yend, result_pro);
				System.out.println((float)(pl.getTotal(xstart, xend, ystart, yend,DIST) / pl.getTotal(xstart, xend, ystart, yend,TIME)));

//				float[] result = new float[slabels.length];
				for(int i=0; i<slabels.length; i++){
	
					if(labels[i].lorh == 0)
						result[g*5+h-1][i] = (float)pl.get(labels[i].low, labels[i].high, xstart, xend, ystart, yend, compare_pro, result_pro);
	
					else if(labels[i].lorh == -1)
						result[g*5+h-1][i] = (float)pl.get(Float.NEGATIVE_INFINITY, labels[i].high, xstart, xend, ystart, yend, compare_pro, result_pro);
					
					else if(labels[i].lorh == 1)
						result[g*5+h-1][i] = (float)pl.get(labels[i].low, Float.POSITIVE_INFINITY, xstart, xend, ystart, yend, compare_pro, result_pro);
						
					else 
						throw new IllegalArgumentException ("\"labels["+i+"].lorh\" must be -1 or 0 or 1");
					
					if (within_bounds)
						result[g*5+h-1][i] = result[g*5+h-1][i] /(float) denominator;
					else
						result[g*5+h-1][i] = result[g*5+h-1][i] /(float) denominator2;
					
				}
				
			}	
		}
		

		for(int i = 0; i<5; i++){
			for(int j = 0; j<result[0].length; j++){
				for(int k=1; k<filenames.length; k++){
					result[i][j] += result[k*5+i][j];
				}
				result[i][j] = result[i][j]/filenames.length;

			}
		}
		if(ref_phone!=0){
			for(int l = 0; l<result[0].length;l++){
				float reference;
				if(ref_phone == -1){
					float[] data = new float[result.length/filenames.length];
					for(int n = 0; n<result.length/filenames.length; n++){
						data[n] = result[n][l];
					}
					reference = data[getMax(data)];
				}else if(ref_phone == -2){
					float[] data = new float[result.length/filenames.length];
					for(int n = 0; n<result.length/filenames.length; n++){
						data[n] = result[n][l];
					}
					reference = data[getMin(data)];
				}else
					reference = result[ref_phone-1][l];
				
				for(int m=0; m<5; m++){
					result[m][l] = result[m][l]-reference;
					if (in_percent && ref_phone !=0)
						result[m][l] = 100*result[m][l]/reference;
					
				}
			}
		}
		try {
			for(int i = 0; i<result.length/filenames.length; i++)
				writer.write(result[i]);
			writer.finish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public static class Labels{

		public float low;
		public float high;
		// -1 if no lower limit, 1 if no higher limit, 0 otherwise
		public short lorh = 0;
	}
}