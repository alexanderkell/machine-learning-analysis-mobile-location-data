package dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
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

	static AmazonDynamoDBClient dynamoDB;
	
	
	private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (/Users/thomas/.aws/credentials).
         */
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
        dynamoDB = new AmazonDynamoDBClient(credentials);
        //Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        Region eur1 =  Region.getRegion(Regions.EU_WEST_1);
        dynamoDB.setRegion(eur1);
    }
	
	public static void main(String args[]) throws Exception{
		init();
		String tableName = "3D_Cloud_Pan_Data";
		
		if (Tables.doesTableExist(dynamoDB, tableName)) {
            System.out.println("Table " + tableName + " is already ACTIVE");
        } else {
		
		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);
		createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)5).withWriteCapacityUnits((long)5));
		
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
		
		Projection projection = new Projection().withProjectionType(ProjectionType.KEYS_ONLY);
		
		LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex()
	    .withIndexName("AlbumTitleIndex").withKeySchema(indexKeySchema).withProjection(projection);
		
		ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
		localSecondaryIndexes.add(localSecondaryIndex);
		createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);
		
		TableDescription  createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
		System.out.println(createdTableDescription);

		
        }
		
		
		
		
	}
	
	
}
