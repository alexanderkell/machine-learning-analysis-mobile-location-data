package Filters;
import Matrices.*;

public class kalman {
	
	Matrix mtrx = new Matrix();
	
	//Transformation matrices
	private double F[][] = new double[2][2]; //State transition matrix - How time t-1 affects t
	private double B[] = new double[2]; //Control input matrix - Applies controls to displacement
	private double H[][] = new double[2][2]; //Unit transformatino matrix
	private double Q[][] = new double[2][2]; //Process noise covariance
	
	//System Estimate matrices
	private double x[][] = new double[2][2]; //Estimated displacement (Past displacement/speed + estimated (max acceleration/deceleration))
	private double P[][] = new double[2][2]; //Variance of x_t-1 and x_t
	
	public void prediction(){
		//x = F.x + B.u - Change in x during \delta t
		x = mtrx.multiply(F, x);
		
		//P = FPF'+Q - Variance of predicted x_t-1 and real x_t
		double F_t[][] = mtrx.transpose(F);
		double FxP[][] = mtrx.multiply(F, P);
		double FxPxFt[][] = mtrx.multiply(FxP, F_t);
		P = mtrx.add(FxPxFt, Q);
	}
	
	public void updated(){
		//y = z - H.x
		double z[][] = new double[2][2];//Measurement vector
		double[][] Hxx = mtrx.multiply(H, x);
		double[][] Y = mtrx.subtract(z, Hxx);
				//
		
		//S = H P H' + R
		double S[][] = new double[2][2];
		double HxP[][] = mtrx.multiply(H, P);
		double H_t[][] = mtrx.transpose(H);
		double HxPxH_t[][] = mtrx.multiply(HxP, H_t);
		double R[][] = new double[2][2]; //Uncertainty matrix - Noisy set of measurements
		S = mtrx.add(HxPxH_t, R);
		
		//Kalman Gain = PH'S^(-1)
		double PH[][] = mtrx.multiply(P, H_t);
		double S_inv[][] = mtrx.invert(S);
		double K[][] = mtrx.multiply(PH, S_inv);
		
		//Updated x
		double KY[][] = mtrx.multiply(K, Y);
		x = mtrx.add(KY, x);
		
		//Updated P
		double KH[][] = mtrx.multiply(K, H);
		double KHP[][] = mtrx.multiply(KH, P);
		P = mtrx.subtract(KHP, P);
	}
	
}
