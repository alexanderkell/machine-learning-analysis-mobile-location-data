package filters;

import java.util.ArrayList;

import objects.PhoneData;

public class SpeedVerify {
	private ArrayList<PhoneData> result;
	private double high;
	
	public SpeedVerify(ArrayList<PhoneData> ph, double high){
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
	private boolean compare(int index, double high){
		return (result.get(index).modspd > high);
	}
	private void reanalyse(int index){
		PhoneData current_point = result.get(index);
		PhoneData previous_point = result.get(index-1);
		// Update tb
		current_point.tb = (current_point.ts.getTime() - previous_point.ts.getTime())/1000;
				
		//Update rsx and rsy
		double xdiff = current_point.rsx = (current_point.x - previous_point.x)/
				result.get(index).tb;
		double ydiff = current_point.rsy = (current_point.y - previous_point.y)/
				result.get(index).tb;
		
		result.get(index).modspd = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
	public ArrayList<PhoneData> getFull(){
		return result;
	}
}
