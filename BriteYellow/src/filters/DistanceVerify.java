package filters;

import java.util.ArrayList;

import objects.PhoneData;

public class DistanceVerify {
	private ArrayList<PhoneData> result;

	private double high;
	
	public DistanceVerify(ArrayList<PhoneData> ph, double high){
		this.result = (ArrayList<PhoneData>) ph.clone();
		this.high = high;
	}
	public void check(){
		int index = 1;
		
		while (index < result.size()){
			if(compare(index, high)){
				result.remove(index);
				if(index < result.size())
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
		return (result.get(index).moddisp > high);
	}
	public void reanalyse(int index){
		double xdiff = result.get(index).x - result.get(index-1).x;
		double ydiff = result.get(index).y - result.get(index-1).y;
		result.get(index).moddisp = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
	public ArrayList<PhoneData> getFull(){
		return result;
	}
}