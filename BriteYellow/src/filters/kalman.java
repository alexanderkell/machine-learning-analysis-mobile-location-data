package filters;
import matrices.*;

public class kalman {
	
	//Transformation matrices
	private double F[][] = new double[2][2]; //State transition matrix - How time t-1 affects t

	
	private double B[] = new double[2]; //Control input matrix - Applies controls to displacement
	private double H[][] = new double[2][2]; //Unit transformation matrix
	private double Q[][] = new double[2][2]; //Process noise covariance
	
	//System Estimate matrices
	private double x[][] = new double[2][2]; //Estimated displacement (Past displacement/speed + estimated (max acceleration/deceleration))
	private double P[][] = new double[2][2]; //Variance of x_t-1 and x_t
	private double[][] P1;
	private double[][] P2;
	
	public kalman(){
		
	}
	public void prediction(){
		//x = F.x + B.u - Change in x during \delta t
		x = Matrix.multiply(F, x);
		
		//P = FPF'+Q - Variance of predicted x_t-1 and real x_t
		double F_t[][] = Matrix.transpose(F);
		double FxP[][] = Matrix.multiply(F, P);
		double FxPxFt[][] = Matrix.multiply(FxP, F_t);
		P1 = Matrix.add(FxPxFt, Q);
	}
	
	public void updated(){
		//y = z - H.x
		double z[][] = new double[2][2];//Measurement vector
		double[][] Hxx = Matrix.multiply(H, x);
		double[][] Y = Matrix.subtract(z, Hxx);
				//
		
		//S = H P H' + R
		double S[][] = new double[2][2];
		double HxP[][] = Matrix.multiply(H, P);
		double H_t[][] = Matrix.transpose(H);
		double HxPxH_t[][] = Matrix.multiply(HxP, H_t);
		double R[][] = new double[2][2]; //Uncertainty matrix - Noisy set of measurements
		S = Matrix.add(HxPxH_t, R);
		
		//Kalman Gain = PH'S^(-1)
		double PH[][] = Matrix.multiply(P, H_t);
		double S_inv[][] = Matrix.invert(S);
		double K[][] = Matrix.multiply(PH, S_inv);
		
		//Updated x
		double KY[][] = Matrix.multiply(K, Y);
		x = Matrix.add(KY, x);
		
		//Updated P
		double KH[][] = Matrix.multiply(K, H);
		double KHP[][] = Matrix.multiply(KH, P);
		P2 = Matrix.subtract(KHP, P);
	}
	
	// Getter methods
	// ?? resultant P in prediction is different from the actual updated
	public double[][] getP(){
		P = Matrix.multiply(P1, P2);
		return P;
	}
	// Setter methods
	public void setF(double[][] F){
		this.F = F;
	}
	public void setB(double[] B){
		this.B = B;
	}
}
