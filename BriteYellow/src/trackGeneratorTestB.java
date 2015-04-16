import graphing.XYPlot;

import java.util.ArrayList;

import maths.DataGetter;
import objects.PhoneData;
import objects.TrackInfo;
import Bootstrapping.*;


public class trackGeneratorTestB {
	public static void main(String args[]) throws Exception{
		MLearningOptimisation MLOp = new MLearningOptimisation(200, 10, 10, 2);
//		String type = "ZX1B23QBS53771758c578bbd85"; //Security
//		String type = "ZX1B23QFSP48abead89f52e3bb"; //Business
		String type = "TA92903URNf067ff16fcf8e045"; //Shopper
		ArrayList<PhoneData> pd = MLOp.selectPhoneandFilter(type);//Security
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
		
		
		
		XYPlot xyp = new XYPlot();
		//xyp.plot(cumSpeed[0], cumSpeed[1], cumSpeed[0], cumSpeed[1], cumSpeed[0], cumSpeed[1], "Cumulative Speed Distribution", "Cumulative Speed Distribution - Shopper", "Speed (coordinates/sec)", "Cumulative Probability", "5");	
		xyp.plot(cumAngle[0], cumAngle[1], cumAngle[0], cumAngle[1], cumAngle[0], cumAngle[1], "Cumulative Angle Distribution", "Cumulative Angle Distribution - Shopper", "Angle (rad)", "Cumulative Probability", "5");
	
	
		coordinatesGenerator test = new coordinatesGenerator();
		int numberofPoints = 10000;
		ArrayList<Coordinates> path = test.generatePath(cumSpeed, cumAngle, numberofPoints);

		CoordinatestoPhoneData one = new CoordinatestoPhoneData();
		//PhoneData[] pdArray = one.convertToPhoneData(path, type);
		//DataGetter dg = new DataGetter(pdArray);
		//System.out.println(dg.getDistanceBetween(10));
		
		
		double x[] = new double[path.size()];
		double y[] = new double[path.size()];
		for(int i = 0; i<path.size(); i++){
			//System.out.println("Time: "+path.get(i).timestamp+"s Coordinate: "+path.get(i).getX()+", "+path.get(i).getY());
			x[i] = path.get(i).getX();
			y[i] = path.get(i).getY();
		}
		
		//XYPlot xyp = new XYPlot();
		//xyp.plot(x, y, x, y, x, y, "trackGenerator", "Generated Tracks", "x-axis", "y-axis", "5");	
	
		
//		System.out.println("hi");
	}
	
}
