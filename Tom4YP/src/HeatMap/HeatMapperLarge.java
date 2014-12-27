package HeatMap;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import Maths.*;



public class HeatMapperLarge {
	
	
	public void speedHeat(int opt, String fn) throws ParseException, IOException{
		int xval = 0;
		int yval = 0;
		DataFormatOperations DFO = new DataFormatOperations(opt, fn);
		double zsp = 0;
		String[][] newdat = DFO.getFull();
		
		int length = DFO.getLength();
		
		double[] i = new double[length];
		double[] j = new double[length];
		double[] k = new double[3];
		
		for(int c = 0; c<length; c++){
			k = DFO.getXYZValue(c);
			try{
				i[c] = k[0];			
				
			}catch(NumberFormatException ie){
				i[c] = 0;			
			}
			try{			
				
				j[c] = k[1];
				
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
		
		int xi = 0;
		int yi = 0;
		
		for(int x=0; x<(length); x++){
			k = DFO.getXYZValue(x);
			try{
			xval = (int) k[0];
			}catch(NumberFormatException xe){
				xval = 0;
			}
			//xval -= (int) i[tmx];
			if(xval<0){
				xval=0;
			}
			try{
			yval = (int) k[1];
			}catch(NumberFormatException ye){
				yval = 0;
			}
			//yval -= (int) j[tmy];
			if(yval<0){
				yval=0;
			}
			if(newdat[12][x] != null){
				zsp = DFO.getModSValue(x);
			
			}else{
				zsp = 0;
			}
			//System.out.println(xval + " " + yval + "   " + zsp);
			if(zsp==0){
				count++;
			}else{
				count0++;
			}
			
			xi= -3;
			try{
				while(xi<7){
						zhs[xval+xi][yval] = zsp-1;
						zhs[xval+xi][yval-3] = zsp-1;
						zhs[xval+xi][yval-2] = zsp-1;
						zhs[xval+xi][yval-1] = zsp-1;
						zhs[xval+xi][yval+1] = zsp-1;
						zhs[xval+xi][yval+2] = zsp-1;
						zhs[xval+xi][yval+3] = zsp-1;
						xi++;
				}
					zhs[xval][yval] = zsp;
			}catch(ArrayIndexOutOfBoundsException aob){
				
			}
		}//System.out.println(" Non 0: "+count+"     Zero: "+count0);
		
		
		HeatChart map = new HeatChart(zhs);

		// Step 2: Customise the chart.
		map.setTitle("Speed");
		map.setXAxisLabel("X Co Ordinate");
		map.setYAxisLabel("Y Co Ordinate");
		map.setCellWidth(2);
		map.setCellHeight(3);
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
	
	
	public void AccHeat(int opt, String fn) throws ParseException, IOException{
		int xval = 0;
		int yval = 0;
		DataFormatOperations DFO = new DataFormatOperations(opt, fn);
		double zsp = 0;
		String[][] newdat = DFO.getFull();
		
		int length = 0;
		while (newdat[0][length] != null){
			length++;
		}
		
		
		double[] i = new double[length];
		double[] j = new double[length];
		double[] k = new double[3];
		
		for(int c = 0; c<length; c++){
			k = DFO.getXYZValue(c);
			try{
				i[c] = k[0];			
				
			}catch(NumberFormatException ie){
				i[c] = 0;			
			}
			try{			
				
				j[c] = k[1];
				
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
			k = DFO.getXYZValue(x);
			try{
			xval = (int) k[0];
			}catch(NumberFormatException xe){
				xval = 0;
			}
			//xval -= (int) i[tmx];
			if(xval<0){
				xval=0;
			}
			try{
			yval = (int) k[1];
			}catch(NumberFormatException ye){
				yval = 0;
			}
			//yval -= (int) j[tmy];
			if(yval<0){
				yval=0;
			}
			if(newdat[12][x] != null){
				zsp = DFO.getModAValue(x);
			
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
	
}