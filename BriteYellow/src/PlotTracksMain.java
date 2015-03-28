import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
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

	private static int totaltracks;
	public static void main(String args[]) throws Exception{
		
		System.out.println("PlotTracksMain");
		
		getPhoneIDAndPlot();
				
	}

	private static void plot(final String phoneid) throws SQLException{

		final insertMySQL mysql = new insertMySQL();
				
		final TrackChangeListener tcl = new TrackChangeListener(){

			@Override
			public PhoneData[] setTrack(int index) {
				// TODO Auto-generated method stub
				if(index >= 1 && index <= totaltracks){
					try {
						
						ArrayList<PhoneData> filtered = mysql.query("FilteredData", "PhoneID = '"+phoneid+"' AND TrackNo = "+ index);
						return filtered.toArray(new PhoneData[filtered.size()]);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				return null;
			}

			
		};
		totaltracks = mysql.totalTracksQuery("FilteredData", "PhoneID = '"+phoneid+"'");
		if(totaltracks == 0)
			System.err.println("There are no tracks associated with this phone id: "+phoneid);
		else
			PlotTracks.plotTrack2(PlotTracks.X, PlotTracks.Y, 0.1f, tcl, totaltracks);

	}
	private static void getPhoneIDAndPlot() {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame();
		frame.setResizable(false);
		
		JLabel selectlabel = new JLabel("Select a phone: ");
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
				cbox1.setEnabled(false);
				panel.remove(okbutton);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				final JLabel oklabel = new JLabel("Getting tracks...");
				oklabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				panel.add(oklabel);
				frame.pack();
				new Thread(){
					public void run(){
						try {
							plot(phones[cbox1.getSelectedIndex()]);
							frame.dispose();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
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
