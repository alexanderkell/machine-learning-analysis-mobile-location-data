package autoregression;
import java.util.ArrayList;

import objects.PhoneData;


public class ARDataFeed {
	
	private ArrayList<PhoneData> PD;
	int select;
	private char mp;
	private char xyz;
	private int length;
	private boolean myn = false;
	private int order;
	private double[] data;
	
	/**
	 * 
	 * @param PD is the passed DataBase
	 * 
	 * this sets the database from the parent class
	 * 
	 */
	
	
	public void setData(ArrayList<PhoneData> PD){
		this.PD = PD;
		length = PD.size();
		data = new double[length];
	}
	
	
	public void setMeasuredParameter(char mp, char xyz){
		
		this.mp = mp;
		this.xyz = xyz;
		
	}
	
	private void setupParameterArray(){
		
		if(mp == 'p'){
			if(xyz == 'x'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).x;
				}
			}
			else if(xyz == 'y'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).y;
				}
			}
			else if(xyz == 'z'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).z;
				}
			}
			else{
				System.out.println("Reset the Parameter Options");
			}
		}
		
		else if(mp == 's'){
			if(xyz == 'x'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).rsx;
				}
			}
			else if(xyz == 'y'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).rsy;
				}
			}
			else if(xyz == 'z'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).rsz;
				}
			}
			else if(xyz == 'm'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).modspd;
				}
			}
			else if(xyz == 't'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).spdtheta;
				}
			}
			else{
				System.out.println("Reset the Parameter Options");
			}
		}
		
		else if(mp == 'a'){
			if(xyz == 'x'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).rax;
				}
			}
			else if(xyz == 'y'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).ray;
				}
			}
			else if(xyz == 'z'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).raz;
				}
			}
			else if(xyz == 'm'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).modacc;
				}
			}
			else if(xyz == 't'){
				for(int i = 0; i<length; i++){
					data[i] = PD.get(i).acctheta;
				}
			}
			else{
				System.out.println("Reset the Parameter Options");
			}
		}
		
		else{
			System.out.println("Reset the Parameter Options");
		}
		
	}
	
	public void setRemoveMean(char rm){
		if(rm == 'y'){
			myn = true;
		}
		else if(rm == 'n'){
			myn = false;
		}
		else{
			
			System.out.print("Please re-enter y or n");
		}
	}
	
	public void setOrder(int order){
		this.order = order;
		
	}
	
	public double[] runAR(){
		setupParameterArray();
		AutoregressionCalc AR = new AutoregressionCalc();
		double[] arcos = null;
		try {
			arcos = AR.calculateARCoefficients(data, order, myn);
		} catch (Exception e) {
			
			System.out.println("error!");
		}
		return arcos;
	}

	
}
