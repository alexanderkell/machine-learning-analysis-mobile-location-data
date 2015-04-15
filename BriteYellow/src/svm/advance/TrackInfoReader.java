package svm.advance;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TrackInfoReader {

	private ArrayList<String> types_list;
	private ArrayList<double[]> data_list;
	
	private ArrayList<String> filteredtypes;
	private ArrayList<double[]> filtereddata;
	
	private final BufferedReader fr;
	
	private String[] headers;
	
	private int X1, X2, Y1, Y2;
	
	final static String[] type = new String[]{
		"Business",
		"Security",
		"Shopper"
	};
	public TrackInfoReader(String filename) throws IOException{

		fr = new BufferedReader(new FileReader(filename));
		types_list = new ArrayList<String>();
		data_list = new ArrayList<double[]>();
		
		readFile();
		
		fr.close();
		
		findXsYsLocation();
	}
	
	public void findXsYsLocation(){
		for(int i = 0; i < headers.length; i++){
			if(headers[i].equals("X1"))
				X1 = i;
			else if(headers[i].equals("X2"))
				X2 = i;
			else if(headers[i].equals("Y1"))
				Y1 = i;
			else if(headers[i].equals("Y2"))
				Y2 = i;
		}

	}
	public int findAxisLocation(String name){
		for(int i = 0; i < headers.length; i++){
			if(headers[i].equals(name))
				return i;
		}
		return -1;
	}
	public void filter(final double x1, final double y1, final double x2, final double y2){
		filteredtypes = new ArrayList<String>();
		filtereddata = new ArrayList<double[]>();
		
		for(int i = 0; i < data_list.size(); i++){
			double[] subdata = data_list.get(i);
//			System.out.println(subdata[X1]+" "+subdata[X2]+" "+subdata[Y1]+" "+subdata[Y2]);
			if(subdata[X1] == x1 && subdata[X2] == x2 && subdata[Y1] == y1 && subdata[Y2] == y2){
				filteredtypes.add(types_list.get(i));
				filtereddata.add(subdata);
			}
				
		}
	}
	public double[][] getExtractedColumns(int... cols){
		double[][] result = new double[filtereddata.size()][cols.length];
		for(int i = 0; i < filtereddata.size(); i++){
			double[] subdata = filtereddata.get(i);
//			System.out.println(subdata[X1]+" "+subdata[X2]+" "+subdata[Y1]+" "+subdata[Y2]);
			for(int j = 0; j < cols.length; j++){
				result[i][j] = subdata[cols[j]];
			}
				
		}
		return result;
	}
	public String[] getTypes(){
		return filteredtypes.toArray(new String[filteredtypes.size()]);
	}
	public double[][] getData(){
		return filtereddata.toArray(new double[filtereddata.size()][]);
	}
	public String[] getHeaders(){
		return headers;
	}
	
	private void readFile() throws IOException{
		try{
			//
			String[] h = fr.readLine().split(",");
			headers = new String[h.length-1];
			for(int i = 0; i < headers.length; i++){
				headers[i] = h[i+1];
			}
			
			while(true){
				String line = fr.readLine();
				String[] segments = line.split(",");
				
				// The type of person (at column 0)
				String type = String.valueOf(characteristicType(segments[0]));
				
				// The rest of the attributes (starting from column 1)
				double[] d = new double[segments.length-1];
				for(int i = 0; i < d.length; i++){
					d[i] = Double.parseDouble(segments[i+1]);
				}
				
				// Add the type and the attributes
				types_list.add(type);
				data_list.add(d);
			}
		} catch (EOFException | NullPointerException e){
			
		}
		
	}
	
	
	public static String characteristicType(String phoneID){
		if(phoneID.contentEquals("HT25TW5055273593c875a9898b00")){
			return type[0];
		}else if(phoneID.contentEquals("ZX1B23QBS53771758c578bbd85")){
			return type[1];
		}else if(phoneID.contentEquals("TA92903URNf067ff16fcf8e045")){
			return type[2];
		}else if(phoneID.contentEquals("YT910K6675876ded0861342065")){
			return type[2];
		}else if(phoneID.contentEquals("ZX1B23QFSP48abead89f52e3bb")){
			return type[0];
		}
		return null;
	}

}
