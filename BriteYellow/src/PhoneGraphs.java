import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import CSVImport.CSVReaders;
import Graphing.PlotHelper;

public class PhoneGraphs {
	
	private static String[][] cd;
	public static void main(String[] args){
		Scanner ui = new Scanner( System.in );//state the scanner
	

		System.out.println("Enter the Full Address of the Data File You Wish to Input:");  
		System.out.println("(for example: '/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv')");
		String fn = ui.nextLine();
		CSVReaders Read = new CSVReaders(fn);
		System.out.println("Set as: " + Read.getFileName() + "\n");
		ui.close();
		
		// Set default selected phone to phone 1
		cd = Read.myPhone(1);
		String[] labels = new String[]{"Phone 1 data"};	//The first series is invisible
		PlotHelper plot = new PlotHelper("Phone 1 positions", "X", "Y", labels);
		plot.setAxisRange(0, 1100, 0, 400);
		plot.setRangeAxisInverted(true);
		plot.setSeriesPaint(labels[0], Color.RED);
		plot.setSeriesLinesVisble(labels[0], true);

		plot.addData(labels[0], cd[0], cd[1]);
		showPlot(plot, labels, Read);
	}

	private static void showPlot(final PlotHelper plot, final String[] labels, final CSVReaders read){
		
		// Create a JFrame and set layout to "BorderLayout"
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		
		// Create the rest
		final JPanel panel = new JPanel();	// JPanel as container various components below
		final JComboBox<String> combobox = new JComboBox<String>(	// JComboBox for choosing a phone
				new String[]{ "Phone 1", "Phone 2",	"Phone 3", "Phone 4", "Phone 5", "Phone 6"}
				);
		final SpinnerNumberModel spinnermodel1 = new SpinnerNumberModel(	//SpinnerNumberModel 1 for spinner 1
	    		1,	//Initial value
	    		1,  //Minimum
	    		cd[0].length, //Maximum
	    		1	//Step
	    		);
	    final JSpinner spinner1 = new JSpinner(spinnermodel1);	// Spinner 1

	    final SpinnerNumberModel spinnermodel2 = new SpinnerNumberModel(	//SpinnerNumberModel 2 for spinner 2
	    		cd[0].length,	//Initial value
	    		1,  //Minimum
	    		cd[0].length, //Maximum
	    		1	//Step
	    		);
	    
	    final JSpinner spinner2 = new JSpinner(spinnermodel2);
		final JLabel maxpointlabel = new JLabel("/"+String.valueOf(cd[0].length));
		
	    // Add the chart to the center of the JFrame
		frame.add(plot.getChartPanel(), BorderLayout.CENTER);
		
		// Add the JPanel to the bottom (south) of the JFrame
		frame.add(panel, BorderLayout.SOUTH);
		
		// Set layout "FlowLayout" for JPanel and add everything
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(new JLabel("Showing"));
		panel.add(combobox);
		panel.add(new JLabel("for points from"));	// JLabel for "Showing points"
//	    panel.add(Box.createHorizontalStrut(5));	// Horizontal space
	    
	    panel.add(spinner1);	// Add spinner 1
//	    panel.add(Box.createHorizontalStrut(5));	// Horizontal space
	    panel.add(new JLabel("to"));	// JLabel for "to"
//	    panel.add(Box.createHorizontalStrut(5));	// Horizontal space

	    panel.add(spinner2);	// Add spinner 2
	    panel.add(maxpointlabel);
	    
	    // Configure components
	    // Add change listener to the combobox
	    combobox.addItemListener(new ItemListener(){
	    	private int index;
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				int new_index = combobox.getSelectedIndex()+1;
				if(new_index != index){
					index = new_index;
					String index_name = (String) combobox.getSelectedItem();
					
					plot.clearData(labels[0]);
					
					cd = read.myPhone(index);
					plot.changeSeriesName(labels[0], index_name);	
					plot.setTitle(index_name+" positions "+spinner1.getValue()+" to "+spinner2.getValue());
					
					if(cd[0].length>0) {
						spinner1.setEnabled(true);
						spinner2.setEnabled(true);
						maxpointlabel.setEnabled(true);
						spinner2.setValue(1);
						spinner2.setValue(1);
						spinnermodel1.setMaximum(cd[0].length);
						spinnermodel2.setMaximum(cd[0].length);
						spinner2.setValue(cd[0].length);
						maxpointlabel.setText("/"+String.valueOf(cd[0].length));
						plot.addData(labels[0], cd[0], cd[1]);
					} else {
						spinner1.setEnabled(false);
						spinner2.setEnabled(false);
						maxpointlabel.setEnabled(false);
						JOptionPane.showMessageDialog(null,index_name+" doesn't contain any data.","Oops :(", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
	    	
	    });
	    // Add change listener to spinner 1
	    spinner1.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				spinnermodel2.setMinimum((Integer) spinner1.getValue());
				plot.clearData(labels[0]);
				plot.addData(labels[0], cd[0],cd[1], ((Integer) spinner1.getValue())-1, (Integer) spinner2.getValue()-1);
				plot.setTitle(combobox.getSelectedItem()+" positions "+spinner1.getValue()+" to "+spinner2.getValue());
			}
	    	
	    });
	    
	    spinner2.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				spinnermodel1.setMaximum((Integer) spinner2.getValue());
				plot.clearData(labels[0]);
				plot.addData(labels[0], cd[0],cd[1], ((Integer) spinner1.getValue())-1, (Integer) spinner2.getValue()-1);
				plot.setTitle(combobox.getSelectedItem()+" positions "+spinner1.getValue()+" to "+spinner2.getValue());

			}
	    	
	    });
		
	    // Set title
		plot.setTitle(combobox.getSelectedItem()+" positions "+spinner1.getValue()+" to "+spinner2.getValue());
		frame.pack();
		frame.setVisible(true);
	}
}
