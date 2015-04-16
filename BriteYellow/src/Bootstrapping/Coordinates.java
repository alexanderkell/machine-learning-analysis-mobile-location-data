package Bootstrapping;

import java.io.Serializable;
import java.sql.Timestamp;

public class Coordinates implements Serializable {
	
	double x;
	double y;
	long tsLong;
	int character;
	Timestamp timestamp;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public long getTsLong() {
		return tsLong;
	}
	public void setTsLong(long l) {
		this.tsLong = l;
	}
	public void setTimestamp(long tsLong){
		Timestamp timestamp = new Timestamp(tsLong);
		this.timestamp = timestamp;
	}
	
	public Timestamp getTimestamp(){
		Timestamp one = new Timestamp(tsLong);
		return one;
		
	}
	
	public int getCharacter() {
		return character;
	}
	public void setCharacter(int character) {
		this.character = character;
	}
	@Override
	public String toString() {
		return "Coordinates [x=" + x + ", y=" + y + ", timestamp=" + timestamp
				+ ", character=" + character + "]";
	}
	
	
}
