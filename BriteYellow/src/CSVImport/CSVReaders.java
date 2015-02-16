package CSVImport;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVReaders{
    static String fn =new String("C:\\Users\\Fezan\\Documents\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv"); 
    final static int amd = 20;
    static int length;
    String[][] data;
	private ArrayList<String[]> ph1;
	private ArrayList<String[]> ph2;
	private ArrayList<String[]> ph3;
	private ArrayList<String[]> ph4;
	private ArrayList<String[]> ph5;
	private ArrayList<String[]> ph6;

    public CSVReaders(String fn){
    	try{
    	
    		CSVReaders.fn = fn;
    		length = CSVReaders.findLength();
			data = new String[amd][length];
    		String filename = new String(fn);//reads file name and hierarchy
    		Scanner scanner = new Scanner(new File(filename));//Get scanner instance
    		scanner.useDelimiter(",|\r\n|\n"); //Set the delimiter used in file, the "\r\n" is exclusively for Windows
    		for(int x=0; x<5;x++){//intialise the data matrix to avoid null pointer errors
    			for(int y=0; y<length;y++){
      			data[x][y]="0";
    			}
    		}
        
    		int i = 0;//indices of data
    		int j = 0;        
    		while (scanner.hasNext()){//while loop for writing data into main data matrix of raw data
    			switch (i){//switch statement with case for each cell
        			case 0:
        			case 1:
        			case 2:
        			case 3: data[i][j] = scanner.next();
        			break;
        			case 4: data[i][j] = scanner.next();
        			i=-1;//goes back to beginning column
        			j++;//next row
        			break;
       
        	}	
        

    			i++;//i incremented to write into each cell of array

    		}
    		scanner.close();
    		categorise();
    		}catch(FileNotFoundException fnf){
    			System.out.println("File Not Found, Sorry!");
    		
    		}
    	for(int rd =0; rd<amd;rd++){
    		data[rd][0] = "0";
    	}
    }
    
    public CSVReaders() throws FileNotFoundException{
		this(fn);
    }
    
    
    public String getFileName(){
    	return fn;
    }
	
   
    public void setFileName(String fn){
    	this.fn = fn;
    	
    }
	
    private void categorise(){
    	String ph1n = "HT25TW5055273593c875a9898b00";//variables denoting phone IDs
        String ph2n = "ZX1B23QBS53771758c578bbd85";
        String ph3n = "TA92903URNf067ff16fcf8e045";
        String ph4n = "YT910K6675876ded0861342065";
        String ph5n = "ZX1B23QFSP48abead89f52e3bb";
        String ph6n = "8d32435715629c24a4f3a16b";
        ph1 = new ArrayList<String[]>();
        ph2 = new ArrayList<String[]>();
        ph3 = new ArrayList<String[]>();
        ph4 = new ArrayList<String[]>();
        ph5 = new ArrayList<String[]>();
        ph6 = new ArrayList<String[]>();
        
        for (int k = 0; k < length; k++){
        	String dat = data[4][k];
        	if (dat.contains(ph1n)){//workaround so data can be categorised, if phone id is phone 1 then sel=1
                ph1.add(extractArrayCol(data,k));
            }
        	else if (dat.contains(ph2n)){
                ph2.add(extractArrayCol(data,k));
            }
        	else if (dat.contains(ph3n)){
        		ph3.add(extractArrayCol(data,k));
            }
        	else if (dat.contains(ph4n)){
        		ph4.add(extractArrayCol(data,k));
            }
        	else if (dat.contains(ph5n)){
        		ph5.add(extractArrayCol(data,k));
            }
        	else if (dat.contains(ph6n)){
        		ph6.add(extractArrayCol(data,k));;
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
            return data;
        }
        
        return null;
        
	}
	
	/**
	 * Method for extracting the column of a 2D array
	 * @param the 2D array
	 * @param column of interest
	 * @return the column of an 2D array
	 */
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
	private static String[] extractArrayCol(String[][] array, int col_of_interest){
		String[] colArray = new String[array.length];
		for(int row = 0; row < array.length; row++){
		    colArray[row] = array[row][col_of_interest];
		}
		return colArray;
	}
	public static int findLength(){
		try{
			Scanner scanner = new Scanner(new File(fn));
			length = 0;
		    scanner.useDelimiter(",|\\n"); //Set the delimiter used in file
		    String currentvalue = new String();
		    while (scanner.hasNext())
		    {
		       currentvalue = scanner.next();
		        length++;
		    }
		    length/=5;   
		    
		    scanner.close();
			}catch(FileNotFoundException fnf){
			System.out.println("File Not Found, Sorry!");
		}
		
		return length;
	}
      public static void main(String[] args){
    	  
      }
}
