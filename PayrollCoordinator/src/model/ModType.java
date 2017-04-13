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
			ps = con.prepareStatement("INSERT INTO tbModType (type_name) VALUES(?)");
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
			ps = con.prepareStatement("SELECT type_name FROM tbModType");
			rs = ps.executeQuery();
			while(rs.next()) {
				mods.add(rs.getString("type_name"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mods;

	}
	
	public static ObservableList<ModType> fill1() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModType> mods = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbModType");
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
