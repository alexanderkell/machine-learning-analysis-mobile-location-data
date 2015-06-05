package svm.advance;

import graphing.PlotHelper;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.event.ChartProgressEvent;

import comirva.util.*;

import csvexport.CSVWriter;
import dialogs.ProgressDialog;

public class MainTestBench {

	// File containing the track analysis data
	final static String filename = "Generated_Track_Store_Whole_Corridor\\trackdatanew\\oldKmeans\\kmeans_train.csv";
	final static String testfilename = "Generated_Track_Store_Whole_Corridor\\trackdatanew\\oldKmeans\\kmeans_test.csv";;

	// For filtering track with x y bounds
	final static int[][] xy = new int[][]{
			{200,302},
			{850,364}
	};
	// For PCA analysis
	final static boolean pca = false;	// Perform PCA analysis or not
	final static int pca_dimension = 6;
	final static int[] pca_columns = new int[]{
			1,2,3,4,5,6
	};
	// 1,3
	// For normal analysis
/*	final static String[] cols = new String[]{
		"pathLength","timeStopped","noStops","timeSpent","inactiveTime","sThetaChange","sThetaIn","sThetaOut","sThetaInOut","timePerStop","totAvrgSpeed","timesStoppedHere","pathPerShortest","timePerShortest","speedLessThan3","speedLessThan2","speedLessThan1","anglelargerthan5","anglelargerthan10","anglelargerthan15","anglelargerthan20","speedLargerThan10"
	};
*/	final static String[] cols = new String[]{
//		"pathLength","timeSpent","sThetaChange","timePerStop","totAvrgSpeed","timesStoppedHere","pathPerShortest","timePerShortest","speedLessThan3","speedLessThan2","speedLessThan1","anglelargerthan5","anglelargerthan10","anglelargerthan15","anglelargerthan20","speedLargerThan10"
		"DistToCluster1","DistToCluster2","DistToCluster3"
	};
		
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TrackInfoReader tir = new TrackInfoReader(filename);
		
		tir.filter(xy[0][0],xy[0][1],xy[1][0],xy[1][1]);

		
//		write(types,data);
//		plotWithOutAnalysis(types,data, tir,pca_columns);
		plot(tir);
		tir = new TrackInfoReader(testfilename);
		
		tir.filter(xy[0][0],xy[0][1],xy[1][0],xy[1][1]);
		predict(tir);
	}
	
	public static void plotWithOutAnalysis(String[] types, double[][] data, TrackInfoReader tir, int... columns){
		
		// For extracting the columns for PCA analysis
		int[] headercolumns = new int[tir.findAxisLocation("x1")-1];
		for(int i = 0; i < headercolumns.length; i++){
			headercolumns[i] = i+1;
		}
		double[][] data2 = tir.getExtractedColumns(headercolumns);
		
				
		// Perform PCA analysis
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
		
		
		String people[] = new String[]{
				"Security",
				"Shopper",
				"Business"
				
		};
		PlotHelper ph = new PlotHelper("PCA analysis ("+ pca_dimension +" dimensions) of tracks from ("+xy[0][0]+","+xy[0][1]+") to ("+xy[1][0]+","+xy[1][1]+")" ,"PCA column "+String.valueOf(columns[0]), "PCA column "+String.valueOf(columns[1]), people);
		for(int i = 0; i<types.length; i++){
			ph.addData(types[i],pca_result2[i][pca_columns[0]],pca_result2[i][pca_columns[1]]);
		}
		ph.showDialog();
	}
	
	
	
	public static double[][] extractCols(TrackInfoReader tir){
		int[] headercolumns = new int[tir.findAxisLocation("x1")-1];
		for(int i = 0; i < headercolumns.length; i++){
			headercolumns[i] = i+1;
		}
		return tir.getExtractedColumns(headercolumns);
	}
	public static double[][] pca(String[] types, double[][] data, int... columns){
		
				
		// Perform PCA analysis
		PCA pca = new PCA(data,pca_dimension);
		
		double[][] pca_result = pca.getPCATransformedDataAsDoubleArray();
		System.out.println("Dimension: "+pca_result[0].length);
/*		double[][] pca_result2 = new double[pca_result.length][pca_result[0].length+1];
		for(int i = 0; i < pca_result.length; i++){
			pca_result2[i][0] = data[i][0];
			for(int j = 0; j < pca_result[0].length; j++){
				pca_result2[i][j] = pca_result[i][j];
			}
		}
*/		return pca_result;
	}
	
	public static STrainHelper train(String[] types, double[][] data, CustomChartProgressListener ccpl, int... columns) throws IOException{
		STrainHelper sth = new STrainHelper(types, data,ccpl);
		
		sth.setParam(STrainHelper._t, 1);
//		sth.setParam(STrainHelper._h, 0);
		sth.setParam(STrainHelper._g, 3);
		sth.setParam(STrainHelper._r, 3);
		sth.setParam(STrainHelper._c, 10);
		sth.setParam(STrainHelper._d, 6);
		sth.svmTrain(columns);
		return sth;
	}
	public static void plot(TrackInfoReader tir) throws IOException{
		int step = 0;
		final int totalsteps = 3;
		
		// For extracting the columns for PCA analysis
		final ProgressDialog svmpd = new ProgressDialog("SVM training in progress");

		// Get the types of person corresponding to the track
		String[] types = tir.getTypes();
		
		int[] columns = new int[cols.length];
		for(int i=0; i<cols.length; i++){
			columns[i] = tir.findAxisLocation(cols[i]);
		}
		
		// Perform PCA analysis
		step++;
		double[][] pca_result = null;
		double[][] data = tir.getData();
		if(pca){
			// Get the right columns
			double[][] pca_data = tir.getExtractedColumns(columns);
			svmpd.updateProgress("Performing PCA analysis (Step "+ step+"/"+totalsteps+")");
			pca_result  = pca(types, pca_data, pca_columns);
		}
		System.out.println("Number of records to be trained: "+types.length);
		svmpd.updateProgress("Training "+types.length+" records (Step "+ ++step+"/"+totalsteps+")");

		CustomChartProgressListener ccpl = new CustomChartProgressListener(){

			@Override
			public void chartProgress(ChartProgressEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void print(String s) {
				// TODO Auto-generated method stub
				if(! s.equals("."))
				svmpd.updateLog(s);
			}

			@Override
			public void onAbort() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void finish() {
				// TODO Auto-generated method stub
				svmpd.finish();
			}

			@Override
			public void progressUpdated(int percent) {
				// TODO Auto-generated method stub
				svmpd.updateProgress(percent);
			}
			
		};
		STrainHelper sth = null;
		
		int[] pca_columns2;
		if(pca){
			pca_columns2 = new int[pca_columns.length];
			for(int i = 0;i<pca_columns2.length; i++)
				pca_columns2[i] = pca_columns[i]-1;
			sth = train(types, pca_result, ccpl, pca_columns2);
		}else{
			sth = train(types, data, ccpl, columns);
		}
		// For plotting hyperplanes
		svmpd.updateProgress("Plotting results (Step "+ ++step+"/"+totalsteps+")");


		if(pca && pca_columns.length == 2){
			sth.plot_graph("PCA analysis ("+ pca_dimension +" dimensions) of tracks from ("+xy[0][0]+","+xy[0][1]+") to ("+xy[1][0]+","+xy[1][1]+")" ,"PCA column "+String.valueOf(pca_columns[0]), "PCA column "+String.valueOf(pca_columns[1]));
		} else if(!pca && cols.length == 2){
			sth.plot_graph("From ("+xy[0][0]+","+xy[0][1]+") to ("+xy[1][0]+","+xy[1][1]+")" , cols[columns[0]], cols[columns[1]]);
		} else{
			ccpl.finish();
		}
		
		// Find the accuracy of the hyperplane
		SPredictHelper sph = new SPredictHelper(sth.getModel(), sth.getStrNumConverter());
		for(int i = 0; i<types.length; i++){
			if(pca)
				sph.predict(types[i], pca_result[i], pca_columns2);
			else
				sph.predict(types[i], data[i], columns);
		}
		System.out.println(
				"Correct/Tested: "+sph.getCorrect()+"/"+sph.getTotal()+" (Accuracy: "+sph.getAcurracy()*100+"%)"
				);
	}
	
/*	public static void plot(String[] types, double[][] data, TrackInfoReader tir) throws IOException{
		System.out.println(types.length);
		
		STrainHelper sth = new STrainHelper(types, data);
		sth.setParam(STrainHelper._t, STrainHelper.POLY);
		sth.setParam(STrainHelper._h, 0);
		int[] columns = new int[cols.length];
		for(int i=0; i<cols.length; i++){
			columns[i] = tir.findAxisLocation(cols[i]);
		}
		sth.svmTrain(columns);
		
		
//		// For plotting hyperplanes
//		String[] headers = tir.getHeaders();
//		sth.plot_graph("From ("+xy[0][0]+","+xy[0][1]+") to ("+xy[1][0]+","+xy[1][1]+")" , headers[columns[0]], headers[columns[1]], null);
	}
*/	public static void predict(TrackInfoReader tir) throws IOException{
		int step = 0;
		final int totalsteps = 3;
		
		// For extracting the columns for PCA analysis
		final ProgressDialog svmpd = new ProgressDialog("SVM predicting in progress");

		// Get the types of person corresponding to the track
		String[] types = tir.getTypes();
		
		int[] columns = new int[cols.length];
		for(int i=0; i<cols.length; i++){
			columns[i] = tir.findAxisLocation(cols[i]);
		}
		
		// Perform PCA analysis
		step++;
		double[][] pca_result = null;
		double[][] data = tir.getData();
		if(pca){
			// Get the right columns

			double[][] pca_data = tir.getExtractedColumns(columns);
			svmpd.updateProgress("Performing PCA analysis (Step "+ step+"/"+totalsteps+")");
			pca_result  = pca(types, pca_data, pca_columns);
		}
		System.out.println("Number of records to be tested: "+types.length);
		svmpd.updateProgress("Testing "+types.length+" records (Step "+ ++step+"/"+totalsteps+")");

		CustomChartProgressListener ccpl = new CustomChartProgressListener(){

			@Override
			public void chartProgress(ChartProgressEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void print(String s) {
				// TODO Auto-generated method stub
				if(! s.equals("."))
				svmpd.updateLog(s);
			}

			@Override
			public void onAbort() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void finish() {
				// TODO Auto-generated method stub
				svmpd.finish();
			}

			@Override
			public void progressUpdated(int percent) {
				// TODO Auto-generated method stub
				svmpd.updateProgress(percent);
			}
			
		};
		// For plotting hyperplanes
		svmpd.updateProgress("Plotting results (Step "+ ++step+"/"+totalsteps+")");


		
		// Predict results
		// Extract columns for PCA
		int[] pca_columns2;
		if(pca){
			pca_columns2 = new int[pca_columns.length];
			for(int i = 0;i<pca_columns2.length; i++)
				pca_columns2[i] = pca_columns[i]-1;
		}
		String[] phones = new String[]{
				"Shopper",
				"Security",
				"Business",
		};
		
		for(int h = 0; h<phones.length; h++){
			SPredictHelper sph = new SPredictHelper(new File("").getAbsolutePath()+"/src/svm/models/new.train.model","src\\svm\\models\\new.train.snc");

			for(int i = 0; i<types.length; i++){
//				System.out.println(types[i]);
				if(types[i].equals(phones[h])){
					if(pca){
						
						sph.predict(types[i], pca_result[i], pca_columns2);
					}else
						sph.predict(types[i], data[i], columns);
				}
			}
			ccpl.finish();
			System.out.println(
					"Correct/Tested: "+sph.getCorrect()+"/"+sph.getTotal()+" (Accuracy: "+sph.getAcurracy()*100+"%)"
					);
		};
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
