import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import csvexport.CSVWriter;

import Distribution.StatsGenerator;

import maths.PhoneData;
import mysql.insertMySQL;


public class StatGeneratorMain extends TimerTask{

	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
        "TA92903URNf067ff16fcf8e045",
        "YT910K6675876ded0861342065",
        "ZX1B23QFSP48abead89f52e3bb",
	};
	
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
//		StatsGenerator.STHETACHANGE_NO,	
		
//		StatsGenerator.AVERAGE_ACCELERATION,
		
		
	};
	
	private JProgressBar jpb;

	private long starttime;
	private JFrame jframe;

	private JLabel statuslabel;
	private JLabel timelabel;

	private Timer timer;
	public StatGeneratorMain(){
		jpb = new JProgressBar();
		statuslabel = new JLabel("Preparing ...");
		timelabel = new JLabel(" ");
		starttime = System.currentTimeMillis();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(statuslabel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(jpb);
		panel.add(Box.createVerticalStrut(15));
		panel.add(timelabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(new JLabel("To terminate: go to console and press \"Ctrl\" + \"C\""));
		
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jframe.setTitle("Generating stats - StatsGeneratorMain");
		jframe.add(panel);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.setVisible(true);
		
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 1000);
	}
	public void setStatus(int phoneindex, int track, int totaltracks){
		
		jpb.setValue(100*phoneindex/phones.length + 100/phones.length*track/totaltracks);
		statuslabel.setText("Phone: "+phones[phoneindex]+", Track: "+track+"/"+totaltracks);
	}
	public void finish(){
		timer.cancel();
		// Set progressbar value to 100
		jpb.setValue(100);
		// Updated the total time spent
		long elapsed = (System.currentTimeMillis() - starttime)/1000;
		timelabel.setText("Total Time spent: "+elapsed+" second(s)");
		statuslabel.setText("Finished! Closing in 5 seconds");
		try{
			Thread.sleep(5000);
		} catch (InterruptedException e){
			
		}
		jframe.dispose();
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
	public static void main(String[] args) throws ParseException, SQLException, IOException {
		// TODO Auto-generated method stub
		StatGeneratorMain sgm = new StatGeneratorMain();
		System.out.println("StatGenerator main method");
		// Create and initialise CSVWriter
		final CSVWriter[] cw = new CSVWriter[(xbounds.length-1)*(ybounds.length-1)];
		for(int j = 0; j < xbounds.length-1; j++){
			for(int k = 0; k < ybounds.length-1; k++){
				int index = j*(ybounds.length-1) + k;
				cw[index] = new CSVWriter("Statistics/From ("+xbounds[j]+", "+xbounds[j+1]+") to ("+ybounds[k]+", "+ybounds[k+1]+")");
				String[] headers = new String[property.length+2];
				headers[0] = "PhoneID";
				headers[1] = "Track";
				for(int m=0; m<property.length; m++){
					headers[m+2] = "Total "+StatsGenerator.getAxisName(property[m]);
				}
				cw[index].write(headers);
			}
		}
		
		// Create connection to the mySQL server
		insertMySQL mysql = new insertMySQL();
		for(int i = 0; i<phones.length; i++){
			
			int max_track = mysql.totalTracksQuery("FilteredData", "PhoneID = '"+phones[i]+"'");
			System.out.println("PhoneID "+phones[i]+" has "+max_track+" tracks");
			
			for(int track = 1; track <= max_track; track++){
				String query = "PhoneID = '"+phones[i]+"' AND TrackNo = "+String.valueOf(track);
				
				ArrayList<PhoneData> filtered;
				try{
					filtered = mysql.query("FilteredData", query);
				} catch (SQLException e){
					System.err.println("Track "+track+"/"+max_track+" is empty");
					continue;
				}
				
				// Update status
				sgm.setStatus(i, track, max_track);
				System.out.println("Track: "+track+"/"+max_track+" has "+filtered.size()+" data point(s)");
				
				// Generate stats
				if(filtered.size() > 0){
					StatsGenerator sg = new StatsGenerator(filtered);
					for(int j = 0; j < xbounds.length-1; j++){
						for(int k = 0; k < ybounds.length-1; k++){
							int index = j*(ybounds.length-1) + k;
							
							String[] results = new String[property.length+2];
							results[0] = phones[i];
							results[1] = String.valueOf(track);
							for(int l = 0; l < property.length; l++){
								results[l+2] = String.valueOf(sg.getTotalAverage(property[l], xbounds[j], xbounds[j+1], ybounds[k], ybounds[k+1]));
							}
							cw[index].write(results);
						}
					}
				}
				
			}
		}
		
		for(CSVWriter index : cw)
			index.finish();
		sgm.finish();
	}

}
