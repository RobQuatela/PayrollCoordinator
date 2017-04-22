package model;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AlertMessage extends Alert {

	public AlertMessage(AlertType alertType) {
		super(alertType);
		// TODO Auto-generated constructor stub
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
		this.setTitle("QDRIVE - Payroll Coordinator");
		this.setHeaderText("Would you like to add Employee(s) below?");
		this.getDialogPane().setContent(expContent);
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		this.getButtonTypes().setAll(yes, no);
		
		return this;
	}

}
