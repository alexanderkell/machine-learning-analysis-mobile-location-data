import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import HeatMap.HeatMapper;


public class HeatTestBench {
	
	public static void main(String[] args) throws ParseException, IOException{
		
		HeatMapper hm = new HeatMapper();
		hm.speedHeat(1, "/Users/thomas/4th-year-project/Tom4YP/src/24th Sept ORDERED.csv");
		
		
	}

}
