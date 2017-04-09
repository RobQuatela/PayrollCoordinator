package model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class Employee {

	private SimpleStringProperty empID;
	private SimpleStringProperty empName;
	
	public Employee(String empID, String name) {
		this.empID = new SimpleStringProperty(empID);
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
	
	public static void Insert(ObservableList<EmployeeOriginal> list) {
		Connection con = null;
		PreparedStatement ps = null;
		ObservableList<Employee> employees = lookForDup(list);
		
		if (employees.size() > 0) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);

			for (Employee employee : employees) {
				pw.println("Employee ID: " + employee.getEmpID());
				pw.println("Employee Name: " + employee.getEmpName());
				pw.println();
			}

			TextArea textArea = new TextArea(sw.toString());
			textArea.setWrapText(true);
			textArea.setEditable(false);
			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);

			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);
			GridPane expContent = new GridPane();
			expContent.add(textArea, 0, 0);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("QDRIVE - Payroll Coordinator");
			alert.setHeaderText("Would you like to add Employee(s) below?");
			alert.getDialogPane().setContent(expContent);
			ButtonType yes = new ButtonType("Yes");
			ButtonType no = new ButtonType("No");
			alert.getButtonTypes().setAll(yes, no);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == yes) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/dbPayroll?autoReconnect=true&useSSL=false", "root", "P@ssG0!");
					ps = con.prepareStatement("INSERT INTO tbEmployee (emp_id, emp_name) VALUES (?, ?)");
					for (Employee employee : employees) {
						ps.setString(1, employee.getEmpID());
						ps.setString(2, employee.getEmpName());
						ps.executeUpdate();
					}
					
					Alert insertConfirm = new Alert(AlertType.CONFIRMATION);
					insertConfirm.setTitle("QDRIVE - Payroll Coordinator");
					insertConfirm.setContentText("Employee Insert(s) successful!!");
					insertConfirm.showAndWait();
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			Alert alertNothing = new Alert(AlertType.INFORMATION);
			alertNothing.setTitle("QDRIVE - Payroll Coordinator");
			alertNothing.setContentText("No additional Employees created...");
			alertNothing.showAndWait();
		}
	}
	
	private static ObservableList<Employee> lookForDup(ObservableList<EmployeeOriginal> employees) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ObservableList<Employee> emps = FXCollections.observableArrayList();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dbPayroll?autoReconnect=true&useSSL=false",
					"root", "P@ssG0!");
			for(EmployeeOriginal employee : employees) {
				stmt = con.prepareStatement("SELECT COUNT(emp_id) AS total FROM tbEmployee WHERE emp_id = ?");
				stmt.setString(1, employee.getEmpID());
				rs = stmt.executeQuery();
				while(rs.next()) {
					if(rs.getInt("total") == 0)
						emps.add(new Employee(employee.getEmpID(), employee.getEmpName()));
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return emps;

	}
}
