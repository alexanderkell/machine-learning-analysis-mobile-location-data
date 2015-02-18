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
		int i = 0,r=0;
		double x;
		x = Double.parseDouble(fn[16][i]);
		while(i<fn[0].length){
			if(x==select)
				{r++;}
			i++;
		}
		
		String[][] newerdat = new String[20][r];
		
		int counter = 0;
	while(i<fn[0].length)
		{		

			if(x==select)
			{
				for(int k =0; k<20; k++){
					newerdat[k][counter++] = fn[k][i];
				}	
			}	
		}
		return newerdat;
		
	}
}
