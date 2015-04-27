import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objects.PhoneData;
import dialogs.ProgressDialog;
import dialogs.ProgressDialog.ProgressDialogListener;
import dynamodb.NoSQLDownload;
import dynamodb.NoSQLDownload.SQLListener;
import graphing.PlotTracks;
import graphing.TrackChangeListener;

public class PlotTracksMain2 extends TimerTask implements ActionListener, ChangeListener, ProgressDialogListener, SQLListener{
	
	// The name of this app
	final static String TITLE = "PlotTracksMain2";

	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
		"TA92903URNf067ff16fcf8e045",
		"YT910K6675876ded0861342065",
		"ZX1B23QFSP48abead89f52e3bb"
	};
	
	private static Timer timer;
	
	public static void main(String args[]) throws Exception{
		
		System.out.println(TITLE);
		
		new PlotTracksMain2().getPhoneIDAndPlot();
				
	}

	private int tracks_done = 0;
	private int totaltracks = -1;
	private boolean plotted = false;


	private TrackChangeListener tcl;


	private NoSQLDownload msd;

	private void plot(SQLListener ptm, boolean offline) throws Exception{
		msd = new NoSQLDownload("Processed_Data");
		msd.setPTMListener(ptm);

		boolean success = msd.downloadAndSerialise(phoneid, offline);
		totaltracks  = msd.getTotalTracks();
		
		if (!offline && !success)
			JOptionPane.showMessageDialog(null, 
					"Unable to connect to database and check for updates\nLocal data copy will be used instead\nBe aware that local data copy might not be up-to-date", 
					"Unable to connect to database", 
					JOptionPane.WARNING_MESSAGE
					);
		// Update the number of tracks done
		tracks_done = totaltracks;
		ptm.statusUpdated(4, "Please wait ...",null);
		ptm.stepProgressUpdated(4, -1);
		
		
		if(totaltracks == 0)
			System.err.println("There are no tracks associated with this phone id: "+phoneid);
		else if(!plotted)
			plottracks();
		
		ptm.finish(0, null);
	}
	
	private JFrame frame;

	private JComboBox<String> cbox1;

	private JButton okbutton;
	private JCheckBox offlinecheckbox;

	private JPanel panel;
	
	private JLabel offlinelabel;

	private void getPhoneIDAndPlot() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		
		frame.setMinimumSize(
				new Dimension(ProgressDialog.DEFAULT_WIDTH/3*2, 
						ProgressDialog.DEFAULT_HEIGHT/3*2)
				);
		JLabel selectlabel = new JLabel("<html><b>Please select a phone</b></html>");
		selectlabel.setFont(ProgressDialog.progressfontNormal);
		selectlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		cbox1 = new JComboBox<String>(phones);
//		cbox1.setFont(ProgressDialog.progressfontSmall);
		cbox1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		okbutton = new JButton("Plot it");
		okbutton.setFont(ProgressDialog.progressfontSmall);
		okbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		offlinecheckbox = new JCheckBox("Offline mode");
//		offlinecheckbox.setFont(ProgressDialog.progressfontSmall);
		offlinecheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		offlinelabel = new JLabel("<html><font color = 'red'><i>Local data copy will be used if available<br>" +
				"Be aware that it might not be up-to-date</font></i></html>");
//		offlinelabel.setFont(ProgressDialog.progressfontSmall);
		offlinelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(Box.createVerticalGlue());
		panel.add(selectlabel);
		panel.add(Box.createVerticalStrut(15));
		panel.add(cbox1);
		panel.add(Box.createVerticalStrut(20));
		panel.add(okbutton);
		panel.add(Box.createVerticalStrut(10));
		panel.add(offlinecheckbox);
		panel.add(offlinelabel);

		panel.add(Box.createVerticalGlue());
		okbutton.addActionListener(this);
		offlinecheckbox.addChangeListener(this);
		
		frame.setTitle("Select phone - PlotTracksMain2");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		offlinelabel.setVisible(offlinecheckbox.isSelected());
	}
	
	private long starttime;

	private String phoneid;

	private Thread plotthread;

	private ProgressDialog dialog;

	public void startTimer(long starttime){
		this.starttime = starttime;
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 1000);
		
	}
	public void finishTimer(){
		long elapsed = (System.currentTimeMillis() - starttime)/1000;
		timer.cancel();
		dialog.updateInfo2("Time spent: "+elapsed+" second(s)");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		long elapsed = (System.currentTimeMillis() - starttime)/1000;
		dialog.updateInfo2("Elapsed Time: "+elapsed+" second(s)");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		final JButton button = (JButton)e.getSource();
		if(button == okbutton){

			frame.dispose();
			
			dialog = new ProgressDialog("Please wait... - PlotTrackMain2");
			dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			dialog.updateProgress("Please wait...");


			dialog.setProgressDialogListener(this);
			

			startTimer(System.currentTimeMillis());
			
			tcl = new TrackChangeListener(){

				@Override
				public PhoneData[] setTrack(int index) {
					// TODO Auto-generated method stub
					if(index >= 1 && index <= totaltracks){
						try {
							return NoSQLDownload.deserialise(phoneid, index);
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
						
						frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						phoneid = phones[cbox1.getSelectedIndex()];
						plot(PlotTracksMain2.this, offlinecheckbox.isSelected());
						
					
						
					}catch (Exception e1) {
						// TODO Auto-generated catch block
						if(this!=null && this.isAlive())
							finishTimer();
						StackTraceElement trace = e1.getStackTrace()[0];
						statusUpdated(-1, "Oops! An error has occured", "\n<font color = 'red'><i>"+e1.toString()+"\n at line "+trace.getLineNumber()+
								" in class \""+ trace.getClassName()+"\"</i></font>\n\nPlease also check the console for the full error message");
						// Revalidate and repaint the label as it may have changed size
						e1.printStackTrace();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
					
				}
			};
			plotthread.start();
		}
	}
	public void exit(int exit_code){
		System.err.println("User aborted");
		System.exit(exit_code);
	}
	@Override
	public void statusUpdated(int step, String mainmsg, String submsg) {
		// TODO Auto-generated method stub
		// Replace all \n with <br> and the whole message into
		// html format
		if(submsg == null){
			dialog.updateLog(mainmsg);
		} else
			dialog.updateLog(mainmsg+" "+submsg);
		if(step == -1){
			final String newmsg = "<html>"+mainmsg+" "+submsg.replaceAll("\n", "<br>")+"</html>";
			dialog.updateProgress(":( An error has occurred");
			dialog.updateInfo("Check console for details");
			JOptionPane.showMessageDialog(null, newmsg, "An error has occurred", JOptionPane.ERROR_MESSAGE);
			exit(1);
			
		} else {
			dialog.setTitle(mainmsg+" - "+TITLE);
			if(submsg == null)
				dialog.updateProgress(mainmsg);
			else
				dialog.updateProgress("<html><center>"+mainmsg+"<br><font size=-1>"+submsg+"</font></center></html>");
				
		}
		
	}

	@Override
	public void stepProgressUpdated(int step,
			int percent) {
		// TODO Auto-generated method stub
		if(percent>=0){
			dialog.updateProgress(percent);
		} else {
			dialog.updateProgress(-1);
		}
		if(step == 3 && percent > 0){
			dialog.updateInfo("The download will resume next time if cancelled");
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
	}

	@Override
	public void finish(int exit, String msg) {
		// TODO Auto-generated method stub
		finishTimer();
		dialog.finish();
	}
	
	public void plottracks(){
		PlotTracks.plotTrack2(PlotTracks.X, PlotTracks.Y, 0.1f, tcl, totaltracks);
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JComponent component = (JComponent)e.getSource();
		if(component == offlinecheckbox)
			offlinelabel.setVisible(offlinecheckbox.isSelected());					
	}
	
	@Override
	public void onAbort() {
		// TODO Auto-generated method stub
		exit(0);
	}
}
