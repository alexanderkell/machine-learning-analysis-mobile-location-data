/**
 * 
 */
package Distribution;

import java.text.ParseException;
import java.util.ArrayList;

import Maths.DataFormatOperations;

/**
 *
 */
public class ProbabilityList {

	// variable secret codes
	private final static int POSITION = 10;	//X Y Z position
	private final static int MODSPD = 11;  // Time travelling at modulus speed
	private final static int MODACC = 12;  // Modulus acceleration
	private final static int STHETA = 13;  // Speed theta
	private final static int ATHETA = 14;  // Acceleration theta
	private final static int STILL = 15;	//Amount of time standing still
	private final static int DISAPPEAR = 16;  //Amount of time disappear from the tracking system
	
	private final static int TIME = 90; // Amount of time
	private final static int DIST = 91;	// Distance travelled
	private final static int NUMBER = 92; // Number of times occurring
	
	private DataFormatOperations dfo;
	private int length;	// length of the data
	
	/** Constructor to load the data
	 * 
	 * @param fn filename
	 * @param opt option
	 * @throws ParseException 
	 */
	public ProbabilityList(int opt, String fn) throws ParseException{
		dfo = new DataFormatOperations(opt, fn);
		length = dfo.getLength();
	}
	
	/** Constructor to load the data
	 * 
	 * @param fn filename
	 * @param opt option
	 * @throws ParseException 
	 */
	public ProbabilityList(String fn, int opt) throws ParseException{
		this(opt, fn);
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
				result += dfo.getDistanceBetween(indices.get(i));
			} else if(result_property == TIME){
				result += dfo.getTimeBetweenValue(indices.get(i));
			} else if(result_property == NUMBER){
				result++;
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
				value = dfo.getModSValue(indices.get(i));
			else if(compare_property == MODACC)
				value = dfo.getModAValue(indices.get(i));
			else if(compare_property == STHETA)
				value = dfo.getSThetaValue(indices.get(i));
			else if(compare_property == ATHETA)
				value = dfo.getAThetaValue(indices.get(i));
			else if(compare_property == STILL){
				if(dfo.isStandingStill(indices.get(i))){
					if(!was_still)
						value = 0;
					value += dfo.getTimeBetweenValue(indices.get(i));
					was_still = true;
					if(i<indices.size()-1)
						continue;
				} else {
					was_still = false;
				}
			}else if(compare_property == DISAPPEAR)
				value = dfo.getTimeBetweenValue(indices.get(i));
			else
				throw new IllegalArgumentException("You might have place the wrong arguments in \"compare_property\"");
			
			
			if( value >= low && value < high ){
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
	private ArrayList<Integer> getIndicesWithinRange(double xcoordinate, double ycoordinate, double radius, int property){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = new ArrayList<Integer>(); 
		
		// The starting index
		int start = getStartingIndex(property);
		
		for(int i=start; i<length; i++){
			// Calculate the distance between the user's point and the points in the data list
			double[] b = new double[]{	xcoordinate, ycoordinate, 0	};
			
			if(getLengthBetween(dfo.getXYZValue(i),b) <= radius){
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
	private ArrayList<Integer> getIndicesWithinRange(double xstart, double xend, double ystart, double yend, int property){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = new ArrayList<Integer>(); 
		
		// The starting index
		int start = getStartingIndex(property);

		boolean match = false;
		for(int i=start; i<length; i++){
			// Calculate the distance between the user's point and the points in the data list
			double[] point = dfo.getXYZValue(i);
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
	private int getStartingIndex(final int property){
		// If it is related to speed or difference in positions or time between the points, the starting index is 1
		if(property == MODSPD || property == STHETA || property == STILL ||
				 property == DISAPPEAR || property == TIME || property == DIST)
			return 1;
		
		// If it is related to accleration or difference in speeds, the starting index is 2
		else if(property == MODACC || property == ATHETA)
			return 2;
		
		// It is is related to positions, the starting index is 0
		else 
			return 0;
	}
	
	
	public double getMaxSpeed(){
		double max_speed = dfo.getSThetaValue(2);
		for(int i=2; i<length; i++){
			if(max_speed < dfo.getSThetaValue(i)){
				max_speed = dfo.getSThetaValue(i);
			}
		}
		return max_speed;
	}
	public double getMinSpeed(){
		
		double min_speed = dfo.getSThetaValue(2);
		for(int i=2; i<length; i++){
			if(min_speed > dfo.getSThetaValue(i)){
				min_speed = dfo.getSThetaValue(i);
			}
		}
		return min_speed;
	}
	
	public double getSpeed(double low, double high){
		
		int count = 0;
		for(int i=2; i<length; i++){
			if(dfo.getModSValue(i) >= low && dfo.getModSValue(i)<high){
				count++;
			}
		}
		return count;
	}
	
	
	// Ignore the following
/*	public double getMintb(){
		int position = 0;
		double max_speed = dfo.getSThetaValue(2);
		for(int i=2; i<length; i++){
			if(max_speed < dfo.getSThetaValue(i)){
				max_speed = dfo.getSThetaValue(i);
				position = i;
			}
		}

		double[] speeds = dfo.getXYZSpeedValue(position);
		System.out.println("Speeds are "+speeds[0]+" "+speeds[1]+" "+speeds[2]);
		System.out.println("Position is "+position);
		System.out.println("Time is "+dfo.getDateTimeString(position));
		return max_speed;
	}
*/	/** Test bench
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		

		float step = 60f;
		double xstart = 200, xend = 900, ystart = 0, yend = 375;
		int compare_pro = DISAPPEAR;
		int result_pro = NUMBER;
		
		for(int j=1; j<=5; j++){
		ProbabilityList pl = new ProbabilityList(j, "C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/24th Sept ORDERED.csv");
		double denominator = pl.get(60, 960, xstart, xend, ystart, yend, compare_pro, result_pro);
		double denominator2 = pl.get(60,10000,xstart, xend, ystart, yend, compare_pro, result_pro);
//		System.out.println((int) denominator);

		for(int i=1; i<16; i++){
	//		if(i==-1)
	//			System.out.print((float)pl.get(0, 0.01, xstart, xend, ystart, yend, compare_pro, result_pro)/(float)denominator+" ");
			if(i==0)
				System.out.print((float) pl.get(1,step, xstart, xend, ystart, yend, compare_pro, result_pro)/ (float)denominator2+" ");
			else
				System.out.print((float)pl.get(i*step, i*step+step, xstart, xend, ystart, yend, compare_pro, result_pro)/(float)denominator2+" ");

		}
		System.out.print((float)(1-denominator/denominator2));
		System.out.print(";\n");
		}
//		System.out.println("Total: "+pl.getSThetaProbability(0,20*step, xstart, xend, ystart, yend)+" ");

//		System.out.println("\n"+pl.getStillProbability(30, 40, 0, 1100, 0, 375)+" ");

/*		for(int i=-1; i<10; i++){
			if(i==-1)
				System.out.print(pl.getSThetaProbability(0, Math.PI/2+0.1, 0, 1100, 0, 375)+" ");
			else if(i==0)
				System.out.print(pl.getSThetaProbability(-Math.PI/2,0, 0, 1100, 0, 375)+" ");
			else
				System.out.print(pl.getSThetaProbability(i*20, i*20+20, 0, 1100, 0, 375)+" ");

		}
		System.out.println("\n"+pl.getSThetaProbability(0, 200, 0, 1100, 0, 375)+" ");
*/
		
//		System.out.print("\nMin Speed: "+pl.getMinSpeed()+"\t");
//		System.out.print("Max Speed: "+pl.getMaxSpeed());
//		System.out.print("Max Speed: "+pl.getMintb());
		
//		System.out.println("Average Speed: "+pl.getTotal(xstart,xend,ystart,yend, DIST) / pl.getTotal(xstart,xend,ystart,yend, TIME));

	}
}