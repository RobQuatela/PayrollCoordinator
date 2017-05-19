package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class ModHistory {

	private int historyID;
	private int modEmpID;
	private int modID;
	
	public ModHistory(int id, int modemp, int mod) {
		historyID = id;
		modEmpID = modemp;
		modID = mod;
	}

	public int getModEmpID() {
		return modEmpID;
	}

	public void setModEmpID(int modEmpID) {
		this.modEmpID = modEmpID;
	}

	public int getModID() {
		return modID;
	}

	public void setModID(int modID) {
		this.modID = modID;
	}

	public int getHistoryID() {
		return historyID;
	}
	
	public static void insert(ObservableList<ModPayData> modData, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		modData = searchForDupModPayData(modData, start, end);
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodhistory (mod_id, modemp_id) VALUES(?, ?)");
			for(ModPayData data : modData) {
				modEmps = searchForDupModEmps(modData, start, end);
				for (ModEmp modEmp : modEmps) {
					ps.setInt(1, data.getModID());
					ps.setInt(2, modEmp.getModEmpID());
					ps.executeUpdate();
				}
				data.updateData(modEmps, data.getModID(), data.getModHoursReg(), 
						data.getModHoursOT(), data.getModRate());
			}
			
			AlertMessage success = new AlertMessage(AlertType.CONFIRMATION, "Your employee modifications have been added!");
			success.showAndWait();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ObservableList<ModEmp> searchForDupModEmps(ObservableList<ModPayData> modData, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbmodhistory WHERE mod_id = ? AND modemp_id = ?");
			for(ModPayData data : modData) {
				modEmps = ModEmp.getModEmp(data.getEmpID(), start, end);
				for (ModEmp modEmp : modEmps) {
					ps.setInt(1, data.getModID());
					ps.setInt(2, modEmp.getModEmpID());
					rs = ps.executeQuery();
					while(rs.next()) {
						modEmps.remove(modEmp);
					}
				}
				if(modEmps.isEmpty()) {
					modData.remove(data);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modEmps;
	}
	
	private static ObservableList<ModPayData> searchForDupModPayData(ObservableList<ModPayData> modData, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbmodhistory WHERE mod_id = ? AND modemp_id = ?");
			for(ModPayData data : modData) {
				modEmps = ModEmp.getModEmp(data.getEmpID(), start, end);
				for (ModEmp modEmp : modEmps) {
					ps.setInt(1, data.getModID());
					ps.setInt(2, modEmp.getModEmpID());
					rs = ps.executeQuery();
					while(rs.next()) {
						modEmps.remove(modEmp);
					}
				}
				if(modEmps.isEmpty()) {
					modData.remove(data);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return modData;
	}
}
