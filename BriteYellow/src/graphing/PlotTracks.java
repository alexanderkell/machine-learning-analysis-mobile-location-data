package graphing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import maths.PhoneData;

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
					System.err.println(e.toString());
				} catch (InterruptedException e){
					System.err.println("User Aborted");
					return;
				}
			 
			plot.addData(label[0], Double.parseDouble(track_info[row][i]), Double.parseDouble(track_info[col][i]));
		}
	}
		
		
		
		/**Plot the track against time
		 * 
		 * @param track_info	Track information contained in the PhoneData object
		 * @param row
		 * @param col
		 * @param timescaler	Time scaler: Specify how long 1 second is
		 */
		public static void plotTrack2(final PhoneData[] track_info, final int row, final int col, float timescaler){
			Image im = new ImageIcon("map.jpg").getImage(); 
			
			final String[] label = new String[]{
				"Phone data"
			};
			final PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
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
			
			
			final JLabel jlabel1 = new JLabel();	// Label for "Playing at XX Speed"
			jlabel1.setText("Playing at "+(1/timescaler)+"X speed");
			
			final JLabel jlabel2 = new JLabel();	// Label for showing the current point number
			final JLabel jlabel3 = new JLabel();	// Label for showing start time
			
			final JLabel jlabel4 = new JLabel();	// Label for showing end time
			
					
			final JProgressBar jpb = new JProgressBar();	//ProgressBar for showing the current time
			jpb.setStringPainted(true);
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			frame.add(panel, BorderLayout.SOUTH);
			
			
			JPanel subpanel = new JPanel();
			subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.LINE_AXIS));
			
			panel.add(jlabel1);
			jlabel1.setAlignmentX(Container.LEFT_ALIGNMENT);
			subpanel.add(jlabel2);
			subpanel.add(Box.createHorizontalStrut(20));
			subpanel.add(jlabel3);
			subpanel.add(Box.createHorizontalStrut(5));
			subpanel.add(jpb);
			subpanel.add(Box.createHorizontalStrut(5));
			subpanel.add(jlabel4);
			
			subpanel.setAlignmentX(Container.LEFT_ALIGNMENT);
			panel.add(subpanel);
			frame.pack();
			frame.setVisible(true);
			
		
			final Timer timer = new Timer(true);

			final ExtendedTimerTask ttask = new ExtendedTimerTask(){
				private int i = 0;
				private long current_time = track_info[0].ts.getTime()-1000;
				private long curr_time_diff = 0;
				private long points_time_diff = 0;

				public void setCurrentTime(float percent){
					if( i == 0){
						current_time = track_info[i].ts.getTime()+ (long)(points_time_diff*percent)/1000*1000;
					} else{
						current_time = track_info[i-1].ts.getTime()+(long)(points_time_diff*percent)/1000*1000;
					}
					curr_time_diff = current_time - track_info[i-1].ts.getTime();
					jpb.setValue((int)(100*curr_time_diff/points_time_diff));
					jpb.setString(new Timestamp(current_time).toString());
				}
				public void run(){
					
					current_time+=1000;
//					System.out.println(current_time);
//					System.out.println(track_info[i].ts.getTime());
//					System.out.println((current_time - track_info[i].ts.getTime()));
					while((current_time - track_info[i].ts.getTime())>=0 && i<track_info.length){
//						jpb.setValue(0);
						if(i<track_info.length - 1){
							points_time_diff = track_info[i+1].ts.getTime() - track_info[i].ts.getTime();
							jlabel4.setText(track_info[i+1].ts.toString());
						
						}
						jlabel2.setText("Point "+(i+1)+" / "+track_info.length);
						jlabel3.setText(track_info[i].ts.toString());
						
						// Get the attributes
						Double r = getAttributeDouble(track_info[i], row);
						  
						Double c = getAttributeDouble(track_info[i], col);

						plot.addData(label[0], r, c);
						i++;
					}
						
					curr_time_diff = current_time - track_info[i-1].ts.getTime();
					
					if(i == track_info.length){
						timer.cancel();
						jlabel1.setText("Stopped");
					}
					
					// Set current time
					jpb.setString(new Timestamp(current_time).toString());
					int barvalue = points_time_diff==0 ? 0: (int) (100*curr_time_diff/points_time_diff);
					jpb.setValue(barvalue);
					
					
				}
			};
			
			timer.scheduleAtFixedRate(ttask, 0, (long)(timescaler*1000));
			
			jpb.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
										
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					ttask.setCurrentTime((float)arg0.getX() / (float)jpb.getWidth());
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		}
		
		
		
		
		
		public static void plotTrack2(final PhoneData[] before,final PhoneData[] after, final int row, final int col, float timescaler){
			Image im = new ImageIcon("map.jpg").getImage(); 
			
			final String[] label = new String[]{
				"Before filtering", "After filtering"
			};
			final PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
			plot.setAxisRange(0, 1100, 0, 500);
			plot.setRangeAxisInverted(true);
			plot.setSeriesLinesVisble(label[0], true);
			plot.setSeriesLinesVisble(label[1], true);
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
			
			
			final JLabel jlabel1 = new JLabel();	// Label for "Playing at XX Speed"
			jlabel1.setText("Playing at "+(1/timescaler)+"X speed");
			
			final JLabel jlabel2 = new JLabel();	// Label for showing the current point number
			final JLabel jlabel3 = new JLabel();	// Label for showing start time
			
			final JLabel jlabel4 = new JLabel();	// Label for showing end time
			
					
			final JProgressBar jpb = new JProgressBar();	//ProgressBar for showing the current time
			jpb.setStringPainted(true);
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			frame.add(panel, BorderLayout.SOUTH);
			
			
			JPanel subpanel = new JPanel();
			subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.LINE_AXIS));
			
			panel.add(jlabel1);
			jlabel1.setAlignmentX(Container.LEFT_ALIGNMENT);
			subpanel.add(jlabel2);
			subpanel.add(Box.createHorizontalStrut(20));
			subpanel.add(jlabel3);
			subpanel.add(Box.createHorizontalStrut(5));
			subpanel.add(jpb);
			subpanel.add(Box.createHorizontalStrut(5));
			subpanel.add(jlabel4);
			
			subpanel.setAlignmentX(Container.LEFT_ALIGNMENT);
			panel.add(subpanel);
			frame.pack();
			frame.setVisible(true);
			
		
			final Timer timer = new Timer(true);

			final ExtendedTimerTask ttask = new ExtendedTimerTask(){
				private boolean ibeforechanged = false;
				
				private int ibefore = 0;
				private int iafter_at_ibefore = 0;
				private int iafter = 0;
				private long current_time = before[0].ts.getTime()-1000;
				private long curr_time_diff = 0;
				private long points_time_diff = 0;

				public void setCurrentTime(float percent){
					if( ibefore == 0){
						current_time = before[ibefore].ts.getTime()+ (long)(points_time_diff*percent)/1000*1000;
					} else{
						current_time = before[ibefore-1].ts.getTime()+(long)(points_time_diff*percent)/1000*1000;
					}
					curr_time_diff = current_time - before[ibefore-1].ts.getTime();
					jpb.setValue((int)(100*curr_time_diff/points_time_diff));
					jpb.setString(new Timestamp(current_time).toString());
					
					while (current_time >= after[iafter-1].ts.getTime()){
						// Get the attributes
						Double r = getAttribute(after[iafter], row);;
						  
						Double c = getAttributeDouble(after[iafter], col);

						plot.addData(label[1], r, c);
						iafter++;
					}
					while (current_time < after[iafter-1].ts.getTime()){
						// Get the attributes
						plot.removeData(label[1], plot.getItemCount(label[1]));
						iafter--;
					}
				}
				public void run(){
					
					current_time+=1000;
//					System.out.println(current_time);
//					System.out.println(track_info[i].ts.getTime());
//					System.out.println((current_time - track_info[i].ts.getTime()));
					while(ibefore<before.length && (current_time - before[ibefore].ts.getTime())>=0){
//						jpb.setValue(0);
						if(ibefore<before.length - 1){
							points_time_diff = before[ibefore+1].ts.getTime() - before[ibefore].ts.getTime();
							jlabel4.setText(before[ibefore+1].ts.toString());
						
						}
						jlabel2.setText("Point "+(ibefore+1)+" / "+before.length);
						jlabel3.setText(before[ibefore].ts.toString());
						
						// Get the attributes
						Double r = getAttribute(before[ibefore], row);
						  
						Double c = getAttributeDouble(before[ibefore], col);
						
						plot.addData(label[0], r, c);
						ibefore++;
						
						ibeforechanged = true;
					}
					
					if(after != null){
						while(iafter<after.length && (current_time - after[iafter].ts.getTime())>=0){
							
							// Get the attributes
							Double r = getAttribute(after[iafter], row);;
							  
							Double c = getAttributeDouble(after[iafter], col);
	
							plot.addData(label[1], r, c);
							iafter++;
							
						}
					}
					
					if(ibeforechanged){
						iafter_at_ibefore = iafter;
						ibeforechanged = false;
					}
					curr_time_diff = current_time - before[ibefore-1].ts.getTime();
					
					if(ibefore == before.length){
						timer.cancel();
						jlabel1.setText("Stopped");
					}
					
					// Set current time
					jpb.setString(new Timestamp(current_time).toString());
					int barvalue = points_time_diff==0 ? 0: (int) (100*curr_time_diff/points_time_diff);
					jpb.setValue(barvalue);
					
					
				}
			};
			
			timer.scheduleAtFixedRate(ttask, 0, (long)(timescaler*1000));
			
			jpb.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
										
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					ttask.setCurrentTime((float)arg0.getX() / (float)jpb.getWidth());
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		}
		/**Plot the track at N points per second
		 * 
		 * @param track_info	Track information contained in the phone data object
		 * @param row
		 * @param col
		 * @param pointspersec	How many second does 1 point take to show
		 */
		public static void plotTrack3(final PhoneData[] track_info, final int row, final int col, float pointspersec){
			Image im = new ImageIcon("map.jpg").getImage(); 
			
			final String[] label = new String[]{
				"Phone data"
			};
			final PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
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
			
			
			final JLabel jlabel1 = new JLabel();	// Label for "Playing at XX Speed"
			jlabel1.setText("Showing "+(1/pointspersec)+"point(s) / second");
			
			final JLabel jlabel2 = new JLabel();	// Label for showing the current point number
			final JLabel jlabel3 = new JLabel();	// Label for showing start time
		
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			frame.add(panel, BorderLayout.SOUTH);
			
			
			JPanel subpanel = new JPanel();
			subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.LINE_AXIS));
			
			panel.add(jlabel1);
			jlabel1.setAlignmentX(Container.LEFT_ALIGNMENT);
			subpanel.add(jlabel2);
			subpanel.add(Box.createHorizontalStrut(20));
			subpanel.add(jlabel3);
			
			subpanel.setAlignmentX(Container.LEFT_ALIGNMENT);
			panel.add(subpanel);
			frame.pack();
			frame.setVisible(true);
			
		
			final Timer timer = new Timer(true);

			final TimerTask ttask = new TimerTask(){
				private int i = 0;

				public void run(){
					jlabel2.setText("Point "+(i+1)+" / "+track_info.length);
					jlabel3.setText(track_info[i].ts.toString());
						
					// Get the attributes
					Double r = getAttributeDouble(track_info[i], row);
					  
					Double c = getAttributeDouble(track_info[i], col);
					plot.addData(label[0], r, c);
					i++;
						
					if(i == track_info.length){
						timer.cancel();
						jlabel1.setText("Stopped");
					}				
					
				}
			};
			
			timer.scheduleAtFixedRate(ttask, 0, (long)(pointspersec*1000));
			
		}

		public static double getAttribute(PhoneData point, int property){
			try{
				  return getAttributeDouble(point, property);
			  } catch (IllegalArgumentException e){
				  try{
					  return (double) getAttributeInt(point, property);
				  } catch (IllegalArgumentException f){
					  throw new IllegalArgumentException(
						"It is not possible to plot graph with the attribute you have defined (3rd argument)"
					  );
				  }
			  }
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
	abstract class ExtendedTimerTask extends TimerTask{
		public abstract void setCurrentTime(float percent);
		
		public abstract void run();
		
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
		
