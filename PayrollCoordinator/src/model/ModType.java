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

	private SimpleIntegerProperty modTypeID;
	private SimpleStringProperty modTypeName;
	
	public ModType(int id, String name) {
		modTypeID = new SimpleIntegerProperty(id);
		modTypeName = new SimpleStringProperty(name);
	}
	
	public ModType(String name) {
		modTypeName = new SimpleStringProperty(name);
	}

	public String getModTypeName() {
		return modTypeName.get();
	}

	public void setModTypeName(String modName) {
		this.modTypeName.set(modName);
	}

	public int getModTypeID() {
		return modTypeID.get();
	}
	
	public static void insert(ModType mod) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodtype (modtype_name) VALUES(?)");
			ps.setString(1, mod.getModTypeName());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		return mods;

	}
	
	public static int searchModTypeID(String modName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int modID = 0;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT modtype_id FROM tbmodtype WHERE modtype_name = ?");
			ps.setString(1, modName);
			rs = ps.executeQuery();
			while(rs.next()) {
				modID = rs.getInt("modtype_id");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modID;

	}
	
	public static String searchModTypeName(int modID) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String modName = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT modtype_name FROM tbmodtype WHERE modtype_id = ?");
			ps.setInt(1, modID);
			rs = ps.executeQuery();
			while(rs.next()) {
				modName = rs.getString("modtype_name");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				mods.add(new ModType(rs.getInt(1), rs.getString(2)));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mods;

	}
}
