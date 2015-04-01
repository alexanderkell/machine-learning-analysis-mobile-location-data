import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import maths.*;
import mysql.*;
import mysql.MySQLDownload.PTMListener;
import graphing.PlotTracks;
import graphing.TrackChangeListener;

public class PlotTracksMain2 extends TimerTask implements ActionListener{
	

	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
		"TA92903URNf067ff16fcf8e045",
		"YT910K6675876ded0861342065",
		"ZX1B23QFSP48abead89f52e3bb"
	};

	
	private static Timer timer;
	
	public static void main(String args[]) throws Exception{
		
		System.out.println("PlotTracksMain");
		
		new PlotTracksMain2().getPhoneIDAndPlot();
				
	}

	private int tracks_done = 0;
	private int totaltracks = -1;
	private boolean plotted = false;


	private TrackChangeListener tcl;


	private MySQLDownload msd;

	private void plot(PTMListener ptm) throws SQLException, ClassNotFoundException, IOException{
		
		msd = new MySQLDownload("FilteredData");
		msd.setPTMListener(ptm);

		boolean success = msd.downloadAndSerialise(phoneid);
		totaltracks  = msd.getTotalTracks();
		if (!success)
			JOptionPane.showMessageDialog(null, 
					"Unable to connect to database and check for updates\nLocal data copy will be used instead\nBe aware that local data copy might not be up-to-date", 
					"Unable to connect to database", 
					JOptionPane.WARNING_MESSAGE
					);
		// Update the number of tracks done
		tracks_done = totaltracks;
		ptm.statusUpdated(4, "Please wait ...");
		ptm.stepProgressUpdated(4, -1);
		
		
		if(totaltracks == 0)
			System.err.println("There are no tracks associated with this phone id: "+phoneid);
		else if(!plotted)
			plottracks();
		
		ptm.finish(0, null);
	}
	
	protected static JLabel timelabel;

	private JFrame frame;

	private JComboBox<String> cbox1;

	private JButton okbutton;

	private JPanel panel;

	private void getPhoneIDAndPlot() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setResizable(false);
		
		JLabel selectlabel = new JLabel("Select a phone: ");
		selectlabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		cbox1 = new JComboBox<String>(phones);
		cbox1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		okbutton = new JButton("Plot it");
		okbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(selectlabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(cbox1);
		panel.add(Box.createVerticalStrut(15));
		panel.add(okbutton);
		okbutton.addActionListener(this);
		
		frame.setTitle("Select axis - StatsReader");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private long starttime;

	private JButton cancelbutton;

	private JLabel cancellabel;

	private String phoneid;

	private Thread plotthread;

	public void startTimer(long starttime){
		this.starttime = starttime;
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 1000);
		
	}
	public void finishTimer(){
		long elapsed = (System.currentTimeMillis() - starttime)/1000;
		timer.cancel();
		timelabel.setText("Time spent: "+elapsed+" second(s)");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		long elapsed = (System.currentTimeMillis() - starttime)/1000;
		timelabel.setText("Elapsed Time: "+elapsed+" second(s)");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
		if(button == okbutton){

			panel.removeAll();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			final JLabel oklabel = new JLabel("Please Wait ...");
			oklabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(oklabel);
			panel.add(Box.createVerticalStrut(5));
			final JProgressBar jpb = new JProgressBar();
			jpb.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(jpb);
			panel.add(Box.createVerticalStrut(10));
			timelabel = new JLabel();
			timelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			jpb.setIndeterminate(true);
			
			panel.add(timelabel);
			panel.add(Box.createVerticalStrut(15));
			
			cancellabel = new JLabel("This process will resume next time if cancelled");
			cancellabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			cancelbutton = new JButton("Cancel");
			cancelbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
			cancelbutton.addActionListener(this);
			panel.add(cancellabel);
			panel.add(Box.createVerticalStrut(5));
			panel.add(cancelbutton);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			startTimer(System.currentTimeMillis());
			
			tcl = new TrackChangeListener(){

				@Override
				public PhoneData[] setTrack(int index) {
					// TODO Auto-generated method stub
					if(index >= 1 && index <= totaltracks){
						try {
							return MySQLDownload.deserialise(phoneid, index);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							System.out.println(tracks_done+" "+index);
							if(tracks_done<index)
								JOptionPane.showMessageDialog(null, "Track "+index+" is being downloaded and not yet available", "Track "+index+" not yet available", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(null, "Cannot load phone \""+phoneid+"\" track "+index+"\n"+e.toString(), "Cannot load track "+index, JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					}
					return null;
				}

				
			};
			
			plotthread = new Thread(){
				public void run(){
					try {
						
						PTMListener ptm = new PTMListener(){

							@Override
							public void statusUpdated(int step, String msg) {
								// TODO Auto-generated method stub
								// Replace all \n with <br> and the whole message into
								// html format
								msg = "<html>"+msg.replaceAll("\n", "<br>")+"</html>";
								oklabel.setHorizontalAlignment(JLabel.CENTER);
								oklabel.setText(msg);
								frame.pack();
								frame.revalidate();
								frame.repaint();
							}

							@Override
							public void stepProgressUpdated(int step,
									int percent) {
								// TODO Auto-generated method stub
								if(percent>=0){
									jpb.setIndeterminate(false);
									jpb.setValue(percent);
								} else {
									jpb.setIndeterminate(true);
								}
/*								if(step == 3 && percent > 0){
									totaltracks = msd.getTotalTracks();
									if(totaltracks>0){
										tracks_done = totaltracks*percent/100;
										if(!plotted && tracks_done>=1){
											plottracks();
											plotted = true;
										}
										//System.out.println("tracks_done "+tracks_done);
									}
								}
*/							}

							@Override
							public void finish(int exit, String msg) {
								// TODO Auto-generated method stub
								finishTimer();
								frame.dispose();
							}
							
						};
						frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						phoneid = phones[cbox1.getSelectedIndex()];
						plot(ptm);
						
					
						
					}catch (Exception e1) {
						// TODO Auto-generated catch block
						if(this!=null && this.isAlive())
							finishTimer();
						StackTraceElement trace = e1.getStackTrace()[0];
						oklabel.setText("<html>Oops! An error has occured: <br><font color = 'red'><i>"+e1.toString()+"<br> at line "+trace.getLineNumber()+
								" in class \""+ trace.getClassName()+"\"</i></font><br><br>Please also check the console for the full error message</html>");
						// Revalidate and repaint the label as it may have changed size
						oklabel.revalidate();
						frame.pack();
						e1.printStackTrace();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
					
				}
			};
			plotthread.start();
		} else if(button == cancelbutton){
			frame.dispose();
			System.err.println("User aborted");
			System.exit(1);
		}
	}
	public void plottracks(){
		PlotTracks.plotTrack2(PlotTracks.X, PlotTracks.Y, 1f, tcl, totaltracks);

	}
}
