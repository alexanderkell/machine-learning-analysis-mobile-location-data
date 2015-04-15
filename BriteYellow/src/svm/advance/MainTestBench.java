package svm.advance;

import java.io.IOException;

import comirva.util.*;

import csvexport.CSVWriter;

public class MainTestBench {

	// File containing the track analysis data
	final static String filename = "The_Big_Track_Analysis\\trackdatafiles\\input.csv";

	// For filtering track with x y bounds
	final static int[][] xy = new int[][]{
			{200,302},
			{850,364}
	};
	// For PCA analysis
	final static int pca_dimension = 6;
	final static int[] pca_columns = new int[]{
			3,6
	};
	// 1,3
	// For normal analysis
	final static String[] cols = new String[]{
		"TIMESSTOPPEDHERE","TOTAVRGSPEED"
	};
		
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TrackInfoReader tir = new TrackInfoReader(filename);
		
		tir.filter(xy[0][0],xy[0][1],xy[1][0],xy[1][1]);
		String[] types = tir.getTypes();
		double[][] data = tir.getData();

//		plot(types,data,tir);
		
		
//		write(types,data);
//		plot(types,data,tir);
		plot(types,data, tir,pca_columns);
	}
	
	public static void plot(String[] types, double[][] data, TrackInfoReader tir, int... columns) throws IOException{
		// For extracting the columns for PCA analysis
		double[][] data2 = tir.getExtractedColumns(1,2,3,4,5,6,7,8,9,10,11,12);
		PCA pca = new PCA(data2,pca_dimension);
		
		double[][] pca_result = pca.getPCATransformedDataAsDoubleArray();
		System.out.println("Dimension: "+pca_result[0].length);
		double[][] pca_result2 = new double[pca_result.length][pca_result[0].length+1];
		for(int i = 0; i < pca_result.length; i++){
			pca_result2[i][0] = data[i][0];
			for(int j = 0; j < pca_result[0].length; j++){
				pca_result2[i][j+1] = pca_result[i][j];
			}
		}
		System.out.println(types.length);
		
		STrainHelper sth = new STrainHelper(types, pca_result2);
		sth.setParam(STrainHelper._t, STrainHelper.RBF);
		sth.setParam(STrainHelper._h, 0);
		sth.svmTrain(columns);
		
		
		// For plotting hyperplanes
//		String[] headers = tir.getHeaders();
		sth.plot_graph("PCA analysis of tracks from ("+xy[0][0]+","+xy[0][1]+") to ("+xy[1][0]+","+xy[1][1]+")" ,"PCA column "+String.valueOf(columns[0]), "PCA column "+String.valueOf(columns[1]), null);
	}
	
	public static void plot(String[] types, double[][] data, TrackInfoReader tir) throws IOException{
		System.out.println(types.length);
		
		STrainHelper sth = new STrainHelper(types, data);
		sth.setParam(STrainHelper._t, STrainHelper.POLY);
		sth.setParam(STrainHelper._h, 0);
		int[] columns = new int[cols.length];
		for(int i=0; i<cols.length; i++){
			columns[i] = tir.findAxisLocation(cols[i]);
		}
		sth.svmTrain(columns);
		
		
		// For plotting hyperplanes
		String[] headers = tir.getHeaders();
		sth.plot_graph("From ("+xy[0][0]+","+xy[0][1]+") to ("+xy[1][0]+","+xy[1][1]+")" , headers[columns[0]], headers[columns[1]], null);
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
