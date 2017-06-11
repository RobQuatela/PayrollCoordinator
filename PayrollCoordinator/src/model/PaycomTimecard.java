package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PaycomTimecard {

	private SimpleIntegerProperty timecardID;
	private SimpleStringProperty empID;
	private SimpleStringProperty deptCode;
	private Date date;
	private SimpleStringProperty punchtime;
	private SimpleStringProperty modTypeID;
	private SimpleStringProperty taxCode;
	private SimpleStringProperty comments;
	private SimpleStringProperty laborAllocation;
	private SimpleDoubleProperty hours;
	private SimpleDoubleProperty dollars;
	private SimpleDoubleProperty tempRate;
	private SimpleDoubleProperty units;
	
	public PaycomTimecard(String emp_id, LocalDate date, String modType, double hours, double dollars, double tempRate) {
		empID = new SimpleStringProperty(emp_id);
		this.date = Date.valueOf(date);
		modTypeID = new SimpleStringProperty(modType);
		this.hours = new SimpleDoubleProperty(hours);
		this.dollars = new SimpleDoubleProperty(dollars);
		this.tempRate = new SimpleDoubleProperty(tempRate);
	}
	
	public PaycomTimecard(String emp_id, String deptCode, LocalDate date, String punch, String modType, String taxCode, String comments,
			String labor, double hours, double dollars, double tempRate, double unit) {
		empID = new SimpleStringProperty(emp_id);
		this.deptCode = new SimpleStringProperty(deptCode);
		this.date = Date.valueOf(date);
		punchtime = new SimpleStringProperty(punch);
		modTypeID = new SimpleStringProperty(modType);
		this.taxCode = new SimpleStringProperty(taxCode);
		this.comments = new SimpleStringProperty(comments);
		this.laborAllocation = new SimpleStringProperty(labor);
		this.hours = new SimpleDoubleProperty(hours);
		this.dollars = new SimpleDoubleProperty(dollars);
		this.tempRate = new SimpleDoubleProperty(tempRate);
		this.units = new SimpleDoubleProperty(unit);
	}

	public String getEmpID() {
		return empID.get();
	}

	public void setEmpID(String empID) {
		this.empID.set(empID);
	}

	public String getDeptCode() {
		return deptCode.get();
	}

	public void setDeptCode(String deptCode) {
		this.deptCode.set(deptCode);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPunchtime() {
		return punchtime.get();
	}

	public void setPunchtime(String punchtime) {
		this.punchtime.set(punchtime);
	}

	public String getModTypeID() {
		return modTypeID.get();
	}

	public void setModTypeID(String modTypeID) {
		this.modTypeID.set(modTypeID);
	}

	public String getTaxCode() {
		return taxCode.get();
	}

	public void setTaxCode(String taxCode) {
		this.taxCode.set(taxCode);
	}

	public String getComments() {
		return comments.get();
	}

	public void setComments(String comments) {
		this.comments.set(comments);
	}

	public String getLaborAllocation() {
		return laborAllocation.get();
	}

	public void setLaborAllocation(String laborAllocation) {
		this.laborAllocation.set(laborAllocation);
	}

	public double getHours() {
		return hours.get();
	}

	public void setHours(double hours) {
		this.hours.set(hours);
	}

	public double getDollars() {
		return dollars.get();
	}

	public void setDollars(double dollars) {
		this.dollars.set(dollars);
	}

	public double getTempRate() {
		return tempRate.get();
	}

	public void setTempRate(double tempRate) {
		this.tempRate.set(tempRate);
	}

	public double getUnits() {
		return units.get();
	}

	public void setUnits(double units) {
		this.units.set(units);
	}

	public int getTimecardID() {
		return timecardID.get();
	}
	
	protected void insert(PaycomTimecard timecard) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("INSERT INTO tbpaycom_timecard (emp_id, timecard_deptcode, timecard_date, timecard_punchtime, modtype_id, " +
					"timecard_taxcode, timecard_comments, timecard_laborallocation, timecard_hours, timecard_dollars, timecard_temprate, timecard_units) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, timecard.getEmpID());
			ps.setString(2, " ");
			ps.setDate(3, timecard.getDate());
			ps.setString(4, " ");
			ps.setString(5, timecard.getModTypeID());
			ps.setString(6, " ");
			ps.setString(7, " ");
			ps.setString(8, " ");
			ps.setDouble(9, timecard.getHours());
			ps.setDouble(10, timecard.getDollars());
			ps.setDouble(11, timecard.getTempRate());
			ps.setDouble(12, 0);
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
	
	protected void update(PaycomTimecard timecard) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("UPDATE tbpaycom_timecard SET timecard_hours = ?, timecard_dollars = ?, timecard_temprate = ? WHERE emp_id = ? AND " +
					"timecard_date = ? AND modtype_id = ?");
			ps.setDouble(1, timecard.getHours());
			ps.setDouble(2, timecard.getDollars());
			ps.setDouble(3, timecard.getTempRate());
			ps.setString(4, timecard.getEmpID());
			ps.setDate(5, timecard.getDate());
			ps.setString(6, timecard.getModTypeID());
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
	
	public static void insertOrUpdate(PaycomTimecard timecard) {
		if(!timecard.isDuplicate(timecard)) {
			if(timecard.isUpdate(timecard)) {
				timecard.update(timecard);
			}
			else
				timecard.insert(timecard);
		}
	}
	
	protected boolean isUpdate(PaycomTimecard timecard) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean update = false;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbpaycom_timecard WHERE emp_id = ? AND timecard_date = ? AND modtype_id = ? AND (timecard_hours != ? " +
					"OR timecard_dollars != ? OR timecard_temprate != ?)");
			ps.setString(1, timecard.getEmpID());
			ps.setDate(2, timecard.getDate());
			ps.setString(3, timecard.getModTypeID());
			ps.setDouble(4, timecard.getHours());
			ps.setDouble(5, timecard.getDollars());
			ps.setDouble(6, timecard.getTempRate());
			rs = ps.executeQuery();
			if(rs.next())
				update = true;
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
		
		return update;
	}
	
	protected boolean isDuplicate(PaycomTimecard timecard) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean duplicate = false;
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbpaycom_timecard WHERE emp_id = ? AND timecard_date = ? AND modtype_id = ? AND timecard_hours = ? " +
					"AND timecard_dollars = ? AND timecard_temprate = ?");
			ps.setString(1, timecard.getEmpID());
			ps.setDate(2, timecard.getDate());
			ps.setString(3, timecard.getModTypeID());
			ps.setDouble(4, timecard.getHours());
			ps.setDouble(5, timecard.getDollars());
			ps.setDouble(6, timecard.getTempRate());
			rs = ps.executeQuery();
			if(rs.next())
				duplicate = true;
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
		
		return duplicate;
	}
	
	public static ObservableList<PaycomTimecard> getPaycomTimecard(LocalDate date, Company company) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ObservableList<PaycomTimecard> timecard = FXCollections.observableArrayList();
		
		try {
			con = DBConnect.connect();
			ps = con.prepareStatement("SELECT * FROM tbpaycom_timecard INNER JOIN tbemployee ON tbpaycom_timecard.emp_id = tbemployee.emp_id WHERE " +
					"tbpaycom_timecard.timecard_date = ? AND tbemployee.co_id = ?");
			ps.setDate(1, Date.valueOf(date));
			ps.setInt(2, company.getCoID());
			rs = ps.executeQuery();
			while(rs.next()) {
				timecard.add(new PaycomTimecard(rs.getString("emp_id"), rs.getString("timecard_deptcode"), rs.getDate("timecard_date").toLocalDate(), 
						rs.getString("timecard_punchtime"), rs.getString("modtype_id"), rs.getString("timecard_taxcode"),
						rs.getString("timecard_comments"), rs.getString("timecard_laborallocation"), rs.getDouble("timecard_hours"), 
						rs.getDouble("timecard_dollars"), rs.getDouble("timecard_temprate"), rs.getDouble("timecard_units")));
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
		
		return timecard;
	}
	
}
