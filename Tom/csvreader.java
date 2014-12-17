import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;

public class csvReader{
     //public static void main(String[] args) throws FileNotFoundException{
	static String fn =new String("C:/Users/Thomas/Documents/4th-year-project/Tom/24th Sept ORDERED.csv");
	public static String fSet(String fname){
		csvReader.fn=fname;
		return fn;
			
	}
	
	public static String[][] wd(int opt) throws FileNotFoundException{
		String filename = new String(fn);//reads file name and hierarchy
		Scanner scanner = new Scanner(new File(filename));//Get scanner instance
        scanner.useDelimiter(",|\\n"); //Set the delimiter used in file
        int length = 20000; //following will calc length of csv: csvsize.wil("24th Sept.csv");
        String[][] data = new String[8][length];
        for(int x=0; x<5;x++){//intialise the data matrix to avoid null pointer errors
      		for(int y=0; y<length;y++){
      			data[x][y]="0";
      		}
      	}
        String ph1n = new String("HT25TW5055273593c875a9898b00");//variables denoting phone IDs
        String ph2n = new String("ZX1B23QBS53771758c578bbd85");
        String ph3n = new String("TA92903URNf067ff16fcf8e045");
        String[][] ph1 = new String[8][length]; //where ph1 is HT25TW5055273593c875a9898b00
        String[][] ph2 = new String[8][length]; //where ph2 is ZX1B23QBS53771758c578bbd85
        String[][] ph3 = new String[8][length]; //where ph3 is TA92903URNf067ff16fcf8e045
        int i = 0;//indices of data
        int j = 0;        
        while (scanner.hasNext()){//while loop for writing data into main data matrix
        	
        	switch (i){//switch statement with case for each cell
        		case 0: data[i][j] = scanner.next();
        				break;
        		case 1: data[i][j] = scanner.next();
						break;
        		case 2: data[i][j] = scanner.next();
        				break;
        		case 3: data[i][j] = scanner.next();
						break;
        		case 4: data[i][j] = scanner.next();
        				i=-1;//goes back to beginning column
        				j++;//next row
						break;
       
        	}
        
        //System.out.print(scanner.next());
        i++;

       }
        int pin1 = 0;
        int pin2 = 0;
        int pin3 = 0;
        String dat = new String();
        int sel = 0;
        for (int k = 0; k < length; k++){
        	dat = data[4][k];
        	if (dat.contains(ph1n)){//workaround so data can be categorised, if phone id is phone 1 then sel=1
                sel = 1;
                }
        	if (dat.contains(ph2n)){
                sel = 2;
                }
        	if (dat.contains(ph3n)){
                sel = 3;
                }
            switch(sel){ //switch statement writes individual phone data into individual matrices
            case 1: 
            	 for(int x=0; x<5;x++){
            	 ph1[x][pin1] = data[x][k];
            	}
            pin1++;
            break;
            case 2:
             	for(int x=0; x<5;x++){
             	 ph2[x][pin2] = data[x][k];
             	}
            pin2++;
            break;
            case 3:
              	for(int x=0; x<5;x++){
              	 ph3[x][pin3] = data[x][k];
              	}
            pin3++;
            break;
            }
        }
        
        
      
        scanner.close();
        
        if(opt==1){
       return ph1;
        }      	
        if(opt==2){
            return ph2;
             }
        if(opt==3){
            return ph3;
             }
        if(opt==4){
            return data;
             }
        else{
        	return null;
        }
	}
      
}