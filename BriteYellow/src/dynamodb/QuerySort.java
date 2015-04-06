package dynamodb;

import java.util.Comparator;

public class QuerySort implements Comparator<PhoneDataDB>{
	
	@Override
	public int compare(PhoneDataDB data1, PhoneDataDB data2){
		
		long TSL1 = data1.getTimestampLong();
		long TSL2 = data2.getTimestampLong();
		
		if(TSL1 < TSL2){
			return -1;	
		}
		else if(TSL1 > TSL2){
			return +1;	
		}else{
			return 0;
		}
		
	}
	
	
}
