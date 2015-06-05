package svm.advance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import csvexport.CSVWriter;

public class AddPrint {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader fr = new BufferedReader(new FileReader("Generated_Track_Store_Whole_Corridor/trackdatanew/oldKmeans/kmeans.csv"));
		CSVWriter cw = new CSVWriter("Generated_Track_Store_Whole_Corridor/trackdatanew/oldKmeans/kmeans_train");
		String line = null;
		while((line = fr.readLine())!=null){
			cw.write(new String[]{line+",200.0,302.0,850.0,364.0"});
		}
		fr.close();
		cw.finish();
	}

}
