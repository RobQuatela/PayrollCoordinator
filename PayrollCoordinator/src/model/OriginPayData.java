package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
	
	public OriginPayData(int id, Date date, int eID, int cID, double regHours, double otHours, double rate) {
		originID = new SimpleIntegerProperty(id);
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
	
	public void insert(ObservableList<OriginPayData> payData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbOriginPayData (origin_end_date, co_id, emp_id, " +
			"origin_hours_reg, origin_hours_ot, origin_rate) VALUES (?, ?, ?, ?, ?, ?)");
			for(OriginPayData data : payData) {
				ps.setDate(1, data.getOriginEndDate());
				ps.setInt(2, data.getCoID());
				ps.setInt(3, data.getEmpID());
				ps.setDouble(4, data.getOriginHoursReg());
				ps.setDouble(5, data.getOriginHoursOT());
				ps.setDouble(6, data.getOriginRate());
				ps.executeUpdate();
			}
			
			Alert insertConfirm = new Alert(AlertType.CONFIRMATION);
			insertConfirm.setTitle("QDRIVE - Payroll Coordinator");
			insertConfirm.setContentText("Pay Data Insert successful!!");
			insertConfirm.showAndWait();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ObservableList<OriginPayData> searchForDup(ObservableList<OriginPayData> payData) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<OriginPayData> payDataDup = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			for(OriginPayData data : payData) {
				ps = con.prepareStatement("SELECT COUNT(origin_id) AS total FROM tbOriginPayData" +
			" WHERE origin_end_date = ? AND co_id = ? AND emp_id = ?");
				ps.setDate(1, data.getOriginEndDate());
				ps.setInt(2, data.getCoID());
				ps.setInt(3, data.getEmpID());
				rs = ps.executeQuery();
				while(rs.next()) {
					if(rs.getInt("total") > 0) {
						payDataDup.add(new OriginPayData(data.getOriginEndDate(), data.getCoID(), data.getEmpID(),
								data.getOriginHoursReg(), data.getOriginHoursOT(), data.getOriginRate()));
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payDataDup;
	}
}
