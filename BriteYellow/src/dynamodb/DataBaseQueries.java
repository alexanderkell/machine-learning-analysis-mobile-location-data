package dynamodb;

import java.sql.Timestamp;

public class DataBaseQueries extends DataBaseOperations{

	public DataBaseQueries() throws Exception {
		super();
	}
	
	public PhoneDataDB loadFromTable(String phone_id, Timestamp ts){
		double tsd = ts.getTime();
		PhoneDataDB result = mapper.load(PhoneDataDB.class, phone_id, tsd);
		
		return result;
	}
	
	
	/*public ArrayList<PhoneDataDB> queryTimeRange(String phone_id, Timestamp ts1, Timestamp ts2){
		
		
	}
	*/

}
