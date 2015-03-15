package svm.advance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**A class which maps a list of strings into integers for processing purpose in LibSvm
 * 
 */
public class StrNumConverter {
	
	/* Note that there might be other possible ways of implementing this class
	 * e.g. using Map or Hashmap
	 * 
	 */
	
	private ArrayList<String> str_labels_map;
	private String model_file_name;
	private boolean print;
	
	public StrNumConverter(){
		str_labels_map = new ArrayList<String>();
	}
	public StrNumConverter(ArrayList<String> str_labels_map){
		this.str_labels_map = str_labels_map;
	}
	public StrNumConverter(String model_file_name, String filename){
		str_labels_map = new ArrayList<String>();
		
		DataInputStream in = null;
		try {
			 
			in = new DataInputStream(new
		            BufferedInputStream(new FileInputStream(filename)));
			this.model_file_name = in.readUTF();
			if( !this.model_file_name.equals(model_file_name)){
				System.err.println("This String Number Converter file \""+filename+"\" is not intended to used with the current model file\n"+
						"User specified model file: \""+model_file_name+"\"\n"+
						"Expected model file: \""+this.model_file_name+"\"\n"+
						"The program will continue but unexpected results may occurred");
				
			}
			int total = in.readInt();
			try{
				for(int i = 0; i<total; i++){
					int index = in.readInt();
					if(str_labels_map.size() != index)
						printError(filename);
					str_labels_map.add(in.readUTF());
					
				}
				
				if(in.available()>0){
					printError(filename);
					System.err.println("The number in the first line doesn't match the total number of lines following.");
					System.err.println("It is recommended that you don't use this file to avoid any unexpected errors.");
				}
				
			} catch (EOFException e){
			
				printError(filename);
				System.err.println("The number in the first line doesn't match the total number of lines following.");
				System.err.println("It is recommended that you don't use this file to avoid any unexpected errors.");

			}
			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	private void printError(String filename){
		if(!print){
			System.err.println("The file \""+filename+"\" may have been modified manually");
			print = true;
		}
	}
	public void save2File(String model_file_name, String filename){
		try {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(filename)));
			out.writeUTF(model_file_name);
			out.writeInt(str_labels_map.size());
			for(int i = 0; i<str_labels_map.size(); i++){
				out.writeInt(i);
				out.writeUTF(str_labels_map.get(i));
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getIndex(String str_label){
		for(int i = 0; i < str_labels_map.size(); i++){
			if(str_label.equals(str_labels_map.get(i))){
				return i;
			}
		}
		
		str_labels_map.add(str_label);
		return str_labels_map.size()-1;
	}
	
	public String getName(int index){
		return str_labels_map.get(index);
	}
	
	public String[] getLabels(){
		return str_labels_map.toArray(new String[str_labels_map.size()]);
	}
}
