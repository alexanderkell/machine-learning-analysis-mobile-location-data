package splitting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Sorting {


    public static void main(String[] args) {

		String csvFile = "C:\\Users\\Fezan\\Documents\\4th-year-project\\BriteYellow\\bin\\security.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		//String test="";
		
		try {
				FileReader fr = new FileReader(csvFile);
				br = new BufferedReader(fr);
				int i = 0;
				int j = 0;
				int r = 1;
				int s=0,t=0,u=0;
				File file = new File("Security-" + j + ".csv");
				if (!file.exists()) {
					file.createNewFile();
			}
				
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((line = br.readLine()) != null) {
 		
 				// use comma as separator
				String[] data = line.split(cvsSplitBy);
				
				double temp = Double.parseDouble(data[0]);

				if (temp>300 && temp<850)
					{
					u=1;
					}
				if(u==1){
				if(temp > 850){s=1;t=0;}
				if(temp<850 && s==1)
					{
						r++;
						s=0;
						u=0;
					}
				
				if(temp < 300){t=1;s=0;}				
				if(temp>300 && t==1)
					{
						r++;
						t=0;
						u=0;
					}
				}
				data[4] =data[4] +","+ r;
				String content = data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4];

					 if (i == 0 || i % 1000 == 0)
					 	{
						 	if (i % 1000 == 0) 
						 		{
						 			bw.close();
						 			file = new File("Security-" + j + ".csv");
						 		}
					
						 	j++;
					
						 	// if file doesnt exists, then create it
						 	if (!file.exists()) 
						 		{
						 			file.createNewFile();
						 		}
				
						 	fw = new FileWriter(file.getAbsoluteFile());
						 	bw = new BufferedWriter(fw);
					 	
					}
					 
					 if(temp > 300 && temp < 850)
					 	{
						temp=0;
						bw.write(content);
						bw.write(System.getProperty("line.separator"));
					 	}
	
				System.out.println(content);
				i++;
 			}
 			
 			bw.close();
 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		System.out.println("Done");
  	}
}