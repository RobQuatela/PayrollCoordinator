package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {

	private SimpleIntegerProperty empID;
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
	
	public int getEmpID() {
		return empID.get();
	}
}
