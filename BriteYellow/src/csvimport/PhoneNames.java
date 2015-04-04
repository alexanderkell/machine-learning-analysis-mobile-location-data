package csvimport;

public class PhoneNames {
	final String ph1n = "HT25TW5055273593c875a9898b00";
    final String ph2n = "ZX1B23QBS53771758c578bbd85";
    final String ph3n = "TA92903URNf067ff16fcf8e045";
    final String ph4n = "YT910K6675876ded0861342065";
    final String ph5n = "ZX1B23QFSP48abead89f52e3bb";
    final String ph6n = "8d32435715629c24a4f3a16b";
    
    
	public String numberToName(int REF){
		if (REF == 1){
			return ph1n;
		}
		else if (REF == 2){
			return ph2n;
		}
		else if (REF == 3){
			return ph3n;
		}
		else if (REF == 4){
			return ph4n;
		}
		else if (REF == 5){
			return ph5n;
		}
		else if (REF == 6){
			return ph6n;
		}
		else{
			return null;
		}
		
	}
	
	
}
