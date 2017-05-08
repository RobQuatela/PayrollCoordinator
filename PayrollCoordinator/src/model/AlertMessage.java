package model;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class AlertMessage extends Alert {

	String title = "QDRIVE - Payroll Coordinator";
	
	public AlertMessage(AlertType alertType) {
		super(alertType);
	}
	public AlertMessage(AlertType alertType, String header) {
		super(alertType);
		this.setTitle(this.title);
		this.setHeaderText(header);
	}
	public AlertMessage(AlertType alertType, String header, String content) {
		super(alertType);
		this.setTitle(this.title);
		this.setHeaderText(header);
		this.setContentText(content);
	}
	
	public AlertMessage employeeInfo(ObservableList<Employee> employees) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		for(Employee employee : employees) {
			pw.println("Employee ID: " + employee.getEmpID());
			pw.println("Employee Name: " + employee.getEmpName());
			pw.println();
		}
		
		TextArea ta = new TextArea(sw.toString());
		ta.setWrapText(true);
		ta.setEditable(false);
		ta.setMaxWidth(Double.MAX_VALUE);
		ta.setMaxHeight(Double.MAX_VALUE);
		
		GridPane.setVgrow(ta, Priority.ALWAYS);
		GridPane.setHgrow(ta, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.add(ta, 0, 0);
		this.setHeaderText("Would you like to add Employee(s) below?");
		this.getDialogPane().setContent(expContent);
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		this.getButtonTypes().setAll(yes, no);
		
		return this;
	}
	
	public AlertMessage originPayDataInfo(ObservableList<OriginPayData> payData) {
		GridPane gp = new GridPane();
		gp.add(new Text("Employee"), 0, 0);
		gp.add(new Text("Regular Hours"), 1, 0);
		gp.add(new Text("OT Hours"), 2, 0);
		gp.add(new Text("Rate"), 3, 0);
		
		for(int i = 0; i < payData.size(); i++) {
			gp.add(new Text(Employee.searchEmployeeName(payData.get(i).getEmpID())), 0, i + 1);
			gp.add(new Text(String.valueOf(payData.get(i).getOriginHoursReg())), 1, i + 1);
			gp.add(new Text(String.valueOf(payData.get(i).getOriginHoursOT())), 2, i + 1);
			gp.add(new Text(String.valueOf(payData.get(i).getOriginRate())), 3, i + 1);
		}
		
		this.getDialogPane().setContent(gp);
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		this.getButtonTypes().setAll(yes, no);
		
		return this;
	}
	
	public AlertMessage originPayDataInfo(OriginPayData payData) {
		GridPane gp = new GridPane();
		gp.add(new Text("Employee"), 0, 0);
		gp.add(new Text("Regular Hours"), 1, 0);
		gp.add(new Text("OT Hours"), 2, 0);
		gp.add(new Text("Rate"), 3, 0);
		
		gp.add(new Text(Employee.searchEmployeeName(payData.getEmpID())), 0, 1);
		gp.add(new Text(String.valueOf(payData.getOriginHoursReg())), 1, 2);
		gp.add(new Text(String.valueOf(payData.getOriginHoursOT())), 2, 3);
		gp.add(new Text(String.valueOf(payData.getOriginRate())), 3, 4);

		this.getDialogPane().setContent(gp);
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		this.getButtonTypes().setAll(yes, no);
		
		return this;
	}

}
