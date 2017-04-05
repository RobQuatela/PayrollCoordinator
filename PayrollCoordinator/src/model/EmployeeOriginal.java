package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmployeeOriginal {

	private SimpleIntegerProperty empID;
	private SimpleStringProperty empName;
	private SimpleDoubleProperty originHoursReg;
	private SimpleDoubleProperty originHoursOT;
	private SimpleDoubleProperty originRate;
	
	public EmployeeOriginal(int empID, String name, double reg, double ot, double rate) {
		this.empID = new SimpleIntegerProperty(empID);
		empName = new SimpleStringProperty(name);
		originHoursReg = new SimpleDoubleProperty(reg);
		originHoursOT = new SimpleDoubleProperty(ot);
		originRate = new SimpleDoubleProperty(rate);
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public String getEmpName() {
		return empName.get();
	}
	public void setEmpName(String name) {
		empName.set(name);
	}
	
	public double getOriginHoursReg() {
		return originHoursReg.get();
	}
	public void setOriginHoursReg(double reg) {
		originHoursReg.set(reg);
	}
	
	public double getOriginHoursOT() {
		return originHoursOT.get();
	}
	public void setOriginHoursOT(double ot) {
		originHoursOT.set(ot);
	}
	
	public double getOriginRate() {
		return originRate.get();
	}
	public void setOriginRate(double rate) {
		originRate.set(rate);
	}
}
