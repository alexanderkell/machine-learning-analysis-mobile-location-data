package objects;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "REPLACED_BY_VALUE_IN_PROPERTIES_FILE")
public class TrackInfo extends DataBaseObject {
	
	private double PATH_LENGTH;
	private double TIME_STOPPED; 
	private double NO_STOPS; 
	private double TIME_SPENT; 
	private double INACTIVE_TIME; 
	private double STHETACHANGE; 
	private double STHETAIN; 
	private double STHETAOUT; 
	private double STHETAINOUT;
	private double TIMEPERSTOP;
	private double TOTAVRGSPEED;
	private double TIMESSTOPPEDHERE;
	
	
	private double X1;
	private double Y1;
	private double X2;
	private double Y2;
	
	@DynamoDBHashKey(attributeName="PHONE_ID")
	public String getPHONE_ID() {
		return PHONE_ID;
	}
	public void setPHONE_ID(String phone_id){
		this.PHONE_ID = phone_id;
	}
	
	@DynamoDBAttribute
	public double getTIMESSTOPPEDHERE() {
		return TIMESSTOPPEDHERE;
	}
	public void setTIMESSTOPPEDHERE(double tIMESSTOPPEDHERE){
		TIMESSTOPPEDHERE = tIMESSTOPPEDHERE;
	}
	
	
	@DynamoDBAttribute
	public double getTOTAVRGSPEED() {
		return TOTAVRGSPEED;
	}	
	public void setTOTAVRGSPEED(double tOTAVRGSPEED){
		TOTAVRGSPEED = tOTAVRGSPEED;
	}
	
	
	@DynamoDBAttribute
	public double getTIMEPERSTOP() {
		return TIMEPERSTOP;
	}
	public void setTIMEPERSTOP(double tIMEPERSTOP){
		TIMEPERSTOP = tIMEPERSTOP;
	}
	
	
	@DynamoDBRangeKey(attributeName = "TRACK_NO")
	public int getTRACK_NO() {
		return TRACK_NO;
	}
	public void setTRACK_NO(int tRACK_NO) {
		TRACK_NO = tRACK_NO;
	}
	
	@DynamoDBAttribute
	public double getX1() {
		return X1;
	}
	public void setX1(double x1) {
		X1 = x1;
	}
	
	@DynamoDBAttribute
	public double getY1() {
		return Y1;
	}
	public void setY1(double y1) {
		Y1 = y1;
	}
	
	@DynamoDBAttribute
	public double getX2() {
		return X2;
	}
	public void setX2(double x2) {
		X2 = x2;
	}
	
	@DynamoDBAttribute
	public double getY2() {
		return Y2;
	}
	public void setY2(double y2) {
		Y2 = y2;
	}
	
	@DynamoDBAttribute
	public double getPATH_LENGTH() {
		return PATH_LENGTH;
	}
	public void setPATH_LENGTH(double pATH_LENGTH) {
		PATH_LENGTH = pATH_LENGTH;
	}
	
	@DynamoDBAttribute
	public double getTIME_STOPPED() {
		return TIME_STOPPED;
	}
	public void setTIME_STOPPED(double tIME_STOPPED) {
		TIME_STOPPED = tIME_STOPPED;
	}
	
	@DynamoDBAttribute
	public double getNO_STOPS() {
		return NO_STOPS;
	}
	public void setNO_STOPS(double nO_STOPS) {
		NO_STOPS = nO_STOPS;
	}
	
	@DynamoDBAttribute
	public double getTIME_SPENT() {
		return TIME_SPENT;
	}
	public void setTIME_SPENT(double tIME_SPENT) {
		TIME_SPENT = tIME_SPENT;
	}
	
	@DynamoDBAttribute
	public double getINACTIVE_TIME() {
		return INACTIVE_TIME;
	}
	public void setINACTIVE_TIME(double iNACTIVE_TIME) {
		INACTIVE_TIME = iNACTIVE_TIME;
	}
	
	@DynamoDBAttribute
	public double getSTHETACHANGE() {
		return STHETACHANGE;
	}
	public void setSTHETACHANGE(double sTHETACHANGE) {
		STHETACHANGE = sTHETACHANGE;
	}
	
	@DynamoDBAttribute
	public double getSTHETAIN() {
		return STHETAIN;
	}
	public void setSTHETAIN(double sTHETAIN) {
		STHETAIN = sTHETAIN;
	}
	
	@DynamoDBAttribute
	public double getSTHETAOUT() {
		return STHETAOUT;
	}
	public void setSTHETAOUT(double sTHETAOUT) {
		STHETAOUT = sTHETAOUT;
	}
	
	@DynamoDBAttribute
	public double getSTHETAINOUT() {
		return STHETAINOUT;
	}
	public void setSTHETAINOUT(double sTHETAINOUT) {
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
