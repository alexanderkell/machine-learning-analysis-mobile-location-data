package distribution;

import java.text.ParseException;
import java.util.ArrayList;

import maths.DataGetter;

import objects.PhoneData;

public class StatsGenerator extends DataGetter{

	public final static int PATH_LENGTH = 100;
	public final static int SHORTEST_PATH_LENGTH = 101;
	public final static int TIME_STOPPED = 110;
	public final static int NO_STOPS = 111;
	
	public final static int TIME_SPENT = 120;
	public final static int INACTIVE_TIME = 121;
	public final static int STHETACHANGE = 130;
	public final static int STHETAIN = 131;
	public final static int STHETAOUT = 132;
	public final static int STHETAINOUT = 133;	//STEHTAOUT - STHETAIN
	public final static int STHETACHANGE_NO = 134;	// No. of times the person has changed specific angles
	public final static int AVERAGE_SPEED = 140;	// PATH_LENGTH / TIME_SPENT
	public final static int AVERAGE_ACCELERATION = 150;
	
	public final static int TIME_PER_STOP = 210;
	public final static int FREQ_IN_AREA = 250;
	public final static int PATH_PER_SHORTEST_PATH = 260;	// PATH_LENGTH / SHORTEST_PATH_LENGTH
	public final static int TIME_PER_SHORTEST_PATH = 270;	// TIME_SPENT / SHORTEST_PATH_LENGTH
	
	final static int XSTILL = 30;	//Max x distance for determining whether
									// the person is staying still
	final static int YSTILL = 6;	//Max y distance for determining whether
									// the person is staying still
	private int length;
	
	private static int xdirection = 1;	// +1 = moving to right, -1 = moving to left
	private static int ydirection = 1; // +1 = moving up, -1 = moving down
	private static int oldxdirection = 1;
	private static int oldydirection = 1;
	
	public static void main(String args[]) throws ParseException{
		// Extract from real data
		//																					18				22									31
		int[] x = {266,262,255,249,245,242,239,236,234,232,231,230,228,227,226,225,223,222,220,223,231,240,243,242,239,237,236,234,229,228,228,224,212,200, 188};
		int[] y = {318,316,313,310,308,307,306,304,303,302,301,301,300,299,298,298,298,297,296,297,299,301,302,301,301,300,299,299,299,299,299,299,299,298, 297};

		
		PhoneData[] pd = new PhoneData[x.length];
		for(int i = 0 ; i < x.length; i++){
			pd[i] = new PhoneData();
			pd[i].x = x[i];
			pd[i].y = y[i];
		}
		ArrayList<Integer> result = StatsGenerator.processStoodStill(pd);
		System.out.print("StoodStillindices:\n");
		for(int i = 0 ; i < result.size(); i++){
			System.out.print(result.get(i)+"  ");
		}
		
		
	}

	/**Find out which points in the track are stood still
	 * 
	 * @param pd The track in PhoneData[]
	 * @return the indices of the points that are stood still in ArrayList<Integer> format 
	 */
	public static ArrayList<Integer> processStoodStill(PhoneData[] pd){
		
		ArrayList<Integer> t_points = new ArrayList<Integer>();

//		PhoneData[] pd = getFullPhoneData();
		
		int index = 0;
		
			// Find all turning points
			while(index < pd.length){
					
				int temp = findnextturningpoint(pd,index);
				// If there are no more turning points (temp = -1), exit the loop 
				if(temp == -1){
					break;
				} 
				// Otherwise store the turning point 
				else {
					t_points.add(temp);
					index = temp+1;
				}
			}
			
			ArrayList<Integer> store = new ArrayList<Integer>();
			
			try{
				int i = 0;
				while(i < t_points.size()){
					// 1st turning point index
					int tp1 = t_points.get(i);	// i = initial index
					// 2nd turning point index
					int tp2 = t_points.get(++i);	// i = initial index + 1
					// Check whether 2nd turning point is close 1st turning point
					if(isWithin(pd[tp1],pd[tp2])){
						int j = tp1;
						for(; j<=tp2; j++)
							store.add(j);
						// Now, j = tp2+1;
						// All consecutive points are considered standing still and are added to the result
						// if they are close to 1st turning point
						i++; // i = initial index + 2
						while(j<pd.length && isWithin(pd[tp1],pd[j])){
							store.add(j);
							// If the current point j has passed another turning point, increment i (switch i
							// to next turning point)
							if(i<t_points.size() && j > t_points.get(i)){
								i++;
							}
//							System.out.println("tp1 is "+j);
							j++;
							
						}
						
					}
					
				}
			}catch (IndexOutOfBoundsException e){
				// If the loop reaches here, this means that the last turning point or last point is reached
				// This is completely normal
			}
		return store;
	}
	
	public static boolean isWithin(PhoneData pd1, PhoneData pd2){
		double x1 = pd1.x;
		double x2 = pd2.x;
		double y1 = pd1.y;
		double y2 = pd2.y;
		return Math.abs(x2-x1)<=XSTILL && Math.abs(y2-y1)<=YSTILL;
	}
	public static boolean isWithin(PhoneData[] pd, int point1_index, int point2_index){
		return isWithin(pd[point1_index], pd[point2_index]);
	}
	public static int findnextturningpoint(PhoneData[] pd, int current_index){
		for(int i=current_index; i<pd.length-1; i++){
			// Find 1st turning point
			double x1 = pd[i].x;
			double x2 = pd[i+1].x;
			double y1 = pd[i].y;
			double y2 = pd[i+1].y;
//			System.out.println(x1+" "+x2+" "+y1+" "+y2);
			// Update direction
			updateDirection(x1,x2,y1,y2);

			
			if(isDirectionChanged()){
				return i;
			}
		}
		return -1;
	}
		
	
	public static void updateDirection(double xstart, double xend, double ystart, double yend){
		// Store last direction
		oldxdirection = xdirection;
		oldydirection = ydirection;
		if(xend>xstart)
			xdirection = 1;
		else if(xend<xstart)
			xdirection = -1;
		
		if(yend>ystart)
			ydirection = 1;
		else if(yend<ystart)
			ydirection = -1;
//		System.out.println(oldxdirection+" "+xdirection+" "+oldydirection+" "+ydirection);
	}
	/** Return if the x y directions are changed 
	 * 
	 * @param xstart start point x
	 * @param xend end point x
	 * @param ystart start point y
	 * @param yend end point y
	 * @return true if at least 1 direction is changed, false if no directions are changed
	 */
	public static boolean isDirectionChanged(){
		return oldxdirection != xdirection || oldydirection != ydirection;
	}
	
	
	public StatsGenerator(PhoneData[] phonedata) throws ParseException {
		// TODO Auto-generated constructor stub
		super(phonedata);
		length = getLength();
	}
	public StatsGenerator(ArrayList<PhoneData> phonelist) throws ParseException{
		this(phonelist.toArray(new PhoneData[phonelist.size()]));
	}

	/**Get the total <property> in the track
	 * 
	 * @param property
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @return
	 */
	public double getTotalAverage(int property, double xstart, double xend, double ystart, double yend){
		if(property == TIME_PER_STOP){
			// Get the number of stops
			double stops = getTotalAverage(NO_STOPS, xstart, xend, ystart, yend);
			if(stops == 0)
				return 0;
			// Get and return time_stopped / no_of_stops
			return getTotalAverage(TIME_STOPPED, xstart, xend, ystart, yend) /
					stops;
			
		} else if(property == FREQ_IN_AREA){
			// Return the length of the resultant array
			try{
				return get(TIME_SPENT, xstart, xend, ystart, yend).length;
			} catch (NullPointerException e){
				return 0;
			}
			
		} else if(property == AVERAGE_SPEED){
			// Get the time_spent
			double time = getTotalAverage(TIME_SPENT, xstart, xend, ystart, yend);
			if(time == 0)
				return 0;
			// Get and return path_length / time_spent
			return getTotalAverage(PATH_LENGTH, xstart, xend, ystart, yend) /
					time;
		} else if(property == PATH_PER_SHORTEST_PATH || property == TIME_PER_SHORTEST_PATH){
			// Get the shortest_path
			double shortest_path = getTotalAverage(SHORTEST_PATH_LENGTH, xstart, xend, ystart, yend);
			if(shortest_path == 0)
				return 0;
			// Get and return either path_length / shortest_path or time_spent / shortest_path
			if(property == PATH_PER_SHORTEST_PATH)
				return getTotalAverage(PATH_LENGTH, xstart, xend, ystart, yend) / 
						shortest_path;
			else
				return getTotalAverage(TIME_SPENT, xstart, xend, ystart, yend) / 
						shortest_path;
			
			
		} else if(property == PATH_LENGTH || property == TIME_SPENT || property == INACTIVE_TIME ||
				property == NO_STOPS || property == TIME_STOPPED || property == STHETACHANGE ||
				property == SHORTEST_PATH_LENGTH){
			double[] p1 = get(property, xstart, xend, ystart, yend);
			double p1total = 0;
			
			try{
				for(int i = 0; i < p1.length; i++){
					p1total += p1[i];
				}
				return p1total;
			}catch(NullPointerException e){
				return 0;
			}
		}
		throw new IllegalArgumentException("You might have used the wrong method");
	}

	/**Get the total frequency or time occurred for a property at value between low and high for a particular track
	 * 
	 * @param property
	 * @param low
	 * @param high
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @return
	 */
	public double getTotalFreqAt(int property, double low, double high, double xstart, double xend, double ystart, double yend){
		double[] p1 = getFreqAt(property, low, high, xstart, xend, ystart, yend);
		double p1total = 0;
		
		// If p1 is null (might be because the person has not entered the area, return 0
		if(p1 == null)
			return 0;
		
		for(int i = 0; i < p1.length; i++){
			p1total += p1[i];
		}
		return p1total;
	}
	
	/**Get the frequency or time occurred for a property at value between low and high
	 * The size of the array represents the number of times the person has walked pass the specified area
	 *
	 * @param property
	 * @param low
	 * @param high
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @return
	 */
	public double[] getFreqAt(int property, double low, double high, double xstart, double xend, double ystart, double yend){
		ArrayList<Double> al = new ArrayList<Double>();
		
		boolean is_in = false;	// Show whether the ending point of the path segment is in the wanted area
		boolean was_in = false; // Show whether the ending point of the previous path segment was in the wanted area
		double result = 0;	// variable for storing the results of property
		final int starting_index = 2;
		for(int i=starting_index; i<length; i++){
			double x1 = getXYZValue(i-1)[0];
			double y1 = getXYZValue(i-1)[1];
			double x2 = getXYZValue(i)[0];
			double y2 = getXYZValue(i)[1];
			
			
			double fraction = getIntersectFraction(x1,x2,y1,y2, xstart,xend,ystart,yend);
			
			//Need to know if the starting point of the 1st path segment is inside the area
			if(i == starting_index){
				// If the ending point of the last path segment (or starting point of current
				// path segment) is inside the area the following function will return 1.0
				is_in = (getIntersectFraction(x1,x1,y1,y1, xstart,xend,ystart,yend) == 1.0);
				was_in = is_in;
			}
			
			if(fraction > 0){
				if(property == AVERAGE_SPEED){
					double speed = getModSValue(i);
					if(speed>=low && speed<high){
						result += getTimeBetweenValue(i);
					}
				} else if(property == AVERAGE_ACCELERATION){
					double speed = getModSValue(i);
					if(speed>=low && speed<high){
						result += getTimeBetweenValue(i);
					}
				} else if(property == STHETACHANGE){
					double angle = getSThetaChange(i);
					if(angle>=low && angle<high){
						result += getTimeBetweenValue(i);
					}
				} else if(property == STHETACHANGE_NO){
					double angle = getSThetaChange(i);
					if(angle>=low && angle<high){
						result ++;
					}
				} else
					throw new IllegalArgumentException("You might have used the wrong method");
			}
				
			was_in = is_in;
			// Check whether the ending point of the current path segment is inside the area
			is_in = (getIntersectFraction(x2,x2,y2,y2, xstart,xend,ystart,yend) == 1.0);
			
			// Record the result if the person has walked out of the area
			if(!is_in && was_in){
				al.add(result);
				
				// reset the result
				result = 0;
			}
			
		}
		// If the last point of the path is still inside the area, record the results
		if(is_in){
			al.add(result);
			result = 0;
		}
		
		// Return the results
		if(al.size() == 0)
			return null;
		double[] results = new double[al.size()];
		for(int i=0; i<al.size(); i++)
			results[i] = al.get(i);
		
		return results;
	}
	
	/**Get the <property> in the track
	 * The size of the array represents the number of times the person has walked pass the specified area
	 * 
	 * @param property
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @return
	 */
	public double[] get(int property, double xstart, double xend, double ystart, double yend){
		
		
		if(property == AVERAGE_SPEED){
			double[] p1 = get(PATH_LENGTH, xstart, xend, ystart, yend);
			if(p1 == null)
				return null;
			double[] p2 = get(TIME_SPENT, xstart, xend, ystart, yend);
			double[] presult = new double[p1.length];
			for(int i=0; i<presult.length; i++)
				presult[i] = p2[i]==0 ? 0 : p1[i] / p2[i];
	
			return presult;
		}
		if(property == STHETAINOUT){
			double[] p1 = get(STHETAOUT, xstart, xend, ystart, yend);
			if(p1 == null)
				return null;
			double[] p2 = get(STHETAIN, xstart, xend, ystart, yend);
			double[] presult = new double[p1.length];
			for(int i=0; i<presult.length; i++)
				presult[i] = p1[i] - p2[i];
	
			return presult;
		}
		if(property == TIME_PER_STOP){
			double[] p1 = get(TIME_STOPPED, xstart, xend, ystart, yend);
			if(p1 == null)
				return null;
			double[] p2 = get(NO_STOPS, xstart, xend, ystart, yend);
			double[] presult = new double[p1.length];
			for(int i=0; i<presult.length; i++)
				presult[i] = p2[i]==0 ? 0 : p1[i] / p2[i];
	
			return presult;
		}
		
		ArrayList<Integer> stopsindex = null;
		if(property == TIME_STOPPED || property == NO_STOPS){
			stopsindex = processStoodStill(getFullPhoneData());
		}
		ArrayList<Double> al = new ArrayList<Double>();
		
		boolean is_in = false;	// Show whether the ending point of the path segment is in the wanted area
		boolean was_in = false; // Show whether the ending point of the previous path segment was in the wanted area
		boolean was_still = false; //Show whether the person was standing still at the previous point
		double result = 0;	// variable for storing the results of property
		
		double[] firstpoint = null;
		final int starting_index = 2;
		for(int i=starting_index; i<length; i++){

			
			double x1 = getXYZValue(i-1)[0];
			double y1 = getXYZValue(i-1)[1];
			double x2 = getXYZValue(i)[0];
			double y2 = getXYZValue(i)[1];
			
			
			double fraction = getIntersectFraction(x1,x2,y1,y2, xstart,xend,ystart,yend);
			
			//Need to know if the starting point of the 1st path segment is inside the area
			if(i == starting_index){
				// If the ending point of the last path segment (or starting point of current
				// path segment) is inside the area the following function will return 1.0
				is_in = (getIntersectFraction(x1,x1,y1,y1, xstart,xend,ystart,yend) == 1.0);
				// ****** changed was_in = is_in to was_in = false
				//was_in = is_in;
				was_in = false;
			}
//			System.out.println(fraction);
			
			// Skips the points with "TimeBetweenValue" variable > 150 seconds
			if(getTimeBetweenValue(i)>180 && property != INACTIVE_TIME)
				continue;
			if(property == PATH_LENGTH){
				if(fraction > 0)
					result += getDistanceBetween(i) * fraction;
			} else if(property == TIME_STOPPED){
				if(fraction > 0){
					if(stopsindex.contains(new Integer(i)))
						result += getTimeBetweenValue(i);
				}
			} else if(property == NO_STOPS){
				if(fraction > 0){
					if(stopsindex.contains(new Integer(i))){
						if(!was_still){
							result ++;
							was_still = true;
						}
					} else 
						was_still = false;
						
				}
			
			} else if(property == TIME_SPENT){
				if(fraction > 0)
					result += getTimeBetweenValue(i) * fraction;
			} else if(property == INACTIVE_TIME){
				double time = getTimeBetweenValue(i);
				if(is_in && fraction > 0 && time > 10)
					result += time;
			} else if(property == STHETACHANGE){
				if(is_in)
					result += Math.abs(getSThetaChange(i));
				
			} else if(property == STHETAIN){
				if(!is_in && fraction>0)
					result = getSThetaValue(i);
			} else if(property == STHETAOUT){
				if(is_in && fraction > 0)
					result = getSThetaValue(i);
			} else if(property == SHORTEST_PATH_LENGTH){
				if(is_in && !was_in){
					firstpoint = super.getXYZValue(i);
				} else if(was_in && !is_in){
					double[] lastpoint = super.getXYZValue(i);
					if(firstpoint == null)
						System.err.println("Oops :( something unexpected has occured (line 470 - StatsGenerator)");
					// Calculate the modulus distance between the first and last point
					// 1. calculate the sum of square of x y z distance between 
					for(int d = 0; d < lastpoint.length; d++){
						double diff = lastpoint[d] - firstpoint[d];
						result += diff*diff;
					}
					// 2. square root the result
					result = Math.sqrt(result);
				}
			} else
				throw new IllegalArgumentException("Invalid property argument");
			
			was_in = is_in;
			// Check whether the ending point of the current path segment is inside the area
			is_in = (getIntersectFraction(x2,x2,y2,y2, xstart,xend,ystart,yend) == 1.0);
			
			// Record the result if the person has walked out of the area
			if(!is_in && was_in){
				al.add(result);
				
				// reset the result
				result = 0;
			}
			
		}
		// If the last point of the path is still inside the area, record the results
		if(is_in){
			al.add(result);
			result = 0;
		}
		
		// Return the results
		if(al.size() == 0)
			return null;
		double[] results = new double[al.size()];
		for(int i=0; i<al.size(); i++)
			results[i] = al.get(i);
		
		return results;
	}
	/**
	 * 
	 * @param xls	x coordinate of the starting point of the line
	 * @param xle	x coordinate of the ending point of the line
	 * @param yls	y coordinate of the starting point of the line
	 * @param yle	y coordinate of the ending point of the line
	 * @param xstart	x coordinate of the starting point of the area
	 * @param xend		x coordinate of the ending point of the area
	 * @param ystart	y coordinate of the starting point of the area
	 * @param yend		y coordinate of the ending point of the area
	 * @return intersect
	 */
	private double getIntersectFraction(double xls, double xle, double yls, double yle, double xstart, 
			double xend, double ystart, double yend){
		// If the line is inside the area, return 1 (100% inside the area)
		if(xls>= xstart && xls<= xend){
			if(xle>= xstart && xle<= xend){
				if(yls>= ystart && yls<= yend){
					if(yle>= ystart && yle<= yend){
						return 1;
					}
				}
			}
		}

		
		// Extract the 4 lines of the rectangular area
		double[][] p = new double[4][2];
		p[0] = are2LinesIntersect((float)xls, (float)xle, (float)yls, (float)yle, (float)xstart, (float)xend, (float)ystart, (float)ystart);
		p[1] = are2LinesIntersect((float)xls, (float)xle, (float)yls, (float)yle, (float)xend, (float)xend, (float)ystart, (float)yend);
		p[2] = are2LinesIntersect((float)xls, (float)xle, (float)yls, (float)yle, (float)xstart, (float)xend, (float)yend, (float)yend);
		p[3] = are2LinesIntersect((float)xls, (float)xle, (float)yls, (float)yle, (float)xstart, (float)xstart, (float)ystart, (float)yend);

		
		
		// Find out the number of intersect
		ArrayList<double[]> points = new ArrayList<double[]>();
		for(int i = 0; i<p.length; i++){
			if(points.size() == 0 && p[i] != null){
				points.add(p[i]);
				continue;
			}
			for(int j=0 ; j<points.size(); j++){
				double[] point = points.get(j);
				if(p[i] == null)
					continue;
				
				if(!(p[i][0] == point[0] && p[i][1] == point[1])){
					points.add(p[i]);
//					System.out.println("Loop2");
				}
			}
		}
		
		double linelength = Math.sqrt((xle-xls)*(xle-xls)+(yle-yls)*(yle-yls));

		
//		System.out.println("Points.size() = "+points.size());
		if(points.size() == 1){
			if(xls>= xstart && xls<= xend){
				if(yls>= ystart && yls<= yend){
					return Math.sqrt((points.get(0)[0]-xls)*(points.get(0)[0]-xls)+
							(points.get(0)[1]-yls)*(points.get(0)[1]-yls)) / linelength;
				}
			}
			
			return Math.sqrt((points.get(0)[0]-xle)*(points.get(0)[0]-xle)+
					(points.get(0)[1]-yle)*(points.get(0)[1]-yle)) / linelength;
			
		} else if(points.size() == 2){
//			System.out.println(points.get(0)[0]+" "+points.get(0)[1]+"\n"+points.get(1)[0]+" "+points.get(1)[1]);
//			System.out.println((float) Math.sqrt((points.get(0)[0]-points.get(1)[0])*(points.get(0)[0]-points.get(1)[0])+
//					(points.get(0)[1]-points.get(1)[1])*(points.get(0)[1]-points.get(1)[1])));

			return Math.sqrt((points.get(0)[0]-points.get(1)[0])*(points.get(0)[0]-points.get(1)[0])+
					(points.get(0)[1]-points.get(1)[1])*(points.get(0)[1]-points.get(1)[1])) / linelength;
		}
		
		return 0;
		
	}
	
	/**Check interaction between 2 lines and return the intersect point if exists 
	 * 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param x3
	 * @param x4
	 * @param y3
	 * @param y4
	 * @return null if there are no intersections, or the intersect point
	 */
	private double[] are2LinesIntersect(float x1, float x2, float y1, float y2, float x3, float x4, float y3, float y4){
		
		// See also: http://stackoverflow.com/questions/15594424/line-crosses-rectangle-how-to-find-the-cross-points
		// Find out the cross product of the 2 lines
		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		
		

		// ** The "float" conversion below might be necessary to lower the precision of the values, otherwise 
		// the program might otherwise not be able to find the correct intersection points
		// See also: http://stackoverflow.com/questions/588004/is-floating-point-math-broken?lq=1
		// If the cross product is 0, the 2 lines are co-linear and will not intersect
        if (d != 0) {
            float xi =  ((float)((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / (float)d);
            float yi =  ((float)((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / (float)d);
            

            if(x1 <= x2 && xi >= x1 && xi <= x2 || x1 > x2 && xi >= x2 && xi <= x1){
            	if(x3 <= x4 && xi >= x3 && xi <= x4 || x3 > x4 && xi >= x4 && xi <= x3){
            		if(y1 <= y2 && yi >= y1 && yi <= y2 || y1 > y2 && yi >= y2 && yi <= y1){
            			if(y3 <= y4 && yi >= y3 && yi <= y4 || y3 > y4 && yi >= y4 && yi <= y3){
    						return new double[]{xi, yi};
    					}
    				}
    			}
    		}

        }
        return null;
	}
	
	public static String getAxisName(int property){
		if(property == PATH_LENGTH)
			return "path length";
		if(property == TIME_STOPPED)
			return "time stopped";
		if(property == NO_STOPS) //gettotalaverage
			return "number of stops";
		if(property == TIME_SPENT) //gettotalaverage
			return "time spent";
		if(property == INACTIVE_TIME) //gettotalaverage
			return "inactive time";
		if(property == STHETACHANGE) //gettotalaverage
			return "change in speed theta";
		if(property == TIME_PER_STOP) //gettotalaverage
			return "time spent per stop";
		if(property == AVERAGE_SPEED) //gettotalaverage
			return "average speed";
		if(property == FREQ_IN_AREA) //gettotalaverage
			return "times appeared in this area";
		return ProbabilityList.getAxisName(property);
	}
}
