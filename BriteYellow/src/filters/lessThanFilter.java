package filters;

import java.util.ArrayList;

import Maths.PhoneData;

public class FilterMain {
	
	public final static int X = 0;
	public final static int Y = 1;
	public final static int Z = 2;
	public final static int WholeDate = 3;
	public final static int PhoneId = 4;
	public final static int TimeBetween = 5;
	public final static int XSpeed = 6;
	public final static int YSpeed = 7;
	public final static int ZSpeed = 8;
	public final static int MSpeed = 9;
	public final static int STheta = 10;
	public final static int XAcc = 11;
	public final static int YAcc = 12;
	public final static int ZAcc = 13;
	public final static int MAcc = 14;
	public final static int ATheta = 15;
	public final static int Track = 16;
	public final static int XDISP = 17;
	public final static int YDISP = 18;
	public final static int ZDISP = 19;
	public final static int MDISP = 20;
	
	
	ArrayList<PhoneData> ph;
	int length;
	public FilterMain(ArrayList<PhoneData> ph){
		this.ph = ph;
		length = ph.size();
	}
	
	public void filter(final int property, double low, double high){
		int i = 0;
		while( i<length){
			try{
				if(property == X)
					compare(i, ph.get(i).x, low, high);
				if(property == Y)
					compare(i, ph.get(i).y, low, high);
				if(property == Z)
					compare(i, ph.get(i).z, low, high);
		
				if(property == TimeBetween)
					compare(i, ph.get(i).tb, low, high);
					
				if(property == XSpeed)
					compare(i, ph.get(i).rsx, low, high);		
				if(property == YSpeed)
					compare(i, ph.get(i).rsy, low, high);	
				if(property == ZSpeed)
					compare(i, ph.get(i).rsz, low, high);
				if(property == MSpeed)
					compare(i, ph.get(i).modspd, low, high);
				if(property == STheta)
					compare(i, ph.get(i).spdtheta, low, high);
				if(property == XAcc)
					compare(i, ph.get(i).rax, low, high);		
				if(property == YAcc)
					compare(i, ph.get(i).ray, low, high);
				if(property == ZAcc)
					compare(i, ph.get(i).raz, low, high);
				if(property == MAcc)
					compare(i, ph.get(i).modacc, low, high);
				if(property == ATheta)
					compare(i, ph.get(i).acctheta, low, high);
				if(property == XDISP)
					compare(i, ph.get(i).xdisp, low, high);
				if(property == YDISP)
					compare(i, ph.get(i).ydisp, low, high);
				if(property == ZDISP)
					compare(i, ph.get(i).zdisp, low, high);
				if(property == MDISP)
					compare(i, ph.get(i).moddisp, low, high);
				throw new IllegalArgumentException(
					"You might have passed the wrong argument or you have used the wrong method to get attributes"
					);
			} catch(IllegalArgumentException e){}
			
			i++;
		}
	}

	private void compare(int index, double value, double low, double high){
		if (!(value<=low && value>= high)){
			ph.remove(index);
			length -- ;
		}
	}
	

	public ArrayList<PhoneData> getFullPhoneData(){
		return ph;
	}
}
