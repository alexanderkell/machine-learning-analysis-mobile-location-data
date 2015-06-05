package objects;

import objects.TrackInfo;

public class SmartTrackInfo{

	public static final int ID = 0;
	public static final int PATH_LENGTH = 1;
	public static final int TIME_STOPPED = 2; 
	public static final int NO_STOPS = 3; 
	public static final int TIME_SPENT = 4; 
	public static final int INACTIVE_TIME = 5; 
	public static final int STHETACHANGE = 6; 
	public static final int STHETAIN = 7; 
	public static final int STHETAOUT = 8; 
	public static final int STHETAINOUT = 9;
	public static final int TIMEPERSTOP = 10;
	public static final int TOTAVRGSPEED = 11;
	public static final int TIMESSTOPPEDHERE = 12;
	public static final int X1 = 13;
	public static final int Y1 = 14;
	public static final int X2 = 15;
	public static final int Y2 = 16;
	public static final int Characteristic = 17;
	private TrackInfo ti;
	
	/** Constructor
	 * 
	 * @param ti: The TrackInfo object
	 */
	public SmartTrackInfo(TrackInfo ti){
		this.ti = ti;
	}
	/** Get an attribute of type String
	 * 
	 * @param property must be SmartTrackInfo.ID or an exception will be thrown
	 * @return
	 * @throws IllegalArgumentException if the attribute name is not found
	 */
	public String getString(final int property){
		if(property == ID)
			return ti.getId();
		throw new IllegalArgumentException("Oops! Wrong Arguments at getString(??) method!");

	}
	/** Set an attribute of type String
	 * 
	 * @param property must be SmartTrackInfo.ID or an exception will be thrown
	 * @param value
	 * @throws IllegalArgumentException if the attribute name is not found
	 */
	public void setString(final int property, String value){
		if(property == ID)
			ti.setId(value);
		else 
			throw new IllegalArgumentException("Oops! Wrong Arguments at setString(??) method!");
	}
	/** Get an attribute of type int
	 * 
	 * @param property must be SmartTrackInfo.Charateristics or an exception will be thrown
	 * @return
	 * @throws IllegalArgumentException if the attribute name is not found
	 */
	public int getInt(final int property){
		if(property == Characteristic)
			return ti.getCharacteristic();
		throw new IllegalArgumentException("Oops! Wrong Arguments at getInt(??) method!");
	}
	/** Set an attribute of type int
	 * 
	 * @param property must be SmartTrackInfo.Charateristics or an exception will be thrown
	 * @param value
	 * @throws IllegalArgumentException if the attribute name is not found
	 */
	public void setInt(final int property, int value){
		if(property == Characteristic)
			ti.setCharacteristic(value);
		else 
			throw new IllegalArgumentException("Oops! Wrong Arguments at setInt(??) method!");
	}
	/**Get an attribute of type double
	 * 
	 * @param property: the attribute name
	 * @return
	 * @throws IllegalArgumentException if the attribute name is not found
	 */
	public double getDouble(final int property){	
		if(property == PATH_LENGTH)
			return ti.getPATH_LENGTH();
		if(property == TIME_STOPPED)
			return ti.getTIME_STOPPED();
		if(property == NO_STOPS)
			return ti.getNO_STOPS();
		if(property == TIME_SPENT) 
			return ti.getTIME_SPENT();
		if(property == INACTIVE_TIME)
			return ti.getINACTIVE_TIME();
		if(property == STHETACHANGE)
			return ti.getSTHETACHANGE();
		if(property == STHETAIN)
			return ti.getSTHETAIN();
		if(property == STHETAOUT)
			return ti.getSTHETAOUT();
		if(property == STHETAINOUT)
			return ti.getSTHETAINOUT();
		if(property == TIMEPERSTOP)
			return ti.getTIMEPERSTOP();
		if(property == TOTAVRGSPEED)
			return ti.getTOTAVRGSPEED();
		if(property == TIMESSTOPPEDHERE)
			return ti.getTIMESSTOPPEDHERE();
		if(property == X1)
			return ti.getX1();
		if(property == Y1)
			return ti.getY1();
		if(property == X2)
			return ti.getX2();
		if(property == Y2)
			return ti.getY2();
		if(property == Characteristic)
			return ti.getCharacteristic();
		throw new IllegalArgumentException("Oops! Wrong Arguments at getDouble(??) method!");
	}
	/**Set an attribute of type double
	 * 
	 * @param property: the attribute name
	 * @param value
	 * @throws IllegalArgumentException if the attribute name is not found
	 */
	public void setDouble(int property, double value){
		if(property == PATH_LENGTH)
			ti.setPATH_LENGTH(value);
		else if(property == TIME_STOPPED)
			ti.setTIME_STOPPED(value);
		else if(property == NO_STOPS)
			ti.setNO_STOPS(value);
		else if(property == TIME_SPENT) 
			ti.setTIME_SPENT(value);
		else if(property == INACTIVE_TIME)
			ti.setINACTIVE_TIME(value);
		else if(property == STHETACHANGE)
			ti.setSTHETACHANGE(value);
		else if(property == STHETAIN)
			ti.setSTHETAIN(value);
		else if(property == STHETAOUT)
			ti.setSTHETAOUT(value);
		else if(property == STHETAINOUT)
			ti.setSTHETAINOUT(value);
		else if(property == TIMEPERSTOP)
			ti.setTIMEPERSTOP(value);
		else if(property == TOTAVRGSPEED)
			ti.setTOTAVRGSPEED(value);
		else if(property == TIMESSTOPPEDHERE)
			ti.setTIMESSTOPPEDHERE(value);
		else if(property == X1)
			ti.setX1(value);
		else if(property == Y1)
			ti.setY1(value);
		else if(property == X2)
			ti.setX2(value);
		else if(property == Y2)
			ti.setY2(value);
		else 
			throw new IllegalArgumentException("Oops! Wrong Arguments at setDouble(??) method!");
	}

}
