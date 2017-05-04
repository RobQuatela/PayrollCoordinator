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

	private SimpleIntegerProperty modID;
	private SimpleStringProperty modName;
	
	public ModType(int id, String name) {
		modID = new SimpleIntegerProperty(id);
		modName = new SimpleStringProperty(name);
	}
	
	public ModType(String name) {
		modName = new SimpleStringProperty(name);
	}

	public String getModName() {
		return modName.get();
	}

	public void setModName(String modName) {
		this.modName.set(modName);
	}

	public int getModID() {
		return modID.get();
	}
	
	public static void insert(ModType mod) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodtype (mod_name) VALUES(?)");
			ps.setString(1, mod.getModName());
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
			ps = con.prepareStatement("SELECT mod_name FROM tbmodtype");
			rs = ps.executeQuery();
			while(rs.next()) {
				mods.add(rs.getString("mod_name"));
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
			ps = con.prepareStatement("SELECT mod_id FROM tbmodtype WHERE mod_name = ?");
			ps.setString(1, modName);
			rs = ps.executeQuery();
			while(rs.next()) {
				modID = rs.getInt("mod_id");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modID;

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
