package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class ModPayData {

	private SimpleIntegerProperty modID;
	private SimpleIntegerProperty originID;
	private SimpleStringProperty empID;
	private SimpleStringProperty empName;
	private SimpleDoubleProperty modHoursReg;
	private SimpleDoubleProperty modHoursOT;
	private SimpleDoubleProperty modRate;
	private SimpleStringProperty modPayrollRule;
	
	public ModPayData(int origin, double reg, double ot, double rate, String payrollRule) {
		originID = new SimpleIntegerProperty(origin);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
		modPayrollRule = new SimpleStringProperty(payrollRule);
	}
	
	public ModPayData(int id, int origin, double reg, double ot, double rate) {
		modID = new SimpleIntegerProperty(id);
		originID = new SimpleIntegerProperty(origin);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
	}
	
	public ModPayData(int id, int origin, double reg, double ot, double rate, String rule) {
		modID = new SimpleIntegerProperty(id);
		originID = new SimpleIntegerProperty(origin);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
		modPayrollRule = new SimpleStringProperty(rule);
	}
	
	public ModPayData(int id, String empID, String name, double reg, double ot, double rate) {
		modID = new SimpleIntegerProperty(id);
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
	}
	
	public ModPayData(int id, int origin, String empID, String name, double reg, double ot, double rate, String rule) {
		modID = new SimpleIntegerProperty(id);
		originID = new SimpleIntegerProperty(origin);
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
		modPayrollRule = new SimpleStringProperty(rule);
	}
	
	public ModPayData(String empID, String name, double reg, double ot, double rate) {
		this.empID = new SimpleStringProperty(empID);
		empName = new SimpleStringProperty(name);
		modHoursReg = new SimpleDoubleProperty(reg);
		modHoursOT = new SimpleDoubleProperty(ot);
		modRate = new SimpleDoubleProperty(rate);
	}
	
	public ModPayData(double reg, double ot, double rate) {
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
	
	public String getEmpID() {
		return empID.get();
	}
	
	public String getEmpName() {
		return empName.get();
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
	
	public String getModPayrollRule() {
		return modPayrollRule.get();
	}
	
	public void setModPayrollRule(String rule) {
		modPayrollRule.set(rule);
	}
	
	protected static void insert(ObservableList<ModPayData> modPayData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodpaydata (origin_id, mod_hours_reg, mod_hours_ot, mod_rate, mod_payroll_rule) "
					+ "VALUES (?, ?, ?, ?, ?)");
			for (ModPayData data : modPayData) {
				ps.setInt(1, data.getOriginID());
				ps.setDouble(2, data.getModHoursReg());
				ps.setDouble(3, data.getModHoursOT());
				ps.setDouble(4, data.getModRate());
				ps.setString(5, data.getModPayrollRule());
				ps.executeUpdate();
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

	}
	
	protected static void insert(ModPayData modPayData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodpaydata (origin_id, mod_hours_reg, mod_hours_ot, mod_rate, mod_payroll_rule) "
					+ "VALUES (?, ?, ?, ?, ?)");
			ps.setInt(1, modPayData.getOriginID());
			ps.setDouble(2, modPayData.getModHoursReg());
			ps.setDouble(3, modPayData.getModHoursOT());
			ps.setDouble(4, modPayData.getModRate());
			ps.setString(5,  modPayData.getModPayrollRule());
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
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected static void update(ModPayData modData) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("UPDATE tbmodpaydata SET mod_hours_reg = ?, mod_hours_ot = ?, " +
					"mod_rate = ? WHERE origin_id = ?");
			ps.setDouble(1, modData.getModHoursReg());
			ps.setDouble(2, modData.getModHoursOT());
			ps.setDouble(3, modData.getModRate());
			ps.setInt(4, modData.getOriginID());
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
	 
	protected void updateData(ObservableList<ModEmp> modEmps, int modID, 
			double hoursReg, double hoursOT, double rate, String payrollRule) {
		Connection con = null;
		PreparedStatement ps = null;

		double gross = 0;
		if(payrollRule.equals("7(i) Exemption"))
			gross = Calc.grossCalc7i(rate, hoursReg, hoursOT);
		else
			gross = Calc.grossCalc(rate, hoursReg, hoursOT);
		
		
		for(ModEmp modEmp : modEmps) {
			gross += modEmp.getModEmpAmount();
			hoursReg += modEmp.getModEmpHours();
			if(hoursReg > 40) {
				hoursOT += (hoursReg - 40) - hoursOT;
			}		
		}
		
		//DecimalFormat df = new DecimalFormat("###.##");

		if(payrollRule.equals("7(i) Exemption"))
			rate = Calc.rateCalc7i(gross, hoursReg, hoursOT);
		else
			rate = Calc.rateCalc(gross, hoursReg);
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("UPDATE tbmodpaydata SET mod_hours_reg = ?, mod_hours_ot = ?, " +
					"mod_rate = ? WHERE mod_id = ?");
			ps.setDouble(1, hoursReg);
			ps.setDouble(2, hoursOT);
			ps.setDouble(3, rate);
			ps.setInt(4, modID);
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
	
	public static ObservableList<ModPayData> getModPayData(Company company, LocalDate date) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModPayData> modData = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT tbmodpaydata.mod_id, tboriginpaydata.origin_id, tboriginpaydata.emp_id, tbemployee.emp_name, tbmodpaydata.mod_hours_reg, " +
					"tbmodpaydata.mod_hours_ot, tbmodpaydata.mod_rate, tbmodpaydata.mod_payroll_rule FROM tbmodpaydata INNER JOIN tboriginpaydata ON tbmodpaydata.origin_id = " +
					"tboriginpaydata.origin_id INNER JOIN tbemployee ON tboriginpaydata.emp_id = tbemployee.emp_id WHERE tboriginpaydata.co_id = ? " +
					"AND tboriginpaydata.origin_end_date = ? ORDER BY tbemployee.emp_name");
			ps.setInt(1, company.getCoID());
			ps.setDate(2, Date.valueOf(date));
			rs = ps.executeQuery();
			while(rs.next()) {
				modData.add(
						new ModPayData(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 
								rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8)));
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
		
		return modData;
	}
	
	public static ModPayData getModPayData(int id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ModPayData modData = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT mod_id, mod_hours_reg, mod_hours_ot, mod_rate, mod_payroll_rule  FROM tbmodpaydata WHERE mod_id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()) {
				modData = new ModPayData(rs.getInt(1), 
								rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getString(5));
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
		
		return modData;
	}
	
	public static ModPayData getModPayDataByOrigin(int origin) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ModPayData modData = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT mod_id, origin_id, mod_hours_reg, mod_hours_ot, mod_rate, mod_payroll_rule " +
					"FROM tbmodpaydata WHERE origin_id = ?");
			ps.setInt(1, origin);
			rs = ps.executeQuery();
			while(rs.next())
				modData = new ModPayData(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5),
						rs.getString(6));
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
		
		return modData;
	}
	
}

