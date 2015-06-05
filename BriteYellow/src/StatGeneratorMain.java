import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objects.PhoneData;
import csvexport.CSVWriter;
import dialogs.ProgressDialog;
import dialogs.ProgressDialog.ProgressDialogListener;
import distribution.StatsGenerator;
import dynamodb.NoSQLDownload;
import dynamodb.NoSQLDownload.SQLListener;


public class StatGeneratorMain implements ActionListener, ChangeListener, ProgressDialogListener, SQLListener{

	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
        "TA92903URNf067ff16fcf8e045",
        "YT910K6675876ded0861342065",
        "ZX1B23QFSP48abead89f52e3bb",
	};
	
	public static double[] xbounds = {
		200,850
	};
	public static double[] ybounds = {
		302,364
	};
	
	public int phoneindexcount = 0;
	/*
	public static int[] xbounds = {
		200,330,460,590,720,850
	};
	public static int[] ybounds = {
		302,322,344,364
	};
	*/
	
	// The name of this app
	final static String TITLE = "StatGenerator";
	
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
		StatsGenerator.PATH_PER_SHORTEST_PATH,
		StatsGenerator.TIME_PER_SHORTEST_PATH
		
		
//		StatsGenerator.STHETAIN, 
//		StatsGenerator.STHETAOUT,
//		StatsGenerator.STHETAINOUT,	
		
//		StatsGenerator.AVERAGE_ACCELERATION,
		
		
	};
	public static double[][] property2 = {
		{StatsGenerator.AVERAGE_SPEED, 0, 3},
		{StatsGenerator.AVERAGE_SPEED, 0, 2},
		{StatsGenerator.AVERAGE_SPEED, 0, 1},
		{StatsGenerator.AVERAGE_SPEED, 10, Double.POSITIVE_INFINITY},
		{StatsGenerator.STHETACHANGE, 5/180*Math.PI, Double.POSITIVE_INFINITY},
		{StatsGenerator.STHETACHANGE, 10/180*Math.PI, Double.POSITIVE_INFINITY},
		{StatsGenerator.STHETACHANGE, 15/180*Math.PI, Double.POSITIVE_INFINITY},
		{StatsGenerator.STHETACHANGE, 20/180*Math.PI, Double.POSITIVE_INFINITY},
		
		{StatsGenerator.STHETACHANGE, -Math.PI/8,Math.PI/8},
/*		{StatsGenerator.AVERAGE_SPEED, 10, 100},
		{StatsGenerator.STHETACHANGE_NO, -Math.PI/8,Math.PI/8},
		{StatsGenerator.STHETACHANGE_NO, -Math.PI,Math.PI},
*/	};
	

	private JFrame jframe2;

	private JCheckBox offlinecheckbox;

	private JLabel offlinelabel;

	private JButton startbutton;

	private ProgressDialog dialog;

	public void setupWelcomeGUI(){
		jframe2 = new JFrame();
		jframe2.setLayout(new GridLayout(0,1));
		jframe2.setTitle("Start screen - "+TITLE);
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
		dialog = new ProgressDialog(TITLE);
		
		statusUpdated(1, "Preparing", null);
		
		dialog.updateInfo("The download process will be resume if cancelled");

		
		dialog.setProgressDialogListener(this);
		
		dialog.startTimer();
	}

	@Override
	public void finish(final int exit, final String err_msg){
		dialog.stopTimer();
		dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if(exit == 0){
			statusUpdated(4,"Finished! Closing in 5 seconds",null);

			try{
				Thread.sleep(5000);
			} catch (InterruptedException e){
				
			}
			dialog.dispose();
		} else {
			statusUpdated(-1,"Run time error",err_msg);
			dialog.updateInfo("Close this dialog when you have finished with it");
		}
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
//			final String newmsg = "<html>"+mainmsg+" "+submsg.replaceAll("\n", "<br>")+"</html>";
			dialog.updateProgress(":( An error has occurred");
			dialog.setTitle("An error has occurred - "+TITLE);
			dialog.updateInfo("Check console for details");
			JTextArea errorarea = new JTextArea();
		  	errorarea.setLineWrap(true);
			errorarea.setEditable(false);
			errorarea.setSize(500, 400);
			errorarea.setText(mainmsg+" "+submsg);
			JScrollPane scrollpane = new JScrollPane(errorarea);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			JOptionPane.showMessageDialog(null, scrollpane, "An error has occurred", JOptionPane.ERROR_MESSAGE);
			exit(1);
			
		} else {
			dialog.setTitle(mainmsg+" - "+TITLE);
			if(submsg == null)
				dialog.updateProgress(mainmsg);
			else
				dialog.updateProgress("<html>"+mainmsg+"<br><font size=-1>"+submsg+"</font></html>");
				
		}
		
	}

	@Override
	public void stepProgressUpdated(int step, int percent) {
		// TODO Auto-generated method stub
		if(step == 3){
			dialog.updateProgress(phoneindexcount*20+percent/5);
		} else if(step == 4){
			dialog.updateProgress(percent);
		}
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
		System.out.println(TITLE);
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
				if (!success && !offline)
					JOptionPane.showMessageDialog(null, "Unable to connect to database and check for updates\nLocal copy of data will be used instead", "Unable to connect to database", JOptionPane.WARNING_MESSAGE);
				phoneindexcount++;
			}
			statusUpdated(4, "Analysing tracks ...",null);
		
		
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
				dialog.updateLog("PhoneID "+phones[i]+" has "+totaltracks[i]+" tracks");
				
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
									results[m+property.length+2] = String.valueOf(sg.getTotalFreqAt((int)property2[m][0], property2[m][1],property2[m][2],xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
								}
								// Write the stats
								cw[index].write(results);
							}
						}
					}
					stepProgressUpdated(4, 100*i/phones.length + 100/phones.length*track/totaltracks[i]);
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
		if(but == startbutton){
			// Prepare the process screen
			setupProcessGUI();
			// Dispose the welcome screen
			jframe2.dispose();
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

	public void exit(int exit_code){
		System.err.println("User aborted");
		System.exit(exit_code);
	}
	@Override
	public void onAbort() {
		// TODO Auto-generated method stub
		exit(0);
	}
}
