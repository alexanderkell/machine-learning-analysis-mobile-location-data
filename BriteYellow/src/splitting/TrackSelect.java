package splitting;

public class TrackSelect
{
	String[][] newdat;

	String[][] fn;
	static int opt = 1;
	static int select=1;
	static int length;

	public static String[][] selecter(String[][] fn,int select)
	{
		int r=0;
		double x;
		int start = -1;
		
		for(int i = 0; i<fn[0].length; i++){
			if(fn[16][i] != null){
				x = Double.parseDouble(fn[16][i]);
				if(x==select){
					if(start== -1){
						start = i;
					}
					r++;
				}
			}
			i++;
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
}
