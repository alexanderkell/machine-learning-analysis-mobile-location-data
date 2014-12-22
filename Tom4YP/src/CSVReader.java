import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;

public class csvReader{
     
	
	static String fn =new String("/Users/thomas/4th-year-project/Tom/24th Sept ORDERED.csv");
	public static String fSet(String fname){
		csvReader.fn=fname;
		return fn;
			
	}
	
	public static String[][] wd(int opt) throws FileNotFoundException{
		int amd = 20;
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
        String ph4n = new String("YT910K6675876ded0861342065");
        String ph5n = new String("ZX1B23QFSP48abead89f52e3bb");
        String ph6n = new String("8d32435715629c24a4f3a16b");
        String[][] ph1 = new String[amd][length]; //where ph1 is HT25TW5055273593c875a9898b00
        String[][] ph2 = new String[amd][length]; //where ph2 is ZX1B23QBS53771758c578bbd85
        String[][] ph3 = new String[amd][length];//where ph3 is TA92903URNf067ff16fcf8e045
        String[][] ph4 = new String[amd][length];//where ph4 is YT910K6675876ded0861342065
        String[][] ph5 = new String[amd][length];//where ph5 is ZX1B23QFSP48abead89f52e3bb
        String[][] ph6 = new String[amd][length];//where ph6 is 8d32435715629c24a4f3a16b
        int i = 0;//indices of data
        int j = 0;        
        while (scanner.hasNext()){//while loop for writing data into main data matrix of raw data
        	
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
        i++;//i incremented to write into each cell of array

       }
        int pin1 = 0;
        int pin2 = 0;
        int pin3 = 0;
        int pin4 = 0;
        int pin5 = 0;
        int pin6 = 0;
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
        	if (dat.contains(ph4n)){
                sel = 4;
                }
        	if (dat.contains(ph5n)){
                sel = 5;
                }
        	if (dat.contains(ph6n)){
                sel = 6;
                }
        	else{
        		
        	}
            switch(sel){ //switch statement writes individual phone data into individual matrices
            	case 1: 
            		for(int x=0; x<5;x++){//writes data into matrix related to the phone
            			ph1[x][pin1] = data[x][k];
            		}
            		pin1++;//next line
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
            	case 4:
            		for(int x=0; x<5;x++){
            			ph4[x][pin4] = data[x][k];
            		}
            		pin4++;
            		break;
            	case 5:
            		for(int x=0; x<5;x++){
            			ph5[x][pin5] = data[x][k];
            		}
            		pin5++;
            	case 6:
            		for(int x=0; x<5;x++){
            			ph6[x][pin6] = data[x][k];
            		}
            		pin6++;
            		break;
            
            
            }
        }
        
              
        scanner.close();
        
        if(opt==1){//returns the data based on the phone id 
        	return ph1;
        }      	
        if(opt==2){
            return ph2;
             }
        if(opt==3){
            return ph3;
             }
        if(opt==4){
            return ph4;
             }
        if(opt==5){
            return ph5;
             }
        if(opt==6){
            return ph6;
             }
        if(opt==7){
            return data;
             }
        else{
        	return null;
        }
	}
      
}