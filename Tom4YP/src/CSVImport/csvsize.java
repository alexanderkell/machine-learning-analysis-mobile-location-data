package CSVImport;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class csvsize
{
	public static int wil(String file) throws FileNotFoundException{
	//Get scanner instance
    Scanner scanner = new Scanner(new File(file));
    int length = 0;
    scanner.useDelimiter(",|\\n"); //Set the delimiter used in file

    while (scanner.hasNext())
    {
        //System.out.print(scanner.next() + "|");
        length++;
    }
    
    length/=5;    	
   	
    scanner.close();
    return length;	
    				}
}
    
    

