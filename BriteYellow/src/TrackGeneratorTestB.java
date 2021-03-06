import graphing.XYPlot;

import java.util.ArrayList;
import java.util.Scanner;

import dynamodb.DataBaseOperations;
import maths.DataGetter;
import objects.PhoneData;
import objects.TrackInfo;
import Bootstrapping.*;


public class TrackGeneratorTestB {
	public static void main(String args[]) throws Exception{
		
		System.out.println("How many tracks would you like to generate?");
		Scanner sc = new Scanner(System.in);
		int x1 = sc.nextInt();
		for(int i = 0; i<3; i++){
			MLearningOptimisation MLOp = new MLearningOptimisation(200, 10, 10, 2);
			String type1[] = new String[3];
			type1[0] = "ZX1B23QBS53771758c578bbd85"; //Security
			type1[1] = "ZX1B23QFSP48abead89f52e3bb"; //Business
			type1[2] = "TA92903URNf067ff16fcf8e045"; //Shopper
			ArrayList<PhoneData> pd = MLOp.selectPhoneandFilter(type1[i]);//Security
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
			
			coordinatesGenerator test = new coordinatesGenerator();
			int numberofTracks = x1;
			
			ArrayList<Coordinates> path = new ArrayList<Coordinates>();
			for(int j=0; j<numberofTracks; j++){
				ArrayList<Coordinates> track = test.generatePath(cumSpeed, cumAngle, 1000, j, path);
				//if(track.get(track.size()-1).getY() < 364 && track.get(track.size()-1).getY() > 302){
					path.addAll(track);
				//}else
					//j--;	
				//}
			}

		
			CoordinatestoPhoneData one = new CoordinatestoPhoneData();
			PhoneData[] pdArray = one.convertToPhoneData(path, type1[i]);
			//DataGetter dg = new DataGetter(pdArray);
			//PhoneData[] fullPhoneData = dg.getFullPhoneData();
			MLearningOptimisation mlo = new MLearningOptimisation(200, 0, 0, 0);
			System.out.println("Creating Track Stats");
			
			//ArrayList<TrackInfo> TrackAnalysis = mlo.writeTrackStats(fullPhoneData);
			ArrayList<TrackInfo> TrackAnalysis = mlo.writeTrackStats(pdArray);
			ArrayList<TrackInfo> trackAnTot = new ArrayList<TrackInfo>();
			trackAnTot.addAll(TrackAnalysis);
			System.out.println("Writing to DB");
			writeToDB(TrackAnalysis);
	
			
		
			double x2[] = new double[path.size()];
			double y[] = new double[path.size()];
			for(int i1 = 0; i1<path.size(); i1++){
				//System.out.println("Time: "+path.get(i).timestamp+"s Coordinate: "+path.get(i).getX()+", "+path.get(i).getY());
				x2[i1] = path.get(i1).getX();
				y[i1] = path.get(i1).getY();
			}
		
		//XYPlot xyp = new XYPlot();
		//xyp.plot(cumSpeed[0], cumSpeed[1], cumSpeed[0], cumSpeed[1], cumSpeed[0], cumSpeed[1], "Cumulative Speed Distribution", "Cumulative Speed Distribution - Shopper", "Speed (coordinates/sec)", "Cumulative Probability", "5");	
		//xyp.plot(x2, y, x2, y, x2, y, "Cumulative Angle Distribution", "Cumulative Angle Distribution - Shopper", "Angle (rad)", "Cumulative Probability", "5");
		}
	}
//		System.out.println("hi");
	
	
	public static void writeToDB(ArrayList<TrackInfo> TrackAnalysis) throws Exception{
		DataBaseOperations DBO = new DataBaseOperations("Generated_Track_Store_Whole_Corridor");
		//DBO.deleteTable();
		System.out.println("Creating Track Table");
		DBO.createTracksTable();
		System.out.println("Writing to Database");
		DBO.batchWrite(TrackAnalysis);
		System.out.println("Write complete");
	}	
}
