package csvimport;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import objects.PhoneData;
public class CSVReaders{
	final static String DEFAULT_FN = "C:\\Users\\Fezan\\Documents\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv";
    private String fn;
    final static int amd = 20;
    private String[] parts;

    private ArrayList<String[]> phs;
	private ArrayList<String[]> ph1;
	private ArrayList<String[]> ph2;
	private ArrayList<String[]> ph3;
	private ArrayList<String[]> ph4;
	private ArrayList<String[]> ph5;
	private ArrayList<String[]> ph6;
	private ArrayList<PhoneData> PDA= new ArrayList<PhoneData>();
	
    public CSVReaders(String fn){
    	this.fn = fn;
    	phs = new ArrayList<String[]>();
    	PhoneData PD;
    	
    	
    	try{
			
    		Scanner scanner = new Scanner(new File(fn));//Get scanner instance
    		String lineSeparator = System.getProperty("line.separator");
    		scanner.useDelimiter(lineSeparator + "|\r\n|\n|\r"); //Set the delimiter used in file, the "\r\n" is exclusively for Windows
    		
    		String[] data;
    		scanner.nextLine();	//Skip the first line
    		
    		while (scanner.hasNext()){//while loop for writing data into main data matrix of raw data
    			parts = scanner.next().split(",");
    			data = new String[amd];
    			for(int x = 0; x < 5; x++){
    				data[x] = parts[x];
    			}
    			try{
    				PD = new PhoneData();
    				PD.x = Double.parseDouble(parts[0]);
    				PD.y = Double.parseDouble(parts[1]);
    				PD.z = Double.parseDouble(parts[2]);
    				PD.wholedatestring = parts[3];
    				PD.phone_id = parts[4];
    				PDA.add(PD);
    			}catch(NumberFormatException nfe){
    				
    			}
    			
    			phs.add(data);
    			
    			
    			
        	}	
    		
    		scanner.close();
    		categorise();
    		}catch(FileNotFoundException fnf){
    			System.out.println("File Not Found, Sorry!");
    			fnf.printStackTrace();
    		}
    }
    
    public CSVReaders() throws FileNotFoundException{
		this(DEFAULT_FN);
    }
    
    
    public String getFileName(){
    	return fn;
    }
	
    public ArrayList<PhoneData> getUncategorisedDataObjectList(){
    	return PDA;
    }
	
    public PhoneData[] getUncategorisedDataObject(){
    	PhoneData[] PD = toArrayPD(PDA);
    	return PD;
    }
    
    private void categorise(){
    	final String ph1n = "HT25TW5055273593c875a9898b00";//variables denoting phone IDs
        final String ph2n = "ZX1B23QBS53771758c578bbd85";
        final String ph3n = "TA92903URNf067ff16fcf8e045";
        final String ph4n = "YT910K6675876ded0861342065";
        final String ph5n = "ZX1B23QFSP48abead89f52e3bb";
        final String ph6n = "8d32435715629c24a4f3a16b";
        ph1 = new ArrayList<String[]>();
        ph2 = new ArrayList<String[]>();
        ph3 = new ArrayList<String[]>();
        ph4 = new ArrayList<String[]>();
        ph5 = new ArrayList<String[]>();
        ph6 = new ArrayList<String[]>();
        
        int length = phs.size();
        
        for (int k = 0; k < length; k++){
        	String dat = phs.get(k)[4];
        	if (dat.contains(ph1n)){//workaround so data can be categorised, if phone id is phone 1 then sel=1
                ph1.add(phs.get(k));
            }
        	else if (dat.contains(ph2n)){
        		ph2.add(phs.get(k));
            }
        	else if (dat.contains(ph3n)){
        		ph3.add(phs.get(k));
            }
        	else if (dat.contains(ph4n)){
        		ph4.add(phs.get(k));
            }
        	else if (dat.contains(ph5n)){
        		ph5.add(phs.get(k));
            }
        	else if (dat.contains(ph6n)){
        		ph6.add(phs.get(k));
            }
            
        }
        
    }
	public String[][] myPhone(int opt){
		
        if(opt==1){//returns the data based on the phone id 
        	return toArray(ph1);
        }      	
        if(opt==2){
            return toArray(ph2);
        }
        if(opt==3){
            return toArray(ph3);
        }
        if(opt==4){
            return toArray(ph4);
        }
        if(opt==5){
            return toArray(ph5);
        }
        if(opt==6){
            return toArray(ph6);
        }
        if(opt==7){
            return toArray(phs);
        }
        
        return null;
        
	}
	
	public int findLength(){
		
		return phs.size();
	}
	
	private static String[][] toArray(ArrayList<String[]> list){
		String[][] result = new String[amd][list.size()];
		for(int i=0; i<list.size(); i++){
			String[] d = list.get(i);
			for(int j=0; j<amd; j++){
				result[j][i] = d[j];
			}
		}
		return result;
	}
	
	private static PhoneData[] toArrayPD(ArrayList<PhoneData> list){
		PhoneData[] result = new PhoneData[list.size()];
		for(int i=0; i<list.size(); i++){
			result[i] = list.get(i);
		}
		return result;
	}


}