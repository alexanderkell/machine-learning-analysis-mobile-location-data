package CSVExport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
	private final FileWriter fw;
	private String filename;
	
	public CSVWriter(String filename) throws IOException{
		fw = new FileWriter(filename);
		this.filename = filename;
	}
	
	public void write(String[] data) throws IOException{
		for(int i=0; i<data.length; i++){
			fw.append(data[i]);
			if(i<data.length-1)
				fw.append(",");
		}
		
		fw.append("\n");
	}
	public void write(Object[] data) throws IOException{
		String[] str_data = new String[data.length];
		for(int i=0; i<data.length; i++){
			str_data[i] = String.valueOf(data[i]);
		}
		write(str_data);
	}
	public void write(double[] data) throws IOException{
		String[] str_data = new String[data.length];
		for(int i=0; i<data.length; i++){
			str_data[i] = String.valueOf(data[i]);
		}
		write(str_data);
	}
	public void write(float[] data) throws IOException{
		String[] str_data = new String[data.length];
		for(int i=0; i<data.length; i++){
			str_data[i] = String.valueOf(data[i]);
		}
		write(str_data);
	}
	public void finish() throws IOException{
		fw.flush();
		fw.close();
	}
	public boolean delete() throws IOException{
		return new File(filename).delete();
	}
	public String getFileName(){
		return filename;
	}

}
