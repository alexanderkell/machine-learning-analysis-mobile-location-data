package splitting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import Maths.DataFormatOperations;


public class TrackSelect
{
	String[][] newdat;
	String[][] newerdat;
	static String fn = new String();
	static int opt = 1;
	static int select=1;
	static int length;
	
	public TrackSelect(int opt, String fn,int select) throws ParseException
	{
		DataFormatOperations DFO = new DataFormatOperations(this.opt,this.fn);
		
		newdat = DFO.getFull();
		length = DFO.getLength();
			
	}
	
	public TrackSelect() throws ParseException
	{
		this(opt, fn,select);
	}

	public String[][] selecter()
	{
		int i = 0;
		double x;
	
	while(i<length)
		{		
			x = Double.parseDouble(newdat[16][i]);

			if(x==select)
			{				
			newerdat = newdat;		
			}	
		}
		return newerdat;
		
	}
}
