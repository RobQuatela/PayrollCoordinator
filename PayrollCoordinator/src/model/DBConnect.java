package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

	private static Connection conn;
	private static String connString = "jdbc:mysql://localhost:3306/dbpayroll?autoReconnect=true&useSSL=false";
	private static String user = "root";
	private static String pass = "P@ssG0!";
	private static String driver = "com.mysql.jdbc.Driver";
	
	public static Connection connect() throws SQLException {
		try {
			Class.forName(driver).newInstance();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(InstantiationException e) {
			e.printStackTrace();
		}catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		
		conn = DriverManager.getConnection(connString, user, pass);
		return conn;
	}
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if(conn != null && !conn.isClosed())
			return conn;
		connect();
		return conn;
	}
}
