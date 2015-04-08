package filters;

import java.util.ArrayList;

import Objects.PhoneData;

public class DistanceVerify {
	private ArrayList<PhoneData> ph;

	private double high;
	
	public DistanceVerify(ArrayList<PhoneData> ph, double high){
		this.ph = ph;
		this.high = high;
	}
	public void check(){
		int index = 1;
		
		while (index < ph.size()){
			if(compare(index, high)){
				ph.remove(index);
				if(index < ph.size())
					reanalyse(index);

			} else {
				index++;
			}
		}
	}
	/**Return if the moddisp is greater than the higher limit
	 * 
	 * @param index
	 * @param high
	 * @return
	 */
	public boolean compare(int index, double high){
		return (ph.get(index).modspd > high);
	}
	public void reanalyse(int index){
		double xdiff = ph.get(index).rsx - ph.get(index-1).rsx;
		double ydiff = ph.get(index).rsy - ph.get(index-1).rsy;
		ph.get(index).modspd = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
	public ArrayList<PhoneData> getFull(){
		return ph;
	}
}
