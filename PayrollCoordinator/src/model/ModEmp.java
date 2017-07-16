package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class ModEmp {

	private SimpleIntegerProperty modEmpID;
	private SimpleStringProperty modTypeID;
	private SimpleStringProperty modTypeName;
	private SimpleStringProperty earningCode;
	private SimpleStringProperty empID;
	private Date modEmpDate;
	private SimpleDoubleProperty modEmpAmount;
	private SimpleDoubleProperty modEmpHours;
	private SimpleStringProperty modEmpDescrip;
	
	public ModEmp(int id, String mod, String emp, Date date, double amount, double hours, String descrip) {
		modEmpID = new SimpleIntegerProperty(id);
		modTypeID = new SimpleStringProperty(mod);
		empID = new SimpleStringProperty(emp);
		modEmpDate = date;
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpHours = new SimpleDoubleProperty(hours);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}
	
	public ModEmp(int id, String mod, String earning, String emp, Date date, double amount, double hours, String descrip) {
		modEmpID = new SimpleIntegerProperty(id);
		modTypeID = new SimpleStringProperty(mod);
		earningCode = new SimpleStringProperty(earning);
		empID = new SimpleStringProperty(emp);
		modEmpDate = date;
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpHours = new SimpleDoubleProperty(hours);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}
	
	public ModEmp(int id, String mod, String type, String emp, LocalDate date, double amount, double hours, String descrip) {
		modEmpID = new SimpleIntegerProperty(id);
		modTypeID = new SimpleStringProperty(mod);
		modTypeName = new SimpleStringProperty(type);
		empID = new SimpleStringProperty(emp);
		modEmpDate = Date.valueOf(date);
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpHours = new SimpleDoubleProperty(hours);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}
	
	public ModEmp(int id, String mod, String earn, String type, String emp, LocalDate date, double amount, double hours, String descrip) {
		modEmpID = new SimpleIntegerProperty(id);
		modTypeID = new SimpleStringProperty(mod);
		earningCode = new SimpleStringProperty(earn);
		modTypeName = new SimpleStringProperty(type);
		empID = new SimpleStringProperty(emp);
		modEmpDate = Date.valueOf(date);
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpHours = new SimpleDoubleProperty(hours);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}
	
	public ModEmp(String mod, String emp, LocalDate date, double amount, double hours, String descrip) {
		modTypeID = new SimpleStringProperty(mod);
		empID = new SimpleStringProperty(emp);
		modEmpDate = Date.valueOf(date);
		modEmpAmount = new SimpleDoubleProperty(amount);
		modEmpHours = new SimpleDoubleProperty(hours);
		modEmpDescrip = new SimpleStringProperty(descrip);
	}
	
	public ModEmp(int id) {
		modEmpID = new SimpleIntegerProperty(id);
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
	
	public String getModTypeID() {
		return modTypeID.get();
	}

	public void setModTypeID(String modID) {
		this.modTypeID.set(modID);
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
	
	public double getModEmpHours() {
		return modEmpHours.get();
	}
	
	public void setModEmpHours(double modEmpHours) {
		this.modEmpHours.set(modEmpHours);
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
			ps = con.prepareStatement("INSERT INTO tbmodemp (modtype_id, emp_id, modemp_date, modemp_amount, modemp_hours, modemp_descrip) " +
			"VALUES (?, ?, ?, ?, ?)");
			for(ModEmp modEmp : modEmps) {
				ps.setString(1, modEmp.getModTypeID());
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
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void insert(ModEmp modEmp) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbmodemp (modtype_id, emp_id, modemp_date, modemp_amount, modemp_hours, modemp_descrip) " +
			"VALUES (?, ?, ?, ?, ?, ?)");
			ps.setString(1, modEmp.getModTypeID());
			ps.setString(2, modEmp.getEmpID());
			ps.setDate(3, modEmp.getModEmpDate());
			ps.setDouble(4, modEmp.getModEmpAmount());
			ps.setDouble(5, modEmp.getModEmpHours());
			ps.setString(6, modEmp.getModEmpDescrip());
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
	
	public static void update(ModEmp modEmp) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("UPDATE tbmodemp SET modtype_id = ?, modemp_date = ?, modemp_amount = ?, modemp_hours = ?, modemp_descrip = ? " +
					"WHERE modemp_id = ?");
			ps.setString(1, modEmp.getModTypeID());
			ps.setDate(2, modEmp.getModEmpDate());
			ps.setDouble(3, modEmp.getModEmpAmount());
			ps.setDouble(4, modEmp.getModEmpHours());
			ps.setString(5, modEmp.getModEmpDescrip());
			ps.setInt(6, modEmp.getModEmpID());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delete() {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("DELETE FROM tbmodemp WHERE modemp_id = ?");
			ps.setInt(1, this.getModEmpID());
			ps.executeUpdate();
			//delete from mod history for recalculation of mod pay data
			//ModHistory.delete(modEmp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ObservableList<ModEmp> fillByEmployee(String empID) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		String sql = "SELECT tbmodemp.modemp_id, tbmodemp.modtype_id, tbmodtype.earningcode_id, tbmodtype.modtype_name, tbmodemp.emp_id, " +
				"tbmodemp.modemp_date, tbmodemp.modemp_amount, tbmodemp.modemp_hours, tbmodemp.modemp_descrip " +
				"FROM tbmodemp INNER JOIN tbmodtype ON tbmodemp.modtype_id = tbmodtype.modtype_id " +
				"WHERE tbmodemp.emp_id = ? ORDER BY tbmodemp.modemp_date DESC";
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement(sql);
			ps.setString(1, empID);
			rs = ps.executeQuery();
			while(rs.next()) {
				modEmps.add(new ModEmp(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5),
						rs.getDate(6).toLocalDate(), rs.getDouble(7),
						rs.getDouble(8), rs.getString(9)));
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
		
		return modEmps;
	}
	
	public static ObservableList<ModEmp> fillByEmployee(String empID, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmps = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			String sql = "SELECT tbmodemp.modemp_id, tbmodemp.modtype_id, tbmodtype.modtype_name, tbmodemp.emp_id, " +
					"tbmodemp.modemp_date, tbmodemp.modemp_amount, tbmodemp.modemp_hours, tbmodemp.modemp_descrip " +
					"FROM tbmodemp INNER JOIN tbmodtype ON tbmodemp.modtype_id = tbmodtype.modtype_id " +
					"WHERE tbmodemp.emp_id = ? AND tbmodemp.modemp_date >= ? AND tbmodemp.modemp_date <= ? AND tbmodtype.earningcode_id = ? ORDER BY tbmodemp.modemp_date DESC";
			//ps = con.prepareStatement("SELECT * FROM tbmodemp WHERE emp_id = ? AND modemp_date >= ? AND modemp_date <= ? ORDER BY modemp_date DESC");
			ps = con.prepareStatement(sql);
			ps.setString(1, empID);
			ps.setDate(2, Date.valueOf(start));
			ps.setDate(3, Date.valueOf(end));
			ps.setString(4, "MOD");
			rs = ps.executeQuery();
			while(rs.next()) {
/*				modEmps.add(new ModEmp(rs.getInt("modemp_id"), rs.getInt("modtype_id"),
						ModType.searchModTypeName(rs.getInt("modemp_id")), rs.getString("emp_id"),
						rs.getDate("modemp_date").toLocalDate(), rs.getDouble("modemp_amount"),
						rs.getDouble("modemp_hours"), rs.getString("modemp_descrip")));*/
				modEmps.add(new ModEmp(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4),
						rs.getDate(5).toLocalDate(), rs.getDouble(6),
						rs.getDouble(7), rs.getString(8)));
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
		
		return modEmps;
	}
	
	public static ObservableList<ModEmp> getModEmp(String empID, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmp = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbmodemp INNER JOIN tbmodtype ON tbmodemp.modtype_id = tbmodtype.modtype_id WHERE emp_id = ? " +
					"AND modemp_date >= ? AND modemp_date <= ? AND tbmodtype.earningcode_id = ?");
			ps.setString(1, empID);
			ps.setDate(2, Date.valueOf(start));
			ps.setDate(3, Date.valueOf(end));
			ps.setString(4, "MOD");
			rs = ps.executeQuery();
			while(rs.next()) {
				modEmp.add(new ModEmp(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getDate(4), rs.getDouble(5),
						rs.getDouble(6), rs.getString(7)));
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
		
		return modEmp;
	}
	
	public static ObservableList<ModEmp> getModEmpPassive(Company company, LocalDate start, LocalDate end) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<ModEmp> modEmp = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT tbmodemp.modemp_id, tbmodemp.modtype_id, tbmodtype.earningcode_id, tbmodemp.emp_id, tbmodemp.modemp_date, " +
					"tbmodemp.modemp_amount, tbmodemp.modemp_hours, tbmodemp.modemp_descrip FROM tbmodemp INNER JOIN tbmodtype ON tbmodemp.modtype_id = tbmodtype.modtype_id " +
					"INNER JOIN tbemployee ON tbmodemp.emp_id = tbemployee.emp_id " +
					"WHERE tbmodtype.earningcode_id != ? AND modemp_date >= ? AND modemp_date <= ? AND tbemployee.co_id = ?");
			ps.setString(1, "MOD");
			ps.setDate(2, Date.valueOf(start));
			ps.setDate(3, Date.valueOf(end));
			ps.setInt(4, company.getCoID());
			rs = ps.executeQuery();
			while(rs.next()) {
				modEmp.add(new ModEmp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getDate(5), rs.getDouble(6),
						rs.getDouble(7), rs.getString(8)));
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
		
		return modEmp;
	}
}
