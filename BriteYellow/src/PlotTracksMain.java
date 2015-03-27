import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import maths.*;
import mysql.*;
import graphing.PlotTracks;
import graphing.TrackChangeListener;

public class PlotTracksMain {
	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
		"TA92903URNf067ff16fcf8e045",
		"YT910K6675876ded0861342065",
		"ZX1B23QFSP48abead89f52e3bb"
	};

	private static int track = 1;
	private static int totaltracks;
	private static PhoneData[] filarray;
	public static void main(String args[]) throws Exception{
		
		System.out.println("Track machine learning 101");

		
		getPhoneID();
		
		
	}

	private static void plot(final String phoneid) throws SQLException{

		final insertMySQL mysql = new insertMySQL();
		final ArrayList<PhoneData> filtered = mysql.query("FilteredData", "PhoneID = '"+phoneid+"' AND TrackNo = "+ track);
		filarray = filtered.toArray(new PhoneData[filtered.size()]);
		totaltracks = mysql.totalTracksQuery("FilteredData", "PhoneID = '"+phoneid+"'");
		
		TrackChangeListener tcl = new TrackChangeListener(){

			@Override
			public String previousTrackName() {
				// TODO Auto-generated method stub
				
				if(track-1 >= 1){
					ArrayList<PhoneData> filtered;
					track--;
					try {
						
						filtered = mysql.query("FilteredData", "PhoneID = '"+phoneid+"' AND TrackNo = "+ track);
						filarray = filtered.toArray(new PhoneData[filtered.size()]);
						return "Track "+ (track)+"/"+totaltracks;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				return null;
			}

			@Override
			public PhoneData[] setTrack() {
				// TODO Auto-generated method stub
				return filarray;
			}

			@Override
			public String nextTrackName() {
				// TODO Auto-generated method stub
				if(track+1 <= totaltracks){
					ArrayList<PhoneData> filtered;
					track++;
					try {
						
						filtered = mysql.query("FilteredData", "PhoneID = '"+phoneid+"' AND TrackNo = "+ track);
						filarray = filtered.toArray(new PhoneData[filtered.size()]);
						return "Track "+ (track)+"/"+totaltracks;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				return null;
			}
			
		};
		PlotTracks.plotTrack2(filtered.toArray(new PhoneData[filtered.size()]), PlotTracks.X, PlotTracks.Y, 0.1f, "Track "+track+"/"+totaltracks, tcl);

	}
	private static void getPhoneID() {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame();
		frame.setResizable(false);
		
		JLabel selectlabel = new JLabel("Select phone id: ");
		selectlabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		final JComboBox<String> cbox1 = new JComboBox<String>(phones);
		cbox1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		final JButton okbutton = new JButton("Plot it");
		okbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(selectlabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(cbox1);
		panel.add(Box.createVerticalStrut(15));
		panel.add(okbutton);
		okbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.remove(okbutton);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				JLabel oklabel = new JLabel("Getting tracks...");
				oklabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				panel.add(oklabel);
				frame.pack();
				new Thread(){
					public void run(){
						try {
							plot((String)(cbox1.getSelectedItem()));
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						frame.dispose();
					}
				}.start();
			}
			
		});
		
		frame.setTitle("Select axis - StatsReader");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
