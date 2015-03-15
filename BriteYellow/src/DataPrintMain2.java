import csvimport.*;

//tests CSVreaders
public class DataPrintMain2 {
	
	public static void main(String[] args){
		
			int option = 7;
			int lines = 10;
			//HT25TW5055273593c875a9898b00.csv
			//24th Sept ORDERED.csv
			CSVReaders Read = new CSVReaders("HT25TW5055273593c875a9898b00.csv");
	
			String cd[][]=Read.myPhone(option);
			
			for (int k = 0; k < lines; k++){
				for (int l = 0; l < 5; l++) {
				System.out.print(cd[l][k] + " ");
				}
			System.out.print("\n");
			}
			System.out.print("\n\n\n\n");

			System.exit(1);
	}
}
