package filters;

public class GapCleaner 
{
	String[][] newdat;

	String[][] fn;
	static int opt = 1;
	static int select=1;
	static int length;

	public static String[][] cleaner(String[][] fn)
	{
		int i = 0,r=0;
		double dif;
		double xdiff = Double.parseDouble(fn[0][i+1]) - Double.parseDouble(fn[0][i]);
		double ydiff = Double.parseDouble(fn[1][i+1]) - Double.parseDouble(fn[1][i]);
		dif = java.lang.Math.pow(xdiff * xdiff + ydiff * ydiff,0.5);
		while(i<fn[0].length){
			if(dif<150)
				{r++;}
			i++;
		}
		
		String[][] newest = new String[20][r];
		
		int counter = 0;
	while(i<fn[0].length)
		{		

			if(dif<150)
			{
				for(int k =0; k<20; k++){
					newest[k][counter++] = fn[k][i];
				}	
			}	
		}
		return newest;
		
	}

}
