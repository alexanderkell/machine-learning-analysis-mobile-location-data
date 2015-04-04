package redundant.filters;

import java.util.ArrayList;

import maths.PhoneData;
import matrices.Matrix;

public class KalmanFilter2BackUp {
	
	int dt = 1;  // our sampling rate

	public final static double u = 10; // define acceleration magnitude
	public final static double HexAccel_noise_mag = 10f; // process noise: the variability in how fast the Hexbug is speeding up (stdv of acceleration: meters/sec^2)
	public final static double tkn_x = 100f;  //measurement noise in the horizontal direction (x axis)
	public final static double tkn_y = 100f;  //measurement noise in the horizontal direction (y axis)
	double[][] Ez;
	double[][] Ex;
	double[][] Q;
	//= [CM_idx(S_frame,1); CM_idx(S_frame,2); 0; 0]; %initized state--it has four components: [positionX; positionY; velocityX; velocityY] of the hexbug
	double[][] Q_estimate;  //estimate of initial location estimation of where the hexbug is (what we are updating)

	private double[][] P;	// Convariance estimation

	private double[][] A;

	private double[][] B;

	private double[][] C;

	private ArrayList<double[][]> Q_loc_estimate;

	private ArrayList<double[][]> vel_estimate;

	private double[][] P_estimate;

	private ArrayList<Double> predic_state;

	private ArrayList<double[][]> predic_var;

	private ArrayList<PhoneData> phonedata;	//Phonedata object containing the attributes for each point

	private int index = 0;	// Current Index (pointer to phonedata)

	private double[][][] Q_loc_meas;
	
//	private ArrayList<Long> current_time;
	
	public KalmanFilter2BackUp(double u, double HexAccel_noise_mag, double tkn_x, double tkn_y, ArrayList<PhoneData> phonedata){
		this.phonedata = phonedata;
		
//		current_time = new ArrayList<Long>();
//		for(int i = 0; i<phonedata.size(); i++){
//			current_time.add(phonedata.get(i).ts.getTime());
//		}

//		this.u = u;
		
		// Initialised position x and y and velocity x and y (at point 0)
		Q = new double[][]{
			{phonedata.get(0).x}, 
			{phonedata.get(0).y},
			{phonedata.get(0).rsx}, 
			{phonedata.get(0).rsy}
		};
		
		Q_estimate = Q;
		
//		this.tkn_x = tkn_x;
//		this.tkn_y = tkn_y;

		
		// ----------------------
		updatedt();
		
		
		
		//C = [1 0 0 0; 0 1 0 0];  %this is our measurement function C, that we apply to the state estimate Q to get our expect next/new measurement
		C = new double[][]{
				{1, 0, 0, 0},
				{0, 1, 0, 0}
		};
		
		// initize estimation variables
		Q_loc_estimate = new ArrayList<double[][]>();
		vel_estimate = new ArrayList<double[][]>();
		
//		P_estimate = P;
		
		predic_state = new ArrayList<Double>();
		predic_var = new ArrayList<double[][]>();
		
		// load the given tracking
	    // Q_loc_meas(:,t) = [ CM_idx(t,1); CM_idx(t,2)];
		// In java: size of Q_loc_meas = total number of points*1row*2colforXandY
		Q_loc_meas = new double[phonedata.size()][1][2];
		
		for(int i = 0; i<phonedata.size(); i++){
			Q_loc_meas[i][1][0] = phonedata.get(i).x;
			Q_loc_meas[i][0][1] = phonedata.get(i).y;
		}
	}
	
	private void updatedt(){
		dt = (int)((phonedata.get(index+1).ts.getTime()/1000 - phonedata.get(index).ts.getTime()/1000));
		
		Ez = new double[][]{
				{tkn_x, 0},	//tkn_x -- measurement noise in the horizontal direction (x axis)
				{0, tkn_y}	//tkn_y -- measurement noise in the horizontal direction (y axis)
			};
			
			/*
			 *  Ex = [dt^4/4 0 dt^3/2 0; ...
			 *        0 dt^4/4 0 dt^3/2; ...
			 *        dt^3/2 0 dt^2 0; ...
			 *        0 dt^3/2 0 dt^2].*HexAccel_noise_mag^2; % Ex convert the process noise (stdv) into covariance matrix
			 */
			Ex = new double[][]{
				{dt*dt*dt*dt/4,  0,  dt*dt*dt/2,  0},
				{0, dt*dt*dt*dt/4,  0,  dt*dt*dt/2},
				{dt*dt*dt/2,  0,  dt*dt/2, 0},
				{0,  dt*dt*dt/2,  0,  dt*dt/2}
			};
			
			P = Ex; // estimate of initial Hexbug position variance (covariance matrix)

			// Define update equations in 2-D! (Coefficent matrices): A physics based model for where we expect the HEXBUG to be [state transition (state + velocity)] + [input control (acceleration)]
			A = new double[][]{
					{1, 0, dt, 0},
					{0, 1, 0, dt},
					{0, 0, 1, 0},
					{0, 0, 0, 1}
			};
			
			// B = [(dt^2/2); (dt^2/2); dt; dt];
			B = new double[][]{
					{dt*dt/2},
					{dt*dt/2},
					{dt},
					{dt}
			};
		
			P_estimate = P;
	}
	
	/** Kalman filter
	 * 
	 * @return if the update is sucessful or not
	 */
	public boolean update(){
	    // do the kalman filter      
		if(index  >= phonedata.size())
			return false;
		
		
		updatedt();
	    // Predict next state of the Hexbug with the last state and predicted motion.
	    Q_estimate = Matrix.add(Matrix.multiply(A, Q_estimate), Matrix.multiply(B,u));
	    predic_state.add(Q_estimate[0][0]);
	    
	    //predict next covariance
	    //P = A * P * A' + Ex;
	    P = Matrix.add(Matrix.multiply(Matrix.multiply(A, P),  Matrix.transpose(A)), Ex);
	    //predic_var = [predic_var; P] ;
	    predic_var.add(P);
	    
	    // predicted Ninja measurement covariance
	    // Kalman Gain
	    // K = P*C'*inv(C*P*C'+Ez);
	    
	    double[][] K = Matrix.multiply(Matrix.multiply(P, Matrix.transpose(C)), Matrix.invert(
	    		Matrix.add(
	    				Matrix.multiply(Matrix.multiply(C, P), Matrix.transpose(C)),
	    				Ez
	    				)	
	    		)
	    	); 
	    
	    // Update the state estimate.
	    // if ~isnan(Q_loc_meas(:,t))
        // Q_estimate = Q_estimate + K * (Q_loc_meas(:,t) - C * Q_estimate);
	    // end
//	    if(! (Double.isNaN(phonedata.get(i).x) && Double.isNaN(phonedata.get(i).y)))
	    
	    
	    if(! (Double.isNaN(Q_loc_meas[index][0][0])) && (Double.isNaN(Q_loc_meas[index][0][1]))){
	    	Q_estimate = Matrix.add(Q_estimate, Matrix.multiply(K, 
	    			Matrix.subtract(Q_loc_meas[index], Matrix.multiply(C, Q_estimate))
	    			)
	    		);
	    }
//	    System.out.println(Q_estimate.length);
	    
	    // update covariance estimation.
	    // P =  (eye(4)-K*C)*P;
	    P = Matrix.multiply(Matrix.subtract(Matrix.identity(4), Matrix.multiply(K, C)), P);
	    
	    // Store data
	    //Q_loc_estimate = [Q_loc_estimate; Q_estimate(1:2)];
	    //vel_estimate = [vel_estimate; Q_estimate(3:4)];
	    
	    Q_loc_estimate.add(new double[][]{{Q_estimate[0][0], Q_estimate[1][0]}});
	    vel_estimate.add(new double[][]{{Q_estimate[2][0], Q_estimate[3][0]}});
	    
	    // Increment i
	    index++;
       	return true;
	    
	}
	public int getCurrentIndex(){
		return index-1;
	}
	/**Return the whole Q_loc_estimate array
	 * 
	 * @return the whole Q_loc_estimate array
	 */
	public ArrayList<double[][]> getQ_loc_estimate(){
		return Q_loc_estimate;
	}
	/**Return the whole vel_estimate array
	 * 
	 * @return the whole vel_estimate array
	 */
	public ArrayList<double[][]> getvel_estimate(){
		return vel_estimate;
	}
	public double getEstimatedPosXAt(int i){
		return Q_loc_estimate.get(i)[0][0];
	}
	public double getEstimatedPosYAt(int i){
		return Q_loc_estimate.get(i)[0][1];
	}
	public double getEstimatedVelXAt(int i){
		return vel_estimate.get(i)[0][0];
	}
	public double getEstimatedVelYAt(int i){
		return vel_estimate.get(i)[0][1];
	}
	
}
