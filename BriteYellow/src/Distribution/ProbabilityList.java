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
	private final static int POSITION = 0;	//X Y Z position
	private final static int MODSPDTIME = 10;  // Time travelling at modulus speed
	private final static int MODSPDDIST = 11;	//Distance travelling at modulus speed
	private final static int MODACC = 20;  // Modulus acceleration
	private final static int STHETA = 30;  // Speed theta
	private final static int ATHETA = 40;  // Acceleration theta
	private final static int STILLTIME = 50;	//Amount of time standing still
	private final static int STILLNO = 51;	//No. of times standing still
	private final static int DISAPPEARTIME = 60;
	private final static int DISAPPEARNO = 61;
	
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
	
	
	/** Get speed probabilities
	 *  for speeds from and including "low" to "high" but not including "high" 
	 *  lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @return the probability
	 */
	public float getModSpdDistPro(double low, double high){
		ArrayList<Integer> indices= new ArrayList<Integer>();
		for(int i=1; i<length; i++)
			indices.add(i);
		
		return getProbability(low, high, indices, MODSPDDIST);
	}
	
	/** Get speed probabilities 
	 *  lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @return the probability
	 */
	public float getModSpdDistPro(double low, double high, double xcoordinate, double ycoordinate){	
		return getModSpdDistPro(low, high, xcoordinate, ycoordinate, 0);
	}
	
	/** Get speed probabilities for position within a certain area which user defines
	 *  Lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @param xcoordinate	X Center of the area with user specifies
	 * @param ycoordinate   Y Center of the area with user specifies
	 * @param radius  Radius of the area
	 * @return the probability
	 */
	public float getModSpdDistPro(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius,MODSPDTIME);
		
		return getProbability(low, high, indices, MODSPDDIST);
	}
	
	/** Get speed probabilities for position within a certain rectangular area which user wants 
	 *  Lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @param xstart Start of the rectangular area - x side
	 * @param xend	End of the rectangular area - x side
	 * @param ystart	Start of the rectangular area - y side
	 * @param yend	End of the rectangular area - y side
	 * @return the probability
	 */
	public float getModSpdDistPro(double low, double high, double xstart, double xend, double ystart, double yend){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, MODSPDTIME);
		
		return getProbability(low, high, indices, MODSPDDIST);
	}
	
	
	
	/** Get speed probabilities
	 *  for speeds from and including "low" to "high" but not including "high" 
	 *  lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @return the probability
	 */
	public float getModSpdTimePro(double low, double high){
		ArrayList<Integer> indices= new ArrayList<Integer>();
		for(int i=1; i<length; i++)
			indices.add(i);
		
		return getProbability(low, high, indices, MODSPDTIME);
	}
	
	/** Get speed probabilities 
	 *  lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @return the probability
	 */
	public float getModSpdTimePro(double low, double high, double xcoordinate, double ycoordinate){	
		return getModSpdTimePro(low, high, xcoordinate, ycoordinate, 0);
	}
	
	/** Get speed probabilities for position within a certain area which user wants 
	 *  Lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @param xcoordinate	X Center of the area with user specifies
	 * @param ycoordinate   Y Center of the area with user specifies
	 * @param radius  Radius of the area
	 * @return the probability
	 */
	public float getModSpdTimePro(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius,MODSPDTIME);
		
		return getProbability(low, high, indices, MODSPDTIME);
	}
	
	/** Get speed probabilities for position within a certain rectangular area which user wants 
	 *  Lower modulus speed must be lower than higher modulus speed or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower modulus speed 
	 * @param high  Higher modulus speed
	 * @param xstart Start of the rectangular area - x side
	 * @param xend	End of the rectangular area - x side
	 * @param ystart	Start of the rectangular area - y side
	 * @param yend	End of the rectangular area - y side
	 * @return the probability
	 */
	public float getModSpdTimePro(double low, double high, double xstart, double xend, double ystart, double yend){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, MODSPDTIME);
		
		return getProbability(low, high, indices, MODSPDTIME);
	}
	
	/** Get acceleration probabilities
	 *  for accelerations from and including "low" to "high" but not including "high" 
	 *  lower modulus acceleration must be lower than higher modulus acceleration or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus acceleration 
	 * @param high  Higher modulus acceleration
	 * @return the probability
	 */
	public float getModAccProbability(double low, double high){
		ArrayList<Integer> indices= new ArrayList<Integer>();
		for(int i=2; i<length; i++)
			indices.add(i);
		
		return getProbability(low, high, indices, MODACC);
	}
	
	/** Get Acceleration probabilities 
	 *  lower modulus acceleration must be lower than higher modulus acceleration or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus acceleration 
	 * @param high  Higher modulus acceleration
	 * @return the probability
	 */
	public float getModAccProbability(double low, double high, double xcoordinate, double ycoordinate){	
		return getModAccProbability(low, high, xcoordinate, ycoordinate, 0);
	}
	
	/** Get acceleration probabilities for position within a certain area which user wants 
	 *  Lower modulus acceleration must be lower than higher modulus acceleration or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower modulus acceleration 
	 * @param high  Higher modulus acceleration
	 * @param xcoordinate	X Center of the area with user specifies
	 * @param ycoordinate   Y Center of the area with user specifies
	 * @param radius  Radius of the area
	 * @return the probability
	 */
	public float getModAccProbability(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius, MODACC);
		
		return getProbability(low, high, indices, MODACC);
	}
	
	/** Get acceleration probabilities for position within a certain rectangular area which user wants 
	 *  Lower modulus acceleration must be lower than higher modulus acceleration or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower modulus acceleration 
	 * @param high  Higher modulus acceleration
	 * @param xstart Start of the rectangular area - x side
	 * @param xend	End of the rectangular area - x side
	 * @param ystart	Start of the rectangular area - y side
	 * @param yend	End of the rectangular area - y side
	 * @return the probability
	 */
	public float getModAccProbability(double low, double high, double xstart, double xend, double ystart, double yend){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, MODACC);
		
		return getProbability(low, high, indices, MODACC);
	}
	
	/** Get speed theta probabilities
	 *  for speed thetas from and including "low" to "high" but not including "high" 
	 *  lower modulus speed theta must be lower than higher speed theta or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower speed theta 
	 * @param high  Higher speed theta
	 * @return the probability
	 */
	public float getSThetaProbability(double low, double high){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i=1; i<length; i++)
			indices.add(i);
		
		return getProbability(low, high, indices, STHETA);
	}
	
	/** Get speed theta probabilities 
	 *  lower modulus speed theta must be lower than higher speed theta or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower speed theta 
	 * @param high  Higher speed theta
	 * @return the probability
	 */
	public float getSThetaProbability(double low, double high, double xcoordinate, double ycoordinate){	
		return getSThetaProbability(low, high, xcoordinate, ycoordinate, 0);
	}
	
	/** Get speed theta probabilities for position within a certain area which user wants 
	 *  Lower modulus speed theta must be lower than higher modulus speed theta or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower speed theta 
	 * @param high  Higher speed theta
	 * @param xcoordinate	X Center of the area with user specifies
	 * @param ycoordinate   Y Center of the area with user specifies
	 * @param radius  Radius of the area
	 * @return the probability
	 */
	public float getSThetaProbability(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius, STHETA);
		
		return getProbability(low, high, indices, STHETA);
	}
	
	/** Get speed theta probabilities for position within a certain rectangular area which user wants 
	 *  Lower modulus speed theta must be lower than higher modulus speed theta or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower speed theta 
	 * @param high  Higher speed theta
	 * @param xstart Start of the rectangular area - x side
	 * @param xend	End of the rectangular area - x side
	 * @param ystart	Start of the rectangular area - y side
	 * @param yend	End of the rectangular area - y side
	 * @return the probability
	 */
	public float getSThetaProbability(double low, double high, double xstart, double xend, double ystart, double yend){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, STHETA);
		
		return getProbability(low, high, indices, STHETA);
	}
	
	
	/** Get acceleration probabilities
	 *  for accelerations from and including "low" to "high" but not including "high" 
	 *  lower modulus acceleration must be lower than higher modulus acceleration or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus acceleration 
	 * @param high  Higher modulus acceleration
	 * @return the probability
	 */
	public float getAThetaProbability(double low, double high){
		ArrayList<Integer> indices= new ArrayList<Integer>();
		for(int i=2; i<length; i++)
			indices.add(i);
		
		return getProbability(low, high, indices, ATHETA);
	}
	
	/** Get Acceleration probabilities 
	 *  lower modulus acceleration must be lower than higher modulus acceleration or an Illegal Argument Exception
	 *  will be thrown
	 * @param low  Lower modulus acceleration 
	 * @param high  Higher modulus acceleration
	 * @return the probability
	 */
	public float getAThetaProbability(double low, double high, double xcoordinate, double ycoordinate){	
		return getAThetaProbability(low, high, xcoordinate, ycoordinate, 0);
	}
	
	/** Get acceleration theta probabilities for position within a certain area which user wants 
	 *  Lower modulus acceleration theta must be lower than higher acceleration theta or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower acceleration theta 
	 * @param high  Higher acceleration theta
	 * @param xcoordinate	X Center of the area with user specifies
	 * @param ycoordinate   Y Center of the area with user specifies
	 * @param radius  Radius of the area
	 * @return the probability
	 */
	public float getAThetaProbability(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius, ATHETA);
		
		return getProbability(low, high, indices, ATHETA);
	}
	
	/** Get acceleration theta probabilities for position within a certain rectangular area which user wants 
	 *  Lower acceleration theta must be lower than higher acceleration theta or an Illegal Argument Exception
	 *  will be thrown
	 * 
	 * @param low  Lower acceleration theta 
	 * @param high  Higher acceleration theta
	 * @param xstart Start of the rectangular area - x side
	 * @param xend	End of the rectangular area - x side
	 * @param ystart	Start of the rectangular area - y side
	 * @param yend	End of the rectangular area - y side
	 * @return the probability
	 */
	public float getAThetaProbability(double low, double high, double xstart, double xend, double ystart, double yend){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, ATHETA);
		
		return getProbability(low, high, indices, ATHETA);
	}
	
	
	public float getStillNoProbability(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius, STILLTIME);
		
		return getStillProbability(low, high, indices, STILLNO);
	}
	public float getStillNoProbability(double low, double high, double xstart, double xend, double ystart, double yend){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, STILLTIME);
		return getStillProbability(low, high, indices, STILLNO);
	}
	
	
	public float getStillTimeProbability(double low, double high, double xcoordinate, double ycoordinate, double radius){
		// ArrayList for storing the indices of the positions which match the criteria
		ArrayList<Integer> indices = getIndicesWithinRange(xcoordinate, ycoordinate, radius, STILLTIME);
		
		return getStillProbability(low, high, indices, STILLTIME);
	}
	public float getStillTimeProbability(double low, double high, double xstart, double xend, double ystart, double yend){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, STILLTIME);
		return getStillProbability(low, high, indices, STILLTIME);
	}
	
	private float getStillProbability(double low, double high, ArrayList<Integer> indices, final int property){
		if( !(property == STILLTIME || property == STILLNO) )
			throw new IllegalArgumentException("You are probably using the wrong method! ((getStillProbably(low, high, indices, property)");
		if (low >= high)
			throw new IllegalArgumentException(
				"Lower limit \"low\" must be smaller than the higher limit \"high\""
				);
		
		double matchtime = 0;	// time in seconds which match the acceleration limits
		double totaltime = 0;	// total time in seconds
		boolean was_still = false;	// was moving or not
		double value = 0;
		int matchno = 0;
		int totalno = 0;
		for(int i=0; i<indices.size(); i++){
			// If the time between the recorded positions is greater than 20 seconds
			// it will be ignored because it would mostly be inaccurate
			if(dfo.getTimeBetweenValue(indices.get(i))>20)
				continue;
			if(dfo.isStandingStill(indices.get(i))){
				value += dfo.getTimeBetweenValue(indices.get(i));
				was_still = true;
			} else {
				was_still = false;
			}
			
			if((!was_still && value >0 )|| (was_still && i==indices.size()-1)){
//				if(value>3){
					if(value>=low && value<high){
						matchtime += value;
						matchno ++;
					} 
					totaltime += value;
					totalno ++;
//				}
				// reset the value
				value = 0;
			}
			
		}
		
		if(property == STILLNO)
			return (float) matchno / (float) totalno;
		else if(property == STILLTIME)
			return (float)matchtime / (float)totaltime;
		else throw new IllegalArgumentException();
	}
	
	
	public float getDisappearTimePro(double low, double high){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i=1; i<length; i++)
			indices.add(i);
		
		return getDisappearPro(low, high, indices, DISAPPEARTIME);
	}
	
	public float getDisappearTimePro(double low, double high, double xstart, double xend, double ystart, double yend){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, DISAPPEARTIME);
		
		return getDisappearPro(low, high, indices, DISAPPEARTIME);
	}
	
	public float getDisappearNoPro(double low, double high){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i=1; i<length; i++)
			indices.add(i);
		
		return getDisappearPro(low, high, indices, DISAPPEARNO);
	}
	
	public float getDisappearNoPro(double low, double high, double xstart, double xend, double ystart, double yend){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, DISAPPEARNO);
		
		return getDisappearPro(low, high, indices, DISAPPEARNO);
	}
	
	private float getDisappearPro(double low, double high, ArrayList<Integer> indices, int property){
		double matchtime = 0;
		double totaltime = 0;
		
		double matchno = 0;
		double totalno = 0;
		for(int i=1; i<indices.size(); i++){
			if(dfo.getTimeBetweenValue(i)>=low && dfo.getTimeBetweenValue(i)<high){
				matchtime += dfo.getTimeBetweenValue(i);
				matchno++;
			}
			totaltime += dfo.getTimeBetweenValue(i);
			totalno++;
		}
		
		if(property == DISAPPEARTIME)
			return (float)matchtime / (float)totaltime;
		else if(property == DISAPPEARNO)
			return (float)matchno / (float)totalno;
		else throw new IllegalArgumentException("You are probably using the wrong method! (getDisappearPro(low, high, indices, property))");
	}

	private double getAverageSpeed(ArrayList<Integer> indices){
		double distance = 0;
		double time = 0;
		for(int i=1; i<indices.size(); i++){

			distance += dfo.getModSValue(indices.get(i));
			time += dfo.getTimeBetweenValue(indices.get(i));
			
		}
		return distance / time;
	}
	public double getAverageSpeed(){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i=1; i<length; i++)
			indices.add(i);
		return getAverageSpeed(indices);
	}
	public double getAverageSpeed(double xstart, double xend, double ystart, double yend){
		ArrayList<Integer> indices = getIndicesWithinRange(xstart, xend, ystart, yend, MODSPDTIME);
		return getAverageSpeed(indices);
	}
	
	private float getProbability(double low, double high, ArrayList<Integer> indices, final int property){
		if (low >= high)
			throw new IllegalArgumentException(
				"Lower limit \"low\" must be smaller than the higher limit \"high\""
				);
		
		// get modulus acceleration value for each
//		int times = 0;	//times appeared
		double matchtime = 0;	// time in seconds which match the limits
		double totaltime = 0;	// total time in seconds
		
		double matchdist = 0;	// distance match the limits
		double totaldist = 0;	// total distance
		double value = 0;
		
		for(int i=0; i<indices.size(); i++){
			if(dfo.getTimeBetweenValue(indices.get(i))>20)
				continue;
			
			if(property == MODSPDTIME || property == MODSPDDIST)
				value = dfo.getModSValue(indices.get(i));
			else if(property == MODACC)
				value = dfo.getModAValue(indices.get(i));
			else if(property == STHETA)
				value = dfo.getSThetaValue(indices.get(i));
			else if(property == ATHETA)
				value = dfo.getAThetaValue(indices.get(i));
			else if(property == STILLTIME || property == STILLNO)
				throw new IllegalArgumentException("You are probably using the wrong method! (getProbability(low, high, indices, property))");
			else
				throw new IllegalArgumentException("Wrong property argument");
			
			// If the modulus speed is in between the 2 limits
			if( ((property == MODSPDTIME || property == MODSPDDIST) && value <100) || !(property == MODSPDTIME || property == MODSPDDIST) ){
				if(value >= low && value <high){
	//				times++;
					matchdist += dfo.getModSValue(indices.get(i));
					matchtime += dfo.getTimeBetweenValue(indices.get(i));
				}
				totaldist += dfo.getModSValue(indices.get(i));
				totaltime += dfo.getTimeBetweenValue(indices.get(i));
			}
		}
		
		if(property == MODSPDTIME || property == MODACC || property == STHETA || property == ATHETA)
			return (float)matchtime / (float)totaltime;
		else if (property == MODSPDDIST)
			return (float)matchdist / (float)totaldist;
		else throw new IllegalArgumentException();
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

		
		for(int i=start; i<length; i++){
			// Calculate the distance between the user's point and the points in the data list
			double[] point = dfo.getXYZValue(i);
			if(point[0]>=xstart && point[0]<=xend && point[1]>=ystart && point[1]<=yend)
				indices.add(i);
			
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
		if(property == MODSPDTIME || property == STHETA || property == STILLTIME ||
				property == STILLNO || property == DISAPPEARTIME || property == DISAPPEARNO)
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
		ProbabilityList pl = new ProbabilityList(4, "C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/26th Sept ORDERED.csv");
		
		float step = 60f;
		double xstart = 122, xend = 1000, ystart = 0, yend = 375;
		for(int i=0; i<16; i++){
//			if(i==-1)
//				System.out.print(pl.getDisappearNoPro(0, 0.01, 122, 1000, 0, 375)+" ");
			if(i==0)
				System.out.print(pl.getDisappearNoPro(1,step, xstart, xend, ystart, yend) / pl.getDisappearNoPro(step,20*step, xstart, xend, ystart, yend)+" ");
			else
				System.out.print(pl.getDisappearNoPro(i*step, i*step+step, xstart, xend, ystart, yend) / pl.getDisappearNoPro(step,20*step, xstart, xend, ystart, yend)+" ");

		}
		System.out.println("\n>16: "+ (1-pl.getDisappearNoPro(0,16*step, xstart, xend, ystart, yend)));

		System.out.println("Total: "+pl.getDisappearNoPro(0,20*step, xstart, xend, ystart, yend)+" ");

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
		
		System.out.print("\nMin Speed: "+pl.getMinSpeed()+"\t");
		System.out.print("Max Speed: "+pl.getMaxSpeed());
//		System.out.print("Max Speed: "+pl.getMintb());
		
		System.out.println("Average Speed: "+pl.getAverageSpeed(120,1010,0,375));

	}
}