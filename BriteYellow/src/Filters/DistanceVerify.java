package Filters;

import java.util.ArrayList;

import Maths.PhoneData;

public class DistanceVerify {
	private ArrayList<PhoneData> ph;
	private int length;
	private double high;
	
	public DistanceVerify(ArrayList<PhoneData> ph, double high){
		this.ph = ph;
		length = ph.size();
		this.high = high;
	}
	public void check(){
		int index = 1;
		
		while (index < length){
			if(compare(index, high)){
				ph.remove(index);
				
				reanalyse(index);
				index--;
			}
			index++;
		}
	}
	/**Return if the moddisp is greater than the higher limit
	 * 
	 * @param index
	 * @param high
	 * @return
	 */
	public boolean compare(int index, double high){
		return (ph.get(index).moddisp > high);
	}
	public void reanalyse(int index){
		double xdiff = ph.get(index).x - ph.get(index-1).x;
		double ydiff = ph.get(index).y - ph.get(index-1).y;
		ph.get(index).moddisp = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
}
