package model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Employee {

	private SimpleStringProperty empID;
	private SimpleStringProperty empName;
	
	public Employee(String name) {
		empName = new SimpleStringProperty(name);
	}
	
	public String getEmpName() {
		return empName.get();
	}
	
	public void setEmpName(String name) {
		empName.set(name);
	}
	
	public String getEmpID() {
		return empID.get();
	}
	
	public static void Insert(ObservableList<EmployeeOriginal> employees) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dbPayroll?autoReconnect=true&useSSL=false", 
					"root", "P@ssG0!");
			ps = con.prepareStatement("INSERT INTO tbEmployee (empID, empName) VALUES (?, ?)");
			for(EmployeeOriginal employee : employees) {
				ps.setString(1, employee.getEmpID());
				ps.setString(2, employee.getEmpName());
				ps.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void lookForDup(ObservableList<EmployeeOriginal> employees) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int counter = 0;
		String empIDs = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dbPayroll?autoReconnect=true&useSSL=false",
					"root", "P@ssG0!");
			for(EmployeeOriginal employee : employees) {
				stmt = con.prepareStatement("SELECT COUNT(empID) FROM tbEmployee WHERE empID = ?");
				stmt.setString(1, employee.getEmpID());
				rs = stmt.executeQuery();
				//if(rs.getIn)
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
