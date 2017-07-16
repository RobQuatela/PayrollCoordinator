package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Company;
import model.Employee;

public class AddEmployeeController implements Initializable {
	
	ObservableList<String> companies = Company.fillCompanyName();
	ObservableList<Employee> emps = FXCollections.observableArrayList();
	
	@FXML
	TableView<Employee> tvEmployees;
	@FXML
	TableColumn<Employee, String> tvEmployeesCompany;
	@FXML
	TableColumn<Employee, String> tvEmployeesPayCode;
	@FXML
	TableColumn<Employee, String> tvEmployeesID;
	@FXML
	TableColumn<Employee, String> tvEmployeesName;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		emps.add(new Employee("N444", "Rob Quatela"));
		emps.add(new Employee("N333", "Sarah Quatela"));
		setTvEmployees(emps);
		
	}
	
	public void setTvEmployees(ObservableList<Employee> employees) {
		tvEmployeesCompany.setCellFactory(ComboBoxTableCell.forTableColumn(companies));
		//tvEmployeesCompany.setCellValueFactory(new PropertyValueFactory<Employee, String>("coID"));
		tvEmployeesPayCode.setCellFactory(ComboBoxTableCell.forTableColumn());
		tvEmployeesID.setCellValueFactory(new PropertyValueFactory<Employee, String>("empID"));
		tvEmployeesName.setCellValueFactory(new PropertyValueFactory<Employee, String>("empName"));
		tvEmployees.setItems(employees);
	}
	
	

}
