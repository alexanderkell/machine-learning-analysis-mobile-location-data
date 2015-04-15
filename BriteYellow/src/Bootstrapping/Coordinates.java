package Bootstrapping;

import java.sql.Timestamp;

public class Coordinates {
	
	double x;
	double y;
	Timestamp timestamp;
	int character;
	
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
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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
