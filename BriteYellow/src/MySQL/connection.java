package MySQL;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
 
public class connection {
 
  public static Connection Connect() {

 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Please install MySQL JDBC Driver");
		e.printStackTrace();
	}
 
	Connection connection = null;
 
	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://localhost:8889/BriteYellow","root", "root");
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
	}
 
	if (connection != null) {
		System.out.println("Connected to Database");
	} else {
		System.out.println("Connection failed, please check database settings");
	}
	return connection;
  }
}