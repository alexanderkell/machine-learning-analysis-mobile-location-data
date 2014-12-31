package Filters;

public class Butterworth {
	
	private static double pre_warp(double Frequency_Cut, double Frequency_Sample){
		double w_d1 = 2*Math.PI*Frequency_Cut;
		double T = 1/Frequency_Sample;
		
		double w_a1 = Math.tan((w_d1*T)/2);
		return w_a1;
	}
		
	public static double[] butterworth(double[] input, double cutFreqMin, double cutFreqMax, double sampFreq){
		double wa1 = pre_warp(cutFreqMin,sampFreq);
		double wa2 = pre_warp(cutFreqMax, sampFreq);
		
		
		double wa1_2 = wa1*wa1;
		
		double a = wa1_2/(1+Math.sqrt(2)*wa1+wa1_2);
		double b = 2*a;
		double c = a;
		double d = (-2+2*wa1_2)/(1+Math.sqrt(2)*wa1+wa1_2);
		double e = (1-Math.sqrt(2)*wa1+wa1_2)/(1+Math.sqrt(2)*wa1+wa1_2);
		
		double out[] = new double[input.length];
		int n = 3;
		
		System.out.println(e);
		
		for(int i = 2; i<input.length; i++){
			out[i] = a*input[i] + b*input[i-1] + c*input[i-2] + d*out[i-1] + e*out[i-2];
		}
		
		double output[]=out;
		
		//in[1] = in[0];
		//in[2] = in[1];
		
		return output;
	}	
}
