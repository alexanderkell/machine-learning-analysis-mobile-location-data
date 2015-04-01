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

public class DataBaseQueries{
	
	static AmazonDynamoDBClient client;
	static DynamoDB dynamo;
	final static String tableName = DBName.dbname;
	static DynamoDBMapper mapper;
	
	/*public static void main(String args[]) throws Exception{
		DataBaseQueries dbq = new DataBaseQueries();
		DataBaseOperations dbo = new DataBaseOperations();
		ArrayList<PhoneDataDB> result = dbq.queryTable("HT25TW5055273593c875a9898b00");
		ArrayList<PhoneData> pd= dbo.convertFromPhoneDataDB(result);
		}*/
	
	public DataBaseQueries() throws Exception {
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/thomas/.aws/credentials), and is in valid format.",
                    e);
        }
        client = new AmazonDynamoDBClient(credentials);
        //Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        Region eur1 =  Region.getRegion(Regions.EU_WEST_1);
        client.setRegion(eur1);
        dynamo = new DynamoDB(client);
        mapper = new DynamoDBMapper(client);
	}
	
	public PhoneDataDB loadFromTable(String phone_id, Timestamp ts){
		double tsd = ts.getTime();
		PhoneDataDB result = mapper.load(PhoneDataDB.class, phone_id, tsd);
		
		return result;
	}
	
	public ArrayList<PhoneDataDB> queryTable(String phone_id){
		PhoneDataDB query = new PhoneDataDB();
		query.setPhoneID(phone_id);
		DynamoDBQueryExpression<PhoneDataDB> DDBE = new DynamoDBQueryExpression<PhoneDataDB>()
				.withHashKeyValues(query);
		List<PhoneDataDB> queryresult = mapper.query(PhoneDataDB.class, DDBE);
		ArrayList<PhoneDataDB> queryresult2 = new ArrayList<PhoneDataDB>(queryresult);
		
		return queryresult2;
		
	}
	
	public ArrayList<PhoneDataDB> queryTable(String phone_id, int track_no){
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
	


}
