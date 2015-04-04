package dynamodb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class DataBaseQueries{
	
	static AmazonDynamoDBClient client;
	static DynamoDB dynamo;
	static DynamoDBMapper mapper;
	private String tableName;
	private DynamoDBMapperConfig DDB_CONFIG;
	
	
	//view main method in comments for details
	public static void main(String args[]) throws Exception{
		DataBaseQueries dbq = new DataBaseQueries("3D_Cloud_Pan_Data");
		
		/*ArrayList<PhoneDataDB> ppp = dbq.scanTable("Track_no", -1, ComparisonOperator.EQ.toString());
		ArrayList<PhoneDataDB> ppp2 = dbq.queryTable("HT25TW5055273593c875a9898b00");
		
		for(int i = 0; i < ppp.size(); i++){
			System.out.println(ppp.get(0).toString());
		}
		
		for(int i = 0; i < ppp2.size(); i++){
			System.out.println(ppp2.get(0).toString());
		}
		
		int max = dbq.findMaxTrackNo("HT25TW5055273593c875a9898b00");
		System.out.println(max);*/
		
		//System.out.println(dbq.readStats());
		
	}
	
	public DataBaseQueries(String tableName) throws Exception {
		this.tableName = tableName;
		DDB_CONFIG = new DynamoDBMapperConfig(new TableNameOverride(tableName));
		
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
	
	/**
	 * loads a single value from the table based on phone id and timestamp
	 * 
	 * 
	 * @param phone_id
	 * @param ts
	 * @return
	 */
	
	
	public PhoneDataDB loadFromTable(String phone_id, Timestamp ts){
		double tsd = ts.getTime();
		PhoneDataDB result = mapper.load(PhoneDataDB.class, phone_id, tsd, DDB_CONFIG);
		
		return result;
	}
	
	/**
	 * 
	 * Queries from table using just the phone id
	 * 
	 * @param phone_id
	 * @return
	 */
	
	public ArrayList<PhoneDataDB> queryTable(String phone_id){
		PhoneDataDB query = new PhoneDataDB();
		query.setPhoneID(phone_id);
		DynamoDBQueryExpression<PhoneDataDB> DDBE = new DynamoDBQueryExpression<PhoneDataDB>()
				.withHashKeyValues(query);
		List<PhoneDataDB> queryresult = mapper.query(PhoneDataDB.class, DDBE, DDB_CONFIG);
		ArrayList<PhoneDataDB> queryresult2 = new ArrayList<PhoneDataDB>(queryresult);
		
		return queryresult2;
		
	}
	/**
	 * Queries from table by the phone_id and track_no
	 * 
	 * 
	 * @param phone_id
	 * @param track_no
	 * @return
	 */
	
	public ArrayList<PhoneDataDB> queryTable(String phone_id, int track_no){
		PhoneDataDB query = new PhoneDataDB();
		query.setPhoneID(phone_id);
		Condition rangeKeyCondition = new Condition()
			.withComparisonOperator(ComparisonOperator.EQ.toString())
			.withAttributeValueList(new AttributeValue().withN(Integer.toString(track_no)));
			
		DynamoDBQueryExpression<PhoneDataDB> DDBE = new DynamoDBQueryExpression<PhoneDataDB>()
				.withHashKeyValues(query)
				.withRangeKeyCondition("Track_no", rangeKeyCondition);
				
		List<PhoneDataDB> queryresult = mapper.query(PhoneDataDB.class, DDBE, DDB_CONFIG);
		ArrayList<PhoneDataDB> queryresult2 = new ArrayList<PhoneDataDB>(queryresult);
		
		return queryresult2;
		
	}
	/**
	 * Scans table for a value of your choice from a chosen column, remember to spell
	 * it exactly how it appears in the table, refer to DataBase for confirmation
	 * 
	 * @param TABLE_ELEMENT
	 * @param VALUE
	 * @param OPERATOR
	 * @return
	 */
	
	
	public ArrayList<PhoneDataDB> scanTable(String TABLE_ELEMENT, double VALUE, String OPERATOR){
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition scanCondition = new Condition()
		.withComparisonOperator(OPERATOR)
		.withAttributeValueList(new AttributeValue().withN(Double.toString(VALUE)));

		scanFilter.put(TABLE_ELEMENT, scanCondition);

		scanExpression.setScanFilter(scanFilter);

		List<PhoneDataDB> scanresult = mapper.scan(PhoneDataDB.class, scanExpression, DDB_CONFIG);
		ArrayList<PhoneDataDB> scanresult2 = new ArrayList<PhoneDataDB>(scanresult);
		return scanresult2;
	}
	
	
	public int findMaxTrackNo(String PHONE_ID){
		int max = 0;
		ArrayList<PhoneDataDB> test = queryTable(PHONE_ID);
		int comp;
		
		for(int i = 0; i < test.size(); i++){
			
			comp = test.get(i).getTrackNo();
			if(comp > max){
				max = comp;
			}
			
		}
		
		return max;
	}
	
	
	public Timestamp readStats(){
		Table table = dynamo.getTable("Write_Stats");
		ItemCollection<QueryOutcome> items = table.query("Table_Name", tableName);
		Iterator<Item> iterator = items.iterator();
		Item thisItem = iterator.next();
		Long timestamplong = thisItem.getLong("Timestamp");	
		Timestamp result = new Timestamp(timestamplong);
		
		return result;
	}
	
	
	/**
	 * A bit unnecessary but just shows the format of the operators e.g. EQ GT LT GE etc... can be used to simplify input 
	 * 
	 * 
	 * @param OP
	 */
	
	
	public String getOperator(String OP){
		OP.toLowerCase();
		String OPERATOR = null;
		
		if(OP.equals("=") || OP.contains("equals")){
			OPERATOR = ComparisonOperator.EQ.toString();
		}
		if(OP.equals("<") || OP.contains("less")){
			OPERATOR = ComparisonOperator.LT.toString();
		}
		if(OP.equals("<=") || (OP.contains("less") && OP.contains("equal"))){
			OPERATOR = ComparisonOperator.LE.toString();
		}
		if(OP.equals(">") || OP.contains("greater")){
			OPERATOR = ComparisonOperator.GT.toString();
		}
		if(OP.equals(">") || (OP.contains("greater") && OP.contains("equal"))){
			OPERATOR = ComparisonOperator.GE.toString();
		}
		
		return OPERATOR;
	}
	


}
