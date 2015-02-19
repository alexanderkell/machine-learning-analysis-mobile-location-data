package Graphing;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Maths.DataFormatOperations;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;

public class PlotTracks {
	
	public final static String[] COLUMNS = {		
		"X", "Y", "Z", "WholeDate","Phone id", "Time Between values", "Xspeed", "YSpeed", "ZSpeed", "ModSpd", "STheta", "Xacc", "Yacc", "Zacc", "ModAcc", "ATheta"	
	};
	public static void plotTrack1(String[][] track_info, int row, int col, float timescaler){
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
			jlabel.setText("<html>"+track_info[i][3] + "<br> Point "+(i+1)+"</html>");
			  try{
				  int tb = (int) (timescaler*1000*(int) Double.parseDouble(track_info[i][5]));
					Thread.sleep(tb);
			  } catch (NumberFormatException e){
					System.out.println(e.toString());
				} catch (InterruptedException e){
					System.err.println("User Aborted");
					return;
				}
			 
			plot.addData(label[0], Double.parseDouble(track_info[i][row]), Double.parseDouble(track_info[i][col]));
		}
		
	}
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
					System.out.println(e.toString());
				} catch (InterruptedException e){
					System.err.println("User Aborted");
					return;
				}
			 
			plot.addData(label[0], Double.parseDouble(track_info[row][i]), Double.parseDouble(track_info[col][i]));
		}
		
	}
	/*public static void main(String args[]) throws ParseException, IOException{
		DataFormatOperations dfo = new DataFormatOperations(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
		for (int i = 1; i<=5; i++){
			dfo.changePhone(i);
			dfo.writeToFile();
		}
	}
*/
	/*public static void main(String args[]) throws ParseException, IOException{
		DataFormatOperations dfo = new DataFormatOperations(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
		String[][] result = dfo.getSort();
		System.out.println(result.length-1);
		for(int i=0; i<result[0].length; i++){
			for(int j=0; j<result.length; j++){
				System.out.print(result[j][i]+"\t");
			}
			System.out.print("\n");
		}
	//	plotTrack2(dfo.getFull(), 0, 1, 0.1f);
	}*/
		
}
