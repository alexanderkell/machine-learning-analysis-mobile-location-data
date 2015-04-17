import graphing.XYPlot;

import java.util.ArrayList;
import java.util.Scanner;

import dynamodb.DataBaseOperations;
import maths.DataGetter;
import objects.PhoneData;
import objects.TrackInfo;
import Bootstrapping.*;


public class trackGeneratorTestB {
	public static void main(String args[]) throws Exception{
		
		
		System.out.println("How many points would you like to generate?");
		Scanner sc = new Scanner(System.in);
		int x = sc.nextInt();
		for(int i = 0; i<3; i++){	
			MLearningOptimisation MLOp = new MLearningOptimisation(200, 10, 10, 2);
			String type1[] = new String[3];
			type1[0] = "ZX1B23QBS53771758c578bbd85"; //Security
			type1[1] = "ZX1B23QFSP48abead89f52e3bb"; //Business
			type1[2] = "TA92903URNf067ff16fcf8e045"; //Shopper
			ArrayList<PhoneData> pd = MLOp.selectPhoneandFilter(type1[i]);//Security
			//ArrayList<PhoneData> pd = MLOp.selectPhoneandFilter("ZX1B23QFSP48abead89f52e3bb");//Business
			//ArrayList<PhoneData> pd = MLOp.selectPhoneandFilter("TA92903URNf067ff16fcf8e045");//Shopper
			DataAggregation DA = new DataAggregation();
		
			double[][] speedno= DA.avSpeedFreq(pd);
			double[][] angleno = DA.avSthetaCh(pd);
			System.out.println("Speed Distribution");
			double[][] speedDist = DA.calcProbability(speedno);
			System.out.println("Speed Theta Change Distribution");
			double[][] angleDist = DA.calcProbability(angleno);
			System.out.println("Calculating Cumulative Distribution");
			double[][] cumSpeed = DA.distToCumulative(speedDist);
			double[][] cumAngle = DA.distToCumulative(angleDist);
			
	/*		
			XYPlot xyp = new XYPlot();
			//xyp.plot(cumSpeed[0], cumSpeed[1], cumSpeed[0], cumSpeed[1], cumSpeed[0], cumSpeed[1], "Cumulative Speed Distribution", "Cumulative Speed Distribution - Shopper", "Speed (coordinates/sec)", "Cumulative Probability", "5");	
			xyp.plot(cumAngle[0], cumAngle[1], cumAngle[0], cumAngle[1], cumAngle[0], cumAngle[1], "Cumulative Angle Distribution", "Cumulative Angle Distribution - Shopper", "Angle (rad)", "Cumulative Probability", "5");
	*/
		
			coordinatesGenerator test = new coordinatesGenerator();
			int numberofPoints = x;
			ArrayList<Coordinates> path = test.generatePath(cumSpeed, cumAngle, numberofPoints);
	
			CoordinatestoPhoneData one = new CoordinatestoPhoneData();
			PhoneData[] pdArray = one.convertToPhoneData(path, type1[i]);
			DataGetter dg = new DataGetter(pdArray);
			System.out.println("Hi!");
			PhoneData[] fullPhoneData = dg.getFullPhoneData();
			MLearningOptimisation mlo = new MLearningOptimisation(200, 0, 0, 0);
			System.out.println("Creating Track Stats");
			ArrayList<TrackInfo> TrackAnalysis = mlo.writeTrackStats(fullPhoneData);
			//ArrayList<TrackInfo> trackAnTot = new ArrayList<TrackInfo>();
			//trackAnTot.addAll(TrackAnalysis);
			System.out.println("Writing to DB");
			writeToDB(TrackAnalysis);
	}
		
		
		
	/*	
		double x[] = new double[path.size()];
		double y[] = new double[path.size()];
		for(int i = 0; i<path.size(); i++){
			//System.out.println("Time: "+path.get(i).timestamp+"s Coordinate: "+path.get(i).getX()+", "+path.get(i).getY());
			x[i] = path.get(i).getX();
			y[i] = path.get(i).getY();
		}
		
		*/
		
//		System.out.println("hi");
	}
	
	public static void writeToDB(ArrayList<TrackInfo> TrackAnalysis) throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("Generated_Track_Store");
		DBO.deleteTable();
		System.out.println("Creating Track Table");
		DBO.createTracksTable();
		System.out.println("Writing to Database");
		DBO.batchWrite(TrackAnalysis);
		System.out.println("Write complete");
	}
	
	
	
}
