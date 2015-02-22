package Distribution;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import CSVExport.CSVWriter;

import Maths.PhoneData;

public class Ratios1 extends ProbabilityList{

	
	public Ratios1(int opt, String fn) throws ParseException {
		super(opt, fn);

	}
	public Ratios1(PhoneData[] phonedata) throws ParseException {
		// TODO Auto-generated constructor stub
		super(phonedata);
	}
	
	public float[][] get(int property1, int property2, double xcenter, double ycenter, double radius){
		ArrayList<Integer> indices;
		if(getStartingIndex(property2) > getStartingIndex(property1))
			indices = getIndicesWithinRange(xcenter, ycenter, radius, property2);
		else
			indices = getIndicesWithinRange(xcenter, ycenter, radius, property1);
		return get(property1, property2, indices);
	}
	public float[][] get(int property1, int property2, double xstart, double xend, double ystart, double yend){
		ArrayList<Integer> indices;
		if(getStartingIndex(property2) > getStartingIndex(property1))
			indices = getIndicesWithinRange(xstart, xend, ystart, yend, property2);
		else
			indices = getIndicesWithinRange(xstart, xend, ystart, yend, property1);
		return get(property1, property2, indices);
	}
	public float[][] get(int property1, int property2, ArrayList<Integer> indices){
		float[][] result = new float[indices.size()][3];
		
		for(int i=0; i<indices.size(); i++){
			result[i][0] = (float)get(property1, indices.get(i));
			result[i][1] = (float)get(property2, indices.get(i));
		}
		
		return result;
	}
	private double get(int compare_property, int index){
		if(compare_property == MODSPD)
			return getModSValue(index);
		else if(compare_property == XSPD)
			return Math.abs(getXYZSpeedValue(index)[0]);
		else if(compare_property == YSPD)
			return Math.abs(getXYZSpeedValue(index)[1]);
		else if(compare_property == ZSPD)
			return Math.abs(getXYZSpeedValue(index)[2]);
		else if(compare_property == MODACC)
			return getModAValue(index);
		else if(compare_property == XACC)
			return Math.abs(getXYZAccelerationValue(index)[0]);
		else if(compare_property == YACC)
			return Math.abs(getXYZAccelerationValue(index)[1]);
		else if(compare_property == ZACC)
			return Math.abs(getXYZAccelerationValue(index)[2]);
		else if(compare_property == STHETA)
			return getSThetaValue(index);
		else if(compare_property == ATHETA)
			return getAThetaValue(index);
		else if(compare_property == STHETACHANGE)
			return getSThetaChange(index);
		else if(compare_property == ATHETACHANGE)
			return getAThetaChange(index);
		else if(compare_property == DISAPPEAR)
			return getTimeBetweenValue(index);
		throw new IllegalArgumentException("You might have enter the wrong property argument!");
	}

	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
//		Ratios ratios = new Ratios (1, "C:/Users/testuser/SkyDrive/Documents/4th year project files/Programs/BriteYellow/src/24th Sept ORDERED.csv");
/*		final String[] PEOPLE = new String[]{
				"Shopper", "Business", "Security" 
		};
		final Color COLOUR[] = new Color[]{
			new Color(0,100,255), new Color(255,0,0),new Color(100,255,0)
		};
		final Shape shapeCir  = new Ellipse2D.Double(-2.5,-2.5,5,5);
		Shape shapeCirStroke = new BasicStroke(0.1f).createStrokedShape(shapeCir);
*/
		
		/*
		 * x range: 87 to 1044 (main corridor from 87 to 659 and from 694 to 1044)
		 * y range: 2 to 364 (main corridor from 302 to 364)
		 */
				
		final int XINTERVAL = 4;
		final int YINTERVAL = 3;
		final double XSTART = 100;
		final double YSTART = 302;
		final double XEND = 1020;
		final double YEND = 364;
/*		
		final int XINTERVAL = 1;
		final int YINTERVAL = 1;
		final double XSTART = 660;
		final double YSTART = 2;
		final double XEND = 693;
		final double YEND = 302;
*/		
		
		final int PORPERTYX = MODSPD;
		final int PORPERTYY = XSPD;
		
		final String XAXIS = getAxisName(PORPERTYX)+" ("+getAxisUnit(PORPERTYX)+")";
		final String YAXIS = getAxisName(PORPERTYY)+" ("+getAxisUnit(PORPERTYY)+")";


		CSVWriter writer = null;
		
		double xstep = (XEND - XSTART)/XINTERVAL;
		double ystep = (YEND - YSTART)/YINTERVAL;
		
		double xstart, xend, ystart, yend;
		
		final String[] filenames = {
				"C:/Users/testuser/SkyDrive/Documents/4th year project files/repos/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv",
				"C:/Users/testuser/SkyDrive/Documents/4th year project files/repos/4th-year-project/BriteYellow/src/26th Sept ORDERED.csv"
		};

		
		for(int a=0; a<XINTERVAL; a++){
			xstart = XSTART + xstep*a;
			xend = xstart+xstep;
			for(int b=0; b<XINTERVAL; b++){
				
				// Calculate xstart, xend, ystart, yend
				
				ystart = YSTART + ystep*b;
				yend = ystart+ystep;
				
				String CHARTTITLE = getAxisName(PORPERTYY)+" vs. "+getAxisName(PORPERTYX)+" from ("+Math.round((float)xstart)+", "+Math.round((float)ystart)+") to ("+Math.round((float)xend)+", "+Math.round((float)yend)+")";
		
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
					Ratios1 ratios = new Ratios1 (1, filenames[h]);
			
					
					for(int i = 0; i<5; i++){
						ratios.changePhone(sequence[i]);
						
						float[][] result = ratios.get(PORPERTYX, PORPERTYY, xstart, xend, ystart, yend);
						if(result.length == 0)
							continue;
						
						
						for(int j=0; j<result.length; j++){
							float[] resultA = new float[3];
							resultA[0] = result[j][0];
							resultA[1] = result[j][0];
							resultA[2] = sequence[i];
							try{
								writer.write(resultA);
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
/*		final String CHARTTITLE = "Speed angle vs Modulus Speed from ("+XSTART+", "+YSTART+") to ("+XEND+", "+YEND+")";

		PlotHelper ph = new PlotHelper(CHARTTITLE, "Modulus Speed", "Speed Angle (radians)", PEOPLE, 1280, 720);
		

		for(int i=0; i<3; i++){
			ph.setSeriesPaint(PEOPLE[i], COLOUR[i]);
			ph.setSeriesShape(PEOPLE[i], shapeCirStroke);
		}
		for(int i = 1; i<=5; i++){
			if(i>1)
				ratios.changePhone(i);
			
			double[][] result = ratios.get(PORPERTY1, PORPERTY2, XSTART, XEND, YSTART, YEND);
			if(result.length == 0)
				continue;
			if(i==1 || i==5)
				ph.addData("Business", result);
			else if(i==2)
				ph.addData("Security", result);
			else if(i==3 || i==4)
				ph.addData("Shopper", result);
		}
		
		ph.showDialog();
*/
/*		for (int i = 0; i<result.length; i++){
			for(int j=0; j<result[0].length; j++){
				System.out.print((float)result[i][j]+" ");
			}
			System.out.print("\n");
		}
*/		
		
	}

}
