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
		
		for(int c = 0; c<length; c++){
			
			i[c] = Double.parseDouble(newdat[0][c]);			
			j[c] = Double.parseDouble(newdat[1][c]);
		}
		
		Arrays.sort(i);
		Arrays.sort(j);
		
		int tmx = 0;
		int tmy = 0;
		while (i[tmx] < 90){
			tmx++;
		//System.out.println(i[tmx]);	
		}
		
		while (i[tmy] < 303){
			tmy++;
		}
		
		int maxx = (int) i[i.length-1];
		int maxy = (int) j[j.length-1];
		int maxxt = maxx - (int) i[tmx];
		int maxyt = maxy - (int) j[tmy];
		
		double[][] zhs = new double[maxxt+1][maxyt+1];
		
		for(int x=0; x<(length); x++){
			xval = Integer.parseInt(newdat[0][x]);
			xval -= (int) i[tmx];
			if(xval<0){
				xval=0;
			}
			yval = Integer.parseInt(newdat[1][x]);
			yval -= (int) j[tmy];
			if(yval<0){
				yval=0;
			}
			if(newdat[12][x] != null){
				zsp = Double.parseDouble(newdat[12][x]);
			
			}else{
				zsp = 0.001;
			}
			
			if(zsp !=0){
				zhs[xval][yval] = zsp;
			}else{
				zhs[xval][yval] = 0;
			}
			
		}
		
		
		HeatChart map = new HeatChart(zhs);

		// Step 2: Customise the chart.
		map.setTitle("Speed");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(6);
		map.setCellHeight(2);
		System.setProperty("Axis", "#FF1493");
		map.setAxisColour(Color.getColor("Axis"));
		map.setShowXAxisValues(false);
		map.setShowYAxisValues(false);
		System.setProperty("LowVal", "#AFEEEE");
		System.setProperty("HighVal", "#191970");
		map.setHighValueColour(Color.getColor("HighVal"));
		map.setLowValueColour(Color.getColor("LowVal"));
		
		
		map.saveToFile(new File("speed heat chart NEW.png"));
			
		
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
