package maths;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class PhoneData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3688773816761833405L;

	
	//position x,y,z
	public double x, y, z;
	
	//whole date in date, string, and timestamp format
	public Date wholedate;
	public String wholedatestring;
	public Timestamp ts;
	
	//time between current position and the previous position
	public double tb;
	
	//Displacement x,y,z
	public double xdisp, ydisp, zdisp, moddisp;
	
	//relative speeds in x,y,z and modulus direction, and angle
	public double rsx, rsy, rsz, modspd, spdtheta;
	
	//relative accelerations in x,y,z and modulus direction
	public double rax, ray, raz, modacc, acctheta;
	
	//Phone id
	public String phone_id;
	
	// Track number
	public int track_no;
	
	// Show if the point is added during interpolation
	public boolean interpolated = false;

	public boolean filtered = false;
}

