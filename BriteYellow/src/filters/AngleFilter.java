package filters;

public class AngleFilter 
{

	String[][] newdat;

	String[][] fn;
	static int opt = 1;
	static int select=1;
	static int length;

	public static String[][] cleaner(String[][] fn)
	{
		int i = 0,r=0;
		//double dif;
		//double speeddiff = Double.parseDouble(fn[14][i+1]) - Double.parseDouble(fn[14][i]);
		//double accdiff = Double.parseDouble(fn[15][i+1]) - Double.parseDouble(fn[15][i]);
		//dif = java.lang.Math.pow(speeddiff * speeddiff,0.5);
		double x = Double.parseDouble(fn[14][i]);
		double y = Double.parseDouble(fn[14][i+1]);
		
		while(i<fn[0].length){
			if(y<(2.355+x) && y>(x-2.355))//75% of Pi angle change range on each side
				{r++;}
			i++;
		}
		
		String[][] newest = new String[20][r];
		
		int counter = 0;
	while(i<fn[0].length)
		{		

			if(y<(2.355+x) && y>(x-2.355))
			{
				for(int k =0; k<20; k++){
					newest[k][counter++] = fn[k][i];
				}	
			}	
		}
		return newest;
		
	}

	

}
