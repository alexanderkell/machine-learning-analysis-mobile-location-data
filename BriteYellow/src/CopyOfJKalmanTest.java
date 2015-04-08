import java.sql.Timestamp;
import java.util.ArrayList;

import objects.PhoneData;
import filters.jkalman.JKalman;
import graphing.PlotTracks;
import jama.Matrix;


public class CopyOfJKalmanTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<PhoneData> output2 = new ArrayList<PhoneData>();
		PhoneData ph = new PhoneData();
		ph.x = 200; ph.y = 100; ph.ts = new Timestamp(0);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 100; ph.y = 200; ph.ts = new Timestamp(1000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 100; ph.y = 200; ph.ts = new Timestamp(2000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 200; ph.y = 200; ph.ts = new Timestamp(3000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 200; ph.y = 200; ph.ts = new Timestamp(4000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 200; ph.y = 200; ph.ts = new Timestamp(5000);
		ph = new PhoneData();
		output2.add(ph);
		ph.x = 300; ph.y = 300; ph.ts = new Timestamp(6000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 400; ph.y = 300; ph.ts = new Timestamp(7000);
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 200; ph.y = 400; ph.ts = new Timestamp(8000);
		output2.add(ph);
		
		ArrayList<PhoneData> al = new ArrayList<PhoneData>();
		
		
		try{
			
			JKalman jk = new JKalman(4, 2);
			
			jk.setState_post(new Matrix(new double[][]{
				 {output2.get(0).x},
				 {output2.get(0).y},
				 {30},
				 {30}
				 }));
			for(int i = 0; i < 8; i++){
				double dt = 1;
				
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
				
				double tkn_x = 100;
				double tkn_y = 100;
				// Ez = [tkn_x 0; 0 tkn_y];
				jk.setMeasurement_noise_cov(new Matrix(new double[][]{
						{tkn_x, 0},
						{0, tkn_y}
						
				}));
				
				Matrix prediction = jk.Predict();
				

				Matrix correction = jk.Correct(new Matrix(new double[][]{
					{output2.get(i).x},
					{output2.get(i).y}
				}));
				
				Matrix resultant = jk.getState_post();
				double[][] array = resultant.getArray();
				System.out.println(output2.get(i).x+" "+output2.get(i).y+" -- "+array[0][0]+" "+array[1][0]);
				
				PhoneData pd = new PhoneData();
				pd.x = array[0][0];
				pd.y = array[1][0];
				pd.ts = new Timestamp(i*1000);
				al.add(pd);
			}
			PlotTracks.plotTrack3(output2.toArray(new PhoneData[output2.size()]), PlotTracks.X, PlotTracks.Y, 1);

			PlotTracks.plotTrack3(al.toArray(new PhoneData[al.size()]), PlotTracks.X, PlotTracks.Y, 1);
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

}
