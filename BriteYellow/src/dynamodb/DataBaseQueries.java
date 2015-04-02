package dynamodb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import maths.PhoneData;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class DataBaseQueries extends DataBaseOperations{
	
	static AmazonDynamoDBClient client;
	static DynamoDB dynamo;
	static DynamoDBMapper mapper;
	
	/*public static void main(String args[]) throws Exception{
		DataBaseQueries dbq = new DataBaseQueries();
		DataBaseOperations dbo = new DataBaseOperations();
		ArrayList<PhoneDataDB> result = dbq.queryTable("HT25TW5055273593c875a9898b00");
		ArrayList<PhoneData> pd= dbo.convertFromPhoneDataDB(result);
		}*/
	
	public DataBaseQueries() throws Exception {
		super();
	}
	
	public PhoneDataDB loadFromRawTable(String phone_id, Timestamp ts){
		double tsd = ts.getTime();
		PhoneDataDB result = mapper.load(PhoneDataDB.class, phone_id, tsd);
		
		return result;
	}
	
	public ProcessedDataDB loadFromFilterTable(String phone_id, Timestamp ts){
		double tsd = ts.getTime();
		ProcessedDataDB result = mapper.load(ProcessedDataDB.class, phone_id, tsd);
		
		return result;
	}
	
	
	public ArrayList<PhoneDataDB> queryRawTable(String phone_id){
		PhoneDataDB query = new PhoneDataDB();
		query.setPhoneID(phone_id);
		DynamoDBQueryExpression<PhoneDataDB> DDBE = new DynamoDBQueryExpression<PhoneDataDB>()
				.withHashKeyValues(query);
		List<PhoneDataDB> queryresult = mapper.query(PhoneDataDB.class, DDBE);
		ArrayList<PhoneDataDB> queryresult2 = new ArrayList<PhoneDataDB>(queryresult);
		
		return queryresult2;
		
	}
	
	public ArrayList<ProcessedDataDB> queryFilterTable(String phone_id){
		ProcessedDataDB query = new ProcessedDataDB();
		query.setPhoneID(phone_id);
		DynamoDBQueryExpression<ProcessedDataDB> DDBE = new DynamoDBQueryExpression<ProcessedDataDB>()
				.withHashKeyValues(query);
		List<ProcessedDataDB> queryresult = mapper.query(ProcessedDataDB.class, DDBE);
		ArrayList<ProcessedDataDB> queryresult2 = new ArrayList<ProcessedDataDB>(queryresult);
		
		return queryresult2;
		
	}
	
	public ArrayList<ProcessedDataDB> queryInterpolatedTable(String phone_id){
		ProcessedDataDB query = new ProcessedDataDB();
		query.setPhoneID(phone_id);
		DynamoDBQueryExpression<ProcessedDataDB> DDBE = new DynamoDBQueryExpression<ProcessedDataDB>()
				.withHashKeyValues(query);
		List<ProcessedDataDB> queryresult = mapper.query(ProcessedDataDB.class, DDBE);
		ArrayList<ProcessedDataDB> queryresult2 = new ArrayList<ProcessedDataDB>(queryresult);
		
		return queryresult2;
		
	}
	
	public ArrayList<PhoneDataDB> queryRawTable(String phone_id, int track_no){
		PhoneDataDB query = new PhoneDataDB();
		query.setPhoneID(phone_id);
		Condition rangeKeyCondition = new Condition()
			.withComparisonOperator(ComparisonOperator.EQ.toString())
			.withAttributeValueList(new AttributeValue().withN(Integer.toString(track_no)));
			
		
		
		DynamoDBQueryExpression<PhoneDataDB> DDBE = new DynamoDBQueryExpression<PhoneDataDB>()
				.withHashKeyValues(query)
				.withRangeKeyCondition("Track_no", rangeKeyCondition);
				
		List<PhoneDataDB> queryresult = mapper.query(PhoneDataDB.class, DDBE);
		ArrayList<PhoneDataDB> queryresult2 = new ArrayList<PhoneDataDB>(queryresult);
		
		return queryresult2;
		
	}
	
	public ArrayList<ProcessedDataDB> queryFilterTable(String phone_id, int track_no){
		ProcessedDataDB query = new ProcessedDataDB();
		query.setPhoneID(phone_id);
		Condition rangeKeyCondition = new Condition()
			.withComparisonOperator(ComparisonOperator.EQ.toString())
			.withAttributeValueList(new AttributeValue().withN(Integer.toString(track_no)));
			
		
		
		DynamoDBQueryExpression<ProcessedDataDB> DDBE = new DynamoDBQueryExpression<ProcessedDataDB>()
				.withHashKeyValues(query)
				.withRangeKeyCondition("Track_no", rangeKeyCondition);
				
		List<ProcessedDataDB> queryresult = mapper.query(ProcessedDataDB.class, DDBE);
		ArrayList<ProcessedDataDB> queryresult2 = new ArrayList<ProcessedDataDB>(queryresult);
		
		return queryresult2;
		
	}
	
	


}
