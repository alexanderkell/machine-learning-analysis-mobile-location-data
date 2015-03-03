package graphing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
import org.jfree.data.xy.XYDataItem;

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
	private static Timer timer;
	private static ExtendedTimerTask ttask;

	/**
	 * @deprecated
	 * @param track_info
	 * @param row
	 * @param col
	 * @param timescaler
	 */
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
		 * @param row	 The type of attribute of the row axis
		 * @param col	 The type of attribute of the column axis
		 * @param timescale	Time scaler: Specify how long 1 second is
		 */
		public static void plotTrack2(final PhoneData[] track_info, final int row, final int col, final float timescale){
			plotTrack2(track_info, null, row, col, timescale);
		}	
		
		/** Plot the track before filtering and after filtering against time
		 * 	
		 * @param before	Track before filtering contained in the PhoneData object
		 * @param after		Track after filtering contained in the PhoneData object
		 * @param row	 The type of attribute of the row axis
		 * @param col	 The type of attribute of the column axis
		 * @param timescale Time scaler: Specify how long 1 second is
		 */
		public static void plotTrack2(final PhoneData[] before,final PhoneData[] after, final int row, final int col, final float timescale
				){
			Image im = new ImageIcon("map.jpg").getImage(); 
			
			final String[] label;
			if(after == null){
				label = new String[]{
						"Phone data", "Last point"
				};
			} else{
				label = new String[]{
				"Before filtering", "After filtering", "Before filter last point", "After filter last point"
				};
			}
			final PlotHelper plot = new PlotHelper(COLUMNS[row]+" vs "+COLUMNS[col], COLUMNS[row], COLUMNS[col], label);
			plot.setAxisRange(0, 1100, 0, 500);
			plot.setRangeAxisInverted(true);
			plot.setSeriesLinesVisble(label[0], true);
			plot.setSeriesShape(label[0], new Ellipse2D.Float(-2.0f, -2.0f, 4f, 4f));
			plot.setSeriesShape(label[1], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
			if(label.length == 4){
				plot.setSeriesShape(label[2], new Ellipse2D.Float(-2.0f, -2.0f, 4f, 4f));
				plot.setSeriesShape(label[3], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
			}
			if(after != null)
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
			
			final JLabel jlabel1 = new JLabel("Playing at "+(1/timescale)+"X speed");	// Label for "Playing at XX Speed"
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
			
			
			
			timer = new Timer(true);
			
			final TimerEventsListener tel = new TimerEventsListener(){

				@Override
				public void currentTimeUpdated(Timestamp curr_time, int percent) {
					// TODO Auto-generated method stub
					// Set current time
					jpb.setString(curr_time.toString());
					jpb.setValue(percent);
				}

				@Override
				public void pointsUpdated(int index) {
					// TODO Auto-generated method stub
					if(index<before.length - 1){
						jlabel4.setText(before[index+1].ts.toString());
					}
					jlabel2.setText("Point "+(index+1)+" / "+before.length);
					jlabel3.setText(before[index].ts.toString());
				}

				@Override
				public void timerStopped() {
					// TODO Auto-generated method stub
					timer.cancel();
					jlabel1.setText("Stopped");
					
				}
				
			};
			final TimeLine tl = new TimeLine(before, after, (int)(100/timescale), tel, plot, row, col, label);
			ttask = new ExtendedTimerTask(tl);
			
//			ttask.setTimeInterval((int)(100/timescaler));
			timer.scheduleAtFixedRate(ttask, 0, 100);
			
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
					timer.cancel();
					tl.setCurrentTime((float)arg0.getX() / (float)jpb.getWidth());
					jlabel1.setText("Paused");
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					timer = new Timer(true);
					ttask = new ExtendedTimerTask(tl);
					timer.scheduleAtFixedRate(ttask, 0, 100);
					jlabel1.setText("Playing at "+(1/timescale)+"X speed");
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
	interface TimerEventsListener{
		
		/**Called when the time is updated
		 * return the current time and percentage (out of 100) of time passed the previous point
		 * 
		 * @param curr_time	current time
		 * @param percent percentage (out of 100) of time passed the previous point
		 */
		public abstract void currentTimeUpdated(Timestamp curr_time, int percent);
		
		/**Called when the time is updated
		 * 
		 * @param index
		 */
		public abstract void pointsUpdated(int index);
		/**Called when the timer stops
		 * 
		 */
		public abstract void timerStopped();
	}

	
	class ExtendedTimerTask extends TimerTask{
		final private TimeLine tl;
		public ExtendedTimerTask(final TimeLine tl){
			this.tl = tl;
		}
		
		public void run(){
			tl.advanceTime();
		}
	};
	class TimeLine{

		private boolean ibeforechanged = false;
		
		private int ibefore = 0, iafter = 0;
		private long current_time;
		private long curr_time_diff = 0;
		private long points_time_diff = 0;

		private int interval = 1000;

		private PhoneData[] before, after;

		private TimerEventsListener etel;

		private final PlotHelper plot;

		private int row;
		private int col;

		private String[] labels;
		private boolean finished = false;	// Indicates whether the timeline has reach the end

		public TimeLine(final PhoneData[] before,final PhoneData[] after, int interval, TimerEventsListener etel, PlotHelper plot, int row, int col, String[] labels){
			this.before = before;
			this.after = after;
			current_time = before[0].ts.getTime();
			this.interval = interval;
			this.etel = etel;
			this.plot = plot;
			this.row = row;
			this.col = col;
			this.labels = labels;
			plot.setSeriesRenderingOrder(false);
		}
		public void setTimeInterval(int interval){
			this.interval = interval;
		}
		public void setCurrentTime(float percent){
			if( ibefore == 0){
				current_time = before[ibefore].ts.getTime()+ (long)(points_time_diff*percent)/interval*interval;
			} else{
				current_time = before[ibefore-1].ts.getTime()+(long)(points_time_diff*percent)/interval*interval;
			}
			curr_time_diff = current_time - before[ibefore-1].ts.getTime();
			etel.currentTimeUpdated(new Timestamp(current_time), (int)(100*curr_time_diff/points_time_diff));
			
			if(after != null){
				while (current_time >= after[iafter].ts.getTime()){
					// Get the attributes
					Double r = PlotTracks.getAttribute(after[iafter], row);;
					  
					Double c = PlotTracks.getAttributeDouble(after[iafter], col);
	
					if(labels.length == 4){
						plot.removeData(labels[3], plot.getItemCount(labels[3])-1);
						plot.addData(labels[3], r, c);
					}
					plot.addData(labels[1], r, c);
					iafter++;
				}
				while (current_time < after[iafter-1].ts.getTime()){
					// Get the attributes
					XYDataItem xydi = plot.removeData(labels[1], plot.getItemCount(labels[1])-1);
					if(labels.length == 4){
						plot.removeData(labels[3], plot.getItemCount(labels[3])-1);
						plot.addData(labels[3], xydi);
					}
					iafter--;
				}
			}
		}
		public long getCurrentTime(){
			return current_time;
		}
		public void setTimeLineFinished(boolean finished){
			this.finished = finished;
		}
		public boolean getTimeLineFinished(){
			return finished;
		}
		public void advanceTime(){
			if(! finished){
				current_time+=interval;
				while(ibefore<before.length && (current_time - before[ibefore].ts.getTime())>=0){
	
					points_time_diff = (ibefore<before.length - 1)? before[ibefore+1].ts.getTime() - before[ibefore].ts.getTime() : 0;
					
					etel.pointsUpdated(ibefore);
					
					// Get the attributes
					Double r = PlotTracks.getAttribute(before[ibefore], row);
					  
					Double c = PlotTracks.getAttributeDouble(before[ibefore], col);
					
					plot.addData(labels[0], r, c);
					if(labels.length == 4){
						if(plot.getItemCount(labels[2]) > 0)
							plot.removeData(labels[2], plot.getItemCount(labels[2])-1);
						plot.addData(labels[2], r, c);
					} else if (after== null && labels.length == 2){
						if(plot.getItemCount(labels[1]) > 0)
						plot.removeData(labels[1], plot.getItemCount(labels[1])-1);
						plot.addData(labels[1], r, c);
					}
					ibefore++;
					
					ibeforechanged = true;
				}
				
				if(after != null){
					while(iafter<after.length && (current_time - after[iafter].ts.getTime())>=0){
						
						// Get the attributes
						Double r = PlotTracks.getAttribute(after[iafter], row);;
						  
						Double c = PlotTracks.getAttributeDouble(after[iafter], col);
	
						plot.addData(labels[1], r, c);
						if(labels.length == 4){
							if(plot.getItemCount(labels[3]) > 0)
								plot.removeData(labels[3], plot.getItemCount(labels[3])-1);
							plot.addData(labels[3], r, c);
						}
						iafter++;
						
					}
				}
			}
			
			if(ibeforechanged){
				ibeforechanged = false;
			}
			curr_time_diff = current_time - before[ibefore-1].ts.getTime();
			
			if(ibefore == before.length){
				etel.timerStopped();
				finished = true;
				points_time_diff = 0;
			}
			
			etel.currentTimeUpdated(new Timestamp(current_time/100*100), points_time_diff==0 ? 0: (int) (100*curr_time_diff/points_time_diff));
			
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
		
