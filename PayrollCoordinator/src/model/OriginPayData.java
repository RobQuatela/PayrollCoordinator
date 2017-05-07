package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.sql.Date;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class OriginPayData {

	private SimpleIntegerProperty originID;
	private Date originEndDate;
	private SimpleIntegerProperty coID;
	private SimpleStringProperty empID;
	private SimpleStringProperty empName;
	private SimpleDoubleProperty originHoursReg;
	private SimpleDoubleProperty originHoursOT;
	private SimpleDoubleProperty originRate;
	
	public OriginPayData(Date date, int cID, String eID, double regHours, double otHours, double rate) {
		originEndDate = date;
		coID = new SimpleIntegerProperty(cID);
		empID = new SimpleStringProperty(eID);
		originHoursReg = new SimpleDoubleProperty(regHours);
		originHoursOT = new SimpleDoubleProperty(otHours);
		originRate = new SimpleDoubleProperty(rate);
	}
	
	public OriginPayData(int id, String eID,String empName, double regHours, double otHours, double rate) {
		originID = new SimpleIntegerProperty(id);
		empID = new SimpleStringProperty(eID);
		this.empName = new SimpleStringProperty(empName);
		originHoursReg = new SimpleDoubleProperty(regHours);
		originHoursOT = new SimpleDoubleProperty(otHours);
		originRate = new SimpleDoubleProperty(rate);
	}
	
	public OriginPayData(int id, Date date, int cID, String eID, double regHours, double otHours, double rate) {
		originID = new SimpleIntegerProperty(id);
		originEndDate = date;
		coID = new SimpleIntegerProperty(cID);
		empID = new SimpleStringProperty(eID);
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

	public String getEmpID() {
		return empID.get();
	}
	public void setEmpID(String id) {
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
	
	public String getEmpName() {
		return empName.get();
	}

	public void setEmpName(String empName) {
		this.empName.set(empName);
	}
	
	public static ObservableList<OriginPayData> fillOriginPayData(Company company, LocalDate date) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<OriginPayData> payData = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT origin_id, emp_id, origin_hours_reg, origin_hours_ot, origin_rate " +
			"FROM tboriginpaydata WHERE co_id = ? AND origin_end_date = ?");
			ps.setInt(1, company.getCoID());
			ps.setDate(2, Date.valueOf(date));
			rs = ps.executeQuery();
			while(rs.next()) {
				payData.add(new OriginPayData(rs.getInt(1), rs.getString(2), Employee.searchEmployeeName(rs.getString(2)), 
						rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payData;
	}
	
	public static int searchLastID() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT MAX(origin_id) FROM tboriginpaydata");
			rs = ps.executeQuery();
			while(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
	}

	protected static void insert(ObservableList<OriginPayData> payData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tboriginpaydata (origin_end_date, co_id, emp_id, " +
			"origin_hours_reg, origin_hours_ot, origin_rate) VALUES (?, ?, ?, ?, ?, ?)");
			for(OriginPayData data : payData) {
				ps.setDate(1, data.getOriginEndDate());
				ps.setInt(2, data.getCoID());
				ps.setString(3, data.getEmpID());
				ps.setDouble(4, data.getOriginHoursReg());
				ps.setDouble(5, data.getOriginHoursOT());
				ps.setDouble(6, data.getOriginRate());
				ps.executeUpdate();
			}
			
			AlertMessage newData = new AlertMessage(AlertType.CONFIRMATION, "The following new data has been inserted!");
			newData = newData.originPayDataInfo(payData);
			newData.getButtonTypes().remove(0,2);
			newData.getButtonTypes().add(ButtonType.OK);
			newData.showAndWait();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static ObservableList<OriginPayData> searchForDup(ObservableList<OriginPayData> payData) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<OriginPayData> payDataDup = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			for(OriginPayData data : payData) {
				ps = con.prepareStatement("SELECT COUNT(origin_id) AS total, origin_id FROM tboriginpaydata " +
			"WHERE origin_end_date = ? AND emp_id = ? GROUP BY origin_id");
				ps.setDate(1, data.getOriginEndDate());
				ps.setString(2, data.getEmpID());
				rs = ps.executeQuery();
				while(rs.next()) {
					if(rs.getInt("total") > 0) {
						payDataDup.add(new OriginPayData(rs.getInt(2), data.getOriginEndDate(),data.getCoID(), data.getEmpID(), 
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
	
	protected static ObservableList<OriginPayData> searchForUpdates(ObservableList<OriginPayData> payData) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<OriginPayData> payDataUpdate = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			for(OriginPayData data : payData) {
				ps = con.prepareStatement("SELECT COUNT(origin_id) AS total, origin_id FROM tboriginpaydata " +
			"WHERE origin_end_date = ? AND emp_id = ? AND (origin_hours_reg != ? OR origin_hours_ot != ? " +
						"OR origin_rate != ?) GROUP BY origin_id");
				ps.setDate(1, data.getOriginEndDate());
				ps.setString(2, data.getEmpID());
				ps.setDouble(3, data.getOriginHoursReg());
				ps.setDouble(4, data.getOriginHoursOT());
				ps.setDouble(5, data.getOriginRate());
				rs = ps.executeQuery();
				while(rs.next()) {
					if(rs.getInt("total") > 0) {
						payDataUpdate.add(new OriginPayData(rs.getInt(2), data.getOriginEndDate(),data.getCoID(), data.getEmpID(), 
								data.getOriginHoursReg(), data.getOriginHoursOT(), data.getOriginRate()));
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payDataUpdate;
	}
	
	protected static void update(ObservableList<OriginPayData> payData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("UPDATE tboriginpaydata SET " +
					"origin_hours_reg = ?, origin_hours_ot = ?, origin_rate = ? " +
					"WHERE origin_id = ?");
			for(OriginPayData data : payData) {
				ps.setDouble(1, data.getOriginHoursReg());
				ps.setDouble(2, data.getOriginHoursOT());
				ps.setDouble(3, data.getOriginRate());
				ps.setInt(4, data.getOriginID());
				ps.executeUpdate();
			}
			
			AlertMessage alert = new AlertMessage(AlertType.CONFIRMATION, "Sucessfully updated employee files!");
			alert.showAndWait();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void insertOrUpdate(ObservableList<OriginPayData> payData) {
		ObservableList<OriginPayData> dup = searchForDup(payData);
		ObservableList<OriginPayData> update = searchForUpdates(dup);
		if(dup.isEmpty()) {
			insert(payData);
		}
		else {
			for(int i = 0; i < dup.size(); i++) {
				for(int t = 0; t < payData.size(); t++) {
					if(payData.get(t).getEmpID() == dup.get(i).getEmpID())
						payData.remove(t);
				}
			}
			if(!update.isEmpty()) {
				AlertMessage alert = new AlertMessage(AlertType.INFORMATION,
						"The following employee(s) data has already been created for "
								+ update.get(0).getOriginEndDate()
								+ ". Would you like to update these values with the new values below?");
				alert = alert.originPayDataInfo(update);
				Optional<ButtonType> result = alert.showAndWait();

				if (result.get() == alert.getButtonTypes().get(0)) {
					update(update);
					if (!payData.isEmpty())
						insert(payData);
				} else {
					AlertMessage test = new AlertMessage(AlertType.INFORMATION, "Duplicate data has been discarded...");
					test.showAndWait();

					insert(payData);
				}
			}
		}
	}
}
