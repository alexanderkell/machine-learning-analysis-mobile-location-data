package maths;

import java.sql.Timestamp;
import java.util.Date;

public class PhoneData{
	//position x,y,z
	public double x, y, z;
	
	//whole date in Data and String format
	public Date wholedate;
	public String wholedatestring;
	
	//time between current position and the previous position
	public double tb;
	
	//Displacement x,y,z
	public double xdisp, ydisp, zdisp, moddisp;
	
	//relative speeds in x,y,z and modulus direction, and angle
	public double rsx, rsy, rsz, modspd, spdtheta;
	
	//relative accelerations in x,y,z and modulus direction
	public double rax, ray, raz, modacc, acctheta;
	
	public String phone_id;
	
	// Track number
	public int track_no;
	
	// Time stamp
	public Timestamp ts;
	
}

