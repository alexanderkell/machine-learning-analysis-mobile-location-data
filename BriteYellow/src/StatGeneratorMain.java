import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import csvexport.CSVWriter;

import distribution.StatsGenerator;
import dynamodb.NoSQLDownload;
import dynamodb.NoSQLDownload.SQLListener;

import maths.PhoneData;


public class StatGeneratorMain extends TimerTask implements ActionListener, ChangeListener, SQLListener{

	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
        "TA92903URNf067ff16fcf8e045",
        "YT910K6675876ded0861342065",
        "ZX1B23QFSP48abead89f52e3bb",
	};
/*	
	public static int[] xbounds = {
		200,850
	};
	public static int[] ybounds = {
		302,364
	};
*/	
	public int phoneindexcount = 0;
	
	public static int[] xbounds = {
		200,330,460,590,720,850
	};
	public static int[] ybounds = {
		302,322,344,364
	};
	
	public static int[] property = {
		StatsGenerator.PATH_LENGTH,	//gettotalaverage
		StatsGenerator.TIME_STOPPED, //gettotalaverage
		StatsGenerator.NO_STOPS, //gettotalaverage
		
		StatsGenerator.TIME_SPENT, //gettotalaverage
		StatsGenerator.INACTIVE_TIME, //gettotalaverage
		StatsGenerator.STHETACHANGE, //gettotalaverage
		StatsGenerator.TIME_PER_STOP, //gettotalaverage
		StatsGenerator.AVERAGE_SPEED, //gettotalaverage
		StatsGenerator.FREQ_IN_AREA, //gettotalaverage
		
		
		
//		StatsGenerator.STHETAIN, 
//		StatsGenerator.STHETAOUT,
//		StatsGenerator.STHETAINOUT,	
		
//		StatsGenerator.AVERAGE_ACCELERATION,
		
		
	};
	public static double[][] property2 = {
/*		{StatsGenerator.AVERAGE_SPEED, 0, 3},
		{StatsGenerator.AVERAGE_SPEED, 10, 100},
		{StatsGenerator.STHETACHANGE_NO, -Math.PI/8,Math.PI/8},
		{StatsGenerator.STHETACHANGE_NO, -Math.PI,Math.PI},
*/	};
	
	private JProgressBar jpb;

	private long starttime;
	private JFrame jframe;

	private JLabel statuslabel;
	private JLabel timelabel;
	private JLabel infolabel;
	
	private Timer timer;

	private JPanel panel;

	private JButton cancelbutton;

	private JFrame jframe2;

	private JCheckBox offlinecheckbox;

	private JLabel offlinelabel;

	private JButton startbutton;
	public void setupWelcomeGUI(){
		jframe2 = new JFrame();
		jframe2.setLayout(new GridLayout(0,1));
		jframe2.setTitle("Start screen - StatsGeneratorMain");
		jframe2.add(new JLabel("Press \"start\" to begin downloading tracks and generating stats"));
		offlinecheckbox = new JCheckBox("Offline mode");
		offlinecheckbox.addChangeListener(this);
		offlinelabel = new JLabel("<html><font color = 'red'><i>Local data copy will be used if available<br>" +
				"Be aware that it might not be up-to-date</font></i></html>");
		offlinelabel.setVisible(offlinecheckbox.isSelected());
		
		jframe2.add(offlinecheckbox);
		jframe2.add(offlinelabel);
		startbutton = new JButton("Start");
		startbutton.addActionListener(this);
		jframe2.add(startbutton);
		jframe2.pack();
		jframe2.setResizable(false);
		jframe2.setLocationRelativeTo(null);
		jframe2.setVisible(true);
	}
	public void setupProcessGUI(){
		jpb = new JProgressBar();
		statuslabel = new JLabel("<html>Preparing ...<br> &nbsp</html>");
		timelabel = new JLabel(" ");
		
		infolabel = new JLabel("The download process will be resume if cancelled");
		cancelbutton = new JButton("Cancel");
		
		// Configure components
		jpb.setAlignmentX(Component.CENTER_ALIGNMENT);
		statuslabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		statuslabel.setHorizontalAlignment(JLabel.LEFT);
		timelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timelabel.setHorizontalAlignment(JLabel.LEFT);
		infolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infolabel.setHorizontalAlignment(JLabel.LEFT);
		cancelbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelbutton.addActionListener(this);
		
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(statuslabel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(jpb);
		panel.add(Box.createVerticalStrut(15));
		panel.add(timelabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(infolabel);	
		panel.add(Box.createVerticalStrut(5));
		panel.add(cancelbutton);
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jframe.setTitle("Generating stats - StatsGeneratorMain");
		jframe.add(panel, BorderLayout.CENTER);
		jframe.pack();
		jframe.setLocationRelativeTo(jframe2);
		jframe.setResizable(false);
		jframe.setMinimumSize(new Dimension(500,170));
		jframe.setVisible(true);
		
		starttime = System.currentTimeMillis();
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 1000);
	}

	@Override
	public void finish(final int exit, final String err_msg){

		timer.cancel();
		final long elapsed = (System.currentTimeMillis() - starttime)/1000;
		timelabel.setText("Total Time spent: "+elapsed+" second(s)");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if(exit == 0){
			jpb.setValue(100);
			statuslabel.setText("Finished! Closing in 5 seconds");

			try{
				Thread.sleep(5000);
			} catch (InterruptedException e){
				
			}
			jframe.dispose();
		} else {
			infolabel.setText("<html>Run time error: <br><font color='red'>"+err_msg+"</font><br><br>Close this dialog when you have finished with it</html>");
			jframe.pack();
			jframe.revalidate();
			jframe.repaint();
		}
	}
	
	@Override
	public void statusUpdated(int step, String msg) {
		// TODO Auto-generated method stub
		// Replace all \n with <br> and the whole message into
		// html format
		msg = "<html>"+msg.replaceAll("\n", "<br>")+"</html>";
		statuslabel.setText(msg);
		jframe.pack();
	}
	@Override
	public void stepProgressUpdated(int step, int percent) {
		// TODO Auto-generated method stub
		if(step == 3){
			if(percent == 0){
				phoneindexcount++;
			}

			jpb.setValue(phoneindexcount*20+percent/5);
		}
		else if(percent>=0){
			if(step>=4){
				cancelbutton.setVisible(false);
				infolabel.setText("To terminate: go to console and press \"Ctrl\" + \"C\"");
			}
			jpb.setIndeterminate(false);
			jpb.setValue(percent);
		} else {
			jpb.setIndeterminate(true);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long elapsed = (System.currentTimeMillis() - starttime)/1000;
		timelabel.setText("Elapsed Time: "+elapsed+" second(s)");
	}
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {
		new StatGeneratorMain();
	}
	
	public StatGeneratorMain(){
		// TODO Auto-generated method stub
		System.out.println("StatGenerator main method");
		// set up the GUI
		setupWelcomeGUI();
	}

	private void processStatGen(boolean offline){
		final int[] totaltracks = new int[phones.length];
		
		// Create connection to the mySQL server
		try{
			NoSQLDownload msd = new NoSQLDownload("Processed_Data");
			
			msd.setPTMListener(this);
			
			for(int i = 0; i<phones.length; i++){
				boolean success = msd.downloadAndSerialise(phones[i], offline);
				totaltracks[i] = msd.getTotalTracks();
				if (!success)
					JOptionPane.showMessageDialog(null, "Unable to connect to database and check for updates\nLocal copy of data will be used instead", "Unable to connect to database", JOptionPane.WARNING_MESSAGE);
			}
			statusUpdated(4, "Analysing tracks ...");
			stepProgressUpdated(4, 100);
		
		
			// Create CSVWriters
			final CSVWriter[] cw = new CSVWriter[(xbounds.length-1)*(ybounds.length-1)];
			
			// Initialise the CSVWriters and write the headers
			for(int j = 0; j < xbounds.length-1; j++){
				for(int k = 0; k < ybounds.length-1; k++){
					int index = j*(ybounds.length-1) + k;
					// Initialise the CSVWriters, destination of CSVWriter: "Statistics/"
					cw[index] = new CSVWriter("Statistics/From ("+xbounds[j]+", "+xbounds[j+1]+") to ("+ybounds[k]+", "+ybounds[k+1]+")");
					// Write the headers
					String[] headers = new String[property.length+property2.length+2];
					headers[0] = "PhoneID";
					headers[1] = "Track";
					for(int m=0; m<property.length; m++){
						headers[m+2] = "Total "+StatsGenerator.getAxisName(property[m]);
					}
					for(int n=0; n<property2.length; n++){
						headers[n+property.length+2] = StatsGenerator.getAxisName(property[n]);
					}
					cw[index].write(headers);
				}
			}
				
			for(int i = 0; i<phones.length; i++){
								
				System.out.println("PhoneID "+phones[i]+" has "+totaltracks[i]+" tracks");
				
				for(int track = 1; track <= totaltracks[i]; track++){
					
					// Generate stats
					PhoneData[] data = NoSQLDownload.deserialise(phones[i], track);
					if(data.length > 0){
						StatsGenerator sg = new StatsGenerator(data);
						for(int j = 0; j < xbounds.length-1; j++){
							for(int k = 0; k < ybounds.length-1; k++){
								int index = j*(ybounds.length-1) + k;
								
								String[] results = new String[property.length+property2.length+2];
								results[0] = phones[i];
								results[1] = String.valueOf(track);
								for(int l = 0; l < property.length; l++){
									results[l+2] = String.valueOf(sg.getTotalAverage(property[l], xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
								}
								for(int m = 0; m < property2.length; m++){
									results[m+property.length+2] = String.valueOf(sg.getTotalFreqAt(property2[m][0], property2[m][1],property2[m][2],xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
								}
								// Write the stats
								cw[index].write(results);
							}
						}
					}
					
				}
			}
			
			// Flush and finialise the files
			for(CSVWriter index : cw)
				index.finish();
			// Tell GUI that the process is finished
			finish(0, null);
		} catch (Exception e){
			finish(1, e.toString()+" (at line "+e.getStackTrace()[0].getLineNumber()+")");
		}

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton but = (JButton)e.getSource();
		if(but == cancelbutton){
			jframe.dispose();
			System.err.println("User aborted");
			System.exit(1);
		} else if(but == startbutton){
			// Dispose the welcome screen
			jframe2.dispose();
			// Prepare the process screen
			setupProcessGUI();
			// Run the actual process
			// New thread is necessary to allow the process screen to initalise before
			// this actual process is run
			new Thread(){
				public void run(){
					processStatGen(offlinecheckbox.isSelected());
				}
			}.start();
			
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JComponent component = (JComponent)e.getSource();
		if(component == offlinecheckbox){
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					offlinelabel.setVisible(offlinecheckbox.isSelected());
					jframe2.pack();
				}
				
			});
					
		}
	}

}
