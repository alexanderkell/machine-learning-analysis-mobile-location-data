package mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import maths.PhoneData;

public class MySQLDownload{
	public interface PTMListener{
		public abstract void statusUpdated(int step, String msg);
		public abstract void stepProgressUpdated(int step, int percent);
		public abstract void finish(int exit, String msg);
	}

	private String tablename;
	public MySQLDownload(String tablename){
		this.tablename = tablename;
	}
	public static void serialiseATrack(final String phoneid, final int track, final PhoneData[] data, final String version){
		// Folder which stores tracks
		String folder = "Tracks/"+phoneid+"/";
		String file = phoneid+" track "+track+".track";
		
		try {
			FileOutputStream fout = new FileOutputStream(folder+file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(data);
			oos.writeUTF(version);
			oos.writeUTF(phoneid);
			oos.writeInt(track);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private insertMySQL mysql;
	private String version;
	private String date;
	private PTMListener ptm;
	private int totaltracks = -1;
	public void serialiseProperies(final String phoneid, final String version, final String date, final int totaltrack){
		// Folder which stores tracks
		String folder = "Tracks/"+phoneid+"/";
		String filename = folder+phoneid+".properties";
		
		File ffolder = new File(folder);
		if(! ffolder.exists())
			ffolder.mkdirs();
		try{

			FileOutputStream fout = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeUTF(version);
			oos.writeInt(totaltrack);
			oos.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public int checkProperties(final String phoneid, final String version, final String date) throws ClassNotFoundException, IOException{
		boolean valid = false;
		String filename = "Tracks/"+phoneid+"/"+phoneid+".properties";
		if(!new File(filename).exists()){
//			valid = false;
			return -1;
		}
		
		FileInputStream fout = new FileInputStream(filename);
		ObjectInputStream oos = new ObjectInputStream(fout);

		int totaltracks = -1;
		try{
			String localversion = (String)oos.readUTF();
			valid = version==null ? true : localversion.equals(version);
			
			totaltracks = oos.readInt();
			
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
	public boolean downloadAndSerialise(String phoneid) throws SQLException, ClassNotFoundException, IOException{
		
		try{
			connect();
			
			if(ptm!=null){
				ptm.statusUpdated(3, "Updating local copies of the tracks  for phone "+phoneid+ " from \n(Database version 10 ("+date+"))...");
				ptm.stepProgressUpdated(3, 0);
			}
	
			totaltracks = checkProperties(phoneid, version, date);
			if(totaltracks == -1)
				totaltracks = Integer.parseInt(singleItemQuery(phoneid, "max(TrackNo)"));
			serialiseProperies(phoneid, version, date, totaltracks);
	
			for(int i = 1; i <= totaltracks; i++){
				if(!MySQLDownload.checkATrack(phoneid, i, version)){
					if(ptm!=null){
						ptm.statusUpdated(3, "Updating local copies of the tracks for phone "+phoneid+ " ("+(i)+"/"+totaltracks+") \n(Database version 10 ("+date+"))");
						ptm.stepProgressUpdated(3, 100*(i)/totaltracks);
					}
					ArrayList<PhoneData> filtered = query("PhoneID = '"+phoneid+"' AND TrackNo = "+ (i));
					PhoneData[] filteredarray = filtered.toArray(new PhoneData[filtered.size()]);
					MySQLDownload.serialiseATrack(phoneid, i, filteredarray, version);					
				}
	
			}
			
			serialiseProperies(phoneid, version, date, totaltracks);
			return true;
		} catch (NullPointerException e){
			totaltracks = checkProperties(phoneid, version, date);
			if(totaltracks == -1){
				e.printStackTrace();
				throw new NullPointerException("Cannot connect to database and cannot verify the local version of tracks");
			} else{
				return false;
			}
		}
		

	}
	public int getTotalTracks(){
		return totaltracks;
	}
	public ArrayList<PhoneData> query(String query) throws SQLException{
		return mysql.query(tablename, query);
	}
	public String singleItemQuery(String phoneid, String select) throws SQLException{
		return mysql.singleItemQuery(tablename, "PhoneID = '"+phoneid+"'", select);
	}
	public static boolean checkATrack(final String phoneid, final int track, final String version){
		String filename = "Tracks/"+phoneid+"/"+phoneid+" track "+track+".track";
		boolean result = false;
		try{
			FileInputStream fout = new FileInputStream(filename);
			ObjectInputStream oos = new ObjectInputStream(fout);
			// Skip the phonedata object
			oos.readObject();
			// Check version, phone id and track
			result = (version.equals(oos.readUTF()) && phoneid.equals(oos.readUTF()) && track == oos.readInt());
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
	
	public void setPTMListener(PTMListener ptm){
		this.ptm = ptm;
	}
	public void connect() throws SQLException{
		if(version == null){
			if(ptm!=null){
				ptm.statusUpdated(1, "Connecting to the MySQL server ...");
			}
			mysql = new insertMySQL();
			if(ptm!=null){
				ptm.statusUpdated(2, "Checking database version ...");
				ptm.stepProgressUpdated(2, 0);
			}
			try{
				version = mysql.singleItemQuery("information_schema.tables", "TABLE_NAME = '"+tablename+"'", "VERSION");
				System.out.println("Table version: "+version);
				if(ptm!=null)
					ptm.stepProgressUpdated(2, 50);
				date = mysql.singleItemQuery("information_schema.tables", "TABLE_NAME = 'FilteredData'", "UPDATE_TIME");
				System.out.println("Table last updated: "+date);
				if(ptm!=null)
					ptm.stepProgressUpdated(2, 100);
	//				System.out.println("Table size: "+mysql.singleItemQuery("information_schema.tables", "TABLE_NAME = 'FilteredData'", "DATA_LENGTH")+" byte(s)");
	//			if(ptm!=null)	
	//				ptm.stepProgressUpdated(2, 100);
			} catch (NullPointerException e){
				
			}
		}
	}

}
