package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class ModEmp {

	private SimpleIntegerProperty modEmpID;
	private SimpleIntegerProperty modID;
	private SimpleStringProperty empID;
	private Date modEmpDate;
	private SimpleDoubleProperty modEmpAmount;
	private SimpleStringProperty modEmpDescrip;
	
	public ModEmp(int id, int mod, String emp, LocalDate date, double amount, String descrip) {
		modEmpID = new SimpleIntegerProperty(id);
		modID = new SimpleIntegerProperty(mod);
		empID = new SimpleStringProperty(emp);
		modEmpDate = Date.valueOf(date);
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}
	
	public ModEmp(int mod, String emp, LocalDate date, double amount, String descrip) {
		modID = new SimpleIntegerProperty(mod);
		empID = new SimpleStringProperty(emp);
		modEmpDate = Date.valueOf(date);
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}

	public int getModID() {
		return modID.get();
	}

	public void setModID(int modID) {
		this.modID.set(modID);
	}

	public String getEmpID() {
		return empID.get();
	}

	public void setEmpID(String empID) {
		this.empID.set(empID);
	}

	public Date getModEmpDate() {
		return modEmpDate;
	}

	public void setModEmpDate(LocalDate modEmpDate) {
		this.modEmpDate = Date.valueOf(modEmpDate);
	}

	public double getModEmpAmount() {
		return modEmpAmount.get();
	}

	public void setModEmpAmount(double modEmpAmount) {
		this.modEmpAmount.set(modEmpAmount);
	}

	public String getModEmpDescrip() {
		return modEmpDescrip.get();
	}

	public void setModEmpDescrip(String modEmpDescrip) {
		this.modEmpDescrip.set(modEmpDescrip);
	}

	public int getModEmpID() {
		return modEmpID.get();
	}

	public void setModEmpID(int modEmpID) {
		this.modEmpID.set(modEmpID);
	}
	
	public static void insert(ObservableList<ModEmp> modEmps) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbModEmp (mod_id, emp_id, modemp_date, modemp_amount, modemp_descrip " +
			"VALUES (?, ?, ?, ?, ?)");
			for(ModEmp modEmp : modEmps) {
				ps.setInt(1, modEmp.getModID());
				ps.setString(2, modEmp.getEmpID());
				ps.setDate(3, modEmp.getModEmpDate());
				ps.setDouble(4, modEmp.getModEmpAmount());
				ps.setString(5, modEmp.getModEmpDescrip());
				ps.executeUpdate();
			}
			
			AlertMessage sucess = new AlertMessage(AlertType.CONFIRMATION, "You have successfully inserted modications for employee!");
			sucess.showAndWait();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void insert(ModEmp modEmp) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbModEmp (mod_id, emp_id, modemp_date, modemp_amount, modemp_descrip " +
			"VALUES (?, ?, ?, ?, ?)");
			ps.setInt(1, modEmp.getModID());
			ps.setString(2, modEmp.getEmpID());
			ps.setDate(3, modEmp.getModEmpDate());
			ps.setDouble(4, modEmp.getModEmpAmount());
			ps.setString(5, modEmp.getModEmpDescrip());
			ps.executeUpdate();
			
			AlertMessage sucess = new AlertMessage(AlertType.CONFIRMATION, "You have successfully inserted modications for employee!");
			sucess.showAndWait();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
