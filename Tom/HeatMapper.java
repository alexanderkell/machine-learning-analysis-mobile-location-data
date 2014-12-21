import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;


public class HeatMapper {
	
	
	public static void speedHeat(int opt) throws ParseException, IOException{
		int xval = 0;
		int yval = 0;
		double zsp = 0;
		String[][] newdat = dataFormat.datcalc(opt);
		int length = 0;
		while (newdat[0][length] != null){
			length++;
		}
		double[] i = new double[length];
		double[] j = new double[length];
		double[] k = new double[length];
		
		for(int c = 0; c<length; c++){
			
			i[c] = Double.parseDouble(newdat[0][c]);			
			j[c] = Double.parseDouble(newdat[1][c]);
			k[c] = Double.parseDouble(newdat[2][c]);
		}
		Arrays.sort(i);
		Arrays.sort(j);
		Arrays.sort(k);
		int maxx=(int) i[i.length-1];
		int maxy=(int) j[j.length-1];
		int maxz=(int) k[k.length-1];
		double[][] zhs = new double[maxx+1][maxy+1];
		
		/*for (int k = 0; k < length; k++){
			for (int l = 0; l < 14; l++) {
				System.out.print(newdat[l][k] + " ");
			}
				System.out.print("\n");
			}*/
		
		for(int x=0; x<length; x++){
			xval = Integer.parseInt(newdat[0][x]);
			yval = Integer.parseInt(newdat[1][x]);
			if(newdat[12][x] != null){
				zsp = Double.parseDouble(newdat[12][x]);
			
			}else{
				zsp = 0;
			}
			zhs[xval][yval] = zsp;

			
		}
		
		HeatChart map = new HeatChart(zhs);

		// Step 2: Customise the chart.
		map.setTitle("Heat Map of Speed in Different Positions");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(2);
		map.setCellHeight(2);
		map.setAxisColour(Color.green);
		
		map.saveToFile(new File("speed heat chart.png"));
			
		
	}
	
	
	public static void accHeat(int opt) throws ParseException, IOException{
		int xval = 0;
		int yval = 0;
		double zap = 0;
		String[][] newdat = dataFormat.datcalc(opt);
		int length = 0;
		while (newdat[0][length] != null){
			length++;
		}
		double[] i = new double[length];
		double[] j = new double[length];
		double[] k = new double[length];
		
		for(int c = 0; c<length; c++){
			
			i[c] = Double.parseDouble(newdat[0][c]);			
			j[c] = Double.parseDouble(newdat[1][c]);
			k[c] = Double.parseDouble(newdat[2][c]);
		}
		Arrays.sort(i);
		Arrays.sort(j);
		Arrays.sort(k);
		int maxx=(int) i[i.length-1];
		int maxy=(int) j[j.length-1];
		int maxz=(int) k[k.length-1];
		double[][] zha = new double[maxx+1][maxy+1];
		
		
		for(int x=0; x<length; x++){
			xval = Integer.parseInt(newdat[0][x]);
			yval = Integer.parseInt(newdat[1][x]);
			if(newdat[13][x] != null){
				zap = Double.parseDouble(newdat[13][x]);
			
			}else{
				zap = 0;
			}
			zha[xval][yval] = zap;

			
		}
		
		HeatChart map = new HeatChart(zha);

		// Step 2: Customise the chart.
		map.setTitle("Heat Map of Acceleration in Different Positions");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(2);
		map.setCellHeight(2);
		
		map.saveToFile(new File("acc heat chart.png"));
		
		
	}

}
