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

public class DataSorting {
	String[][] newdat;
	static int opt = 1;
	static String fn = new String();
	static int length;
	
	public DataSorting(int opt, String fn) throws ParseException {
				this.opt = opt;
				this.fn = fn;
				//create an instance of the newdatFormatOperations class
				DataFormatOperations DFO = new DataFormatOperations(this.opt, this.fn);
				
				//gets the full matrix
				newdat = DFO.getFull();
				length = DFO.getLength();
				
				/*//selects how many lines to print
				int length = 100;
				
				//prints the array
				for (int k = 0; k < length; k++){
				for (int l = 0; l < 16; l++) {
					System.out.print(newdat[l][k] + " || ");
				}
					System.out.print("\n");
				}*/
		
	}
	
	public DataSorting() throws ParseException{
		this(opt, fn);
		
	}
	
	public String[][] getSort(){
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		while(i<length){
			
			x = Double.parseDouble(newdat[0][i]);
			if(x>200 && x<850){
				newdat[16][i] = String.valueOf(track);
				
			}
			else if(x>850){r=1;s=0;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
			}
			
			
			else if(x<200){s=1;r=0;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
			}
			
			System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
		}
		return newdat;
	}



}

