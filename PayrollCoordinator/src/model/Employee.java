package model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
	private SimpleIntegerProperty isActive;
	
	public Employee(String empID, String name, int coid) {
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
		coID = new SimpleIntegerProperty(coid);
	}
	
	public Employee(String empID, String name) {
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
	}
	
	public Employee(String empID, String name, int coid, int active) {
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
		coID = new SimpleIntegerProperty(coid);
		isActive = new SimpleIntegerProperty(active);
	}
	
	public int getIsActive() {
		return isActive.get();
	}
	
	public void setIsActive(int active) {
		isActive.set(active);
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
			AlertMessage alert = new AlertMessage(AlertType.INFORMATION);
			alert = alert.employeeInfo(employees);
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == alert.getButtonTypes().get(0)) {
				try {
					con = DBConnect.connect();
					ps = con.prepareStatement("INSERT INTO tbemployee (emp_id, emp_name, co_id) VALUES (?, ?, ?)");
					for (Employee employee : employees) {
						ps.setString(1, employee.getEmpID());
						ps.setString(2, employee.getEmpName());
						ps.setInt(3, company.getCoID());
						ps.executeUpdate();
					}
					
					AlertMessage insertConfirm = new AlertMessage(AlertType.CONFIRMATION, "Employee insert(s) sucessfull!!");
					insertConfirm.showAndWait();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else {
				AlertMessage selectNo = new AlertMessage(AlertType.CONFIRMATION, "You have selected no...");
				selectNo.showAndWait();
			}
		}
		else {
			AlertMessage alertNothing = new AlertMessage(AlertType.INFORMATION, "No additional employees created...");
			alertNothing.showAndWait();

		}
	}
	
	public void insert(Employee emp) {
		Connection con = null;
		PreparedStatement ps = null;
		String dup = lookForDup(emp);
		
		if(!dup.isEmpty()) {
			AlertMessage dupAlert = new AlertMessage(AlertType.WARNING, "We cannot insert new employee. " +
					dup + " already has the same employee ID no. Please choose another employee ID no.");
			dupAlert.showAndWait();
		}
		else {
			try {
				con = DBConnect.connect();
				ps = con.prepareStatement("INSERT INTO tbemployee (emp_id, emp_name, co_id) VALUES (?, ?, ?)");
				ps.setString(1, emp.getEmpID());
				ps.setString(2, emp.getEmpName());
				ps.setInt(3, emp.getCoID());
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void update(Employee emp, String id) {
		Connection con = null;
		PreparedStatement ps = null;
		String dup = "";
		if(!emp.getEmpID().equals(id))
			dup = lookForDup(emp);
		
		if(!dup.isEmpty()) {
			AlertMessage dupAlert = new AlertMessage(AlertType.WARNING, "We cannot insert new employee. " +
					dup + " already has the same employee ID no. Please choose another employee ID no.");
			dupAlert.showAndWait();
		}
		else {
			try {
				con = DBConnect.connect();
				ps = con.prepareStatement("UPDATE tbemployee SET emp_id = ?, emp_name = ? WHERE emp_id = ?");
				ps.setString(1, emp.getEmpID());
				ps.setString(2, emp.getEmpName());
				ps.setString(3, id);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String lookForDup(Employee emp) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = "";
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT emp_name FROM tbemployee WHERE emp_id = ?");
			ps.setString(1, emp.getEmpID());
			rs = ps.executeQuery();
			if(rs.next())
				name = rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return name;
	}
	
/*	private static String lookForDup(String emp) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = "";
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT emp_name FROM tbemployee WHERE emp_id = ?");
			ps.setString(1, emp);
			rs = ps.executeQuery();
			if(rs.next())
				name = rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return name;
	}*/
	
	private static ObservableList<Employee> lookForDup(ObservableList<EmployeeOriginal> employees, Company company) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ObservableList<Employee> emps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			for(EmployeeOriginal employee : employees) {
				stmt = con.prepareStatement("SELECT COUNT(emp_id) AS total FROM tbemployee WHERE emp_id = ?");
				stmt.setString(1, employee.getEmpID());
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
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			ps = con.prepareStatement("SELECT emp_name FROM tbemployee WHERE co_id = ?");
			ps.setInt(1, company.getCoID());
			rs = ps.executeQuery();
			while(rs.next()) {
				empName.add(rs.getString("emp_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return empName;
	}
	
	public static String searchEmployeeName(String empID) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String empName = "";
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT emp_name FROM tbemployee WHERE emp_id = ?");
			ps.setString(1, empID);
			rs = ps.executeQuery();
			while(rs.next()) {
				empName = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			ps = con.prepareStatement("SELECT * FROM tbemployee WHERE co_id = ? ORDER BY emp_name ASC");
			ps.setInt(1, company.getCoID());
			rs = ps.executeQuery();
			while(rs.next()) {
				employee.add(new Employee(rs.getString(1), rs.getString(2), rs.getInt(3)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return employee;
	}
}
