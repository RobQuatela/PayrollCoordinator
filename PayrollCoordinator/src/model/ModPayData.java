package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

public class ModPayData {

	private SimpleIntegerProperty modID;
	private SimpleIntegerProperty originID;
	private SimpleDoubleProperty modHoursReg;
	private SimpleDoubleProperty modHoursOT;
	private SimpleDoubleProperty modRate;
	
	public ModPayData(int origin, double reg, double ot, double rate) {
		originID = new SimpleIntegerProperty(origin);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
	}
	
	public ModPayData(int id, int origin, double reg, double ot, double rate) {
		modID = new SimpleIntegerProperty(id);
		originID = new SimpleIntegerProperty(origin);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
	}

	public int getOriginID() {
		return originID.get();
	}

	public void setOriginID(int originID) {
		this.originID.set(originID);
	}

	public double getModHoursReg() {
		return modHoursReg.get();
	}

	public void setModHoursReg(double modHoursReg) {
		this.modHoursReg.set(modHoursReg);;
	}

	public double getModHoursOT() {
		return modHoursOT.get();
	}

	public void setModHoursOT(double modHoursOT) {
		this.modHoursOT.set(modHoursOT);
	}

	public double getModRate() {
		return modRate.get();
	}

	public void setModRate(double modRate) {
		this.modRate.set(modRate);
	}

	public int getModID() {
		return modID.get();
	}
	
	protected static void insert(ObservableList<ModPayData> modPayData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodpaydata (origin_id, mod_hours_reg, mod_hours_ot, mod_rate), "
					+ "VALUES (?, ?, ?, ?)");
			for (ModPayData data : modPayData) {
				ps.setInt(1, data.getOriginID());
				ps.setDouble(2, data.getModHoursReg());
				ps.setDouble(3, data.getModHoursOT());
				ps.setDouble(4, data.getModRate());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected static void update(ObservableList<ModPayData> modData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("UPDATE tbmodpaydata SET mod_hours_reg = ?, mod_hours_ot = ? " +
					"mod_rate = ? WHERE origin_id = ?");
			for(ModPayData data : modData) {
				ps.setDouble(1, data.getModHoursReg());
				ps.setDouble(2, data.getModHoursOT());
				ps.setDouble(3, data.getModRate());
				ps.setInt(4, data.getOriginID());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	}

