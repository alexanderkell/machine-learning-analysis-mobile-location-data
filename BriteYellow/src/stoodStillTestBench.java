import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

import maths.PhoneData;
import Distribution.*;

public class stoodStillTestBench {
	public static void main(String args[]) throws ParseException{
//				   0 T1  2  3  4  5 L6  7   8  9   10  11T 12  13 14  15  16L  17
		//Test for only one point slowing down (Stopping point = 2)
//		int[] x = {0, 5, 10, 11, 15, 20};
//		int[] y = {5, 5, 5,  4,  5,  5,};
		//Test for only 2 points of slow down ()
//		int[] x = {0, 5, 10, 11, 9, 15, 20};
//		int[] y = {5, 5, 5,  4,  5,  5, 5,};
		//Test for 3 points of slow down (Therefore stoppeed)
//		int[] x = {0, 5, 10, 11, 9, 11, 15, 20};
//		int[] y = {5, 5, 5,  4,  5, 5,   5, 5,};
		//Test for 10 points of slow down
//		int[] x = {0,5,10, 11, 9, 10, 9, 10, 11, 11, 12, 40, 45, 50, 55,0,5,10, 11, 9, 10, 9, 10, 11, 11, 12, 40, 45, 50, 55,0,5,10, 11, 9, 10, 9, 10, 11, 11, 12, 40, 45, 50, 55,0,5,10, 11, 9, 10, 9, 10, 11, 11, 12, 40, 45, 50, 55};
//		int[] y = {5,5,5,  5,  5, 4,  5, 6,  6,   3, 3,  40, 40, 40, 40, 5,5,5,  5,  5, 4,  5, 6,  6,   3, 3,  40, 40, 40, 40, 5,5,5,  5,  5, 4,  5, 6,  6,   3, 3,  40, 40, 40, 40, 5,5,5,  5,  5, 4,  5, 6,  6,   3, 3,  40, 40, 40, 40};
		
		//Test for quading up one slow down point
//		int[] x = {0, 5, 10, 11, 15, 20, 0, 5, 10, 11, 15, 20, 0, 5, 10, 11, 15, 20, 0, 5, 10, 11, 15, 20};
//		int[] y = {5, 5, 5,  4,  5,  5,5, 5, 5,  4,  5,  5, 5, 5, 5,  4,  5,  5,5, 5, 5,  4,  5,  5};
		//Test for quading up only 2 points of slow down ()
//		int[] x = {0, 5, 10, 11, 9, 15, 20, 0, 5, 10, 11, 9, 15, 20, 0, 5, 10, 11, 9, 15, 20, 0, 5, 10, 11, 9, 15, 20};
//		int[] y = {5, 5, 5,  4,  5,  5, 5,5, 5, 5,  4,  5,  5, 5,5, 5, 5,  4,  5,  5, 5,5, 5, 5,  4,  5,  5, 5};
		//Test for 3 points of slow down (Therefore stoppeed)
		int[] x = {0, 5, 10, 11, 9, 11, 15, 20, 0, 5, 10, 11, 9, 11, 15, 20, 0, 5, 10, 11, 9, 11, 15, 20, 0, 5, 10, 11, 9, 11, 15, 20};
		int[] y = {5, 5, 5,  4,  5, 5,   5, 5,5, 5, 5,  4,  5, 5,   5, 5,5, 5, 5,  4,  5, 5,   5, 5,5, 5, 5,  4,  5, 5,   5, 5};
		
		int XSTILL=2;
		int YSTILL=2;
		
		//Create test track in PhoneData format
		ArrayList<PhoneData> pd = new ArrayList<PhoneData>();
		for(int i=0; i<x.length; i++){
			PhoneData pdIn = new PhoneData();
			pdIn.x=x[i];
			pdIn.y=y[i];
			pd.add(pdIn);
		}

		LinkedList store = new LinkedList();
		
		//Run through track
		for(int i=0; i<pd.size()-2; i++){
			//If we find a slowing down of equal value to XSTILL, YSTILL then continue
			if(Math.abs(pd.get(i).x-pd.get(i+1).x)<=XSTILL && Math.abs(pd.get(i).y-pd.get(i+1).y)<=YSTILL){
				System.out.println(i+" is the first stopping point");
				int j = 2;
				//If there is a second point that remains within XSTILL, YSTILL radius of first stopping then continue
				if(Math.abs(pd.get(i+j).x-pd.get(i).x)<=XSTILL && Math.abs(pd.get(i+j).y-pd.get(i).y)<=YSTILL){
					j++;
					//If there is a 3rd point that remains within XSTILL, YSTILL radius of first stopping point then we have stopped
					if(Math.abs(pd.get(i+j).x-pd.get(i).x)<=XSTILL && Math.abs(pd.get(i+j).y-pd.get(i).y)<=YSTILL){
						j++;
						//Now we can add all previous points as have confirmed stopped
						store.addLast(i+j-4); 
						store.addLast(i+j-3);
						store.addLast(i+j-2);
						store.addLast(i+j-1);
						//Add all successive points which remain within the stopping point radius
						while(Math.abs(pd.get(i+j).x-pd.get(i).x) <= XSTILL && Math.abs(pd.get(i+j).y-pd.get(i).y) <= YSTILL){
							store.addLast(i+j);
							j++;

						}
						//The following coordinate has left the stopping point radius
						System.out.println(i+j+ " is the leaving point");	
						i=i+j++;
					}else
						continue;
				}else
					continue;
			}
		}
		System.out.printf("We were stopped at points: "+store.toString());
	}
}