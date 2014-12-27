import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import HeatMap.*;


public class HeatTestBench {
	
	public static void main(String[] args) throws ParseException, IOException{
		
		HeatMapperLarge hm = new HeatMapperLarge();
		hm.speedHeat(4, "/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
		
		
	}

}
