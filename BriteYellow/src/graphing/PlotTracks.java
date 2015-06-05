package graphing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

import objects.PhoneData;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;

public class PlotTracks implements ActionListener,ChangeListener, KeyListener,MouseListener,MouseMotionListener, WindowFocusListener, PropertyChangeListener{
	
	public final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", 
		"Yacc", "Zacc", "ModAcc", "ATheta"
	};
	
	static ImageIcon loading = new ImageIcon("src\\dialogs\\loadingSmall.gif", "Loading");
	static Font labelFont = new Font("normal", Font.BOLD, 14);
	static Font labelFontLarge = new Font("large", Font.BOLD, 18);
	final static String PLAYSYMBOL = ""+(char)9658;
	final static String PAUSESYMBOL = ""+(char)73+(char)73;
	final static String STOPSYMBOL = ""+(char)9632;
	
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
	
	// Timer and TimerTask
	private static Timer timer;
	private static ExtendedTimerTask ttask;
	
	// Indicate if the timer is paused or playing
	private static boolean paused = false;
	
	// Declaration of static int fields for variable "points_or_time"
	public final static int TIME = -2;
	public final static int POINTS = -1;
	
	// Components in the player
	final private JButton jbutton1;
	final private JButton jbutton2;
	final private JButton jbutton3;
	
	final private JButton jlabel1;
	final private JLabel jlabel2;
	final private JLabel jlabel3;
	final private JProgressBar jpb;
	private TimeLine tl;
	private int points_or_time;
	private float period;
	private String PLAY;
	private String PAUSE;
	private String STOP;
	private String PAUSE2;
	private boolean helddown;
	final private JButton jlabelA;
	private Thread butthread;
	private JSpinner jspinner1;
	private JButton jbuttonDone;
	private JDialog internal_dialog;
	private JButton jlabelB;
	private JButton jbutton5;
	private JButton jbutton4;
	private TrackChangeListener tcl;
	private JLabel jlabel5;
	private String[] label;
	private int row;
	private int col;
	private PlotHelper plot;
	private JFrame frame;
	private JProgressBar durationbar;
	
	// For the change track functions
	private int max_tracks;
	private static int current_track = 1;
	private JPanel subpanel1;
	private JLabel jlabel7;
	private JButton jbuttonDone2;
	private JSpinner jspinner2;
	private JDialog internal_dialog2;
	private JPanel subpanel1a;
	private Thread updateTrackThread;
	private PhoneData[] before;

	private JDialog internal_dialogA;

	private JFormattedTextField jFTextFieldA1;

	private JButton jbuttonDoneA;

	private JComboBox<String> jlabelA2;
	

	/**
	 * @deprecated
	 * @param track_info
	 * @param row
	 * @param col
	 * @param timescaler
	 */
	@Deprecated
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
		new PlotTracks(before, after, row, col, TIME, timescale);
	}
	public static void plotTrack2(final int row, final int col, final float timescale, TrackChangeListener tcl, int max_tracks){
		new PlotTracks(row, col, TIME, timescale, tcl, max_tracks);
	}
	/**Plot the track at N points per second
	 * 
	 * @param track_info	Track information contained in the phone data object
	 * @param row
	 * @param col
	 * @param pointspersec	How many second does 1 point take to show
	 */
	public static void plotTrack3(final PhoneData[] track_info, final int row, final int col, final float pointspersec){
		new PlotTracks(track_info, null, row, col, POINTS, pointspersec);
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
	
	/**
	 * @deprecated as not all PhoneData has the date in the String format
	 * @param p
	 * @param attribute
	 * @return
	 */
	@Deprecated
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

	private PlotTracks(final int row, final int col, int points_or_time, float period, TrackChangeListener tcl, int max_tracks){
		this(tcl.setTrack(current_track), null, row, col, points_or_time, period, true);
		this.tcl = tcl;
		this.max_tracks = max_tracks;	// Total amount of tracks
		
		jlabelB = new JButton();
		jlabelB.setFont(labelFont);
		jlabelB.setText("Track "+current_track+" / "+max_tracks+" ("+
				before[0].ts.toString()+" - "+before[before.length-1].ts.toString()+")");
		
		
		jbutton4 = new JButton("|<<");
		jbutton5 = new JButton(">>|");
		
		subpanel1a.setLayout(new BoxLayout(subpanel1a, BoxLayout.LINE_AXIS));
		subpanel1a.add(Box.createHorizontalGlue());
		subpanel1a.add(jbutton4);
		subpanel1a.add(jlabelB);
		subpanel1a.add(jbutton5);
		subpanel1a.add(Box.createHorizontalGlue());
	

		customiseButtons(jbutton4,3,2);
		customiseButtons(jbutton5,3,2);
		customiseButtons(jlabelB,3,1);
		jbutton4.setToolTipText("Previous Track (Page Up)");
		jbutton5.setToolTipText("Next Track (Page Down)");
		jbutton4.addActionListener(this);
		jbutton5.addActionListener(this);
		jlabelB.addActionListener(this);
		
		// For the pop up dialog for changing tracks
		SpinnerModel spinnerModel =
		         new SpinnerNumberModel(1, //initial value
		            1, //min
		            max_tracks, //max
		            1);//step
		jspinner2 = new JSpinner(spinnerModel);
		
		jspinner2.addChangeListener(this);
		jspinner2.setFont(labelFont);
		JLabel jlabel6 = new JLabel("Track: ");
		jlabel6.setFont(labelFont);
		jlabel7 = new JLabel(" / "+max_tracks);
		jlabel7.setFont(labelFont);
		jbuttonDone2 = new JButton("Done");
		jbuttonDone2.setFont(labelFont);
		jbuttonDone2.addActionListener(this);
		
		JPanel jpanel3 = new JPanel();
		jpanel3.setLayout(new BoxLayout(jpanel3,BoxLayout.LINE_AXIS));
		jpanel3.add(jlabel6);
		jpanel3.add(jspinner2);
		jpanel3.add(jlabel7);
		jpanel3.add(Box.createHorizontalStrut(5));
		jpanel3.add(jbuttonDone2);
		jpanel3.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		
		internal_dialog2 = new JDialog();
		internal_dialog2.add(jpanel3);
		internal_dialog2.setUndecorated(true);
		internal_dialog2.pack();
		internal_dialog2.addWindowFocusListener(this);
		
	}
	private PlotTracks(final PhoneData[] before,final PhoneData[] after, final int row, final int col, int points_or_time, float period){
		this(before, after, row,  col, points_or_time, period, true);
		JLabel track_time_length_label = new JLabel("Track duration: ("+before[0].ts.toString()+" - "+before[before.length-1].ts.toString()+")");
		track_time_length_label.setFont(labelFont);
		// create an empty border around the label so that the text position can be lower
		track_time_length_label.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));
		subpanel1a.add(Box.createHorizontalGlue());
		subpanel1a.add(track_time_length_label);
		subpanel1a.add(Box.createHorizontalGlue());
	}
	private PlotTracks(final PhoneData[] before,final PhoneData[] after, final int row, final int col, int points_or_time, float period, boolean check_value){
		if(!check_value)
			throw new IllegalArgumentException("This \"PlotTracks\" constructor should not be called directly");
		this.points_or_time = points_or_time;
		this.period = period;
		this.before = before;
		Image im = new ImageIcon("map.jpg").getImage(); 
		
		this.row = row;
		this.col = col;
		if(after == null){
			label = new String[]{
					"Phone data", null, "Last point", null
			};
		} else{
			label = new String[]{
					"Before filtering", "After filtering", "Before filter last point", "After filter last point"
			};
		}		


		if(points_or_time == TIME){
			PLAY = PLAYSYMBOL+" Playing at "+(1/period)+"x speed";
			PAUSE2 = "<html>"+PAUSESYMBOL+" Paused <i>(Release LMB to resume)</i></html>";
		} else if(points_or_time == POINTS){
			PLAY = PLAYSYMBOL+" Showing "+(1/period)+" point(s) / second";	
		}
		PAUSE = PAUSESYMBOL+" Paused";
		STOP = STOPSYMBOL+" Stopped";
		
		jbutton1 = new JButton(PLAYSYMBOL);	// Label for "Playing at XX speed or points/sec"
		jbutton2 = new JButton("<<");
		jbutton3 = new JButton(">>");
		jlabel1 = new JButton();	// Label for showing the current point number
		jlabel2 = new JLabel();	// Label for showing start time
		jlabel3 = new JLabel();	// Label for showing end time
		jlabelA = new JButton(PLAY); // Label for showing the player status
		jlabelA.setHorizontalAlignment(JLabel.RIGHT);
		
		
		jbutton1.setFont(labelFontLarge);
		jbutton1.setPreferredSize(new Dimension(45,40));

		jbutton2.setFont(labelFont);
		jbutton3.setFont(labelFont);
		jlabel1.setFont(labelFont);
		jlabel2.setFont(labelFont);
		jlabel3.setFont(labelFont);
		jlabelA.setFont(labelFont);

		jlabel1.addActionListener(this);
		jlabelA.addActionListener(this);
		jlabelA.setToolTipText("Click to change the playing speed");
		customiseButtons(jlabelA,3,2);
		
		// For the pop up dialog for changing points
		SpinnerModel spinnerModel =
		         new SpinnerNumberModel(1, //initial value
		            1, //min
		            before.length, //max
		            1);//step
		jspinner1 = new JSpinner(spinnerModel);
		
		jspinner1.addChangeListener(this);
		jspinner1.setFont(labelFont);
		JLabel jlabel4 = new JLabel("Point: ");
		jlabel4.setFont(labelFont);
		jlabel5 = new JLabel(" / "+before.length);
		jlabel5.setFont(labelFont);
		jbuttonDone = new JButton("Done");
		jbuttonDone.setFont(labelFont);
		jbuttonDone.addActionListener(this);
		JPanel jpanel2 = new JPanel();
		jpanel2.setLayout(new BoxLayout(jpanel2,BoxLayout.LINE_AXIS));
		jpanel2.add(jlabel4);
		jpanel2.add(jspinner1);
		jpanel2.add(jlabel5);
		jpanel2.add(Box.createHorizontalStrut(5));
		jpanel2.add(jbuttonDone);
		jpanel2.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

		internal_dialog = new JDialog();
		internal_dialog.add(jpanel2);
		internal_dialog.setUndecorated(true);
		internal_dialog.addWindowFocusListener(this);
		
		jpb = new JProgressBar();	//ProgressBar for showing the current time
		jpb.setStringPainted(true);
		jpb.setFont(labelFont);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		subpanel1 = new JPanel();
		subpanel1a = new JPanel();
//		subpanel1a.setAlignmentX(Component.CENTER_ALIGNMENT);
		subpanel1.setLayout(new BorderLayout());
		subpanel1.setAlignmentX(Container.CENTER_ALIGNMENT);
		subpanel1.add(jbutton1, BorderLayout.WEST);
		subpanel1.add(subpanel1a, BorderLayout.CENTER);
		// Add a horizontal glue so that the jlabelA can be appear at the right hand side of the frame
		
		subpanel1.add(jlabelA, BorderLayout.EAST);
		subpanel1.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		
		// Create and configure components
		JPanel subpanel2 = new JPanel();
		subpanel2.setLayout(new BoxLayout(subpanel2, BoxLayout.LINE_AXIS));
		subpanel2.setAlignmentX(Container.CENTER_ALIGNMENT);
		subpanel2.add(jbutton2);
		subpanel2.add(jlabel1);
		subpanel2.add(jbutton3);
		subpanel2.add(Box.createHorizontalStrut(20));
		subpanel2.add(jlabel2);
		if(points_or_time == TIME){
			subpanel2.add(Box.createHorizontalStrut(5));
			subpanel2.add(jpb);
			subpanel2.add(Box.createHorizontalStrut(5));
			subpanel2.add(jlabel3);
		}
		subpanel2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		
		durationbar = new JProgressBar();
		durationbar.setPreferredSize(new Dimension(durationbar.getWidth(), 4));
		durationbar.setForeground(new Color(50,50,255));
		durationbar.setBorderPainted(false);
		
		
		panel.add(subpanel2);
		panel.add(subpanel1);
		panel.add(durationbar);
		
		// setup the progressbar showing the time between the current and next point
		jpb.setForeground(new Color(100,100,255));
		jpb.setBackground(Color.WHITE);
		jpb.setBorder(BorderFactory.createLineBorder(subpanel2.getBackground()));
		
		jpb.setUI(new BasicProgressBarUI() {
		      protected Color getSelectionBackground() { return Color.BLUE; }
		      protected Color getSelectionForeground() { return Color.WHITE; }
		    });
		
	
		TimerEventsListener tel = setupTEL();
		// Refresh rate of TimeLine is 10/second
		tl = new TimeLine(before, after, (int)(100/period), tel, row, col, label[0], label[1], label[2], label[3]);

		
		// Get and configure the plot
		plot = tl.getPlot();
		plot.setSeriesRenderingOrder(false);
		plot.setAxisRange(0, 1175, 0, 500);
		plot.setRangeAxisInverted(true);
		plot.setSeriesLinesVisble(label[0], true);
		plot.setSeriesShape(label[0], new Ellipse2D.Float(-2.0f, -2.0f, 4f, 4f));
		plot.setSeriesPaint(label[0], Color.RED);
		plot.setSeriesShape(label[2], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
		plot.setSeriesPaint(label[2], new Color(255,200,0));	// Yellow

		if(after != null){
			plot.setSeriesLinesVisble(label[1], true);
			plot.setSeriesShape(label[1], new Ellipse2D.Float(-2.0f, -2.0f, 4f, 4f));
			plot.setSeriesPaint(label[1], Color.BLUE);
			plot.setSeriesShape(label[3], new Rectangle2D.Float(-3.0f, -3.0f, 6f, 6f));
			plot.setSeriesPaint(label[3], new Color(0,200,255));	// Blue Green
		}

		XYPlot xyplot = plot.getXYPlot();
		// Clear background paint
		xyplot.setBackgroundPaint(null);
		// Set background image to the map
		xyplot.setBackgroundImage(im);
		
		// Create and configure JFrame and add the components to it
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(plot.getChartPanel(), BorderLayout.CENTER);
		frame.add(panel, BorderLayout.SOUTH);
		frame.pack();

		customiseButtons(jbutton1,3,6);
		customiseButtons(jbutton2,3,1);
		customiseButtons(jbutton3,3,1);
		customiseButtons(jlabel1,3,1);
		jlabel1.setToolTipText("Click to change current point");	
		jbutton2.setToolTipText("Previous Point (\u2190)");
		jbutton3.setToolTipText("Next Point (\u2192)");
		
		jbutton1.addActionListener(this);
		jbutton2.addMouseListener(this);
		jbutton3.addMouseListener(this);
		

		
		// Pack the frame again in case something doesn't fit well in the frame
		frame.pack();
		frame.addKeyListener(this);
		
		jpb.addMouseListener(this);
		jpb.addMouseMotionListener(this);
		frame.setVisible(true);
		play();
		
		
		JPanel jpanelA = new JPanel();
		jpanelA.setLayout(new BoxLayout(jpanelA,BoxLayout.LINE_AXIS));

		
		JLabel jlabelA1 = new JLabel("Speed: ");
		jlabelA1.setFont(labelFont);
		jlabelA2 = new JComboBox<String>(
				new String[]{"x"," points/second"}
				);
		jlabelA2.setFont(labelFont);
		if(points_or_time == TIME){
			jlabelA2.setSelectedIndex(0);
		} else if(points_or_time == POINTS){
			jlabelA2.setSelectedIndex(1);
		}
		
		jlabelA2.addActionListener(this);
		
		jbuttonDoneA = new JButton("Done");
		jbuttonDoneA.setFont(labelFont);
		jbuttonDoneA.addActionListener(this);
		
		jpanelA.add(jlabelA1);
		jFTextFieldA1 = new JFormattedTextField();
		jFTextFieldA1.setValue(1/period);

		jFTextFieldA1.addPropertyChangeListener("value", this);
		jpanelA.add(jFTextFieldA1);

		jpanelA.add(jlabelA2);
		jpanelA.add(Box.createHorizontalStrut(5));
		jpanelA.add(jbuttonDoneA);
		jpanelA.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		
		internal_dialogA = new JDialog();
		internal_dialogA.add(jpanelA);
		internal_dialogA.setUndecorated(true);
		internal_dialogA.pack();
		internal_dialogA.addWindowFocusListener(this);
	}

	private TimerEventsListener setupTEL(){
		final TimerEventsListener tel = new TimerEventsListener(){
			
			@Override
			public void currentTimeUpdated(Timestamp curr_time, int percent) {
				// TODO Auto-generated method stub
				// Set current time
				
				jpb.setString(curr_time.toString());
				jpb.setValue(percent);
				jpb.revalidate();
				jpb.repaint();
				
				long duration = tl.getEndTime() - tl.getStartTime();
				long elasped = tl.getCurrentTime() - tl.getStartTime();
				durationbar.setValue((int) (elasped*100/duration));
			}

			@Override
			public void pointsUpdated(int index) {
				// TODO Auto-generated method stub
				if(index<before.length - 1){
					jlabel3.setText(before[index+1].ts.toString());
				}
				jlabel1.setText("Point "+(index+1)+" / "+before.length);
				jlabel2.setText(before[index].ts.toString());
			}

			@Override
			public void timerStopped() {
				// TODO Auto-generated method stub
				timer.cancel();
				paused = true;
				jbutton1.setText(PLAYSYMBOL);
				jbutton1.setToolTipText("Play from beginning (space)");
				jlabelA.setText(STOP);
			}
		};
		
//		tl = new TimeLine(before, after, (int)(100/period), tel, row, col, label[0], label[1], label[2], label[3]);
		return tel;
		
	}
	/**To be called from the updateTrackLabel() method only
	 * 
	 * @param before	PhoneData before filtering
	 * @param after		PhoneData after filtering
	 */
	private void changeTrack(final PhoneData[] before, final PhoneData[] after){
		this.before = before;
		// For the pop up dialog
		SpinnerModel spinnerModel =
		         new SpinnerNumberModel(1, //initial value
		            1, //min
		            before.length, //max
		            1);//step
		jspinner1.setModel(spinnerModel);
		jlabel5.setText(" / "+before.length);
		TimerEventsListener tel = setupTEL();

		for(int i = 0; i<label.length; i++)
			plot.clearData(label[i]);
		// Pause the player if it is currently playing otherwise the old data will keep playing, causing unpredictable behaviour
		if(paused == false)
			playOrPause(true);
		tl = new TimeLine(before, after, (int)(100/period), tel, row, col, label[0], label[1], label[2], label[3], plot);
		tl.setCurrentPoint(before.length-1);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == jpb){
			if(arg0.getX()> 0 && arg0.getX()<jpb.getWidth())
				tl.setCurrentTime((float)arg0.getX() / (float)jpb.getWidth());
		}
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


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
		JComponent component = (JComponent)arg0.getSource();
		if(component == jpb){
			if(!paused && !tl.getTimeLineFinished()){
				timer.cancel();
				jbutton1.setText(PLAYSYMBOL);
				jbutton1.setToolTipText(PAUSE2);
				jlabelA.setText(PAUSE2);
			}
			tl.setCurrentTime((float)arg0.getX() / (float)jpb.getWidth());
		} else if(component == jbutton2){
			rewind();
			helddown = true;
			if(butthread!=null && butthread.isAlive())
				butthread.interrupt();
			
			butthread = new Thread(){
				public void run(){
					try {
						Thread.sleep(600);
						while(helddown){
							rewind();
							Thread.sleep(40);
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
					}
				}
			};
			butthread.start();
			
				
		} else if(component == jbutton3){
			fastforward();
			helddown = true;
			if(butthread!=null && butthread.isAlive())
				butthread.interrupt();
				
			butthread = new Thread(){
				public void run(){
					try {
						Thread.sleep(600);
						while(helddown){
							fastforward();
							Thread.sleep(40);
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
					}
				}
			};
			butthread.start();
				
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JComponent component = (JComponent) arg0.getSource();
		if(component == jbutton2 || component == jbutton3)
			helddown = false;
		else if(component == jpb){
			if(!paused && !tl.getTimeLineFinished())
				play();
		}
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		int keycode = arg0.getKeyCode();
		if(keycode == KeyEvent.VK_SPACE){
			playOrPause(!paused);
		} else if(keycode == KeyEvent.VK_LEFT){
			if(!helddown)
				rewind();
		} else if(keycode == KeyEvent.VK_RIGHT){
			if(!helddown)
				fastforward();
		} else if(keycode == KeyEvent.VK_PAGE_UP){
			if(tcl!=null)
				updateTrackLabel(current_track-1);
		} else if(keycode == KeyEvent.VK_PAGE_DOWN){
			if(tcl!=null)
				updateTrackLabel(current_track+1);
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


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JComponent com = (JComponent) arg0.getSource();
		if(com == jbutton1){
			playOrPause(!paused);
		}else if (com == jlabel1){
			playOrPause(true);
			jspinner1.setValue(tl.getCurrentPoint()+1);
			Point point = jlabel1.getLocationOnScreen();
			internal_dialog.pack();
			internal_dialog.setLocation(point.x, point.y+jlabel1.getHeight());
			internal_dialog.setVisible(true);
		} else if (com == jbuttonDone){
			internal_dialog.setVisible(false);
		} else if (com == jbutton4){	// Previous track button pressed
			updateTrackLabel(current_track-1);
		} else if (com == jbutton5){	// Next track button pressed
			updateTrackLabel(current_track+1);
		} else if (com == jlabelB){
			Point point = jlabelB.getLocationOnScreen();
			jspinner2.setValue(current_track);
			internal_dialog2.setLocation(point.x, point.y+jlabelB.getHeight());
			internal_dialog2.setVisible(true);
		} else if (com == jbuttonDone2){
			internal_dialog2.setVisible(false);
		} else if (com == jlabelA){
			Point point = jlabelA.getLocationOnScreen();
			internal_dialogA.setLocation(point.x, point.y+jlabelA.getHeight());
			internal_dialogA.setVisible(true);
		} else if (com == jbuttonDoneA){
			internal_dialogA.setVisible(false);
		} else if (com == jlabelA2){
			int selected = jlabelA2.getSelectedIndex();
			points_or_time = selected == 0 ? TIME : POINTS;
			if(points_or_time == TIME){
				PLAY = PLAYSYMBOL+" Playing at "+(1.0f/period)+"x speed";
			} else if(points_or_time == POINTS){
				PLAY = PLAYSYMBOL+" Showing "+(1.0f/period)+" point(s) / second";	
			}
			
			ttask.setPlayAgainst(points_or_time);
			if(!paused){
				playOrPause(true);
				playOrPause(false);
			}
			
		}
	}
	/**
	 * 
	 * @param prev_or_next Previous point = true, Next point = false
	 */
	private void updateTrackLabel(final int index){
		if(updateTrackThread == null || !updateTrackThread.isAlive()){
			final String old_name = jlabelB.getText();
			jlabelB.setIcon(loading);
			jlabelB.setText("Loading ...");
			// New thread is required for the loading message to show
			updateTrackThread = new Thread(){
				public void run(){
					jlabelB.revalidate();
					jlabelB.repaint();
					
					
					if(index>=1 && index <= max_tracks){
						PhoneData[] newtrack = tcl.setTrack(index);
						// Check if the track is empty	
						try{
							jlabelB.setText("Track "+index+" / "+max_tracks+" ("+
									newtrack[0].ts.toString()+" - "+newtrack[newtrack.length-1].ts.toString()+")");
							changeTrack(newtrack, null);
							current_track = index;
						} catch (NullPointerException | ArrayIndexOutOfBoundsException e){
							e.printStackTrace();
							System.err.println("Track "+index+" is empty");
							jlabelB.setText(old_name);
							return;
						}
					
					} else {
						jlabelB.setText(old_name);
					}
					jlabelB.setIcon(null);
				}
			};
			updateTrackThread.start();

		}
	}
	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		JComponent com = (JComponent)arg0.getSource();
		if(com == jspinner1){
			tl.setCurrentPoint((int)jspinner1.getValue()-1);
		} else if(com == jspinner2){
			// Obtain the new track and pass it to the updateTrackLabel() method
			int index = (int)jspinner2.getValue();
			// Update track if the selected track is different from the old track
			if(index != current_track)
				updateTrackLabel(index);
		} else
			((JButton)com).setContentAreaFilled(((JButton)com).getModel().isRollover());
	}
	@Override
	public void windowGainedFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == internal_dialog)
			internal_dialog.setVisible(false);
		else if(e.getSource() == internal_dialog2)
			internal_dialog2.setVisible(false);
		else if(e.getSource() == internal_dialogA)
			internal_dialogA.setVisible(false);
	}
	
	private void customiseButtons(JButton b, int width, int height){
		b.setFocusable(false);
		b.setFocusPainted(false);
		b.setMargin(new Insets(height, width, height, width));
		b.setContentAreaFilled(false);
		b.addChangeListener(this);
		b.setHorizontalAlignment(JButton.CENTER);
	}
	private void playOrPause(boolean paused){
		PlotTracks.paused = paused;
		if(paused){
			timer.cancel();
			jbutton1.setText(PLAYSYMBOL);
			jbutton1.setToolTipText("Play (space)");
			jlabelA.setText(PAUSE);
		} else {
			if(tl.getTimeLineFinished())
				tl.setCurrentPoint(0);
			play();
		}

	}
	private void play(){
		jbutton1.setText(PAUSESYMBOL);
		jbutton1.setToolTipText("Pause (space)");
		jlabelA.setText(PLAY);
		timer = new Timer(true);
		if(points_or_time == TIME){
			ttask = new ExtendedTimerTask(tl, TIME);
			timer.scheduleAtFixedRate(ttask, 0, 100);
		} else if(points_or_time == POINTS){
			ttask = new ExtendedTimerTask(tl, POINTS);
			timer.scheduleAtFixedRate(ttask, 0, (long)(period*1000));
		} else
			throw new IllegalArgumentException ("Invalid \"points_or_time\" argument");
	}
	private void rewind(){
		tl.setCurrentPoint(tl.getCurrentPoint()-1);
		if(!tl.getTimeLineFinished() && paused){
			// Updated jbutton1 and jlabelA
			jbutton1.setToolTipText("Play (space)");
			jlabelA.setText(PAUSE);
		}
	}
	private void fastforward(){
		tl.setCurrentPoint(tl.getCurrentPoint()+1);
	}


	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		JFormattedTextField source = (JFormattedTextField) arg0.getSource();
		float value = Float.parseFloat(source.getText());
		if(value>0f){
			period = 1/Float.parseFloat(source.getText());
			tl.setTimeInterval((int)(100/period));
			if(points_or_time == TIME){
				PLAY = PLAYSYMBOL+" Playing at "+(1.0f/period)+"x speed";
			} else if(points_or_time == POINTS){
				PLAY = PLAYSYMBOL+" Showing "+(1.0f/period)+" point(s) / second";	
				
			}
			if(!paused){
				playOrPause(true);
				playOrPause(false);
			}
		} else{
			source.setValue(1/period);
			if(value == 0f && !paused)
				playOrPause(true);
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

	
	private final TimeLine tl;	
	// Set if it is to play against real time or points
	private int play_against;
	
	public ExtendedTimerTask(final TimeLine tl, int play_against){
		this.tl = tl;
		this.play_against = play_against;
	}
	
	public void run(){
		if(play_against == PlotTracks.TIME)
			tl.advanceTime();
		else if(play_against == PlotTracks.POINTS)
			tl.setCurrentPoint(tl.getCurrentPoint()+1);
		else throw new IllegalArgumentException ("Unrecognised paramter passed to the constructor");
	}
	
	public void setPlayAgainst(int play_against){
		this.play_against = play_against;
	}
}
class TimeLine{

	private int ibefore = 0, iafter = 0;
	private long current_time;

	private int interval = 1000;

	private PhoneData[] before, after;

	private TimerEventsListener etel;

	private final PlotHelper plot;

	private int row;
	private int col;

	private String[] labels;
	private boolean finished = false;	// Indicates whether the timeline has reach the end

	private long curr_time_diff = 0;
	private long points_time_diff = 0;
	private long curr_time_diff2;
	private long points_time_diff2 = 0;
	private boolean newtrackisdrawn;
	
	public TimeLine(final PhoneData[] before,final PhoneData[] after, int interval, TimerEventsListener etel, int row, int col,
			String beforeseries, String afterseries, String beforelast, String afterlast){
		this(before, after, interval, etel, row, col,  beforeseries, afterseries, beforelast, afterlast, null);
	}
	public TimeLine(final PhoneData[] before,final PhoneData[] after, int interval, TimerEventsListener etel, int row, int col,
			String beforeseries, String afterseries, String beforelast, String afterlast, PlotHelper plot){
		newtrackisdrawn = true;
		
		// Conditions that are not allowed
		if(before==null || beforeseries==null || (after!=null && afterseries==null) || (after==null&&(afterseries!=null||
				afterlast!=null)))
			throw new IllegalArgumentException("Invalid Arguments");
		
		labels = new String[]{beforeseries, afterseries, beforelast, afterlast};
		// Initialise the time difference between points for before filtering series and after filtering series
		if(beforeseries.length()>1)
			points_time_diff = before[1].ts.getTime() - before[0].ts.getTime();
		if(after!=null && afterseries.length()>1)
			points_time_diff2 = after[1].ts.getTime() - after[0].ts.getTime();
		
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0; i<labels.length; i++){
			if(labels[i]!=null)
				al.add(labels[i]);
		}
		String[] plotlabels = al.toArray(new String[al.size()]);
		if(plot == null){
			this.plot = new PlotHelper(PlotTracks.COLUMNS[row]+" vs "+PlotTracks.COLUMNS[col],PlotTracks.COLUMNS[row],
				PlotTracks.COLUMNS[col], plotlabels);
		} else{
			this.plot = plot;
		}
		this.before = before;
		this.after = after;
		current_time = before[0].ts.getTime();
		this.interval = interval;
		this.etel = etel;
		this.row = row;
		this.col = col;
	}
	
	public PlotHelper getPlot(){
		return plot;
	}
	public void setTimeInterval(int interval){
		this.interval = interval;
	}

	public synchronized void setCurrentPoint(int index){
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
		long previous_time = current_time;
		current_time = before[index].ts.getTime()+ (long)(points_time_diff*percent)/100*100;
		// If the specified index is equal to current point and either the index is the last point or time is the same
		if(!newtrackisdrawn && index == getCurrentPoint() && (index == before.length-1 || previous_time == current_time)){
			if(index == before.length-1){
				etel.timerStopped();
				finished = true;
				points_time_diff = 0;
			}
			return;
		}

		// Decrement the current_time by 1 interval so that the advanceTime method can be used
		current_time -= interval;
		if(!finished){
			// Rewind - Remove recently added points until the set index has reached
			while (ibefore>0 && index<ibefore){
				// Remove the last point if there is one
				if(plot.getItemCount(labels[0])>0)
					plot.removeData(labels[0], plot.getItemCount(labels[0])-1);
				ibefore--;
			}
			if(after != null){			
				// Rewind - Remove recently added points until the set index has reached
				while (iafter>0 && current_time < after[iafter-1].ts.getTime()){
					plot.removeData(labels[1], plot.getItemCount(labels[1])-1);
					iafter--;
				}
			}
			advanceTimeTo(index);
		}

	}
	public long getCurrentTime(){
		return current_time;
	}
	public long getStartTime(){
		return before[0].ts.getTime();
	}
	public long getEndTime(){
		return before[before.length-1].ts.getTime();
	}
	public int getCurrentPoint(){
		//If the first point hasn't been plotted (i.e. index = -1), set index = 0
		return ibefore == 0 ? 0 : ibefore-1;
	}
	public int getTotalPoints(){
		return before.length;
	}
	public void setTimeLineFinished(boolean finished){
		this.finished = finished;
	}
	public boolean getTimeLineFinished(){
		return finished;
	}
	public synchronized void advanceTime(){
		// Adjust current_time so that it can be divided by the interval
		current_time = current_time/interval*interval;
		advanceTime(before.length);
	}
	public synchronized void advanceTimeTo(int index){
		advanceTime(index);
	}
	public synchronized void advanceTime(int index){
		newtrackisdrawn = false;
		if(! finished){
			current_time+=interval;
			
			if(plot.getItemCount(labels[0])>0)
				plot.removeData(labels[0],  plot.getItemCount(labels[0])-1);

			// Add all points whose timestamp is behind the current time by looping this while loop
			while((current_time - before[ibefore].ts.getTime())>=0){

				points_time_diff = (ibefore<before.length - 1)? before[ibefore+1].ts.getTime() - before[ibefore].ts.getTime() : 0;
				etel.pointsUpdated(ibefore);
				
				// Get the attributes
				double r = PlotTracks.getAttributeDouble(before[ibefore], row);	  
				double c = PlotTracks.getAttributeDouble(before[ibefore], col);
				plot.addData(labels[0], r, c);
/*				if(!before[ibefore].interpolated){
					String[] splitted_date = before[ibefore].ts.toString().split("-| ");
					String mod_date = splitted_date[2]+"/"+splitted_date[1]+"/"+splitted_date[0]+" "+splitted_date[3];
					System.out.println((int)r+","+(int)c+","+"0,"+ mod_date +","+before[ibefore].phone_id);
				}
*/				ibefore++;
				
				// Find out if the track has reached an end (for estimating new position purposes)
				finished = ibefore == before.length;

				// Exit loop if advance once is set or the track has reached an end
				if(ibefore == index+1 || finished)
					break;
			}
			
			// Call timerStopped() function if the last point has been reached
			if(finished){
				etel.timerStopped();
				points_time_diff = 0;
			}
			
			// Calculate time difference between the current time and latest added before filtering point
			curr_time_diff = current_time - before[ibefore-1].ts.getTime();
			
			// Calculate the estimated before filtering position
			double[] result;
			if(finished)
				result = estPosition(before[ibefore-1], null, curr_time_diff, points_time_diff);
			else 
				result = estPosition(before[ibefore-1], before[ibefore], curr_time_diff, points_time_diff);
			
			// Update the location of the estimated point
			if(labels[2]!=null){
				if(plot.getItemCount(labels[2]) > 0)
					plot.removeData(labels[2],  plot.getItemCount(labels[2])-1);
				plot.addData(labels[2], result[0], result[1]);
				
			}
			// Add the estimated point to the before filtering series as well so that a line is drawn between the last and estimated points
			plot.addData(labels[0], result[0], result[1]);
			
			if(after != null){
				if(plot.getItemCount(labels[1])>0)
					plot.removeData(labels[1],  plot.getItemCount(labels[1])-1);
				
//				System.out.println(iafter+", "+after.length+" "+current_time+" "+after[iafter+1].ts.getTime());
				// Add all points whose timestamp is behind the current time by looping this while loop
				while((iafter<after.length-1 && (current_time - after[iafter].ts.getTime())>=0)){
//					System.out.println(iafter+", "+after.length);
					// Get the attributes
					Double r;
					Double c;
					if (iafter == after.length){
						r = PlotTracks.getAttributeDouble(after[iafter-1], row);
						c = PlotTracks.getAttributeDouble(after[iafter-1], col);
					} else {
						r = PlotTracks.getAttributeDouble(after[iafter], row);
						c = PlotTracks.getAttributeDouble(after[iafter], col);
					}

					plot.addData(labels[1], r, c);
					if(iafter == after.length-1)
						break;
					iafter++;
				}
				
				// Update points_time_diff2
				points_time_diff2 = (iafter<after.length)? after[iafter].ts.getTime() - after[iafter-1].ts.getTime() : 0;
				
				// Calculate time difference between the current time and latest added after filtering point
				curr_time_diff2 = current_time - after[iafter-1].ts.getTime();
				
				// Calculate the estimated after filtering position
				if(finished)
					result = estPosition(after[iafter], null, curr_time_diff2, points_time_diff2);
				else 
					result = estPosition(after[iafter-1], after[iafter], curr_time_diff2, points_time_diff2);
				
				// Update the location of the last point
				if(labels[3]!=null){
					if(plot.getItemCount(labels[3]) > 0)
						plot.removeData(labels[3],  plot.getItemCount(labels[3])-1);
					plot.addData(labels[3], result[0], result[1]);
				}
				// Add the estimated point to the after filtering series as well so that a line is drawn between the last and estimated points
				plot.addData(labels[1], result[0], result[1]);
			}
			// Update current time
			etel.currentTimeUpdated(new Timestamp(current_time/100*100), points_time_diff==0 ? 0: (int) (100*curr_time_diff/points_time_diff));	

		}
	}
	
	private double[] estPosition(PhoneData pre_point, PhoneData aft_point, long curr_time_difference, long points_time_difference){
		double px = PlotTracks.getAttributeDouble(pre_point, row);
		double py = PlotTracks.getAttributeDouble(pre_point, col);
		if(aft_point == null || points_time_difference == 0)
			return new double[] {px,py};
		
		double nx = PlotTracks.getAttributeDouble(aft_point, row);
		double ny = PlotTracks.getAttributeDouble(aft_point, col);
		float cur_fraction = (float)curr_time_difference / (float)points_time_difference;
		return new double[]{ px+(nx-px)*cur_fraction, py+(ny-py)*cur_fraction};
	}
}
