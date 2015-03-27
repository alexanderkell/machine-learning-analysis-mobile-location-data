package Distribution;

import graphing.PlotHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StatsReader {
	
	final static String cvsSplitBy = ",";
	private ArrayList<String> phoneID;
	private ArrayList<double[]> data;
	private BufferedReader br;
	private String[] headers;
	
	public final static String[] phones = {
		"HT25TW5055273593c875a9898b00",
		"ZX1B23QBS53771758c578bbd85",
        "TA92903URNf067ff16fcf8e045",
        "YT910K6675876ded0861342065",
        "ZX1B23QFSP48abead89f52e3bb",
	};
	
	final static String[] categories = {
		"Business",
		"Security",
		"Shopper"
	};
	
	public StatsReader(){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")+"/Statistics"));
		chooser.setFileFilter(new FileNameExtensionFilter(".csv files", "csv"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
	  		  File f = chooser.getSelectedFile();
	  		  if(f == null){
	  			System.err.println("Empty path");
		  		System.exit(1);
	  		  } else {
	  			  init(f.getAbsolutePath());
	  		  }
	  	} else {
	  		System.err.println("User aborted");
	  		System.exit(1);
	  	}
		
	}
	public StatsReader(String filename){
		init(filename);
	}
	private void init(String filename){		 
		phoneID = new ArrayList<String>();
		data = new ArrayList<double[]>();
		try {
	 
			br = new BufferedReader(new FileReader(filename));
			headers = (br.readLine().split(cvsSplitBy,2))[1].split(cvsSplitBy);
			String line;
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] segments = line.split(cvsSplitBy);
				phoneID.add(segments[0]);
				
				double[] subdata = new double[segments.length-1];
				for(int i = 0; i<subdata.length; i++){
					subdata[i] = Double.parseDouble(segments[i+1]);
				}
				data.add(subdata);
			}
			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void plotSelection(){
		final JFrame frame = new JFrame();
		final JComboBox<String> cbox1 = new JComboBox<String>(headers);
		final JComboBox<String> cbox2 = new JComboBox<String>(headers);
		JButton okbutton = new JButton("OK");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JLabel("Select axis: "));
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cbox1);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cbox2);
		panel.add(Box.createHorizontalStrut(15));
		panel.add(okbutton);
		okbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
				plot(cbox1.getSelectedIndex(), cbox2.getSelectedIndex());
			}
			
		});
		
		frame.setTitle("Select axis - StatsReader");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	public void plot(int index1, int index2){
		PlotHelper plothelper = new PlotHelper(headers[index2]+" vs "+headers[index1], headers[index1], headers[index2], categories);
		for(int i = 0; i < data.size(); i++){
			if(phoneID.get(i).equals(phones[0]) || phoneID.get(i).equals(phones[1]))
				plothelper.addData(categories[0], data.get(i)[index1], data.get(i)[index2]);
			else if(phoneID.get(i).equals(phones[2]))
				plothelper.addData(categories[1], data.get(i)[index1], data.get(i)[index2]);

			else if(phoneID.get(i).equals(phones[3]) || phoneID.get(i).equals(phones[4]))
				plothelper.addData(categories[2], data.get(i)[index1], data.get(i)[index2]);

		}
		plothelper.showDialog();
	}
	
	public static void main(String args[]){
		StatsReader sr = new StatsReader();
		sr.plotSelection();
	}
}
