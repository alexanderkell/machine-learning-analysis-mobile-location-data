package Maths;
import java.util.*;
import java.text.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.lang.Math;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;

import CSVExport.CSVWriter;
import CSVImport.CSVReaders;
import Graphing.PlotHelper;

public class DataFormatOperations{
	
<<<<<<< HEAD
	public final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", "Yacc", "Zacc", "ModAcc", "ATheta"	
=======
	final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", "Yacc", "Zacc", "ModAcc", "ATheta", "Track"
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
	};
	public final static int X = 0;
	public final static int Y = 1;
	public final static int Z = 2;
	public final static int WholeDate = 3;
	public final static int PhoneId = 4;
	public final static int TimeBetween = 5;
	public final static int XSpeed = 6;
	public final static int YSpeed = 7;
	public final static int ZSpeed = 8;
	public final static int MSpeed = 9;
	public final static int STheta = 10;
	public final static int XAcc = 11;
	public final static int YAcc = 12;
	public final static int ZAcc = 13;
	public final static int MAcc = 14;
	public final static int ATheta = 15;
	public final static int Track = 16;
	
	//initialise all variables
	//time between variable
	private double tb = 0;
	//relative speed
	private double rsx = 0;
	private double rsy = 0;
	private double rsz = 0;
	//relative acceleration
	private double rax = 0;
	private double ray = 0;
	private double raz = 0;
	//time variables
	private double hr = 0;
	private double mn = 0;
	private double sc = 0;
	private double hr2 = 0;
	private double mn2 = 0;
	private double sc2 = 0;
	private double xco = 0;
	private double yco = 0;
	private double zco = 0;
	private double modspd = 0;
	private double modacc = 0;
	private double spdtheta = 0;
	private double acctheta = 0;
	
	

	//sets up the date formats to be used for splitting up the different constituent parts of the date
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
	DateFormat hour = new SimpleDateFormat("HH", Locale.ENGLISH);
	DateFormat min = new SimpleDateFormat("mm", Locale.ENGLISH);
	DateFormat sec = new SimpleDateFormat("ss", Locale.ENGLISH);
	
	
	//class variables
	private CSVReaders Read;
	private int opt;
	private int length;
	private String fn;
	private String temp = new String();
	//read in data
	private String cdcalc[][];
	private PhoneData cdcalc2[];
	
	public DataFormatOperations(int opt, String fn) throws ParseException{
		
		//Read and store the phone data
		Read = new CSVReaders(fn);

		cdcalc = Read.myPhone(opt);
		processData1();
		processData2();
	}
	public void reanalyse(String[][] cdcalc){
		this.cdcalc = cdcalc;
		processData1();
		processData2();
	}
	public void reanalyse(PhoneData[] cdcalc2){
		this.cdcalc2 = cdcalc2;
		processData2();
	}
	public void processData1(){
		
//		this.opt = opt;
		//create constructor object
		length = cdcalc[0].length;
		
		cdcalc2 = new PhoneData[length];
		
		// Store the x,y,z speeds
		for(int i=0; i<length; i++){
			cdcalc2[i] = new PhoneData();
			cdcalc2[i].phone_id = cdcalc[4][i];
			try{
				cdcalc2[i].x = Double.parseDouble(cdcalc[0][i]);
			} catch (NumberFormatException e) {
				cdcalc2[i].x = 0;
				}
			try{
				cdcalc2[i].y = Double.parseDouble(cdcalc[1][i]);
			} catch (NumberFormatException e) {
				cdcalc2[i].y = 0;
				}
			try{
				cdcalc2[i].z = Double.parseDouble(cdcalc[2][i]);
			} catch (NumberFormatException e) {
				cdcalc2[i].z = 0;
				}
			cdcalc2[i].wholedatestring = cdcalc[3][i];
			try{
				cdcalc2[i].wholedate = df.parse(cdcalc[3][i]);
			}catch(ParseException pe){
				
			}
		}
		
	}
	public void processData2(){
		
		//works out the time between each reading based on the time
			for(int y = 0; y<length-1; y++){
				
				Date wholedate1 =  cdcalc2[y].wholedate; 
				Date wholedate2 =  cdcalc2[y+1].wholedate;
				
				if(wholedate1.compareTo(wholedate2)>0){
					System.err.println("Please Put Data in Date and Time Order Before Running!");
					break;
				}else{
					temp = hour.format(wholedate1);
					hr =Double.parseDouble(temp);
					temp = min.format(wholedate1);
					mn =Double.parseDouble(temp);
					temp = sec.format(wholedate1);
					sc =Double.parseDouble(temp); 
					temp = hour.format(wholedate2);
					hr2 =Double.parseDouble(temp);
					temp = min.format(wholedate2);
					mn2 =Double.parseDouble(temp);
					temp = sec.format(wholedate2);
					sc2 =Double.parseDouble(temp);
					tb = (hr2 - hr)*60*60 + (mn2 - mn)*60 + (sc2 - sc);
					cdcalc2[y+1].tb = tb;
				}
						
	        }
			
			for (int k = 1; k < length; k++){//working out relative speeds in all directions (velocity)
				
				xco = cdcalc2[k].x - cdcalc2[k-1].x;
				yco = cdcalc2[k].y - cdcalc2[k-1].y;
				zco = cdcalc2[k].z - cdcalc2[k-1].z;

				if(cdcalc2[k].tb == 0){
					// If the time difference is 0, assume time difference is 1 second and take distance / 1 second
					cdcalc2[k].rsx = rsx = xco;
					cdcalc2[k].rsy = rsy = yco;
					cdcalc2[k].rsz = zco;
				} else{
					// Calculate Speeds (distance / time)
					cdcalc2[k].rsx = rsx = xco/cdcalc2[k].tb;
					cdcalc2[k].rsy = rsy = yco/cdcalc2[k].tb;
					cdcalc2[k].rsz = zco/cdcalc2[k].tb;
				}
				
				// Calculate the angle
				// If the person is not moving (zero speed), assume the angle is
				// as the previous angle
				if(rsx == 0 && rsy == 0)
					cdcalc2[k].spdtheta = cdcalc2[k-1].spdtheta;
				else
					cdcalc2[k].spdtheta = Math.atan(rsy/rsx);
				
				
				cdcalc2[k].modspd = Math.sqrt(rsx*rsx + rsy*rsy);
			}
			
			//working out relative acceleration in all directions
			for(int l=2; l<length; l++){
				
				rax = (cdcalc2[l].rsx - cdcalc2[l-1].rsx);
				ray = (cdcalc2[l].rsy - cdcalc2[l-1].rsy);
				raz = (cdcalc2[l].rsz - cdcalc2[l-1].rsz);
				if(cdcalc2[l].tb == 0){
					// If the time difference is 0, assume time difference is 1 second and take distance / 1 second
					cdcalc2[l].rax = rax;
					cdcalc2[l].ray = ray;
					cdcalc2[l].raz = raz;
				} else {
					cdcalc2[l].rax = rax/ cdcalc2[l].tb;
					cdcalc2[l].ray = ray/ cdcalc2[l].tb;
					cdcalc2[l].raz = raz/ cdcalc2[l].tb;
				}
				
				// If the person is not moving (zero speed), assume the angle is
				// as the previous angle
				if(rax == 0 && ray == 0){
					cdcalc2[l].acctheta = cdcalc2[l-1].acctheta;

				}else{
					cdcalc2[l].acctheta = Math.atan(ray/rax);
				}
				cdcalc2[l].modacc = (cdcalc2[l].modspd - cdcalc2[l-1].modspd) / cdcalc2[l].tb;
				
<<<<<<< HEAD
			}
			
			getSort();
=======
			
		}
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
	}
	public int getPhone(){
		return opt;
	}
	
	public void changePhone(int opt){
		cdcalc = Read.myPhone(opt);
		processData1();
		processData2();
	}
	
	public int getLength(){
		return length;
	}
	

	
	public String getFileName(){
		return fn;
	}
	
	public void writeToFile() throws IOException{
//		System.out.println(String.valueOf(cdcalc2[1].wholedate));
		CSVExport.CSVWriter cw = new CSVExport.CSVWriter(new SimpleDateFormat("dd-MM").format(cdcalc2[1].wholedate)+" phone "+getPhone()+".csv");
		PhoneData[] pd = getFullPhoneData();
		cw.write(COLUMNS);

		for(int i=0; i<length; i++){
			PhoneData pdi = pd[i];
			String[] data = new String[]{
				cdcalc[0][i],
				cdcalc[1][i],
				cdcalc[2][i],
				cdcalc[3][i],
				cdcalc[4][i],
				String.valueOf(pdi.tb),
				String.valueOf(pdi.rsx),
				String.valueOf(pdi.rsy),
				String.valueOf(pdi.rsz),
				String.valueOf(pdi.modspd),
				String.valueOf(pdi.spdtheta),
				String.valueOf(pdi.rax),
				String.valueOf(pdi.ray),
				String.valueOf(pdi.raz),
				String.valueOf(pdi.modacc),
				String.valueOf(pdi.acctheta),
			};
			cw.write(data);
		}
		cw.finish();
	}
	public String[][] getFull(){
		if(cdcalc[5][0] == null){	// Just to check if the full String is already created, if yes, no need to build full String[][] again
			// Build the full String[][]
			for(int i=0; i<cdcalc[0].length; i++){
				cdcalc[5][i] = String.valueOf(cdcalc2[i].tb);
				cdcalc[6][i] = String.valueOf(cdcalc2[i].rsx);
				cdcalc[7][i] = String.valueOf(cdcalc2[i].rsy);
				cdcalc[8][i] = String.valueOf(cdcalc2[i].rsz);
				cdcalc[9][i] = String.valueOf(cdcalc2[i].rax);
				cdcalc[10][i] = String.valueOf(cdcalc2[i].ray);
				cdcalc[11][i] = String.valueOf(cdcalc2[i].raz);
				cdcalc[12][i] = String.valueOf(cdcalc2[i].modspd);
				cdcalc[13][i] = String.valueOf(cdcalc2[i].modacc);
				cdcalc[14][i] = String.valueOf(cdcalc2[i].spdtheta);
				cdcalc[15][i] = String.valueOf(cdcalc2[i].acctheta);
				
			}
		}
		return cdcalc;
	}
	public PhoneData[] getFullPhoneData(){
		return cdcalc2;
	}
	
	public double[] getXYZValue(int index){
		return new double[]{
				cdcalc2[index].x,
				cdcalc2[index].y,
				cdcalc2[index].z
		};
			
	}
	
	public String getDateTimeString(int index){
		
		return cdcalc2[index].wholedatestring;
		
	}
		
	public double getHour(int index){
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			String date = hour.format(id);
			hr =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return hr;
	}
	
	public double getMin(int index){
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			String date = min.format(id);
			mn =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return mn;
	}
	
	public double getSec(int index){
		try {
			Date id =  df.parse(cdcalc2[index].wholedatestring);
			String date = sec.format(id);
			sc =Double.parseDouble(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	return sc;
	}
	
	public double getTimeBetweenValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");

		return cdcalc2[index].tb;
		
	}
	
	// Not to be confused with getModSValue(int index)
	public double getDistanceBetween(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");
		
		// Calculate different between 2 recorded position
		double xdiff = cdcalc2[index].x - cdcalc2[index-1].x;
		double ydiff = cdcalc2[index].y - cdcalc2[index-1].y;
		return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
	
	public double getX(int index){
		return cdcalc2[index].x;
	}
	public double getY(int index){
		return cdcalc2[index].y;
	}
	public double getZ(int index){
		return cdcalc2[index].z;
	}
	public double[] getXYZDistanceBetween(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");
		
		// Calculate different between 2 recorded position
		double xdiff = cdcalc2[index].x - cdcalc2[index-1].x;
		double ydiff = cdcalc2[index].y - cdcalc2[index-1].y;
		double zdiff = cdcalc2[index].z - cdcalc2[index-1].z;
		return new double[]{
			xdiff, ydiff, zdiff
			};
	}
	public double[] getXYZSpeedValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Speed index must be greater than or equal to 1");

		return new double[]{
			cdcalc2[index].rsx,
			cdcalc2[index].rsy,
			cdcalc2[index].rsz
		};
		
	}
	
	public double[] getXYZAccelerationValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than or equal to 2");

		return new double[]{
				cdcalc2[index].rax,
				cdcalc2[index].ray,
				cdcalc2[index].raz
			};
	}
	
	// Not to be confused with getDistanceBetween(int index)
	public double getModSValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Speed index must be greater than or equal to 1");

		return cdcalc2[index].modspd;
		
	}
	
	public double getModAValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than or equal to 2");

		return cdcalc2[index].modacc;
		
	}
	
	public double getSThetaValue(int index){
		if(index<1)
			throw new IllegalArgumentException("Speed index must be greater than or equal to 1");

		return cdcalc2[index].spdtheta;
		
	}
	
	public double getAThetaValue(int index){
		if(index<2)
			throw new IllegalArgumentException("Acceleration index must be greater than or equal to 2");
		
		return cdcalc2[index].acctheta;
		
	}
	public double getSThetaChange(int index){
		if(index<2)
			throw new IllegalArgumentException("Speed theta change index must be greater than or equal to 2");

		return cdcalc2[index].spdtheta - cdcalc2[index-1].spdtheta;
		
	}
	public double getAThetaChange(int index){
		if(index<3)
			throw new IllegalArgumentException("Acceleration theta change index must be greater than or equal to 3");
		
		return cdcalc2[index].acctheta - cdcalc2[index-1].acctheta;
		
	}
	
	public boolean isStandingStill(int index){
		if(index<1)
			throw new IllegalArgumentException("Index must be greater than or equal to 1");
		// Return true if the modulus speed is less than 1 point/sec, or false if not
		return cdcalc2[index].modspd ==0;
	}


	
	/**Set the track number for a point
	 * 
	 * @param index
	 */
	public void setTrackNo(int index, int track_no){
		cdcalc2[index].track_no = track_no;
	}
	
	
<<<<<<< HEAD

	public void getSort(){
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		while(i<length){
			
			x = cdcalc2[i].x;
			if(x>200 && x<850){
				cdcalc2[i].track_no = track;
			}
			else if(x>850){
				r=1;
				s=0;
				cdcalc2[i].track_no = -1;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
				cdcalc2[i].track_no = -1;
			}
			
			
			else if(x<200){s=1;r=0;
				cdcalc2[i].track_no = -1;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
				cdcalc2[i].track_no = -1;
			}
			
			//System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
=======
/*	public void plotTrackTest(){
		String[] label = new String[]{
			"Phone data"
		};
		PlotHelper plot = new PlotHelper(COLUMNS[0]+" vs "+COLUMNS[1], COLUMNS[0], COLUMNS[1], label);
		plot.showDialog();
		for(int i = 0; i<cdcalc2.length; i++){
			Thread.sleep(arg0)
			plot.addData(label[0], cdcalc2[i].x, cdcalc2[i].y);
		}
	}
*/	
	/**Plot the track
	 * 
	 * @param track_info
	 * @param row
	 * @param col
	 */
	public static void plotTrack1(String[][] track_info, int row, int col, float timescaler){
		Image im = new ImageIcon("map.jpg").getImage(); 
		
		String[] label = new String[]{
			"Phone data"
		};
		PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
		plot.setAxisRange(0, 1100, 0, 500);
		plot.setRangeAxisInverted(true);
		XYPlot xyplot = plot.getXYPlot();
		// Clear background paint
		xyplot.setBackgroundPaint(null);
		// Set background image to the map
		xyplot.setBackgroundImage(im);
		
		ChartPanel cpanel = plot.getChartPanel();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(cpanel, BorderLayout.CENTER);
		JLabel jlabel = new JLabel();
		frame.add(jlabel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
		
		for(int i = 0; i<track_info[0].length; i++){
			jlabel.setText("<html>"+track_info[i][3] + "<br> Point "+(i+1)+"</html>");
			  try{
				  int tb = (int) (timescaler*1000*(int) Double.parseDouble(track_info[i][5]));
					Thread.sleep(tb);
			  } catch (NumberFormatException e){
					System.out.println(e.toString());
				} catch (InterruptedException e){
					System.err.println("User Aborted");
					return;
				}
			 
			plot.addData(label[0], Double.parseDouble(track_info[i][row]), Double.parseDouble(track_info[i][col]));
		}
		
	}
	public static void plotTrack2(String[][] track_info, int row, int col, float timescaler){
		Image im = new ImageIcon("map.jpg").getImage(); 
		
		String[] label = new String[]{
			"Phone data"
		};
		PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
		plot.setAxisRange(0, 1100, 0, 500);
		plot.setRangeAxisInverted(true);
		plot.setSeriesLinesVisble(label[0], true);
		XYPlot xyplot = plot.getXYPlot();
		// Clear background paint
		xyplot.setBackgroundPaint(null);
		// Set background image to the map
		xyplot.setBackgroundImage(im);
		
		ChartPanel cpanel = plot.getChartPanel();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(cpanel, BorderLayout.CENTER);
		JLabel jlabel1 = new JLabel();
		jlabel1.setText("Playing at "+(1/timescaler)+"X speed");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel, BorderLayout.SOUTH);
		JLabel jlabel2 = new JLabel();
		panel.add(jlabel1);
		panel.add(jlabel2);
		frame.pack();
		frame.setVisible(true);
		
		for(int i = 0; i<track_info[0].length; i++){
			jlabel2.setText("<html><p>"+track_info[3][i] + "&nbsp;&nbsp; Point "+(i+1)+" / "+track_info[0].length+"</p></html>");
			  try{
				  int tb = (int) (timescaler*1000*(int) Double.parseDouble(track_info[5][i]));
					Thread.sleep(tb);
			  } catch (NumberFormatException e){
					System.out.println(e.toString());
				} catch (InterruptedException e){
					System.err.println("User Aborted");
					return;
				}
			 
			plot.addData(label[0], Double.parseDouble(track_info[row][i]), Double.parseDouble(track_info[col][i]));
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
		}
		jlabel1.setText("Stopped");
		
	}
	
	public static void plotTrack2(PhoneData[] track_info, int row, int col, float timescaler){
		Image im = new ImageIcon("map.jpg").getImage(); 
		
		String[] label = new String[]{
			"Phone data"
		};
		PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
		plot.setAxisRange(0, 1100, 0, 500);
		plot.setRangeAxisInverted(true);
		plot.setSeriesLinesVisble(label[0], true);
		XYPlot xyplot = plot.getXYPlot();
		// Clear background paint
		xyplot.setBackgroundPaint(null);
		// Set background image to the map
		xyplot.setBackgroundImage(im);
		
		ChartPanel cpanel = plot.getChartPanel();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(cpanel, BorderLayout.CENTER);
		JLabel jlabel1 = new JLabel();
		jlabel1.setText("Playing at "+(1/timescaler)+"X speed");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel, BorderLayout.SOUTH);
		JLabel jlabel2 = new JLabel();
		panel.add(jlabel1);
		panel.add(jlabel2);
		frame.pack();
		frame.setVisible(true);
		
		for(int i = 0; i<track_info.length; i++){
			jlabel2.setText("<html><p>"+track_info[i].wholedatestring + "&nbsp;&nbsp; Point "+(i+1)+" / "+track_info.length+"</p></html>");
			  try{
				  int tb = (int) (timescaler*1000*(int) track_info[i].tb);
					Thread.sleep(tb);
			  } catch (NumberFormatException e){
					System.out.println(e.toString());
				} catch (InterruptedException e){
					System.err.println("User Aborted");
					return;
				}
			
			  
			  Double r;
			  try{
				  r = getAttributeDouble(track_info[i], row);
			  } catch (IllegalArgumentException e){
				  try{
					  r = (double) getAttributeInt(track_info[i], row);
				  } catch (IllegalArgumentException f){
					  throw new IllegalArgumentException(
						 "It is not possible to plot graph with the attribute you have defined (2nd argument)"
						);
				  }
			  }
			  Double c;
			  try{
				  c = getAttributeDouble(track_info[i], col);
			  } catch (IllegalArgumentException e){
				  try{
					  c = (double) getAttributeInt(track_info[i], col);
				  } catch (IllegalArgumentException f){
					  throw new IllegalArgumentException(
						"It is not possible to plot graph with the attribute you have defined (3rd argument)"
					  );
				  }
			  }
			 plot.addData(label[0], r, c);
		}
		jlabel1.setText("Stopped");
		
	}
<<<<<<< HEAD
/*	public void getSort(){
=======
	
	public static double getAttributeDouble(PhoneData p, int attribute){
		if(attribute == X)
			return p.x;
		if(attribute == Y)
			return p.y;
		if(attribute == Z)
			return p.z;

		if(attribute == TimeBetween)
			return p.tb;
		if(attribute == XSpeed)
			return p.rsx;		
		if(attribute == YSpeed)
				return p.rsy;
		if(attribute == ZSpeed)
			return p.rsz;
		if(attribute == MSpeed)
			return p.modspd;
		if(attribute == STheta)
			return p.spdtheta;
		if(attribute == XAcc)
			return p.rax;		
		if(attribute == YAcc)
			return p.ray;
		if(attribute == ZAcc)
			return p.raz;
		if(attribute == MAcc)
			return p.modacc;
		if(attribute == ATheta)
			return p.acctheta;
		throw new IllegalArgumentException(
			"You might have passed the wrong argument or you have used the wrong method to get attributes"
			);
	}

	public static int getAttributeInt(PhoneData p, int attribute){
		if(attribute == Track)
			return p.track_no;
		throw new IllegalArgumentException(
				"You might have passed the wrong argument or you have used the wrong method to get attributes"
				);
	}
	public static Date getDate(PhoneData p){
		return p.wholedate;
	}
	
	
	public static String getAttributeString(PhoneData p, int attribute){
		if(attribute == WholeDate)
			return p.wholedatestring;
		try{
			return String.valueOf(getAttributeDouble(p, attribute));
		} catch (IllegalArgumentException e){
			try{
				return String.valueOf(getAttributeInt(p, attribute));
			} catch (IllegalArgumentException f){
				throw new IllegalArgumentException("You might have passed the wrong argument");
			}
		}
	}
	
	
	public PhoneData[] getSort2(){
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		while(i<length){
			
			x = cdcalc2[i].x;
			if(x>200 && x<850){
				setTrackNo(i,track);
				cdcalc2[i].track_no = track;
			}
			else if(x>850){r=1;s=0;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
			}
			
			
			else if(x<200){s=1;r=0;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
			}
			
			i++;
		}
		return cdcalc2;
	}
	
	public String[][] getSort(){
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		String[][] newdat = cdcalc;
		
		while(i<length){
			
			x = Double.parseDouble(newdat[0][i]);
			if(x>200 && x<850){
				newdat[16][i] = String.valueOf(track);
				cdcalc2[i].track_no = track;
			}
			else if(x>850){r=1;s=0;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
			}
			
			
			else if(x<200){s=1;r=0;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
			}
			
<<<<<<< HEAD
			//System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
		}
		cdcalc = newdat;
=======
//			System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
		}
		return newdat;
	}

	
	public class PhoneData{
		//position x,y,z
		public double x, y, z;
		
		//whole date in Data and String format
		public Date wholedate;
		public String wholedatestring;
		
		//time between current position and the previous position
		public double tb;
		
		//relative speeds in x,y,z and modulus direction, and angle
		public double rsx, rsy, rsz, modspd, spdtheta;
		
		//relative accelerations in x,y,z and modulus direction
		public double rax, ray, raz, modacc, acctheta;
		
		public String phone_id;
		
		public int track_no;
	}
	
/*	public static void main(String args[]) throws ParseException, IOException{
		DataFormatOperations dfo = new DataFormatOperations(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
		for (int i = 1; i<=5; i++){
			dfo.changePhone(i);
			dfo.writeToFile();
		}
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
	}
	*/
}