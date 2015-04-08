package filters.jkalman;

import jama.Matrix;

import java.util.ArrayList;

import Objects.PhoneData;

public class JKalmanHelper extends JKalman{
	
	private final ArrayList<PhoneData> data;
	private final ArrayList<PhoneData> result;
//	private final JKalman jk;
	
	private int index = 0;

	public JKalmanHelper(ArrayList<PhoneData> data) throws Exception{
		this(data, 0, 0);
	}
	public JKalmanHelper(ArrayList<PhoneData> data, double tkn_x, double tkn_y) throws Exception{
		super(4,2);
		
		this.data = data;
		result = new ArrayList<PhoneData>();
		
		
//		jk = new JKalman(4, 2);
		setState_post(new Matrix(new double[][]{
				 {data.get(0).x},
				 {data.get(0).y},
				 {0},
				 {0}
				 }));
		setMeasurement_noise_cov(tkn_x, tkn_y);
	}
	
	public void setMeasurement_noise_cov(double tkn_x, double tkn_y){
		// Ez = [tkn_x 0; 0 tkn_y];
		setMeasurement_noise_cov(new Matrix(new double[][]{
				{tkn_x, 0},
				{0, tkn_y}
				
		}));
	}

	public PhoneData processData(){
		
		double dt = 1;
		if(index>0)
			dt = (data.get(index).ts.getTime() - data.get(index-1).ts.getTime())/1000;

		// A
		setTransition_matrix(new Matrix(new double[][]{
				{1, 0, dt, 0},
				{0, 1, 0, dt},
				{0, 0, 1, 0},
				{0, 0, 0, 1}
		}));
		
		// B
		setControl_matrix(new Matrix(new double[][]{
				{dt*dt/2},
				{dt*dt/2},
				{dt},
				{dt},
		}));
		
		// C
		setMeasurement_matrix(new Matrix(new double[][]{
				{1, 0, 0, 0},
				{0, 1, 0, 0}
				
		}));
		
		// Ex = [dt^4/4 0 dt^3/2 0; ...
	    // 0 dt^4/4 0 dt^3/2; ...
	    // dt^3/2 0 dt^2 0; ...
	    // 0 dt^3/2 0 dt^2].*HexAccel_noise_mag^2;
		setError_cov_pre(new Matrix(new double[][]{
				{dt*dt*dt*dt/4, 0, dt*dt*dt/2, 0},
				{0, dt*dt*dt*dt/4, 0, dt*dt*dt/2},
				{dt*dt*dt/2, 0, dt*dt, 0},
				{0, dt*dt*dt/2, 0, dt*dt}
		}) );
		
		
		Predict();
		Matrix resultant = Correct(new Matrix(new double[][]{
			{data.get(index).x},
			{data.get(index).y}
		}));
		
		double[][] array = resultant.getArray();
		
		PhoneData pd = new PhoneData();
		pd.x = array[0][0];
		pd.y = array[1][0];
		pd.rsx = array[2][0];
		pd.rsy = array[3][0];
		pd.ts = data.get(index).ts;
		pd.wholedate = data.get(index).wholedate;
		pd.phone_id = data.get(index).phone_id;
		pd.track_no = data.get(index).track_no;
		result.add(pd);
		index++;
		
		return pd;
		
	}
	
	public boolean isEndReached(){
		return index == data.size();
	}
	public int getCurrentIndex(){
		return index;
	}
	public ArrayList<PhoneData> getFullResult(){
		return result;
	}
}