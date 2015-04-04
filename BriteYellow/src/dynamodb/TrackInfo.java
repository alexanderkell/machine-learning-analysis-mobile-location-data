package dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "REPLACED_BY_VALUE_IN_PROPERTIES_FILE")
public class TrackInfo {
	
	private String PHONE_ID;
	private int TRACK_NO;
	private int PATH_LENGTH;
	private int TIME_STOPPED; 
	private int NO_STOPS; 
	private int TIME_SPENT; 
	private int INACTIVE_TIME; 
	private int STHETACHANGE; 
	private int STHETAIN; 
	private int STHETAOUT; 
	private int STHETAINOUT;
	private int X1;
	private int Y1;
	private int X2;
	private int Y2;
	
	@DynamoDBHashKey
	public String getPHONE_ID() {
		return PHONE_ID;
	}
	public void setPHONE_ID(String pHONE_ID) {
		PHONE_ID = pHONE_ID;
	}
	
	@DynamoDBRangeKey
	public int getTRACK_NO() {
		return TRACK_NO;
	}
	public void setTRACK_NO(int tRACK_NO) {
		TRACK_NO = tRACK_NO;
	}
	
	@DynamoDBAttribute
	public int getX1() {
		return X1;
	}
	public void setX1(int x1) {
		X1 = x1;
	}
	
	@DynamoDBAttribute
	public int getY1() {
		return Y1;
	}
	public void setY1(int y1) {
		Y1 = y1;
	}
	
	@DynamoDBAttribute
	public int getX2() {
		return X2;
	}
	public void setX2(int x2) {
		X2 = x2;
	}
	
	@DynamoDBAttribute
	public int getY2() {
		return Y2;
	}
	public void setY2(int y2) {
		Y2 = y2;
	}
	
	@DynamoDBAttribute
	public int getPATH_LENGTH() {
		return PATH_LENGTH;
	}
	public void setPATH_LENGTH(int pATH_LENGTH) {
		PATH_LENGTH = pATH_LENGTH;
	}
	
	@DynamoDBAttribute
	public int getTIME_STOPPED() {
		return TIME_STOPPED;
	}
	public void setTIME_STOPPED(int tIME_STOPPED) {
		TIME_STOPPED = tIME_STOPPED;
	}
	
	@DynamoDBAttribute
	public int getNO_STOPS() {
		return NO_STOPS;
	}
	public void setNO_STOPS(int nO_STOPS) {
		NO_STOPS = nO_STOPS;
	}
	
	@DynamoDBAttribute
	public int getTIME_SPENT() {
		return TIME_SPENT;
	}
	public void setTIME_SPENT(int tIME_SPENT) {
		TIME_SPENT = tIME_SPENT;
	}
	
	@DynamoDBAttribute
	public int getINACTIVE_TIME() {
		return INACTIVE_TIME;
	}
	public void setINACTIVE_TIME(int iNACTIVE_TIME) {
		INACTIVE_TIME = iNACTIVE_TIME;
	}
	
	@DynamoDBAttribute
	public int getSTHETACHANGE() {
		return STHETACHANGE;
	}
	public void setSTHETACHANGE(int sTHETACHANGE) {
		STHETACHANGE = sTHETACHANGE;
	}
	
	@DynamoDBAttribute
	public int getSTHETAIN() {
		return STHETAIN;
	}
	public void setSTHETAIN(int sTHETAIN) {
		STHETAIN = sTHETAIN;
	}
	
	@DynamoDBAttribute
	public int getSTHETAOUT() {
		return STHETAOUT;
	}
	public void setSTHETAOUT(int sTHETAOUT) {
		STHETAOUT = sTHETAOUT;
	}
	
	@DynamoDBAttribute
	public int getSTHETAINOUT() {
		return STHETAINOUT;
	}
	public void setSTHETAINOUT(int sTHETAINOUT) {
		STHETAINOUT = sTHETAINOUT;
	}
	@Override
	public String toString() {
		return "TrackInfo [PHONE_ID=" + PHONE_ID + ", TRACK_NO=" + TRACK_NO
				+ ", PATH_LENGTH=" + PATH_LENGTH + ", TIME_STOPPED="
				+ TIME_STOPPED + ", NO_STOPS=" + NO_STOPS + ", TIME_SPENT="
				+ TIME_SPENT + ", INACTIVE_TIME=" + INACTIVE_TIME
				+ ", STHETACHANGE=" + STHETACHANGE + ", STHETAIN=" + STHETAIN
				+ ", STHETAOUT=" + STHETAOUT + ", STHETAINOUT=" + STHETAINOUT
				+ ", X1=" + X1 + ", Y1=" + Y1 + ", X2=" + X2 + ", Y2=" + Y2
				+ "]";
	} 
	

}
