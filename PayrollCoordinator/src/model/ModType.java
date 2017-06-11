package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModType {

	private SimpleStringProperty modTypeID;
	private SimpleStringProperty earningCode;
	private SimpleStringProperty modTypeName;
	
	public ModType(String id, String earningCode, String name) {
		modTypeID = new SimpleStringProperty(id);
		this.earningCode = new SimpleStringProperty(earningCode);
		modTypeName = new SimpleStringProperty(name);
	}
	
	public ModType(String name) {
		modTypeName = new SimpleStringProperty(name);
	}
	
	public String getEarningCode() {
		return earningCode.get();
	}
	
	public void setEarningCode(String earningCode) {
		this.earningCode.set(earningCode);
	}

	public String getModTypeName() {
		return modTypeName.get();
	}

	public void setModTypeName(String modName) {
		this.modTypeName.set(modName);
	}

	public String getModTypeID() {
		return modTypeID.get();
	}
	
	public static void insert(ModType mod) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodtype (modtype_name) VALUES(?)");
			ps.setString(1, mod.getModTypeID());
			ps.setString(2, mod.getModTypeName());
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
	
	public static ObservableList<String> fill() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<String> mods = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT modtype_name FROM tbmodtype");
			rs = ps.executeQuery();
			while(rs.next()) {
				mods.add(rs.getString("modtype_name"));
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
		
		return mods;

	}
	
	public static String searchModTypeID(String modName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String modID = "";
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT modtype_id FROM tbmodtype WHERE modtype_name = ?");
			ps.setString(1, modName);
			rs = ps.executeQuery();
			while(rs.next()) {
				modID = rs.getString("modtype_id");
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
		
		return modID;

	}
	
	public static String searchModTypeName(String modID) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String modName = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT modtype_name FROM tbmodtype WHERE modtype_id = ?");
			ps.setString(1, modID);
			rs = ps.executeQuery();
			while(rs.next()) {
				modName = rs.getString("modtype_name");
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
		
		return modName;

	}
	
	public static ObservableList<ModType> fillModTypes() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModType> mods = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbmodtype");
			rs = ps.executeQuery();
			while(rs.next()) {
				mods.add(new ModType(rs.getString(1), rs.getString(2), rs.getString(3)));
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
		
		return mods;

	}
}
