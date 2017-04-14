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
	private SimpleIntegerProperty coID;
	
	public Employee(String empID, String name, int coid) {
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
		coID = new SimpleIntegerProperty(coid);
	}
	
	public int getCoID() {
		return coID.get();
	}

	public void setCoID(int coID) {
		this.coID.set(coID);
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
	
	public static void Insert(ObservableList<EmployeeOriginal> list, Company company) {
		Connection con = null;
		PreparedStatement ps = null;
		ObservableList<Employee> employees = lookForDup(list, company);
		
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
					con = DBConnect.connect();
					ps = con.prepareStatement("INSERT INTO tbEmployee (emp_id, emp_name, co_id) VALUES (?, ?, ?)");
					for (Employee employee : employees) {
						ps.setString(1, employee.getEmpID());
						ps.setString(2, employee.getEmpName());
						ps.setInt(3, company.getCoID());
						ps.executeUpdate();
					}
					
					Alert insertConfirm = new Alert(AlertType.CONFIRMATION);
					insertConfirm.setTitle("QDRIVE - Payroll Coordinator");
					insertConfirm.setContentText("Employee Insert(s) successful!!");
					insertConfirm.showAndWait();
					
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
	
	private static ObservableList<Employee> lookForDup(ObservableList<EmployeeOriginal> employees, Company company) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ObservableList<Employee> emps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			for(EmployeeOriginal employee : employees) {
				stmt = con.prepareStatement("SELECT COUNT(emp_id) AS total FROM tbEmployee WHERE emp_id = ? AND co_id = ?");
				stmt.setString(1, employee.getEmpID());
				stmt.setInt(2, company.getCoID());
				rs = stmt.executeQuery();
				while(rs.next()) {
					if(rs.getInt("total") == 0)
						emps.add(new Employee(employee.getEmpID(), employee.getEmpName(), company.getCoID()));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return emps;

	}
	
	public static ObservableList<String> fillEmployeeName(Company company) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<String> empName = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT emp_name FROM tbEmployee WHERE co_id = ?");
			ps.setInt(1, company.getCoID());
			rs = ps.executeQuery();
			while(rs.next()) {
				empName.add(rs.getString("emp_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return empName;
	}
	
	public static ObservableList<Employee> fillEmployee(Company company) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<Employee> employee = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbEmployee WHERE co_id = ?");
			ps.setInt(1, company.getCoID());
			rs = ps.executeQuery();
			while(rs.next()) {
				employee.add(new Employee(rs.getString(1), rs.getString(2), rs.getInt(3)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employee;
	}
}
