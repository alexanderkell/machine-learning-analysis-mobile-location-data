package splitting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class IDsplitter {


    public static void main(String[] args) {
    	//double temp=3;
		String csvFile = "C:\\Users\\Fezan\\Documents\\4th-year-project\\BriteYellow\\src\\24th Sept Ordered.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String test="TA92903URNf067ff16fcf8e045";//insert the ID which will separate the files
		
		try {
				FileReader fr = new FileReader(csvFile);
				br = new BufferedReader(fr);
				int i = 0;
				int j = 0;
				File file = new File("Sort-" + j + ".txt");
				if (!file.exists()) {
					file.createNewFile();
			}
				
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((line = br.readLine()) != null) {
 		
 				// use comma as separator
				String[] data = line.split(cvsSplitBy);
				
				String content = data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4];

					 if (i == 0 || i % 100000 == 0)
					 	{
						 	if (i % 100000 == 0) 
						 		{
						 			bw.close();
						 			//insert the file name
						 			file = new File("shopper1.csv");
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

					 if(data[4].equals(test)){
						bw.write(content);
						bw.write(System.getProperty("line.separator"));
					 }
					 
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