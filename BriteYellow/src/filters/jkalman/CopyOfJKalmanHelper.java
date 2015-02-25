package filters.jkalman;

import java.sql.Timestamp;
import java.util.ArrayList;

import maths.PhoneData;

import jama.Matrix;

public class CopyOfJKalmanHelper {

	// Measurement noise in x and y direction
	private double tkn_x;
	private double tkn_y;
	
	private final JKalman jk;
	private static int dt = 1;
	
	private long current_time;
	private int current_index;
	
	// ArrayList of inputs and results
	private final ArrayList<PhoneData> data;	// input
	private final ArrayList<PhoneData> result;	// result

	public CopyOfJKalmanHelper(ArrayList<PhoneData> data, double tkn_x, double tkn_y) throws Exception {
		// TODO Auto-generated constructor stub
		this.data = data;
		
		jk = new JKalman(4, 2);
		this.tkn_x = tkn_x;
		this.tkn_y = tkn_y;
		
		this.current_time = data.get(0).ts.getTime();
		current_index = 0;
		
		result = new ArrayList<PhoneData>();
		// Add the first data point
		result.add(data.get(0));
		
		jk.setState_post(new Matrix(new double[][]{
				 {data.get(0).x},
				 {data.get(0).y},
				 {100},
				 {100}
				 }));
		
		// Ez = [tkn_x 0; 0 tkn_y];
		jk.setMeasurement_noise_cov(new Matrix(new double[][]{
				{tkn_x, 0},
				{0, tkn_y}
				
		}));
		
	}
	
	private void updatedt(double dt){
		// A
		jk.setTransition_matrix(new Matrix(new double[][]{
				{1, 0, dt, 0},
				{0, 1, 0, dt},
				{0, 0, 1, 0},
				{0, 0, 0, 1}
		}));
		
		// B
		jk.setControl_matrix(new Matrix(new double[][]{
				{dt*dt/2},
				{dt*dt/2},
				{dt},
				{dt},
		}));
		
		// C
		jk.setMeasurement_matrix(new Matrix(new double[][]{
				{1, 0, 0, 0},
				{0, 1, 0, 0}
				
		}));
		
		// Ex = [dt^4/4 0 dt^3/2 0; ...
	    // 0 dt^4/4 0 dt^3/2; ...
	    // dt^3/2 0 dt^2 0; ...
	    // 0 dt^3/2 0 dt^2].*HexAccel_noise_mag^2;
		jk.setError_cov_pre(new Matrix(new double[][]{
				{dt*dt*dt*dt/4, 0, dt*dt*dt/2, 0},
				{0, dt*dt*dt*dt/4, 0, dt*dt*dt/2},
				{dt*dt*dt/2, 0, dt*dt, 0},
				{0, dt*dt*dt/2, 0, dt*dt}
		}) );

		
		
	}

	public PhoneData processData(){
		
		
		current_index ++ ;
		long dt = data.get(current_index).ts.getTime() - data.get(current_index-1).ts.getTime();
		updatedt((int)(dt/1000));

		jk.Predict();
		Matrix m = jk.Correct(new Matrix(new double[][]{
					{data.get(current_index).x},
					{data.get(current_index).y}
				}));
		double[][] d = m.getArray();
		PhoneData pd = new PhoneData();
		pd.x = d[0][0];
		pd.y = d[1][0];
		pd.rsx = d[2][0];
		pd.rsy = d[3][0];
		pd.ts = new Timestamp(current_time);
		
		pd.tb = dt ;
		
		result.add(pd);
		return pd;
	}
/*	public PhoneData processData(){
		Matrix m;
		System.out.println("Current index: "+current_index);
		if(data.get(current_index).ts.getTime() == current_time){
			updatedt(0);
			jk.Predict();
			m = 	
			current_index++;
		} else {
			updatedt(1);
			current_time += 1000;
			m = jk.Predict();
			if(data.get(current_index).ts.getTime() == current_time){
				m = jk.Correct(new Matrix(new double[][]{
					{data.get(current_index).x},
					{data.get(current_index).y}
					}));
				current_index++;
				System.out.println(true);
			} else {
				System.out.println(false);
			}

		}	// end if ... else
		double[][] d = m.getArray();
		PhoneData pd = new PhoneData();
		pd.x = d[0][0];
		pd.y = d[1][0];
		pd.rsx = d[2][0];
		pd.rsy = d[3][0];
		pd.ts = new Timestamp(current_time);
		if(result.size()>0)
			pd.tb = (pd.ts.compareTo(result.get(result.size()-1).ts))/1000 ;
		
		result.add(pd);
		return pd;
		
	}
*/	
	public boolean isEndReached(){
		return current_index == data.size()-1;
	}
	
	/**Get the current index of the input
	 * 
	 * @return the current index of the input
	 */
	public int getCurrentIndex(){
		return current_index - 1;
	}
	
	public ArrayList<PhoneData> getResult(){
		return result;
		
	}
}
