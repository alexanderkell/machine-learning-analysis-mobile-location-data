package csvexport;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CSVWriter {
	private final FileWriter fw;
	private String filename;
	
	public CSVWriter(String filename) throws IOException{
		// Check if the parent directory exists
		File parent = new File(filename).getParentFile();
		if( !parent.exists())
			parent.mkdirs();
		this.filename = filename+".csv";
		fw = new FileWriter(this.filename);
		
	}
	
	public void write(String[] data) throws IOException{
		for(int i=0; i<data.length; i++){
			fw.append(data[i]);
			if(i<data.length-1)
				fw.append(",");
		}
		
		fw.append("\n");
	}
	
	public void write(String[] data, boolean prep) throws IOException{
		
		for(int i=0; i<data.length; i++){
			fw.append(data[i]);
		}
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
	
	public void write(double[][] data) throws IOException{
		String[] str_data = new String[data.length];
		
		for(int j = 0; j < data[0].length; j++){
			for(int i = 0; i < data.length; i++){
				str_data[i] = String.valueOf(data[i][j]);	
			}
			
			write(str_data);
		}
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
