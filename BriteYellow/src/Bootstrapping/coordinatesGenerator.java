package Bootstrapping;

import graphing.XYPlot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;

import dynamodb.DataBaseOperations;
import maths.DataGetter;
import objects.PhoneData;
import objects.TrackInfo;

public class coordinatesGenerator {
	public static void main(String args[]) throws Exception{
		coordinatesGenerator test = new coordinatesGenerator();
		double cumSpeed[][] = new double[2][10];
		cumSpeed[0][0] = 0;
		cumSpeed[0][1] = 10;				
		cumSpeed[0][2] = 20;
		cumSpeed[0][3] = 30;
		cumSpeed[0][4] = 40;
		cumSpeed[0][5] = 50;
		cumSpeed[0][6] = 60;
		cumSpeed[0][7] = 70;
		cumSpeed[0][8] = 80;
		cumSpeed[0][9] = 90;
		
		cumSpeed[1][0] = 0;
		cumSpeed[1][1] = 40;				
		cumSpeed[1][2] = 80;
		cumSpeed[1][3] = 90;
		cumSpeed[1][4] = 100;
		cumSpeed[1][5] = 110.5;
		cumSpeed[1][6] = 110.8;
		cumSpeed[1][7] = 120;
		cumSpeed[1][8] = 120.2;
		cumSpeed[1][9] = 120.3;
		
		double cumAngle[][] = new double[2][10];
		cumAngle[0][0] = 0;
		cumAngle[0][1] = 70;				
		cumAngle[0][2] = 80;
		cumAngle[0][3] = 90;
		cumAngle[0][4] = 95;
		cumAngle[0][5] = 96;
		cumAngle[0][6] = 97;
		cumAngle[0][7] = 98;
		cumAngle[0][8] = 99;
		cumAngle[0][9] = 100;
		
		cumAngle[1][0] = 0.5;
		cumAngle[1][1] = 0.10;				
		cumAngle[1][2] = 0.3;
		cumAngle[1][3] = 0.50;
		cumAngle[1][4] = 0.7;
		cumAngle[1][5] = 1;
		cumAngle[1][6] = 1.5;
		cumAngle[1][7] = 2;
		cumAngle[1][8] = 2.5;
		cumAngle[1][9] = 3;
		int numberofPoints = 1000;
		
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		for(int j=0; j<200; j++){
			ArrayList<Coordinates> track = test.generatePath(cumSpeed, cumAngle, 1000, j);
			if(track.get(track.size()-1).getY() < 364 && track.get(track.size()-1).getY() > 302){
				path.addAll(track);
				
				double x[] = new double[path.size()];
				double y[] = new double[path.size()];
				for(int i = 0; i<track.size(); i++){
					x[i] = track.get(i).getX();
					y[i] = track.get(i).getY();
				}
			}else
				j--;	
		}
		
		//ArrayList<Coordinates> path = test.generatePath(cumSpeed, cumAngle, numberofPoints);

		double x[] = new double[path.size()];
		double y[] = new double[path.size()];
		for(int i = 0; i<path.size(); i++){
			x[i] = path.get(i).getX();
			y[i] = path.get(i).getY();
		}
		
		CoordinatestoPhoneData one = new CoordinatestoPhoneData();
		PhoneData[] pdArray = one.convertToPhoneData(path, "H");
		DataGetter dg = new DataGetter(pdArray);
		
		PhoneData[] fullPhoneData = dg.getFullPhoneData();
		
		System.out.println("Creating Track Stats");
		Mlearning mlo = new Mlearning(200, 0, 0, 0);
		ArrayList<TrackInfo> TrackAnalysis = mlo.writeTrackStats(fullPhoneData);
		//ArrayList<TrackInfo> trackAnTot = new ArrayList<TrackInfo>();
		//trackAnTot.addAll(TrackAnalysis);
		System.out.println("Writing to DB");
		writeToDB(TrackAnalysis);
		
		
		XYPlot xyp = new XYPlot();
		xyp.plot(x, y, x, y, x, y, "trackGenerator", "Generated Tracks", "x-axis", "y-axis", "5");	
	}
	
	
	public ArrayList<Coordinates> generatePath(double[][] cumSpeed, double[][] cumAngle, int numberofPoints, int pathNo){
		ArrayList<Coordinates> track = new ArrayList<Coordinates>();
		ArrayList<Coordinates> xy = new ArrayList();
		ArrayList<Coordinates> first = firstPoint(xy, pathNo);	
		
		double randSpeed = returnRandomCumulative(cumSpeed);
		double randAngle = returnRandomCumulative(cumAngle);
		System.out.println(first.get(0).x+" "+first.get(0).y);
		double distance = distanceTravelled(randSpeed);
		double newCords[] = calcNewXandY(first, randAngle, distance);
		track = createPoint(first, newCords[0], newCords[1], pathNo);
		for(int i =0; i<numberofPoints; i++){
			double randSpeed1 = returnRandomCumulative(cumSpeed);
			double randAngle1 = returnRandomCumulative(cumAngle);
			double distance1 = distanceTravelled(randSpeed1);
			double newCords1[] = calcNewXandY(track, randAngle1, distance1);
			track = createPoint(track, newCords1[0], newCords1[1], pathNo);
		}
		return track;
	}
	
	public double[] reachLimit(ArrayList<Coordinates> total, int prop){
		double x2 = total.get(total.size()-1).getX();
		double y2 = total.get(total.size()-1).getY();
		double x1 = total.get(total.size()-2).getX();
		double y1 = total.get(total.size()-2).getY();
		if(prop == 1){	//y>364
			double hyp = Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
			double adj = Math.sqrt((x2-x1)*(x2-x1));
			double theta2 = Math.acos(adj/hyp);
			double theta1 = 0.01*randInt(0, 314);
		
			double theta3=0;
			//if(Double.isNaN(theta2)){
			//	theta3 = theta1;
			//}else{
				theta3 = theta2+theta1;
			//}
			double newDistance = 0.01*randInt(1,200);
			double x_new = newDistance*Math.cos(theta3);
			double y_new = newDistance*Math.sin(theta3);
			double coords[] = {x_new, y_new};
			return coords;
		}else{
			double hyp = Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
			double adj = Math.sqrt((y2-y1)*(y2-y1));
			double theta2 = Math.acos(adj/hyp);
			double theta1 = 0.01*randInt(0, 314);
		
			double theta3=0;
			if(Double.isNaN(theta2)){
				theta3 = theta1;
			}else{
				theta3 = theta2+theta1;
			}
			double newDistance = 0.01*randInt(1,200);
			double x_new = newDistance*Math.cos(theta3);
			double y_new = newDistance*Math.sin(theta3);
			double coords[] = {x_new, y_new};
			return coords;
		}
	}
	
	public ArrayList<Coordinates> createPoint(ArrayList<Coordinates> last, double newX, double newY, int Track){
		Coordinates co = new Coordinates();		
		long time = last.get(last.size()-1).tsLong;
		double x = last.get(last.size()-1).x;
		double y = last.get(last.size()-1).y;

		double x_new = x+newX;
		double y_new = y+newY;
		long time_new = time+1000;
		double stop=0;
		if(y_new < 302 || y_new > 364){ //Reflect if bigger than shop
			y_new=y-newY;
			/*
				double[] coordsCh = reachLimit(last,1);
				ArrayList<Coordinates> ret = createBoundPoint(last, coordsCh[0], coordsCh[1], Track);
				return ret;
			}	
			*/	
		}
			

		if(x < 180 || x > 910){
			x_new=x-newX;
		}
		co.setX(x_new);
		co.setY(y_new);
		co.setTsLong(time_new);
		co.setTimestamp(time_new);
		co.setTrack(Track);
		
		last.add(co);
		return last;
	}
	
	public ArrayList<Coordinates> createBoundPoint(ArrayList<Coordinates> last, double newX, double newY, int Track){
		Coordinates co = new Coordinates();		
		long time = last.get(last.size()-1).tsLong;
		double x = last.get(last.size()-1).x;
		double y = last.get(last.size()-1).y;

		double x_new = x+newX;
		double y_new = y+newY;
		long time_new = time+1000;
		
		co.setX(x_new);
		co.setY(y_new);
		co.setTsLong(time_new);
		co.setTimestamp(time_new);
		co.setTrack(Track);
		last.add(co);
		return last;
	}
	
	
	
	


	public ArrayList<Coordinates> firstPoint(ArrayList<Coordinates> xy, int i){
		Coordinates coord = new Coordinates();
		Coordinates coord1 = new Coordinates();
		double seed = Math.random(); //Decides which side track starts from
		int plus = 10000000*i;
		int first_Y = randInt(302,364);
		if(seed<0.5){
			coord.setX(198); //Set person at far left of corridor
			coord.setY(first_Y); //Decides at what y value we start from
			Timestamp time;
			coord.setTimestamp(plus);
			coord.setTsLong(plus);
			xy.add(coord);
			coord1.setX(200); //Set person at far left of corridor
			coord1.setY(first_Y); //Decides at what y value we start from
			coord1.setTimestamp(plus+1);
			coord1.setTsLong(plus+1);
			
			xy.add(coord1);
			
		}else{
			coord.setX(904);
			coord.setY(first_Y);
			coord.setTimestamp(plus);
			coord.setTsLong(plus);
			xy.add(coord);
			coord1.setX(900);
			coord1.setY(first_Y);
			coord1.setTimestamp(plus+1);
			coord1.setTsLong(plus+1);
			xy.add(coord1);
		}
		return xy;
	}
///////////////////////////////////////////////////////
//Random Attribute Selector
//Name: Alex Kell
//Date: 13/04/2015
//Date of Update: 13/04/2015
//Description: Returns random attribute, weighted  
// by the cumulative distribution generated by data
//////////////////////////////////////////////////////
	public double returnRandomCumulative(double cumDist[][]){
		int random = randInt((int) cumDist[1][0],(int) cumDist[1][cumDist[1].length-1]);
		//Selects random integer between smallest and largest probability value
		double cumRan=0;
		for(int i = 1; i<cumDist[1].length; i++){ //Loop to find appropriate bin
			if(random <= cumDist[1][i] && random > cumDist[1][i-1]){
				//If random value falls between bin then return upper bin limit
				cumRan = cumDist[0][i]; 
				break;
			}
		}
		return cumRan;
	}
	
	public double distanceTravelled(double Speed){
		double time = 1;
		double distance = Speed*time;
		return distance;
	}

	
	public double[] calcNewXandY(ArrayList<Coordinates> total, double newAngle, double newDistance){
		//Find angle between previous coordinates and horizontal

		if(Math.random()<0.5){
			newAngle = -newAngle;
		}
		double x2 = total.get(total.size()-1).getX();
		double y2 = total.get(total.size()-1).getY();
		double x1 = total.get(total.size()-2).getX();
		double y1 = total.get(total.size()-2).getY();
		double hyp = Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
		double adj = Math.sqrt((x2-x1)*(x2-x1));
		double theta2 = Math.acos(adj/hyp);
		double theta3 = 0;
		if(Double.isNaN(theta2)){
			theta3 = newAngle;
		}else{
			theta3 = theta2+newAngle;
		}
		double x_new = newDistance*Math.cos(theta3);
		double y_new = newDistance*Math.sin(theta3);
		
		double coords[] = {x_new, y_new};

		return coords;
	}
	
	/*
	public double[] reflection(ArrayList<Coordinates> total, double newDistance){
		//Find angle between previous coordinates and horizontal
		double x2 = total.get(total.size()-1).getX();
		double y2 = total.get(total.size()-1).getY();
		double x1 = total.get(total.size()-2).getX();
		double y1 = total.get(total.size()-2).getY();
		
		double n = 
		
		double hyp = Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
		double adj = Math.sqrt((x2-x1)*(x2-x1));
		double theta2 = Math.acos(adj/hyp);
		if(Double.isNaN(theta2)){
			theta2 = 0;
		}
		double theta3 = Math.PI-theta2;
		
		double x_new = newDistance*Math.cos(theta3);
		double y_new = newDistance*Math.sin(theta3);
		
		double coords[] = {x_new, y_new};
		return coords;
	}
	*/
		
	public int randInt(int min, int max){
		Random rand = new Random();
		int randomNum = rand.nextInt(max-min+1)+min;
		return randomNum;
	}
	
	public static void writeToDB(ArrayList<TrackInfo> TrackAnalysis) throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("Generated_Track_Store");
		//DBO.deleteTable();
		System.out.println("Creating Track Table");
		DBO.createTracksTable();
		System.out.println("Writing to Database");
		DBO.batchWrite(TrackAnalysis);
		System.out.println("Write complete");
	}
	
}
