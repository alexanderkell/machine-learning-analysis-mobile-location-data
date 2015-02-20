package Interpolation;

import Graphing.PlotHelper;

public class CHSpline {
	
	/**
	 * 
	 * @param t A value between 0 and 1
	 * @param point0	x y coordinate of the starting point
	 * @param modspd0	modulus speed of the starting point
	 * @param heading0	speed angle of the starting point
	 * @param point1	x y coordinate of the next point
	 * @param modspd1	modulus speed of the next point
	 * @param heading1  speed angle of the next point
	 * @return the x y coordinate correspond to t
	 */
	public static double[] cHs(float t, double[] point0, double modspd0, double heading0, double[] point1, double modspd1, double heading1){

		return cHs(
				t,
				point0,
				new double[]{modspd0*Math.cos(heading0), modspd0*Math.sin(heading0)},
				point1,
				new double[]{modspd1*Math.cos(heading1), modspd1*Math.sin(heading1)}
				);
		
	}
	
	public static double[] cHs(float t, double[] point0, double[] speed0, double[] point1, double[] speed1){

		// The starting point's speed in x and y direction
		double H0i = speed0[0];
		double H0j = speed0[1];

		// The next point's speed in x and y direction
		double H1i = speed1[0];
		double H1j = speed1[1];

		// Calculate the point 
		double f1 = (2*t*t*t - 3*t*t + 1) * point0[0] + (t*t*t - 2*t*t + t)*H0i + (-2*t*t*t + 3*t*t)*point1[0] + (t*t*t-t*t)*H1i;
		double f2 = (2*t*t*t - 3*t*t + 1) * point0[1] + (t*t*t - 2*t*t + t)*H0j + (-2*t*t*t + 3*t*t)*point1[1] + (t*t*t-t*t)*H1j;

		return new double[]{
				f1,f2	
		};
	}
	
	/**Test bench for the CHS
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		
		final double[] point0 = new double[]{ 0, 2 };
		final double[] point1 = new double[]{ 1, 1 };
		
		final double modspeed0 = 1;
		final double heading0 = 0;

		final double modspeed1 = 2;
		final double heading1 = 0;
		
		final float step = 0.04f;
		//======================================================
		
		final int steps = (int) (1/step);
		
		final double[][] f = new double[steps+1][2];
		
		for(int i = 0; i<=steps; i++){
			f[i] = cHs(i*step, point0, modspeed0, heading0, point1, modspeed1, heading1);
		}
		
		final String[] labels = new String[]{"SamplePoints", "SampleResults"};

		PlotHelper plot = new PlotHelper("Demo", "X", "Y", labels);
		plot.addData(labels[0], point0[0], point0[1]);
		plot.addData(labels[0], point1[0], point1[1]);
		plot.addData(labels[1], f);
		plot.showDialog();
		
	}
}
