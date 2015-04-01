package dynamodb;

import java.sql.Timestamp;
import java.util.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


@DynamoDBTable(tableName = DBName.dbname) 
public class PhoneDataDB{
	private double x, y, z;
	private Date wholedate;
	private String wholedatestring;
	private Timestamp ts;
	private double tsd;
	private double tb;
	private double xdisp, ydisp, zdisp, moddisp;
	private double rsx, rsy, rsz, modspd, spdtheta;
	private double rax, ray, raz, modacc, acctheta;
	private String phone_id;
	private int track_no;
	private boolean interpolated = false;
	
	
	@DynamoDBAttribute(attributeName = "X_Position")
	public double getXPosition(){
		return x;
	}
	public void setXPosition(double x){
		this.x = x;
	}
	@DynamoDBAttribute(attributeName = "Y_Position")
	public double getYPosition(){
		return y;
	}
	public void setYPosition(double y){
		this.y = y;
	}
	@DynamoDBAttribute(attributeName = "Z_Position")
	public double getZPosition(){
		return z;
	}
	public void setZPosition(double z){
		this.z = z;
	}
	
	
	@DynamoDBIgnore
	public Date getWholeDate(){
		return wholedate;
	}
	public void setWholeDate(Date wholedate){
		this.wholedate = wholedate;
	}
	@DynamoDBIgnore
	public String getWholeDateString(){
		return wholedatestring;
	}
	public void setWholeDateString(String wholedatestring){
		this.wholedatestring = wholedatestring;
	}
	@DynamoDBIgnore
	public Timestamp getTimestamp(){
		return ts;
	}
	public void setTimestamp(Timestamp ts){
		this.ts = ts;
	}
	@DynamoDBRangeKey(attributeName = "Timestamp")
	public double getTimestampDouble(){
		return tsd;
	}
	public void setTimestampDouble(double tsd){
		this.tsd = tsd;
	}
	
	
	@DynamoDBAttribute(attributeName = "Timebetween")
	public double getTimeBetween(){
		return tb;
	}
	public void setTimeBetween(double tb){
		this.tb = tb;
	}
	
	
	@DynamoDBAttribute(attributeName = "X_Displacement")
	public double getXDisplacement(){
		return xdisp;
	}
	public void setXDisplacement(double xdisp){
		this.xdisp = xdisp;
	}
	@DynamoDBAttribute(attributeName = "Y_Displacement")
	public double getYDisplacement(){
		return ydisp;
	}
	public void setYDisplacement(double ydisp){
		this.ydisp = ydisp;
	}
	@DynamoDBAttribute(attributeName = "Z_Displacement")
	public double getZDisplacement(){
		return zdisp;
	}
	public void setZDisplacement(double zdisp){
		this.zdisp = zdisp;
	}
	@DynamoDBAttribute(attributeName = "Displacement_Modulus")
	public double getModDisplacement(){
		return moddisp;
	}
	public void setModDisplacement(double moddisp){
		this.moddisp = moddisp;
	}
	
	
	@DynamoDBAttribute(attributeName = "X_Speed")
	public double getXSpeed(){
		return rsx;
	}
	public void setXSpeed(double rsx){
		this.rsx = rsx;
	}
	@DynamoDBAttribute(attributeName = "Y_Speed")
	public double getYSpeed(){
		return rsy;
	}
	public void setYSpeed(double rsy){
		this.rsy = rsy;
	}
	@DynamoDBAttribute(attributeName = "Z_Speed")
	public double getZSpeed(){
		return rsz;
	}
	public void setZSpeed(double rsz){
		this.rsz = rsz;
	}
	@DynamoDBAttribute(attributeName = "Speed_Modulus")
	public double getModSpeed(){
		return modspd;
	}
	public void setModSpeed(double modspd){
		this.modspd = modspd;
	}
	@DynamoDBAttribute(attributeName = "Speed_Angle")
	public double getSpeedTheta(){
		return spdtheta;
	}
	public void setSpeedTheta(double spdtheta){
		this.spdtheta = spdtheta;
	}
	
	
	@DynamoDBAttribute(attributeName = "X_Acceleration")
	public double getXAcceleration(){
		return rax;
	}
	public void setXAcceleration(double rax){
		this.rax = rax;
	}
	@DynamoDBAttribute(attributeName = "Y_Acceleration")
	public double getYAcceleration(){
		return ray;
	}
	public void setYAcceleration(double ray){
		this.ray = ray;
	}
	@DynamoDBAttribute(attributeName = "Z_Acceleration")
	public double getZAcceleration(){
		return raz;
	}
	public void setZAcceleration(double raz){
		this.raz = raz;
	}
	@DynamoDBAttribute(attributeName = "Acceleration_Modulus")
	public double getModAcceleration(){
		return modacc;
	}
	public void setModAcceleration(double modacc){
		this.modacc = modacc;
	}
	@DynamoDBAttribute(attributeName = "Speed_Angle")
	public double getAccelerationTheta(){
		return acctheta;
	}
	public void setAccelerationTheta(double acctheta){
		this.acctheta = acctheta;
	}
	
	
	 @DynamoDBHashKey(attributeName="Phone_ID")
	public String getPhoneID(){
		return phone_id;
	}
	public void setPhoneID(String phone_id){
		this.phone_id = phone_id;
	}
	
	
	@DynamoDBAttribute(attributeName = "Track_no")
	public int getTrackNo(){
		return track_no;
	}
	public void setTrackNo(int track_no){
		this.track_no = track_no;
	}
	
	
	@DynamoDBIgnore
	public boolean getInterpolated(){
		return interpolated;
	}
	public void setInterpolated(boolean interpolated){
		this.interpolated = interpolated;
	}
	
	
}