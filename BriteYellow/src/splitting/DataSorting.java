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
	
	public DataSorting(int opt, String fn) throws ParseException {
				this.opt = opt;
				this.fn = fn;
				//create an instance of the newdatFormatOperations class
				DataFormatOperations DFO = new DataFormatOperations(this.opt, this.fn);
				
				//gets the full matrix
				newdat = DFO.getFull();
				
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
	
	public DataSorting(String[][] newdat)
{
try{
		int i = 0;
		int j = 0;
		int r = 1;
		System.out.println("try1");
		File file = new File("Sort-" + j + ".txt");
		if (!file.exists())
		{
			file.createNewFile();
		}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);	
			{
			double temp = Double.parseDouble(newdat[0][j]);
			//double number = Double.parseDouble(newdat[54]);
			
			newdat[4][j] =newdat[4][j] +"-"+ r;
			
				if(temp > 1000)
					{
					r++;
					}
				
			String content = newdat[0] + "," + newdat[1] + "," + newdat[2] + "," + newdat[3] + "," + newdat[4];
			System.out.println("try2");
			if (newdat[0]==null)
			{
				j++;
			}
			
				else if(temp < 250 || temp > 1000){
					if (i == 0 || i % 1000 == 0) {
						if (i % 1000 == 0) {
							bw.close();
							file = new File("Sort-" + j + ".txt");
						}
				
						j++;
						System.out.println("try3");
						// if file doesnt exists, then create it
						if (!file.exists()) {
							file.createNewFile();
					}
			
					fw = new FileWriter(file.getAbsoluteFile());
					bw = new BufferedWriter(fw);
				}
			}
			bw.write(content);
			bw.write(System.getProperty("line.separator"));
			//bw.close();

			System.out.println(content);
			i++;
			}
			System.out.println("try4");
			bw.close();
			
	}catch (FileNotFoundException e) 
	{
		e.printStackTrace();
	} catch (IOException e) 
	{
		e.printStackTrace();
	} 	
}
//System.out.println("Done");
	}

