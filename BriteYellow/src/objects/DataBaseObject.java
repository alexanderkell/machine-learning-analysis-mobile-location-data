package objects;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

public abstract class DataBaseObject {

	protected String phone_id;
	protected int track_no;

	public abstract String getPHONE_ID();
	public abstract void setPHONE_ID(String phone_id);
	
	public abstract int getTRACK_NO();
	public abstract void setTRACK_NO(int track_no);
	
	
}
