package graphing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import maths.PhoneData;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;

public class PlotTracks {
	
	public final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", "Yacc", "Zacc", "ModAcc", "ATheta"	

	};
	static Font labelFont = new Font("", Font.BOLD, 13);
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
	
	private static boolean paused = false;

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
		public static void plotTrack2(final PhoneData[] before,final PhoneData[] after, final int row, final int col, final float timescale){
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
			plot.setSeriesPaint(label[0], Color.RED);
			if(label.length == 4){
				plot.setSeriesShape(label[1], new Ellipse2D.Float(-2.0f, -2.0f, 4f, 4f));
				plot.setSeriesPaint(label[1], Color.BLUE);
				plot.setSeriesShape(label[2], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
				plot.setSeriesPaint(label[2], new Color(255,200,0));
				plot.setSeriesShape(label[3], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
				plot.setSeriesPaint(label[3], new Color(0,200,255));
			} else {
				plot.setSeriesShape(label[1], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
				plot.setSeriesPaint(label[1], new Color(255,200,0));
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
			
			
			final String PLAY = "<html> "+(char)9658+" Playing at "+(1/timescale)+"X speed <i>(Press space to pause)</i></html>";
			final String STOP = (char)9632+" Stopped";
			final String PAUSE = " "+(char)73+(char)73+" Paused";
			final JLabel jlabel1 = new JLabel(PLAY);	// Label for "Playing at XX Speed"
			final JLabel jlabel2 = new JLabel();	// Label for showing the current point number
			final JLabel jlabel3 = new JLabel();	// Label for showing start time
			final JLabel jlabel4 = new JLabel();	// Label for showing end time
			
//			labelFont = jlabel1.getFont().deriveFont(13);
			jlabel1.setFont(labelFont);
			jlabel2.setFont(labelFont);
			jlabel3.setFont(labelFont);
			jlabel4.setFont(labelFont);
			
			
			final JProgressBar jpb = new JProgressBar();	//ProgressBar for showing the current time
			jpb.setStringPainted(true);
			jpb.setFont(labelFont);
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			frame.add(panel, BorderLayout.SOUTH);
			panel.setBackground(Color.WHITE);
			
			JPanel subpanel = new JPanel();
			subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.LINE_AXIS));
//			subpanel.setBackground(Color.WHITE);
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
			
			jpb.setForeground(new Color(100,100,255));
			jpb.setBackground(Color.WHITE);
//			jpb.setBorderPainted(false);
			jpb.setBorder(BorderFactory.createLineBorder(subpanel.getBackground()));
			
			jpb.setUI(new BasicProgressBarUI() {
			      protected Color getSelectionBackground() { return Color.BLUE; }
			      protected Color getSelectionForeground() { return Color.WHITE; }
			    });
			
			final TimerEventsListener tel = new TimerEventsListener(){

				@Override
				public void currentTimeUpdated(Timestamp curr_time, int percent) {
					// TODO Auto-generated method stub
					// Set current time
					jpb.setString(curr_time.toString());
					jpb.setValue(percent);
					jpb.revalidate();
					jpb.repaint();
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
					paused = true;
					jlabel1.setText(STOP);
					
				}
				
			};
			
			final TimeLine tl = new TimeLine(before, after, (int)(100/timescale), tel, plot, row, col, label);
			ttask = new ExtendedTimerTask(tl, ExtendedTimerTask.TIME);
			
			timer.scheduleAtFixedRate(ttask, 0, 100);
			
			frame.addKeyListener(new KeyListener(){

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getKeyCode() == KeyEvent.VK_SPACE){
						if(!tl.getTimeLineFinished()){
							paused = !paused;
							if(paused){
								timer.cancel();
								jlabel1.setText("<html> "+PAUSE+" <i>(Press space to resume)</i></html>");
							} else {
								timer = new Timer(true);
								ttask = new ExtendedTimerTask(tl, ExtendedTimerTask.TIME);
								timer.scheduleAtFixedRate(ttask, 0, 100);
								jlabel1.setText(PLAY);
	
							}
						}
					} else if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
						tl.setCurrentPoint(tl.getCurrentPoint()-1);
						if(!tl.getTimeLineFinished() && paused)
							jlabel1.setText("<html> "+PAUSE+" <i>(Press space to resume)</i></html>");
					} else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
						tl.setCurrentPoint(tl.getCurrentPoint()+1);
						if(!tl.getTimeLineFinished() && paused)
							jlabel1.setText("<html> "+PAUSE+" <i>(Press space to resume)</i></html>");
					}
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});

			
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
					if(!paused && !tl.getTimeLineFinished()){
						timer.cancel();
						jlabel1.setText("<html> "+PAUSE+" <i>(Release LMB to resume)</i> </html>");
					}
					tl.setCurrentTime((float)arg0.getX() / (float)jpb.getWidth());
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(!paused && !tl.getTimeLineFinished()){
						timer = new Timer(true);
						ttask = new ExtendedTimerTask(tl, ExtendedTimerTask.TIME);
						timer.scheduleAtFixedRate(ttask, 0, 100);
						jlabel1.setText(PLAY);
					}
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
		public static void plotTrack3(final PhoneData[] track_info, final int row, final int col, final float pointspersec){
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
			
			
			final String PLAY = "<html> "+(char)9658+" Showing at "+(1/pointspersec)+" point(s) / second <i>(Press space to pause)</i></html>";
			final String STOP = (char)9632+" Stopped";
			final String PAUSE = " "+(char)73+(char)73+" Paused";
			
			final JLabel jlabel1 = new JLabel(PLAY);	// Label for "Playing at XX Speed"			
			final JLabel jlabel2 = new JLabel();	// Label for showing the current point number
			final JLabel jlabel3 = new JLabel();	// Label for showing start time
			jlabel1.setFont(labelFont);
			jlabel2.setFont(labelFont);
			jlabel3.setFont(labelFont);

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
		
			final TimerEventsListener tel = new TimerEventsListener(){

				@Override
				public void currentTimeUpdated(Timestamp curr_time, int percent) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void pointsUpdated(int index) {
					// TODO Auto-generated method stub
					jlabel2.setText("Point "+(index+1)+" / "+track_info.length);
					jlabel3.setText(track_info[index].ts.toString());
				}

				@Override
				public void timerStopped() {
					// TODO Auto-generated method stub
					jlabel1.setText(STOP);
				}
				
			};
			
			final TimeLine tl = new TimeLine(track_info, null, 1000, tel, plot, row, col, label);
			ttask = new ExtendedTimerTask(tl, ExtendedTimerTask.POINTS);

			frame.addKeyListener(new KeyListener(){

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getKeyCode() == KeyEvent.VK_SPACE){
						if(!tl.getTimeLineFinished()){
							paused = !paused;
							if(paused){
								timer.cancel();
								jlabel1.setText("<html> "+PAUSE+" <i>(Press space to resume)</i></html>");
							} else {
								timer = new Timer(true);
								ttask = new ExtendedTimerTask(tl, ExtendedTimerTask.POINTS);
								timer.scheduleAtFixedRate(ttask, 0, (long)(pointspersec*1000));
								jlabel1.setText(PLAY);
	
							}
						}
					} else if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
						tl.setCurrentPoint(tl.getCurrentPoint()-1);
						if(!tl.getTimeLineFinished() && paused)
							jlabel1.setText("<html> "+PAUSE+" <i>(Press space to resume)</i></html>");
					} else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
						tl.setCurrentPoint(tl.getCurrentPoint()+1);
						if(!tl.getTimeLineFinished() && paused)
							jlabel1.setText("<html> "+PAUSE+" <i>(Press space to resume)</i></html>");
					}
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});

			timer = new Timer();
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
		final static int TIME = 0;
		final static int POINTS = 1;
		
		final private TimeLine tl;	
		// Set if it is to play against real time or points
		private int play_against;
		
		public ExtendedTimerTask(final TimeLine tl, int play_against){
			this.tl = tl;
			this.play_against = play_against;
		}
		
		public void run(){
			if(play_against == TIME)
				tl.advanceTime();
			else if(play_against == POINTS)
				tl.setCurrentPoint(tl.getCurrentPoint()+1);
			else throw new IllegalArgumentException ("Unrecognised paramter passed to the constructor");
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

		private long points_time_diff2;

		private long curr_time_diff2;

		private boolean temppointsadded = false;
		private boolean temppointsadded2 = false;

		private boolean iafterchanged = false;

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

		public void setCurrentPoint(int index){
			if(index<0)
				index = 0;
			else if (index >= before.length)
				index = before.length-1;
			
			etel.pointsUpdated(index);
			setTimeLineFinished(false);
			setCurrentTime(index, 0);
			
		}
		public void setCurrentTime(float percent){
			setCurrentTime(getCurrentPoint(), percent);
		}
		private synchronized void setCurrentTime(int index, float percent){
			current_time = before[index].ts.getTime()+ (long)(points_time_diff*percent)/interval*interval - interval;
			
			if(!finished){
				// Rewind - Remove recently added points until the set index has reached
				while (ibefore>0 && index<ibefore){
					// Remove the last point if there is one
					if(plot.getItemCount(labels[0])>0)
						plot.removeData(labels[0], plot.getItemCount(labels[0])-1);
					ibefore--;
				}
				if(after != null){
					// Fastforward - Add points until the set index has reached
					while (iafter<after.length && current_time >= after[iafter].ts.getTime()){
						// Get the attributes
						Double r = PlotTracks.getAttributeDouble(after[iafter], row);
						Double c = PlotTracks.getAttributeDouble(after[iafter], col);
		
						if(labels.length == 4){
							if(plot.getItemCount(labels[3])>0)
								plot.removeData(labels[3], plot.getItemCount(labels[3])-1);
							plot.addData(labels[3], r, c);
						}
						plot.addData(labels[1], r, c);
						iafter++;
					}
					// Rewind - Remove recently added points until the set index has reached or 
					while (iafter>0 && current_time < after[iafter-1].ts.getTime()){
						plot.removeData(labels[1], plot.getItemCount(labels[1])-1);
						if(labels.length == 4 && after!= null){
							if(plot.getItemCount(labels[3])>0)
								plot.removeData(labels[3], plot.getItemCount(labels[3])-1);
							plot.addData(labels[3], plot.getData(labels[1], plot.getItemCount(labels[1])-1));
						}
						iafter--;
					}
				}
				advanceTimeOnce();
				etel.currentTimeUpdated(new Timestamp(current_time), points_time_diff==0 ? 0 :(int)(100*curr_time_diff/points_time_diff));
			}

		}
		public long getCurrentTime(){
			return current_time;
		}
		public int getCurrentPoint(){
			//If the first point hasn't been plotted (i.e. index = -1), set index = 0
			return ibefore == 0 ? 0 : ibefore-1;
		}
		public void setTimeLineFinished(boolean finished){
			this.finished = finished;
		}
		public boolean getTimeLineFinished(){
			return finished;
		}
		public synchronized void advanceTime(){
			advanceTime(false);
		}
		public synchronized void advanceTimeOnce(){
			advanceTime(true);
		}
		public synchronized void advanceTime(boolean once){
			if(! finished){
				current_time+=interval;
				// If once == true - Add one point
				// Otherwise - Add all points whose timestamp is behind the current time by looping this while loop
				while(ibefore<before.length && (current_time - before[ibefore].ts.getTime())>=0){
	
					points_time_diff = (ibefore<before.length - 1)? before[ibefore+1].ts.getTime() - before[ibefore].ts.getTime() : 0;
					etel.pointsUpdated(ibefore);
					
					// Get the attributes
					Double r = PlotTracks.getAttributeDouble(before[ibefore], row);	  
					Double c = PlotTracks.getAttributeDouble(before[ibefore], col);
					if(temppointsadded && plot.getItemCount(labels[0])>0){
						plot.removeData(labels[0], plot.getItemCount(labels[0])-1);
						temppointsadded = false;
					}
					plot.addData(labels[0], r, c);
					ibefore++;
					
					ibeforechanged = true;
					if(once)
						break;
				}
				
				
				if(after != null){
					// Add all points whose timestamp is behind the current time by looping this while loop
					while(iafter<after.length && (current_time - after[iafter].ts.getTime())>=0){
						points_time_diff2 = (iafter<after.length - 1)? after[iafter+1].ts.getTime() - after[iafter].ts.getTime() : 0;

						// Get the attributes
						Double r = PlotTracks.getAttributeDouble(after[iafter], row);
						Double c = PlotTracks.getAttributeDouble(after[iafter], col);
	
						if(temppointsadded2 && plot.getItemCount(labels[1])>0){
							plot.removeData(labels[1], plot.getItemCount(labels[1])-1);
							temppointsadded2 = false;
						}
						plot.addData(labels[1], r, c);
						iafter++;
						iafterchanged = true;
					}
					// Calculate time difference between the current time and latest added after filtering point
					curr_time_diff2 = current_time - after[iafter-1].ts.getTime();
				}
			}
			
			// Calculate time difference between the current time and latest added before filtering point
			curr_time_diff = current_time - before[ibefore-1].ts.getTime();
			// Calculate the estimated before filtering position
			if(ibefore < before.length && ( (labels.length == 4 && after != null)|| (after== null && labels.length == 2))){
				double[] result = estPosition(before[ibefore-1], before[ibefore], curr_time_diff, points_time_diff);
				
				if(labels.length == 4 && after != null){
					if(plot.getItemCount(labels[2]) > 0)
						plot.removeData(labels[2],  plot.getItemCount(labels[2])-1);
					plot.addData(labels[2], result[0], result[1]);
					
				} else {
					if(plot.getItemCount(labels[1]) > 0)
						plot.removeData(labels[1],  plot.getItemCount(labels[1])-1);
					plot.addData(labels[1], result[0], result[1]);
				}
				if(!ibeforechanged && plot.getItemCount(labels[0])>0)
					plot.removeData(labels[0],  plot.getItemCount(labels[0])-1);
				plot.addData(labels[0], result[0], result[1]);
				temppointsadded = true;
			}
			// Calculate the estimated after filtering position
			if(after != null && iafter < after.length && labels.length == 4){
				double[] result = estPosition(after[iafter-1], after[iafter], curr_time_diff2, points_time_diff2);
				
				if(plot.getItemCount(labels[3]) > 0)
					plot.removeData(labels[3],  plot.getItemCount(labels[3])-1);
				plot.addData(labels[3], result[0], result[1]);
				
				if(!iafterchanged && plot.getItemCount(labels[1])>0 )
					plot.removeData(labels[1],  plot.getItemCount(labels[1])-1);
				plot.addData(labels[1], result[0], result[1]);
				temppointsadded2 = true;
			}
			
			if(ibeforechanged){
				ibeforechanged = false;
			}
			if(iafterchanged){
				iafterchanged = false;
			}
			
			
			if(ibefore == before.length){
				etel.timerStopped();
				finished = true;
				points_time_diff = 0;
			}
			
			etel.currentTimeUpdated(new Timestamp(current_time/100*100), points_time_diff==0 ? 0: (int) (100*curr_time_diff/points_time_diff));
			
		}
		
		private double[] estPosition(PhoneData pre_point, PhoneData aft_point, long curr_time_difference, long points_time_difference){
			double px = PlotTracks.getAttribute(pre_point, row);
			double py = PlotTracks.getAttributeDouble(pre_point, col);
			double nx = PlotTracks.getAttribute(aft_point, row);
			double ny = PlotTracks.getAttributeDouble(aft_point, col);
			float cur_faction = (float)curr_time_difference / (float)points_time_difference;
			return new double[]{ px+(nx-px)*cur_faction, py+(ny-py)*cur_faction};
			
		}
	}	
