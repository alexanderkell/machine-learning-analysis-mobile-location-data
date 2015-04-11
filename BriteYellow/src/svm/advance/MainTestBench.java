package svm.advance;

import java.io.IOException;

import csvexport.CSVWriter;

public class MainTestBench {

	
	final static int[][] xy = new int[][]{
			{200,322},
			{330,344}
	};
	
	final static String[] cols = new String[]{
			"PATH_LENGTH","TOTAVRGSPEED"
	};
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final String filename = "null\\trackdatafiles\\input.csv";

		TrackInfoReader tir = new TrackInfoReader(filename);
		
		tir.filter(xy[0][0],xy[0][1],xy[1][0],xy[1][1]);
		String[] types = tir.getTypes();
		double[][] data = tir.getData();

		plot(types,data,tir);
	}
	
	public static void plot(String[] types, double[][] data, TrackInfoReader tir) throws IOException{
		System.out.println(types.length);
		
		STrainHelper sth = new STrainHelper(types, data);
		sth.setParam(STrainHelper._t, 2);
		sth.setParam(STrainHelper._h, 0);
		int[] columns = new int[cols.length];
		for(int i=0; i<cols.length; i++){
			columns[i] = tir.findAxisLocation(cols[i]);
		}
		sth.svmTrain(columns);
		
		
		// For plotting hyperplanes
		String[] headers = tir.getHeaders();
		sth.plot_graph(headers[columns[0]], headers[columns[1]], null);
	}
	public static void predict(String[] types, double[][] data, TrackInfoReader tir) throws IOException{
		System.out.println(types.length);
		
		STrainHelper sth = new STrainHelper(types, data);
		sth.setParam(STrainHelper._t, 2);
		sth.setParam(STrainHelper._h, 0);
		int[] columns = new int[cols.length];
		for(int i=0; i<cols.length; i++){
			columns[i] = tir.findAxisLocation(cols[i]);
		}
		sth.svmTrain(columns);
		double[][] trained_data = sth.getTrainedData();
		SPredictHelper sph = new SPredictHelper(sth.getModel(),sth.getStrNumConverter());
		for(int i = 0; i < data.length; i++){
			sph.predict(types[i], trained_data[i]);
		}
		System.out.println(
				"Correct/Tested: "+sph.getCorrect()+"/"+sph.getTotal()+" (Accuracy: "+sph.getAcurracy()*100+"%)"
				);
	}
	public static void write(String[] types, double[][] data) throws IOException{

		String[][] str_data = new String[data.length][data[0].length+1];
		for(int i = 0; i < data.length; i++){
			str_data[i][0] = types[i];
			for(int j = 0; j < data[0].length; j++){
				str_data[i][j+1] = String.valueOf(data[i][j]);
			}
		}
		
		CSVWriter cw = new CSVWriter("null\\trackdatafiles\\splittedinputs\\"+xy[0][0]+","+xy[0][1]+","+xy[1][0]+","+xy[1][1]);
		for(int i = 0; i < data.length; i++){
			cw.write(str_data[i]);
		}
		cw.finish();
	}

}
