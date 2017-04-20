package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.opencsv.CSVReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.Company;
import model.Employee;
import model.EmployeeOriginal;
import model.ModType;
import model.OriginPayData;

public class MainController implements Initializable {

    private ObservableList<EmployeeOriginal> originPayData = FXCollections.observableArrayList();
    //private ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    private Button btnImportOriginData;
    @FXML
    private Button btnSaveImport;
    @FXML
    private Button btnClearOriginPayData;
    @FXML
    private TableView<EmployeeOriginal> tvOriginPayData;
    @FXML
    private TableView<OriginPayData> tvOriginPayDataPrev;
    @FXML
    private TableView<Employee> tvEmployeeDetail;
    @FXML
    private TableView<Company> tvCompany;
    @FXML
    private TableColumn<EmployeeOriginal, String> tvOriginPayDataCOLid;
    @FXML
    private TableColumn<EmployeeOriginal, Double> tvOriginPayDataCOLrate;
    @FXML
    private TableColumn<EmployeeOriginal, String> tvOriginPayDataCOLname;
    @FXML
    private TableColumn<EmployeeOriginal, Double> tvOriginPayDataCOLregHours;
    @FXML
    private TableColumn<EmployeeOriginal, Double> tvOriginPayDataCOLotHours;
    @FXML
    private ProgressBar progImport;
    @FXML
    private ComboBox<String> cbImportCompanies;
    @FXML
    private ComboBox<String> cbCompany;
    @FXML
    private ListView<String> lstEmployee;
    @FXML
    private ComboBox<String> cbEmployeeCompanies;
    @FXML
    private TextField txtModInsert;
    @FXML
    private Button btnModInsert;
    @FXML
    private ListView<String> lstModType;
    @FXML
    private TableView<Employee> tvEmployee;
    @FXML
    private TableColumn<Employee, String> tvEmployeeID;
    @FXML
    private TableColumn<Employee, String> tvEmployeeName;
    @FXML
    private Label lblEmployeeAddName;
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbCompanyFill(cbCompany);
		setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		lstModTypeFill();
		tvEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				lblEmployeeAddName.setText(
						tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString());
			}
			
		});
	}
    
	public void btnSaveImport_Clicked(ActionEvent event) {
		Employee.Insert(originPayData, Company.selectCompany(cbCompany.getValue()));
		lstEmployeeFill();
		clearTableData(tvOriginPayData);
	}
    
    public void btnImportOriginData_Clicked(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
    		setValues(importOriginData(selectedFile));
    	}
    }
    
    public void btnClearOriginPayData_Clicked(ActionEvent event) {
    	clearTableData(tvOriginPayData);
    }
    
    public void cbCompany_ValueChanged(ActionEvent event) {
    	tvEmployeeFill();
    }
    
    public void btnModInsert_Clicked(ActionEvent event) {
    	ModType mod = new ModType(txtModInsert.getText());
    	ModType.insert(mod);
    	lstModTypeFill();
    	txtModInsert.clear();
    }
    
    public void btnAddMod_Clicked(ActionEvent event) {
    	lblEmployeeAddName.setText(tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString());
    }
    
    private ObservableList<EmployeeOriginal> importOriginData(File file) {
    	String[] nextLine;
    	try {
    		CSVReader reader = new CSVReader(new FileReader(file.getAbsolutePath()));
    		while((nextLine = reader.readNext()) != null) {
    			originPayData.add(new EmployeeOriginal(nextLine[0], nextLine[1], 
    					Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3]), 
    					Double.parseDouble(nextLine[4])));
    		}
    	}catch(FileNotFoundException e) {
    		e.printStackTrace();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	return originPayData;
    }
    
    public void setValues(ObservableList<EmployeeOriginal> imports) {
    	tvOriginPayDataCOLid.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empID"));
    	tvOriginPayDataCOLname.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empName"));
    	tvOriginPayDataCOLregHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursReg"));
    	tvOriginPayDataCOLotHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursOT"));
    	tvOriginPayDataCOLrate.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originRate"));
    	tvOriginPayData.setItems(imports);
    }
    
    public void setTvEmployee(ObservableList<Employee> employees) {
    	tvEmployeeID.setCellValueFactory(new PropertyValueFactory<Employee, String>("empID"));
    	tvEmployeeName.setCellValueFactory(new PropertyValueFactory<Employee, String>("empName"));
    	tvEmployee.setItems(employees);
    	//lblEmployeeAddName.setText(getCellValueEmployee(tvEmployee, 1, 0).toString());
    	//lblEmployeeAddName.setText(tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString());
    }
    
    public void cbCompanyFill(ComboBox<String> box) {
		box.setItems(Company.fillCompanyName());
		box.getSelectionModel().select(0);
    }
    
    public void lstEmployeeFill() {
    	lstEmployee.setItems(Employee.fillEmployeeName(Company.selectCompany(cbCompany.getValue())));
    }
    
    public void tvEmployeeFill() {
    	tvEmployee.setItems(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
    }
    
    public void lstModTypeFill() {
    	lstModType.setItems(ModType.fill());
    }
    
    public void clearTableData(TableView tv) {
    	for(int i = 0; i < tv.getItems().size(); i++)
    		tv.getItems().clear();
    }


}
