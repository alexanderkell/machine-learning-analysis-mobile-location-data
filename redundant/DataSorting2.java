package redundant;

import java.text.ParseException;

import maths.DataFormatOperations;
import maths.DataFormatOperations.PhoneData;

public class DataSorting2 {
	PhoneData[] newdat;
	static int opt = 1;
	static String fn = new String();
	static int length;
	
	public DataSorting2(int opt, String fn) throws ParseException {
				this.opt = opt;
				this.fn = fn;
				//create an instance of the newdatFormatOperations class
				DataFormatOperations DFO = new DataFormatOperations(this.opt, this.fn);
				
				//gets the full matrix
				newdat = DFO.getFullPhoneData();
				length = DFO.getLength();
		
	}
	
	public DataSorting2() throws ParseException{
		this(opt, fn);
		
	}
	
	public PhoneData[] getSort(){
		int i = 0,r = 0,s = 0;
		int track = 1;
		double x;
		
		while(i<length){
			
			x = newdat[i].x;
			if(x>200 && x<850){
				newdat[i].track_no = track;
				
			}
			else if(x>850){
				r=1; s=0;
				newdat[i].track_no = -1;
			}
			else if(x<850 && r==1){
				track++;
				r=0;
				newdat[i].track_no = -1;
			}
			
			
			else if(x<200){
				s=1; r=0;
				newdat[i].track_no = -1;
			}
			else if(x>200 && s==1){
				track++;
				s=0;
				newdat[i].track_no = -1;
			}
			
//			System.out.println("x = " + newdat[i].x + ", ID = " + newdat[i].track_no);	
			i++;
		}
		return newdat;
	}



}

