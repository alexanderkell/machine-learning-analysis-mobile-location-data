package splitting;

import java.util.ArrayList;

import objects.PhoneData;

public class TrackSelect
{

	public static String[][] selecter(String[][] fn,int select)
	{
		int r=0;
		double x;
		int start = -1;
		
		for(int i = 0; i<fn[0].length; i++){
			System.out.print(fn[16][i]);
			if(fn[16][i] != null){
				x = Double.parseDouble(fn[16][i]);
				if(x==select){
					if(start== -1){
						start = i;
					}
					r++;
				}
			}

		}
		
		String[][] newerdat = new String[20][r];
		

		for(int i = 0; i<r; i++)
		{		
			
			for(int k =0; k<20; k++){
				newerdat[k][i] = fn[k][start+i];
			}	
		
		}
		return newerdat;
		
	}
	
	public static int getTotalTracks(String[][] fn){
		for(int i = fn[0].length-1; i>=0; i--){
			if(fn[16][i]!=null)
				return Integer.parseInt(fn[16][i]);
		}
		return 0;
	}
	
	public static int getTotalTracks(PhoneData[] fn){
		for(int i = fn.length-1; i>=0; i--){
			if(fn[i].track_no!=-1)
				return fn[i].track_no;
		}
		return 0;
	}

	public static PhoneData[] selecter(PhoneData[] fn,int select)
	{
		ArrayList<PhoneData> pdlist = new ArrayList<PhoneData>();
		for(int i = 0; i<fn.length; i++){
			
			if(fn[i].track_no == select){
				
				pdlist.add(fn[i]);
				if(fn[i].track_no == -1){
					System.out.println("Hello");
				}
			}
			
		}
		return pdlist.toArray(new PhoneData[pdlist.size()]);
	}
	
	public static ArrayList<PhoneData> selecter(ArrayList<PhoneData> fn,int select)
	{
		ArrayList<PhoneData> pdlist = new ArrayList<PhoneData>();
		for(int i = 0; i<fn.size(); i++){
			
			if(fn.get(i).track_no == select){
				
				pdlist.add(fn.get(i));
				if(fn.get(i).track_no == -1){
					System.out.println("Hello");
				}
			}
			
		}
		return pdlist;
	}
}
