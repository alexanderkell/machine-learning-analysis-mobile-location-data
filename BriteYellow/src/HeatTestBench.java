import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import HeatMap.*;


public class HeatTestBench {
	
	public static void main(String[] args) throws ParseException, IOException{
		//select the phone
		int opt = 4;
		//filename
		String fn = new String("/Users/thomas/4th-year-project/BriteYellow/src/24th Sept ORDERED.csv");
		//calls the heatmapper class to print the heatmap
		HeatMapperLarge hm = new HeatMapperLarge();
		//the int is the 
		hm.speedHeat(opt, fn);
		
		
	}

}
