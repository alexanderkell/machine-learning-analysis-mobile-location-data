package Graphing;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Maths.DataFormatOperations.PhoneData;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;

public class PlotTracks {
	
	public final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", "Yacc", "Zacc", "ModAcc", "ATheta"	

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
			jlabel.setText("<html>"+track_info[3][i] + "<br> Point "+(i+1)+"</html>");
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
		}
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
		
	}
	/*public static void main(String args[]) throws ParseException, IOException{
		DataFormatOperations dfo = new DataFormatOperations(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
		for (int i = 1; i<=5; i++){
			dfo.changePhone(i);
			dfo.writeToFile();
		}
	}
*/
	/*public static void main(String args[]) throws ParseException, IOException{
		DataFormatOperations dfo = new DataFormatOperations(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
		String[][] result = dfo.getSort();
		System.out.println(result.length-1);
		for(int i=0; i<result[0].length; i++){
			for(int j=0; j<result.length; j++){
				System.out.print(result[j][i]+"\t");
			}
			System.out.print("\n");
		}
	//	plotTrack2(dfo.getFull(), 0, 1, 0.1f);
	}*/
		

