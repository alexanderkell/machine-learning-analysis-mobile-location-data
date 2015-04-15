package Bootstrapping;

import graphing.XYPlot;

import java.util.ArrayList;
import java.util.Random;

public class coordinatesGenerator {
	public static void main(String args[]){
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
		cumAngle[0][1] = 10;				
		cumAngle[0][2] = 20;
		cumAngle[0][3] = 30;
		cumAngle[0][4] = 40;
		cumAngle[0][5] = 50;
		cumAngle[0][6] = 60;
		cumAngle[0][7] = 70;
		cumAngle[0][8] = 80;
		cumAngle[0][9] = 90;
		
		cumAngle[1][0] = 05;
		cumAngle[1][1] = 10;				
		cumAngle[1][2] = 15;
		cumAngle[1][3] = 20;
		cumAngle[1][4] = 25;
		cumAngle[1][5] = 30;
		cumAngle[1][6] = 35;
		cumAngle[1][7] = 40;
		cumAngle[1][8] = 45;
		cumAngle[1][9] = 50;
		int numberofPoints = 1000;
		ArrayList<Coordinates> path = test.generatePath(cumSpeed, cumAngle, numberofPoints);

		double x[] = new double[path.size()];
		double y[] = new double[path.size()];
		for(int i = 0; i<path.size(); i++){
			System.out.println("Time: "+path.get(i).timestamp+"s Coordinate: "+path.get(i).getX()+", "+path.get(i).getY());
			x[i] = path.get(i).getX();
			y[i] = path.get(i).getY();
		}
		
		XYPlot xyp = new XYPlot();
		xyp.plot(x, y, x, y, x, y, "trackGenerator", "Generated Tracks", "x-axis", "y-axis", "5");	
	}
	
	
	public ArrayList<Coordinates> generatePath(double[][] cumSpeed, double[][] cumAngle, int numberofPoints){
		ArrayList<Coordinates> xy = new ArrayList();
		ArrayList<Coordinates> first = firstPoint(xy);	
		
		double randSpeed = returnRandomCumulative(cumSpeed);
		double randAngle = returnRandomCumulative(cumAngle);
		double distance = distanceTravelled(randSpeed);
		double newCords[] = calcNewXandY(first, randAngle, distance);
		ArrayList<Coordinates> track = createPoint(first, newCords[0], newCords[1]);
		for(int i =0; i<numberofPoints; i++){
			double randSpeed1 = returnRandomCumulative(cumSpeed);
			double randAngle1 = returnRandomCumulative(cumAngle);
			double distance1 = distanceTravelled(randSpeed1);
			double newCords1[] = calcNewXandY(track, randAngle1, distance1);
		
			track = createPoint(track, newCords1[0], newCords1[1]);
		}
		return xy;
	}
	
	public ArrayList<Coordinates> createPoint(ArrayList<Coordinates> last, double newX, double newY){
		Coordinates co = new Coordinates();
		
		double time = last.get(last.size()-1).timestamp;
		double x = last.get(last.size()-1).x;
		double y = last.get(last.size()-1).y;

		double x_new = x+newX;
		double y_new = y+newY;
		double time_new = time+1;
		
		if(y_new < 302 || y_new > 364){ //Reflect if bigger than shop
			// x_new = x-newX;
			y_new = y-newY;
		}

		if(x_new < 180 || x_new > 860){
			x_new = x-newX;
			//y_new = y-newY;
		}
		
		co.setX(x_new);
		co.setY(y_new);
		co.setTimestamp(time_new);
		
		last.add(co);
		
		return last;
	}
	


	public ArrayList<Coordinates> firstPoint(ArrayList<Coordinates> xy){
		Coordinates coord = new Coordinates();
		Coordinates coord1 = new Coordinates();
		double seed = Math.random(); //Decides which side track starts from
		int first_Y = randInt(302,364);
		if(seed<0.5){
			coord.setX(198); //Set person at far left of corridor
			coord.setY(first_Y); //Decides at what y value we start from
			coord.setTimestamp(0);
			xy.add(coord);
			coord1.setX(200); //Set person at far left of corridor
			coord1.setY(first_Y); //Decides at what y value we start from
			coord1.setTimestamp(1);
			
			xy.add(coord1);
			
		}else{
			coord.setX(854);
			coord.setY(first_Y);
			coord.setTimestamp(0);
			xy.add(coord);
			coord1.setX(850);
			coord1.setY(first_Y);
			coord1.setTimestamp(1);
			xy.add(coord1);
		}
		return xy;
	}
	
	public double returnRandomCumulative(double cumDist[][]){
		int random = randInt((int) cumDist[1][0],(int) cumDist[1][cumDist[1].length-1]);
		double cumRan=0;
		for(int i = 1; i<cumDist[1].length; i++){
			if(random <= cumDist[1][i] && random > cumDist[1][i-1]){
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
		if(x_new > 60){
			x_new =10;
		}
		if(y_new > 400){
			y_new =10;
		}
		
		
		double coords[] = {x_new, y_new};

		return coords;
	}
	
	
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
		
	public int randInt(int min, int max){
		Random rand = new Random();
		int randomNum = rand.nextInt((max-min)+1)+min;
		return randomNum;
	}
	
}