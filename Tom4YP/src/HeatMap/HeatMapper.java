package HeatMap;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import Maths.*;



public class HeatMapper {
	
	
	public void speedHeat(int opt, String fn) throws ParseException, IOException{
		int xval = 0;
		int yval = 0;
		DataFormatOperations DFO = new DataFormatOperations();
		double zsp = 0;
		
		String[][] newdat = DFO.calcData(opt, fn);
		
		int length = 0;
		while (newdat[0][length] != null){
			length++;
		}
		
		double[] i = new double[length];
		double[] j = new double[length];
		
		for(int c = 0; c<length; c++){
			try{
				i[c] = Double.parseDouble(newdat[0][c]);			
				
			}catch(NumberFormatException ie){
				i[c] = 0;			
			}
			try{			
				j[c] = Double.parseDouble(newdat[1][c]);
				
			}catch(NumberFormatException ie){
				j[c] = 0;			
			}
		}
		
		Arrays.sort(i);
		Arrays.sort(j);
		
		int tmx = 0;
		int tmy = 0;
		while (i[tmx] < 90){
			tmx++;	
		}
		
		while (i[tmy] < 303){
			tmy++;
		}
		
		int maxx = (int) i[i.length-1];
		//System.out.println(maxx);
		int maxy = (int) j[j.length-1];
		//System.out.println(maxy);
		int maxxt = maxx - (int) i[tmx];
		int maxyt = maxy - (int) j[tmy];
		//System.out.println(maxx*maxy);
		int count = 0;
		int count0 = 0;
		double[][] zhs = new double[maxx+1][maxy+1];
		
		for(int x=0; x<(length); x++){
			try{
			xval = Integer.parseInt(newdat[0][x]);
			}catch(NumberFormatException xe){
				xval = 0;
			}
			//xval -= (int) i[tmx];
			if(xval<0){
				xval=0;
			}
			try{
			yval = Integer.parseInt(newdat[1][x]);
			}catch(NumberFormatException ye){
				yval = 0;
			}
			//yval -= (int) j[tmy];
			if(yval<0){
				yval=0;
			}
			if(newdat[12][x] != null){
				zsp = Double.parseDouble(newdat[12][x]);
			
			}else{
				zsp = 0;
			}
			//System.out.println(xval + " " + yval + "   " + zsp);
			if(zsp==0){
				count++;
			}else{
				count0++;
			}
				zhs[xval][yval] = zsp;
	
			
		}//System.out.println(" Non 0: "+count+"     Zero: "+count0);
		
		
		HeatChart map = new HeatChart(zhs);

		// Step 2: Customise the chart.
		map.setTitle("Speed");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(3);
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
	
	
	public static void accHeat(int opt, String fn) throws ParseException, IOException{
		int xval = 0;
		int yval = 0;
		double zap = 0;
		DataFormatOperations DFO = new DataFormatOperations();
		
		String[][] newdat = DFO.calcData(opt, fn);
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
		map.setTitle("Acceleration");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(3);
		map.setCellHeight(2);
		System.setProperty("Axis", "#FF1493");
		map.setAxisColour(Color.getColor("Axis"));
		map.setShowXAxisValues(false);
		map.setShowYAxisValues(false);
		System.setProperty("LowVal", "#AFEEEE");
		System.setProperty("HighVal", "#191970");
		map.setHighValueColour(Color.getColor("HighVal"));
		map.setLowValueColour(Color.getColor("LowVal"));
		
		map.saveToFile(new File("acc heat chart.png"));
		
		
	}

}
