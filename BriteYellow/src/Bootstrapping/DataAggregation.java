package Bootstrapping;

import java.text.ParseException;
import java.util.ArrayList;

import objects.*;
import distribution.StatsGenerator;

public class DataAggregation {

////////////////////////////////////////////////////////
// Speed Distribution
//Name: Alex Kell
//Date: 01/04/2015
//Description: Takes in phone data and calculates frequency of
// specific speed occuring during entire path
//////////////////////////////////////////////////////
	public double[][] avSpeedFreq(ArrayList<PhoneData> pd) throws ParseException{
		StatsGenerator sG = new StatsGenerator(pd);
		int steps =300; //Number of divisions of speed
		int t = 1;
		double x[][] = new double[2][steps];
		for(int i = 1; i<steps; i++){ //Returns frequency speeds have been attained within (200,850) and (302, 364) coords
			x[1][t] = sG.getTotalFreqAt(StatsGenerator.AVERAGE_SPEED, i*0.05, i*0.05+0.05, 200, 850, 302, 364);
			x[0][t] = i*0.05+0.05; //Speeds, divided in 0.05coords/sec
			t++;
		}		
		return x;
	}
	
	public double[][] avSthetaCh(ArrayList<PhoneData> pd) throws ParseException{
		StatsGenerator sG = new StatsGenerator(pd);
		int steps =300;
		double x[][] = new double[2][steps];
		for(int i = 0; i<steps; i++){
			x[1][i] = sG.getTotalFreqAt(StatsGenerator.STHETACHANGE, i*0.015, i*0.015+0.015, 200, 850, 302, 364);
			x[0][i] = i*0.015+0.015;
		}	
		return x;
	}
	
	public double[][] calcProbability(double x[][]){
		double total=0;
		double out[][]=new double[x[0].length][x[1].length]; 
		for(int i = 0; i<x[0].length; i++){
			total = total + x[1][i];
			
		}
		for(int i = 0; i<x[1].length; i++){
			out[1][i] = 10000*(x[1][i]/total);
			out[0][i] = x[0][i];
		}
		return out;
	}
////////////////////////////////////////////////////////////////
//Converts Probability Distribution to Cumulative Distribution
//Name: Alex Kell
//Date: 13/04/2015
////////////////////////////////////////////////////////////////
	public double[][] distToCumulative(double x[][]){
		double cum[][] = new double[x[0].length][x[1].length];
		cum[1][0] = 0;
		for(int i=1; i<x[1].length; i++){
			//Adds each previous probability to itself
			cum[1][i] = (cum[1][i-1] + x[1][i]);
			cum[0][i] = x[0][i]; //Same x-axis
		}
		return cum;
	}
	
}
