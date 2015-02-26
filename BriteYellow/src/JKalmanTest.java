import java.sql.Timestamp;
import java.util.ArrayList;

import filters.jkalman.JKalman;
import filters.jkalman.JKalmanHelper;
import maths.PhoneData;
import jama.Matrix;


public class JKalmanTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<PhoneData> output2 = new ArrayList<PhoneData>();
		PhoneData ph = new PhoneData();
		ph.x = 156; ph.y = 302; ph.ts = new Timestamp(10000); ph.tb = 0;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 374; ph.y = 347; ph.ts = new Timestamp(11000); ph.tb = 1;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 374; ph.y = 347; ph.ts = new Timestamp(13000); ph.tb = 2;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 156; ph.y = 302; ph.ts = new Timestamp(15000);  ph.tb = 2;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 274; ph.y = 347; ph.ts = new Timestamp(16000); ph.tb = 1;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 274; ph.y = 347; ph.ts = new Timestamp(17000); ph.tb = 1;
		ph = new PhoneData();
		output2.add(ph);
		ph.x = 274; ph.y = 347; ph.ts = new Timestamp(20000); ph.tb = 3;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 156; ph.y = 302; ph.ts = new Timestamp(21000); ph.tb = 1;
		output2.add(ph);
		ph = new PhoneData();
		ph.x = 274; ph.y = 347; ph.ts = new Timestamp(22000); ph.tb = 1;
		output2.add(ph);
		
		JKalmanHelper jkh = new JKalmanHelper(output2, 50, 25);
		while(!jkh.isEndReached())
			jkh.processData();
		
		ArrayList<PhoneData> result = jkh.getFullResult();
		for(int i=0; i<result.size(); i++)
			System.out.println(output2.get(i).x+" "+output2.get(i).y+" -- "+result.get(i).x+" "+result.get(i).y+" "+result.get(i).ts.toString());

		ArrayList<PhoneData> al = new ArrayList<PhoneData>();
		
		System.out.println("TIMESTAMP :"+(new Timestamp(20000).getTime() - (new Timestamp(17000).getTime())));
		
		try{
			
			JKalman jk = new JKalman(4, 2);
			
			jk.setState_post(new Matrix(new double[][]{
				 {output2.get(0).x},
				 {output2.get(0).y},
				 {output2.get(1).rsx},
				 {output2.get(1).rsy}
				 }));
			for(int i = 0; i < 8; i++){
				double dt = 1;
				if(i>0)
//					dt = (output2.get(i).ts.getTime() - output2.get(i-1).ts.getTime())/1000;
					dt = output2.get(i).tb;
				System.out.println(dt);
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
				
				double tkn_x = 50;
				double tkn_y = 25;
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
				

				double[][] array = correction.getArray();
				System.out.println(output2.get(i).x+" "+output2.get(i).y+" -- "+array[0][0]+" "+array[1][0]);
				
				PhoneData pd = new PhoneData();
				pd.x = array[0][0];
				pd.y = array[1][0];
				pd.ts = new Timestamp(i*1000);
				al.add(pd);
			}
//			PlotTracks.plotTrack3(output2.toArray(new PhoneData[output2.size()]), PlotTracks.X, PlotTracks.Y, 1);

//			PlotTracks.plotTrack3(al.toArray(new PhoneData[al.size()]), PlotTracks.X, PlotTracks.Y, 1);
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

}
