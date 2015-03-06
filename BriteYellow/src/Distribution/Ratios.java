package Distribution;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import csvexport.CSVWriter;
import maths.PhoneData;

public class Ratios extends ProbabilityList{

	public final static int PATH_LENGTH = 100;
	public final static int TIME_STILL = 101;
	public final static int TIME_SPENT = 102;
	public final static int TIME_DISAPPEAR = 103;
	public final static int STHETACHANGE = 104;
	public final static int STHETAIN = 105;
	public final static int STHETAOUT = 106;
	public final static int STHETAINOUT = 107;	//STEHTAOUT - STHETAIN
	public final static int AVERAGE_SPEED = 110;	// PATH_LENGTH / TIME_SPENT
	
	private int length;
	
	
	public Ratios(int opt, String fn) throws ParseException {
		super(opt, fn);
		length = getLength();
	}
	
	public Ratios(PhoneData[] phonedata) throws ParseException {
		// TODO Auto-generated constructor stub
		super(phonedata);
		length = getLength();
	}
	public Ratios(ArrayList<PhoneData> phonelist) throws ParseException{
		this(phonelist.toArray(new PhoneData[phonelist.size()]));
	}

	@Override
	public void changePhone(int opt){
		super.changePhone(opt);
		length = getLength();
	}
	
	boolean print = false;
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
//			System.out.println(fraction);
			if(property == PATH_LENGTH){
				if(fraction > 0)
					result += getDistanceBetween(i) * fraction;
			} else if(property == TIME_STILL){
				if(fraction > 0){
					if (isStandingStill(i))
						result += getTimeBetweenValue(i);
				}
			} else if(property == TIME_SPENT){
				if(fraction > 0)
					result += getTimeBetweenValue(i) * fraction;
			} else if(property == TIME_DISAPPEAR){
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
	public double getIntersectFraction(double xls, double xle, double yls, double yle, double xstart, 
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
			return "Path length";
		if(property == TIME_STILL)
			return "Time still";
		if(property == TIME_SPENT)
			return "Time spent";
		if(property == TIME_DISAPPEAR)
			return "Time disappeared";
		if(property == STHETACHANGE)
			return "Total angle changed";
		if(property == STHETAIN)
			return "Angle entering the area";
		if(property == STHETAOUT)
			return "Angle leaving the area";
		if(property == STHETAINOUT)
			return "Angle leaving - entering the area";
		if(property == AVERAGE_SPEED)
			return "Average speed";
		return null;
	}
	public static String getAxisUnit(int property){
		if(property == PATH_LENGTH)
			return "points/sec";
		if(property == TIME_STILL)
			return "sec";
		if(property == TIME_SPENT)
			return "sec";
		if(property == TIME_DISAPPEAR)
			return "sec";
		if(property == STHETACHANGE)
			return "radians";
		if(property == STHETAIN)
			return "radians";
		if(property == STHETAOUT)
			return "radians";
		if(property == STHETAINOUT)
			return "radians";
		if(property == AVERAGE_SPEED)
			return "points/sec";
		return null;
	}
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		
		/*
		 * x range: 87 to 1044 (main corridor from 87 to 659 and from 694 to 1044)
		 * y range: 2 to 364 (main corridor from 302 to 364)
		 */
					
		final int XINTERVAL = 4; //4
		final int YINTERVAL = 3; //3
		final double XSTART = 100;
		final double YSTART = 302;
		final double XEND = 1020;//(float) (XSTART+(1044 - XSTART)/4);//1044
		final double YEND = 364;//(float) (YSTART+(364 - YSTART)/3);//364
/*	
		final int XINTERVAL = 1;
		final int YINTERVAL = 1;
		final double XSTART = 660;
		final double YSTART = 2;
		final double XEND = 693;
		final double YEND = 302;
/*

/*		final int XINTERVAL = 1;
		final int YINTERVAL = 1;
		final double XSTART = 250;
		final double YSTART = 302;
		final double XEND = 900;
		final double YEND = 364;
*/	
		final int PORPERTYX = TIME_SPENT;
		final int PORPERTYY = TIME_DISAPPEAR;
		
		
		final String XAXIS = Ratios.getAxisName(PORPERTYX)+" ("+Ratios.getAxisUnit(PORPERTYX)+")";
		final String YAXIS = Ratios.getAxisName(PORPERTYY)+" ("+Ratios.getAxisUnit(PORPERTYY)+")";
		

		CSVWriter writer = null;
		
		float xstep = (float)(XEND - XSTART)/XINTERVAL;
		float ystep = (float)(YEND - YSTART)/YINTERVAL;
		
		double xstart, xend, ystart, yend;
		
		final String[] filenames = {
				"C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/24th Sept ORDERED.csv",
				"C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/26th Sept ORDERED.csv"
		};

		
		for(int a=0; a<XINTERVAL; a++){
			xstart = XSTART + xstep*a;
			xend = xstart+xstep;
			for(int b=0; b<YINTERVAL; b++){
				
				// Calculate xstart, xend, ystart, yend
				
				ystart = YSTART + ystep*b;
				yend = ystart+ystep;
				
//				System.out.println(xstart+" "+xend+" "+ystart+" "+yend);
				String CHARTTITLE = Ratios.getAxisName(PORPERTYY)+" vs. "+Ratios.getAxisName(PORPERTYX)+" from ("+Math.round((float)xstart)+", "+Math.round((float)ystart)+") to ("+Math.round((float)xend)+", "+Math.round((float)yend)+")";
//				System.out.println(CHARTTITLE);
				try {
					writer = new CSVWriter("src/Distribution/Ratios/"+CHARTTITLE+".csv");
					writer.write(new String[]{CHARTTITLE});
					writer.write(new String[]{XAXIS});
					writer.write(new String[]{YAXIS});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final int[] sequence = new int[]{
						1,5,2,3,4
				};
				
				
				long lines = 0;
				for(int h=0; h<filenames.length; h++){
					Ratios ratios = new Ratios (1, filenames[h]);
			
					
					for(int i = 0; i<5; i++){
						ratios.changePhone(sequence[i]);
						
						double[] result1 = ratios.get(PORPERTYX, xstart, xend, ystart, yend);
						double[] result2 = ratios.get(PORPERTYY, xstart, xend, ystart, yend);

						if(result1 == null)
							continue;
						
						
						for(int j=0; j<result1.length; j++){
							try{
								writer.write(new float[]{ (float) result1[j], (float) result2[j], sequence[i]});
								lines ++;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			
						
					}
				}
				try {
					writer.finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// If the results were empty, delete the file
				if(lines == 0){
					try {
						if(! writer.delete())
							System.err.println("Failed to delete \""+writer.getFileName()+"\"");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		
	}

}
