package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OriginPayData {

	private SimpleIntegerProperty originID;
	private SimpleStringProperty originYear;
	private SimpleStringProperty originWeek;
	private SimpleIntegerProperty empID;
	private SimpleDoubleProperty originHoursReg;
	private SimpleDoubleProperty originHoursOT;
	private SimpleDoubleProperty originRate;
	
	public OriginPayData(String oy, String ow, int eID, double regHours, double otHours, double rate) {
		originYear = new SimpleStringProperty(oy);
		originWeek = new SimpleStringProperty(ow);
		empID = new SimpleIntegerProperty(eID);
		originHoursReg = new SimpleDoubleProperty(regHours);
		originHoursOT = new SimpleDoubleProperty(otHours);
		originRate = new SimpleDoubleProperty(rate);
	}
	
	public int getOriginID() {
		return originID.get();
	}
	public String getOriginYear() {
		return originYear.get();
	}
	public void setOriginYear(String year) {
		originYear.set(year);
	}
	
	public String getOriginWeek() {
		return originWeek.get();
	}
	public void setOriginWeek(String week) {
		originWeek.set(week);
	}
	
	public int getEmpID() {
		return empID.get();
	}
	public void setEmpID(int id) {
		empID.set(id);
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
