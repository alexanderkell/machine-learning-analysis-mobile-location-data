package redundant;

public class CodeDumpBU {
	
	
	
	
	
/*	public void getSort(){

	
	public static double getAttributeDouble(PhoneData p, int attribute){
		if(attribute == X)
			return p.x;
		if(attribute == Y)
			return p.y;
		if(attribute == Z)
			return p.z;

		if(attribute == TimeBetween)
			return p.tb;
		if(attribute == XSpeed)
			return p.rsx;		
		if(attribute == YSpeed)
				return p.rsy;
		if(attribute == ZSpeed)
			return p.rsz;
		if(attribute == MSpeed)
			return p.modspd;
		if(attribute == STheta)
			return p.spdtheta;
		if(attribute == XAcc)
			return p.rax;		
		if(attribute == YAcc)
			return p.ray;
		if(attribute == ZAcc)
			return p.raz;
		if(attribute == MAcc)
			return p.modacc;
		if(attribute == ATheta)
			return p.acctheta;
		throw new IllegalArgumentException(
			"You might have passed the wrong argument or you have used the wrong method to get attributes"
			);
	}

	public static int getAttributeInt(PhoneData p, int attribute){
		if(attribute == Track)
			return p.track_no;
		throw new IllegalArgumentException(
				"You might have passed the wrong argument or you have used the wrong method to get attributes"
				);
	}
	public static Date getDate(PhoneData p){
		return p.wholedate;
	}
	
	
	public static String getAttributeString(PhoneData p, int attribute){
		if(attribute == WholeDate)
			return p.wholedatestring;
		try{
			return String.valueOf(getAttributeDouble(p, attribute));
		} catch (IllegalArgumentException e){
			try{
				return String.valueOf(getAttributeInt(p, attribute));
			} catch (IllegalArgumentException f){
				throw new IllegalArgumentException("You might have passed the wrong argument");
			}
		}
	}
	
	
	public PhoneData[] getSort2(){
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		while(i<length){
			
			x = cdcalc2[i].x;
			if(x>200 && x<850){
				setTrackNo(i,track);
				cdcalc2[i].track_no = track;
			}
			else if(x>850){r=1;s=0;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
			}
			
			
			else if(x<200){s=1;r=0;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
			}
			
			i++;
		}
		return cdcalc2;
	}
	
	public String[][] getSort(){
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		String[][] newdat = cdcalc;
		
		while(i<length){
			
			x = Double.parseDouble(newdat[0][i]);
			if(x>200 && x<850){
				newdat[16][i] = String.valueOf(track);
				cdcalc2[i].track_no = track;
			}
			else if(x>850){r=1;s=0;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
			}
			
			
			else if(x<200){s=1;r=0;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
			}
			
<<<<<<< HEAD
			//System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
		}
		cdcalc = newdat;
=======
//			System.out.println("x = " + newdat[0][i] + ", ID = " + newdat[16][i]);	
			i++;
		}
		return newdat;
	}

	
	public class PhoneData{
		//position x,y,z
		public double x, y, z;
		
		//whole date in Data and String format
		public Date wholedate;
		public String wholedatestring;
		
		//time between current position and the previous position
		public double tb;
		
		//relative speeds in x,y,z and modulus direction, and angle
		public double rsx, rsy, rsz, modspd, spdtheta;
		
		//relative accelerations in x,y,z and modulus direction
		public double rax, ray, raz, modacc, acctheta;
		
		public String phone_id;
		
		public int track_no;
	}
	
/*	public static void main(String args[]) throws ParseException, IOException{
		DataFormatOperations dfo = new DataFormatOperations(1, "C:\\Users\\testuser\\SkyDrive\\Documents\\4th year project files\\repos\\4th-year-project\\BriteYellow\\src\\24th Sept ORDERED.csv");
		for (int i = 1; i<=5; i++){
			dfo.changePhone(i);
			dfo.writeToFile();
		}
>>>>>>> 533458e48a93e6fbdf62fbca99f73191cd9e2451
	}
	*/
}
