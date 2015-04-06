package dynamodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import maths.PhoneData;

public class NoSQLDownload{
	public interface SQLListener{
		public abstract void statusUpdated(int step, String msg);
		public abstract void stepProgressUpdated(int step, int percent);
		public abstract void finish(int exit, String msg);
	}

	private String tablename;
	public NoSQLDownload(String tablename){
		this.tablename = tablename;
	}
	public static void serialiseATrack(final String phoneid, final int track, final PhoneData[] data, final String date){
		// Folder which stores tracks
		String folder = "Tracks/"+phoneid+"/";
		String file = phoneid+" track "+track+".track";
		
		try {
			FileOutputStream fout = new FileOutputStream(folder+file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(data);
			oos.writeUTF(date);
			oos.writeUTF(phoneid);
			oos.writeInt(track);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private DataBaseQueries dbq;
	
	private String date;
	private SQLListener ptm;
	private int totaltracks = -1;

	public void serialiseProperies(final String phoneid, final String date, final int totaltrack){
		// Folder which stores tracks
		String folder = "Tracks/"+phoneid+"/";
		String filename = folder+phoneid+".properties";
		
		File ffolder = new File(folder);
		if(! ffolder.exists())
			ffolder.mkdirs();
		try{

			FileOutputStream fout = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeUTF(date);
			oos.writeUTF(phoneid);
			oos.writeInt(totaltrack);
			oos.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public int checkProperties(final String phoneid, final String date) throws ClassNotFoundException, IOException{
		String filename = "Tracks/"+phoneid+"/"+phoneid+".properties";
		if(!new File(filename).exists()){
			return -1;
		}
		
		FileInputStream fout = new FileInputStream(filename);
		ObjectInputStream oos = new ObjectInputStream(fout);

		boolean valid = false;
		int totaltracks = -1;
		try{
//			JOptionPane.showMessageDialog(null, oos.readUTF());
			
			String str_date = oos.readUTF();
			valid = date==null ? true : date.equals(str_date);
			if(valid){
				if(phoneid.equals(oos.readUTF())){
					totaltracks = oos.readInt();
					this.date = date;
				}
			}
		} catch (IOException e){
		} catch (Exception e){
			
		}
		  finally{
		
			oos.close();
		}

		if(valid)
			return totaltracks;
		else
			return -1;

	}
	
	
	public static PhoneData[] deserialise(final String phoneid, final int track) throws ClassNotFoundException, IOException{
		
		String filename = "Tracks/"+phoneid+"/"+phoneid+" track "+track+".track";
		
		FileInputStream fout = new FileInputStream(filename);
		ObjectInputStream oos = new ObjectInputStream(fout);
		PhoneData[] pd = (PhoneData[]) oos.readObject();
		oos.close();
		return pd;
	}
	public boolean downloadAndSerialise(String phoneid, boolean offline) throws Exception, ClassNotFoundException, IOException{
		
		try{
			if(!offline)
				connect();
			
				
			if(date == null)
				throw new NullPointerException();
//			System.out.println("Total tracks: "+dbq.findMaxTrackNo(phoneid));
			totaltracks = checkProperties(phoneid, date);
			if(totaltracks == -1){
				totaltracks = dbq.findMaxTrackNo(phoneid);
//				totaltracks = 100;
				System.out.println(totaltracks);
				serialiseProperies(phoneid, date, totaltracks);
			}

			if(ptm!=null){
				ptm.statusUpdated(3, "Updating local copies of the tracks  for phone "+phoneid+ "\n(Database last updated ("+date+"))...");
				ptm.stepProgressUpdated(3, 0);
			}
				
			for(int i = 1; i <= totaltracks; i++){

				if(!NoSQLDownload.checkATrack(phoneid, i, date)){
					ArrayList<PhoneData> filtered = query(phoneid,i);
					PhoneData[] filteredarray = filtered.toArray(new PhoneData[filtered.size()]);
					serialiseATrack(phoneid, i, filteredarray, date);	
					if(ptm!=null){
						ptm.statusUpdated(3, "Updating local copies of the tracks for phone "+phoneid+ " ("+(i)+"/"+totaltracks+") \n(Database version 10 ("+date+"))");
						ptm.stepProgressUpdated(3, 100*(i)/totaltracks);
					}
				}
	
			}
			
			serialiseProperies(phoneid, date, totaltracks);
			return true;
		} catch (NullPointerException e){
			totaltracks = checkProperties(phoneid, date);
			if(totaltracks == -1){
				int i = 0;
				while(checkATrack(phoneid, ++i, date)){
					totaltracks = i;
				}
				if(totaltracks == -1){
					e.printStackTrace();
					if(offline)
						throw new NullPointerException("Please make sure the tracks are numbered in sequence starting from 1");
					else
						throw new NullPointerException("Cannot connect to database and cannot verify the local version of tracks");
				}
				
			}
			return false;
			
		}
		

	}
	public int getTotalTracks(){
		return totaltracks;
	}
	public ArrayList<PhoneData> query(String phone_id, int track){
		return ObjectConversion.convertFrom(dbq.queryTable(phone_id, track));
	}
	
	public static boolean checkATrack(final String phoneid, final int track, final String date){
		String filename = "Tracks/"+phoneid+"/"+phoneid+" track "+track+".track";
		boolean result = false;
		try{
			FileInputStream fout = new FileInputStream(filename);
			ObjectInputStream oos = new ObjectInputStream(fout);
			// Skip the phonedata object
			oos.readObject();
			// Check version, phone id and track
			if(date == null){
				oos.readUTF();
				result = ( phoneid.equals(oos.readUTF()) && track == oos.readInt());
			} else
				result = (date.equals(oos.readUTF()) && phoneid.equals(oos.readUTF()) && track == oos.readInt());
			oos.close();
		} catch (Exception e){}
/*		} catch (ClassNotFoundException e){
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
		
		return result;
	}
	
	public void setPTMListener(SQLListener ptm){
		this.ptm = ptm;
	}
	public void connect() throws Exception{
		if(date == null){
			if(ptm!=null){
				ptm.statusUpdated(1, "Connecting to the NoSQL server ...");
			}
			dbq = new DataBaseQueries(tablename);
			if(ptm!=null){
				ptm.statusUpdated(2, "Checking database version ...");
//				ptm.stepProgressUpdated(2, 0);
			}
//			date = "NODATE";
			try{
				date = dbq.readStats().toString();
				System.out.println("Database last updated: "+date.toString());
			} catch (NullPointerException e){
				
			} catch (NoSuchElementException e){
				date = "NODATE";
			}
		}
	}

}
