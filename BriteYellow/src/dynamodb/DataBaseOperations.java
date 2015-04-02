package dynamodb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import maths.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;


public class DataBaseOperations {

	static AmazonDynamoDBClient client;
	static DynamoDB dynamo;
	static String tableName;
	static DynamoDBMapper mapper;
	
	/*public static void main(String args[]) throws Exception{
		
		DataBaseOperations DBO = new DataBaseOperations();
		DBO.deleteTable(tableName);
		DBO.createTable(tableName);
		DataGetter DG = new DataGetter(7, "24th Sept ORDERED.CSV");
		DataGetter DG2 = new DataGetter(7, "26th Sept ORDERED.CSV");
		ArrayList<PhoneData> pd24 = DG.getFullPhoneDataList();
		ArrayList<PhoneData> pd26 = DG2.getFullPhoneDataList();
		
		ArrayList<PhoneDataDB> pddb24 = DBO.convertToPhoneDataDB(pd24);
		ArrayList<PhoneDataDB> pddb26 = DBO.convertToPhoneDataDB(pd26);
		System.out.println("Writing 24th Values...");
		DBO.batchSave(pddb24);
		System.out.println("Done 24th");
		System.out.println("Writing 26th Values...");
		DBO.batchSave(pddb26);
		System.out.println("Done 26th");
		//DBO.updateThroughput(tableName,25L,1L);
	}*/
	
	public void makeProcessedTable(PhoneData[] pd) throws InterruptedException{
		createTable("Processed_Data");
		ArrayList<ProcessedDataDB> prddb = convertToProcessedDataDB(pd);
		batchSaveProcessed(prddb);
		
		
	}
	
	public DataBaseOperations() throws Exception {
        
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
	
	public void createTable(String tableName) throws InterruptedException{
		try{
			
			if (Tables.doesTableExist(client, tableName)) {
	            System.out.println("Table " + tableName + " is already ACTIVE");
	        } else {
			
			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);
			createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)25).withWriteCapacityUnits((long)25));
			
			//AttributeDefinitions
			ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("Phone_ID").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("Timestamp").withAttributeType("N"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("Track_no").withAttributeType("N"));

			createTableRequest.setAttributeDefinitions(attributeDefinitions);

			//KeySchema
			ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
			tableKeySchema.add(new KeySchemaElement().withAttributeName("Phone_ID").withKeyType(KeyType.HASH));
			tableKeySchema.add(new KeySchemaElement().withAttributeName("Timestamp").withKeyType(KeyType.RANGE));
			
			createTableRequest.setKeySchema(tableKeySchema);

			ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
			indexKeySchema.add(new KeySchemaElement().withAttributeName("Phone_ID").withKeyType(KeyType.HASH));
			indexKeySchema.add(new KeySchemaElement().withAttributeName("Track_no").withKeyType(KeyType.RANGE));
			
			Projection projection = new Projection().withProjectionType(ProjectionType.ALL);
			
			LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex()
		    .withIndexName("Track_no").withKeySchema(indexKeySchema).withProjection(projection);
			
			ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
			localSecondaryIndexes.add(localSecondaryIndex);
			createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);
			
			TableDescription  createdTableDescription = client.createTable(createTableRequest).getTableDescription();
			System.out.println(createdTableDescription);
			
			System.out.println("Waiting for " + tableName + " to become ACTIVE...");
	        Tables.awaitTableToBecomeActive(client, tableName);
	        }
				
		} catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
		
	}
	
	//deletes a table with a given name
	public void deleteTable(String tableName) {
        Table table = dynamo.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName
                + " to be deleted...this may take a while...");
            table.waitForDelete();

        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
	
	//to add a value or update a value with the same key
	public void saveItem(PhoneDataDB pdb) {
		
		mapper.save(pdb);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(pdb.tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
		
    }
	
	//to add or multiple values
	public void batchSaveRaw(ArrayList<PhoneDataDB> pdb){
		
		mapper.batchSave(pdb);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(pdb.get(0).tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
		
	}
	
	public void deleteItem(PhoneDataDB pdb) {
		
		mapper.delete(pdb);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(pdb.tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
		
    }
	
	public void batchDeleteRaw(ArrayList<PhoneDataDB> pdb){
		
		mapper.batchDelete(pdb);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
		
	}
	
	//to add a value or update a value with the same key
		public void saveItem(ProcessedDataDB pdb) {
			
			mapper.save(pdb);
			
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(pdb.tableName);
	        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
	        System.out.println("Table Description: " + tableDescription);
			
	    }
		
		//to add or multiple values
		public void batchSaveProcessed(ArrayList<ProcessedDataDB> pdb){
			
			mapper.batchSave(pdb);
			
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(pdb.get(0).tableName);
	        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
	        System.out.println("Table Description: " + tableDescription);
			
		}
		
		public void deleteItem(ProcessedDataDB pdb) {
			
			mapper.delete(pdb);
			
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(pdb.tableName);
	        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
	        System.out.println("Table Description: " + tableDescription);
			
	    }
		
		public void batchDeleteProcessed(ArrayList<ProcessedDataDB> pdb){
			
			mapper.batchDelete(pdb);
			
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
	        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
	        System.out.println("Table Description: " + tableDescription);
			
		}
	
	
	
	//converts an Arraylist of PhoneData objects to PhoneDataDB objects
	public ArrayList<PhoneDataDB> convertToPhoneDataDB(ArrayList<PhoneData> pd){
		ArrayList<PhoneDataDB> whole = new ArrayList<PhoneDataDB>();
		PhoneDataDB datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneDataDB();
			datapoint = convertToPhoneDataDB(pd.get(i));
			
			whole.add(datapoint);
			
		}
		
		return whole;
	}
	
	public ArrayList<PhoneDataDB> convertToPhoneDataDB(PhoneData[] pda){
		ArrayList<PhoneData> pd = new ArrayList<PhoneData>(Arrays.asList(pda));
		ArrayList<PhoneDataDB> whole = new ArrayList<PhoneDataDB>();
		PhoneDataDB datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneDataDB();
			datapoint = convertToPhoneDataDB(pd.get(i));
			
			whole.add(datapoint);
			
			
		}
		
		return whole;
	}
	
	public PhoneDataDB convertToPhoneDataDB(PhoneData pd){
		
		PhoneDataDB datapoint;
		
		datapoint = new PhoneDataDB();
		datapoint.setXPosition(pd.x);
		datapoint.setYPosition(pd.y);
		datapoint.setZPosition(pd.z);
		datapoint.setWholeDate(pd.wholedate);
		datapoint.setWholeDateString(pd.wholedatestring);
		datapoint.setTimestamp(pd.ts);
		datapoint.setTimestampDouble(pd.ts.getTime());
		datapoint.setTimeBetween(pd.tb);
		datapoint.setXDisplacement(pd.xdisp);
		datapoint.setYDisplacement(pd.ydisp);
		datapoint.setZDisplacement(pd.zdisp);
		datapoint.setModDisplacement(pd.moddisp);
		datapoint.setXSpeed(pd.rsx);
		datapoint.setYSpeed(pd.rsy);
		datapoint.setZSpeed(pd.rsz);
		datapoint.setModSpeed(pd.modspd);
		datapoint.setSpeedTheta(pd.spdtheta);
		datapoint.setXAcceleration(pd.rax);
		datapoint.setYAcceleration(pd.ray);
		datapoint.setZAcceleration(pd.raz);
		datapoint.setModAcceleration(pd.modacc);
		datapoint.setAccelerationTheta(pd.acctheta);
		datapoint.setPhoneID(pd.phone_id);
		datapoint.setTrackNo(pd.track_no);
		
		return datapoint;
	}
	
	
	//converts an Arraylist of PhoneData objects to ProcessedDataDB objects
		public ArrayList<ProcessedDataDB> convertToProcessedDataDB(ArrayList<PhoneData> pd){
			ArrayList<ProcessedDataDB> whole = new ArrayList<ProcessedDataDB>();
			ProcessedDataDB datapoint;
			
			for(int i = 0; i< pd.size(); i++){
				datapoint = new ProcessedDataDB();
				datapoint = convertToProcessedDataDB(pd.get(i));
				
				whole.add(datapoint);
				
			}
			
			return whole;
		}
		
		public ArrayList<ProcessedDataDB> convertToProcessedDataDB(PhoneData[] pda){
			ArrayList<PhoneData> pd = new ArrayList<PhoneData>(Arrays.asList(pda));
			ArrayList<ProcessedDataDB> whole = new ArrayList<ProcessedDataDB>();
			ProcessedDataDB datapoint;
			
			for(int i = 0; i< pd.size(); i++){
				datapoint = new ProcessedDataDB();
				datapoint = convertToProcessedDataDB(pd.get(i));
				
				whole.add(datapoint);
				
				
			}
			
			return whole;
		}
		
		public ProcessedDataDB convertToProcessedDataDB(PhoneData pd){
			
			ProcessedDataDB datapoint;
			
			datapoint = new ProcessedDataDB();
			datapoint.setXPosition(pd.x);
			datapoint.setYPosition(pd.y);
			datapoint.setZPosition(pd.z);
			datapoint.setWholeDate(pd.wholedate);
			datapoint.setWholeDateString(pd.wholedatestring);
			datapoint.setTimestamp(pd.ts);
			datapoint.setTimestampDouble(pd.ts.getTime());
			datapoint.setTimeBetween(pd.tb);
			datapoint.setXDisplacement(pd.xdisp);
			datapoint.setYDisplacement(pd.ydisp);
			datapoint.setZDisplacement(pd.zdisp);
			datapoint.setModDisplacement(pd.moddisp);
			datapoint.setXSpeed(pd.rsx);
			datapoint.setYSpeed(pd.rsy);
			datapoint.setZSpeed(pd.rsz);
			datapoint.setModSpeed(pd.modspd);
			datapoint.setSpeedTheta(pd.spdtheta);
			datapoint.setXAcceleration(pd.rax);
			datapoint.setYAcceleration(pd.ray);
			datapoint.setZAcceleration(pd.raz);
			datapoint.setModAcceleration(pd.modacc);
			datapoint.setAccelerationTheta(pd.acctheta);
			datapoint.setPhoneID(pd.phone_id);
			datapoint.setTrackNo(pd.track_no);
			
			return datapoint;
		}
	
	
	public ArrayList<PhoneData> convertFromPhoneData(ArrayList<PhoneDataDB> pd){
		ArrayList<PhoneData> whole = new ArrayList<PhoneData>();
		PhoneData datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneData();
			datapoint = convertFrom(pd.get(i));
			whole.add(datapoint);
			
		}
		
		return whole;
	}
	
	public ArrayList<PhoneData> convertFromProcessedData(ArrayList<ProcessedDataDB> pd){
		ArrayList<PhoneData> whole = new ArrayList<PhoneData>();
		PhoneData datapoint;
		
		for(int i = 0; i< pd.size(); i++){
			datapoint = new PhoneData();
			datapoint = convertFrom(pd.get(i));
			whole.add(datapoint);
			
		}
		
		return whole;
	}
	
	
	public PhoneData convertFrom(PhoneDataDB pd){
	
		PhoneData datapoint;
		
		datapoint = new PhoneData();
		datapoint.x = pd.getXPosition();
		datapoint.y = pd.getYPosition();
		datapoint.z = pd.getZPosition();
		datapoint.wholedate = pd.getWholeDate();
		datapoint.wholedatestring = pd.getWholeDateString();
		datapoint.ts = pd.getTimestamp();
		datapoint.tb = pd.getTimeBetween();
		datapoint.xdisp = pd.getXDisplacement();
		datapoint.ydisp = pd.getYDisplacement();
		datapoint.zdisp = pd.getZDisplacement();
		datapoint.moddisp = pd.getModDisplacement();
		datapoint.rsx = pd.getXSpeed();
		datapoint.rsy = pd.getYSpeed();
		datapoint.rsz = pd.getZSpeed();
		datapoint.modspd = pd.getModSpeed();
		datapoint.spdtheta = pd.getSpeedTheta();
		datapoint.rax = pd.getXAcceleration();
		datapoint.ray = pd.getYAcceleration();
		datapoint.raz = pd.getZAcceleration();
		datapoint.modacc = pd.getModAcceleration();
		datapoint.acctheta = pd.getAccelerationTheta();
		datapoint.phone_id = pd.getPhoneID();
		datapoint.track_no = pd.getTrackNo();
	
		
		return datapoint;
	}
	
	public PhoneData convertFrom(ProcessedDataDB pd){
		
		PhoneData datapoint;
		
		datapoint = new PhoneData();
		datapoint.x = pd.getXPosition();
		datapoint.y = pd.getYPosition();
		datapoint.z = pd.getZPosition();
		datapoint.wholedate = pd.getWholeDate();
		datapoint.wholedatestring = pd.getWholeDateString();
		datapoint.ts = pd.getTimestamp();
		datapoint.tb = pd.getTimeBetween();
		datapoint.xdisp = pd.getXDisplacement();
		datapoint.ydisp = pd.getYDisplacement();
		datapoint.zdisp = pd.getZDisplacement();
		datapoint.moddisp = pd.getModDisplacement();
		datapoint.rsx = pd.getXSpeed();
		datapoint.rsy = pd.getYSpeed();
		datapoint.rsz = pd.getZSpeed();
		datapoint.modspd = pd.getModSpeed();
		datapoint.spdtheta = pd.getSpeedTheta();
		datapoint.rax = pd.getXAcceleration();
		datapoint.ray = pd.getYAcceleration();
		datapoint.raz = pd.getZAcceleration();
		datapoint.modacc = pd.getModAcceleration();
		datapoint.acctheta = pd.getAccelerationTheta();
		datapoint.phone_id = pd.getPhoneID();
		datapoint.track_no = pd.getTrackNo();
	
		
		return datapoint;
	}
	
	
	public void updateThroughput(String tableName, long read, long write) throws InterruptedException{
		Table table = dynamo.getTable(tableName);

		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
		    .withReadCapacityUnits(read)
		    .withWriteCapacityUnits(write);

		table.updateTable(provisionedThroughput);
		System.out.println("Waiting for Update to Complete...");
		table.waitForActive();
		System.out.println("Finished Update");
	}
	
}
