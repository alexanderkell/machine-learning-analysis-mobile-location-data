package dynamodb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import objects.DataBaseObject;
import maths.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
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
	private static String tableName;
	static DynamoDBMapper mapper;
	private DynamoDBMapperConfig DDB_CONFIG;
	
	
	public DataBaseOperations(String tableName) throws Exception {
       
		DataBaseOperations.tableName = tableName;
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
	
	public void createTable() throws InterruptedException{
		try{
			
			if (Tables.doesTableExist(client, tableName)) {
	            System.out.println("Table " + tableName + " is already ACTIVE");
	        } else {
			
			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);
			createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)10).withWriteCapacityUnits((long)100));
			
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
	        writeStats();
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
	
	public void createTracksTable() throws InterruptedException{
		try{
			
			if (Tables.doesTableExist(client, tableName)) {
	            System.out.println("Table " + tableName + " is already ACTIVE");
	        } else {
			
	        	CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);
				createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)10).withWriteCapacityUnits((long)50));
				
				//AttributeDefinitions
				ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
				attributeDefinitions.add(new AttributeDefinition().withAttributeName("ID").withAttributeType("S"));
				attributeDefinitions.add(new AttributeDefinition().withAttributeName("Phone_ID").withAttributeType("S"));
				attributeDefinitions.add(new AttributeDefinition().withAttributeName("Track_no").withAttributeType("N"));

				createTableRequest.setAttributeDefinitions(attributeDefinitions);

				//KeySchema
				ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
				tableKeySchema.add(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH));
				
				createTableRequest.setKeySchema(tableKeySchema);

				ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
				indexKeySchema.add(new KeySchemaElement().withAttributeName("Phone_ID").withKeyType(KeyType.HASH));
				indexKeySchema.add(new KeySchemaElement().withAttributeName("Track_no").withKeyType(KeyType.RANGE));
				
				Projection projection = new Projection().withProjectionType(ProjectionType.ALL);
				
				GlobalSecondaryIndex globalSecondaryIndex = new GlobalSecondaryIndex()
				.withProvisionedThroughput(new ProvisionedThroughput()
		        .withReadCapacityUnits((long) 10)
		        .withWriteCapacityUnits((long) 50))
			    .withIndexName("Phone_Index")
			    .withKeySchema(indexKeySchema)
			    .withProjection(projection);
				
				ArrayList<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<GlobalSecondaryIndex>();
				globalSecondaryIndexes.add(globalSecondaryIndex);
				createTableRequest.setGlobalSecondaryIndexes(globalSecondaryIndexes);
				
				TableDescription  createdTableDescription = client.createTable(createTableRequest).getTableDescription();
				System.out.println(createdTableDescription);
				
				System.out.println("Waiting for " + tableName + " to become ACTIVE...");
		        Tables.awaitTableToBecomeActive(client, tableName);
		        writeStats();
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
	public void deleteTable() {
        Table table = dynamo.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName
                + " to be deleted...this may take a while...");
            table.waitForDelete();
            writeStats();

        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
        
    }
	
	//to add a value or update a value with the same key
	public void saveItem(DataBaseObject pdb) {
		
		mapper.save(pdb, DDB_CONFIG);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        writeStats();
    }
	
	//to add multiple values
	public void batchWrite(ArrayList<? extends DataBaseObject> pdb){
		List<DataBaseObject> myEntitiesToDelete = Collections.<DataBaseObject> emptyList();
		mapper.batchWrite(pdb, myEntitiesToDelete, DDB_CONFIG);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        writeStats();
	}
	
	public void deleteItem(DataBaseObject pdb) {
		
		mapper.delete(pdb, DDB_CONFIG);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        writeStats();
    }
	
	public void batchDelete(ArrayList<? extends DataBaseObject> pdb){
		
		mapper.batchDelete(pdb, DDB_CONFIG);
		
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        writeStats();
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
		writeStats();
	}
	
	private static void writeStats(){
		Timestamp NOW = new Timestamp(System.currentTimeMillis());
		
		Table table = dynamo.getTable("Write_Stats");
		Item item = new Item()
		.withPrimaryKey("Table_Name" , tableName)
		.withNumber("Timestamp", NOW.getTime());
		PutItemOutcome outcome = table.putItem(item);
	}
	
}
