package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;

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
	
	public static void insert(ObservableList<ModPayData> modData, LocalDate start, LocalDate end, String coName) {
		Connection con = null;
		PreparedStatement ps = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		modData = searchForDupModPayData(modData, start, end);
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodhistory (mod_id, modemp_id) VALUES(?, ?)");
			for(ModPayData data : modData) {
				modEmps = searchForDupModEmps(data, start, end);
				for (ModEmp modEmp : modEmps) {
					ps.setInt(1, data.getModID());
					ps.setInt(2, modEmp.getModEmpID());
					ps.executeUpdate();
				}
				data.updateData(modEmps, data.getModID(), data.getModHoursReg(), 
						data.getModHoursOT(), data.getModRate(), data.getModPayrollRule());
			}
			
			AlertMessage success = new AlertMessage(AlertType.CONFIRMATION, "Your employee modifications have been added!");
			success.showAndWait();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	private static ObservableList<ModEmp> searchForDupModEmps(ObservableList<ModPayData> modData, LocalDate start, LocalDate end) {
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
	}*/
	
	private static ObservableList<ModEmp> searchForDupModEmps(ModPayData modData, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		ObservableList<ModEmp> newModEmps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbmodhistory WHERE mod_id = ? AND modemp_id = ?");
			modEmps = ModEmp.getModEmp(modData.getEmpID(), start, end);
			for (ModEmp modEmp : modEmps) {
				ps.setInt(1, modData.getModID());
				ps.setInt(2, modEmp.getModEmpID());
				rs = ps.executeQuery();
				if (!rs.next()) {
					// modEmps.remove(modEmp);
					newModEmps.add(modEmp);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newModEmps;
	}
	
	private static ObservableList<ModPayData> searchForDupModPayData(ObservableList<ModPayData> modData, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		Iterator<ModPayData> iterModData = modData.iterator();
		ObservableList<ModPayData> newModData = FXCollections.observableArrayList();
		ObservableList<ModEmp> newModEmps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbmodhistory WHERE mod_id = ? AND modemp_id = ?");
			for(ModPayData data : modData) {
			//while(iterModData.hasNext()) {
				//ModPayData data = iterModData.next();
				modEmps = ModEmp.getModEmp(data.getEmpID(), start, end);
				//Iterator<ModEmp> iterModEmp = modEmps.iterator();
				for (ModEmp modEmp : modEmps) {
				//while(iterModEmp.hasNext()) {
					//ModEmp modEmp = iterModEmp.next();
					ps.setInt(1, data.getModID());
					ps.setInt(2, modEmp.getModEmpID());
					rs = ps.executeQuery();
					if(!rs.next()) {
						//modEmps.remove(modEmp);
						newModEmps.add(modEmp);
					}
				}
				
/*				Iterator<ModEmp> newIterModEmp = iterModEmp;
				while(newIterModEmp.hasNext()) 
					newModEmps.add(newIterModEmp.next());*/
				
				if(!newModEmps.isEmpty()) {
					//modData.remove(data);
					//iterModData.remove();
					newModData.add(data);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
/*		Iterator<ModPayData> newIterModData = iterModData;
		while(newIterModData.hasNext())
			newModData.add(newIterModData.next());*/
		
		return newModData;
	}
	
	public static void delete(ModPayData modData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("DELETE FROM tbmodhistory WHERE mod_id = ?");
			ps.setInt(1, modData.getModID());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
