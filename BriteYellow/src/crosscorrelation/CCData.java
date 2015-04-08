package crosscorrelation;

import java.util.ArrayList;

import Objects.PhoneData;
	
	
	
	public class CCData {
		
		private ArrayList<PhoneData> PD;
		private ArrayList<PhoneData> PD2;
		private int length;
		private char mp;
		private char xyz;
		private double[] data;
		private double[] data2;
		private double[] result;
		private DSP dsp = new DSP();
		
	
	public void setData1(ArrayList<PhoneData> PD){
		
			this.PD = PD;
			length = PD.size();
			data = new double[length];
			data2 = new double[length];
			
	}
	
	public void setData2(ArrayList<PhoneData> PD2){
		
		this.PD2 = PD2;
		length = PD2.size();
	}
	
	public void setMeasuredParameter(char mp, char xyz){
			
		this.mp = mp;
		this.xyz = xyz;
			
	}
	
	private void setupFirstParameterArray(){
		
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
	
	private void setupSecondParameterArray(){
		
		if(mp == 'p'){
			if(xyz == 'x'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).x;
				}
			}
			else if(xyz == 'y'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).y;
				}
			}
			else if(xyz == 'z'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).z;
				}
			}
			else{
				System.out.println("Reset the Parameter Options");
			}
		}
		
		else if(mp == 's'){
			if(xyz == 'x'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).rsx;
				}
			}
			else if(xyz == 'y'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).rsy;
				}
			}
			else if(xyz == 'z'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).rsz;
				}
			}
			else if(xyz == 'm'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).modspd;
				}
			}
			else if(xyz == 't'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).spdtheta;
				}
			}
			else{
				System.out.println("Reset the Parameter Options");
			}
		}
		
		else if(mp == 'a'){
			if(xyz == 'x'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).rax;
				}
			}
			else if(xyz == 'y'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).ray;
				}
			}
			else if(xyz == 'z'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).raz;
				}
			}
			else if(xyz == 'm'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).modacc;
				}
			}
			else if(xyz == 't'){
				for(int i = 0; i<length; i++){
					data2[i] = PD2.get(i).acctheta;
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
	
	private void setupCorrelation(){
		setupFirstParameterArray();
		setupSecondParameterArray();
		result = dsp.xcorr(data, data2);
	}
	
	public double[] getComputedVariables(){
		setupCorrelation();
		
		return result;
	}
	
	
}
