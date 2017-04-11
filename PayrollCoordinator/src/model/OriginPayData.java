package model;

import java.util.Date;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OriginPayData {

	private SimpleIntegerProperty originID;
	private Date originEndDate;
	private SimpleIntegerProperty coID;
	private SimpleIntegerProperty empID;
	private SimpleDoubleProperty originHoursReg;
	private SimpleDoubleProperty originHoursOT;
	private SimpleDoubleProperty originRate;
	
	public OriginPayData(Date date, int eID, int cID, double regHours, double otHours, double rate) {
		originEndDate = date;
		coID = new SimpleIntegerProperty(cID);
		empID = new SimpleIntegerProperty(eID);
		originHoursReg = new SimpleDoubleProperty(regHours);
		originHoursOT = new SimpleDoubleProperty(otHours);
		originRate = new SimpleDoubleProperty(rate);
	}
	
	public int getOriginID() {
		return originID.get();
	}
	
	public Date getOriginEndDate() {
		return originEndDate;
	}
	public void setOriginEndDate(Date date) {
		originEndDate = date;
	}
	
	public int getCoID() {
		return coID.get();
	}
	public void setCoID(int cID) {
		coID.set(cID);
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
	
	private boolean searchForDup() {
		return true;
	}
}
