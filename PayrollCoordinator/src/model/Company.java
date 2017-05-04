package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Company {

	private SimpleIntegerProperty coID;
	private SimpleStringProperty coName;
	
	public Company( int id, String name) {
		coID = new SimpleIntegerProperty(id);
		coName = new SimpleStringProperty(name);
	}
	
	public int getCoID() {
		return coID.get();
	}
	public void setCoID(int id) {
		coID.set(id);
	}
	
	public String getCoName() {
		return coName.get();
	}
	public void setCoName(String name) {
		coName.set(name);
	}
	
	public static String selectCoName(int coID) {
		Connection conn;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String name = null;
		
		try {
			conn = DBConnect.connect();
			stmt = conn.prepareStatement("SELECT co_name FROM tbcompany WHERE co_id = ?");
			stmt.setInt(1, coID);
			rs = stmt.executeQuery();
			while(rs.next())
				name = rs.getString(1);		
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return name;
	}
	
	public static Company selectCompany(String coName) {
		Connection conn;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Company company = null;
		
		try {
			conn = DBConnect.connect();
			stmt = conn.prepareStatement("SELECT * FROM tbcompany WHERE co_name = ?");
			stmt.setString(1, coName);
			rs = stmt.executeQuery();
			while(rs.next()) {
				company = new Company(rs.getInt(1), rs.getString(2));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return company;
	}
	
	public static ObservableList<String> fillCompanyName() {
		Connection conn;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ObservableList<String> companies = FXCollections.observableArrayList();
		
		try {
			conn = DBConnect.connect();
			stmt = conn.prepareStatement("SELECT co_name FROM tbcompany");
			rs = stmt.executeQuery();
			while(rs.next()) {
				companies.add(rs.getString("co_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return companies;
	}
}
