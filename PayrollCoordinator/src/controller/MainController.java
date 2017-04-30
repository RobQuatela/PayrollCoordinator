package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.Company;
import model.Employee;
import model.EmployeeOriginal;
import model.ModEmp;
import model.ModType;
import model.OriginPayData;

public class MainController implements Initializable {

    private ObservableList<EmployeeOriginal> originPayData = FXCollections.observableArrayList();
    private ObservableList<String> payrollTypes = FXCollections.observableArrayList();

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
    private TableColumn<OriginPayData, String> tvOriginPayDataPrevId;
    @FXML
    private TableColumn<OriginPayData, String> tvOriginPayDataPrevName;
    @FXML
    private TableColumn<OriginPayData, Double> tvOriginPayDataPrevRegHours;
    @FXML
    private TableColumn<OriginPayData, Double> tvOriginPayDataPrevOtHours;
    @FXML
    private TableColumn<OriginPayData, Double> tvOriginPayDataPrevRate;
    @FXML
    private TableColumn<OriginPayData, Integer> tvOriginPayDataPrevOrg;
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
    @FXML
    private DatePicker dpOriginDateEnding;
    @FXML
    private ComboBox<String> cbPayrollType;
    @FXML
    private DatePicker dpDateEndingPrev;
    @FXML
    private TableView<ModEmp> tvEmpModAdd;
    @FXML
    private TableColumn<ModEmp, String> tvEmpModAddType;
    @FXML
    private TableColumn<ModEmp, String> tvEmpModAddDate;
    @FXML
    private TableColumn<ModEmp, String> tvEmpModAddAmount;
    @FXML
    private TableColumn<ModEmp, String> tvEmpModAddDescrip;
    
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comboBoxFill(cbCompany, Company.fillCompanyName());
		setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		dpDateEndingPrev.setValue(LocalDate.of(2017, 4, 22));
		setTvOriginPayDataPrev(OriginPayData.fillOriginPayData(Company.selectCompany(cbCompany.getValue()), dpDateEndingPrev.getValue()));
		listViewFill(lstModType, ModType.fill());
		tvEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				lblEmployeeAddName.setText(
						tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString());
			}
			
		});
		
		payrollTypes.add("Original");
		payrollTypes.add("Modified");
		comboBoxFill(cbPayrollType, payrollTypes);
		setTvEmpModAdd();
	}
    
	public void btnSaveImport_Clicked(ActionEvent event) {
		Employee.Insert(originPayData, Company.selectCompany(cbCompany.getValue()));
		//lstEmployeeFill();
		listViewFill(lstEmployee, Employee.fillEmployeeName(Company.selectCompany(cbCompany.getValue())));
		ObservableList<OriginPayData> payData = FXCollections.observableArrayList();
		for(int i = 0; i < tvOriginPayData.getItems().size(); i++) {
			payData.add(new OriginPayData(
					Date.valueOf(dpOriginDateEnding.getValue()),
					1, //place holder for getCoInt method
					tvOriginPayData.getItems().get(i).getEmpID(),
					tvOriginPayData.getItems().get(i).getOriginHoursReg(),
					tvOriginPayData.getItems().get(i).getOriginHoursOT(),
					tvOriginPayData.getItems().get(i).getOriginRate()
					));
		}
		OriginPayData.insertOrUpdate(payData);
		clearTableData(tvOriginPayData);
		dpOriginDateEnding.setValue(null);
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
    
    public void dpDateEndingPrev_ValueChanged(ActionEvent event) {
    	setTvOriginPayDataPrev(OriginPayData.fillOriginPayData(Company.selectCompany(cbCompany.getValue()), 
    			dpDateEndingPrev.getValue()));
    }
    
    public void setValues(ObservableList<EmployeeOriginal> imports) {
    	tvOriginPayDataCOLid.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empID"));
    	tvOriginPayDataCOLname.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empName"));
    	tvOriginPayDataCOLregHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursReg"));
    	tvOriginPayDataCOLotHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursOT"));
    	tvOriginPayDataCOLrate.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originRate"));
    	tvOriginPayData.setItems(imports);
    }
    
    public void setTvOriginPayDataPrev(ObservableList<OriginPayData> payData) {
    	tvOriginPayDataPrevOrg.setCellValueFactory(new PropertyValueFactory<OriginPayData, Integer>("originID"));
    	tvOriginPayDataPrevId.setCellValueFactory(new PropertyValueFactory<OriginPayData, String>("empID"));
    	tvOriginPayDataPrevName.setCellValueFactory(new PropertyValueFactory<OriginPayData, String>("empName"));
    	tvOriginPayDataPrevRegHours.setCellValueFactory(new PropertyValueFactory<OriginPayData, Double>("originHoursReg"));
    	tvOriginPayDataPrevOtHours.setCellValueFactory(new PropertyValueFactory<OriginPayData, Double>("originHoursOT"));
    	tvOriginPayDataPrevRate.setCellValueFactory(new PropertyValueFactory<OriginPayData, Double>("originRate"));
    	
    	tvOriginPayDataPrev.setItems(payData);
    }
    
    public void setTvEmployee(ObservableList<Employee> employees) {
    	tvEmployeeID.setCellValueFactory(new PropertyValueFactory<Employee, String>("empID"));
    	tvEmployeeName.setCellValueFactory(new PropertyValueFactory<Employee, String>("empName"));
    	tvEmployee.setItems(employees);
    }
    
    public void setTvEmpModAdd() {
    	ObservableList<String> modTypes = FXCollections.observableArrayList();
    	modTypes = ModType.fill();
    	tvEmpModAddType.setCellFactory(ComboBoxTableCell.forTableColumn(modTypes));
    	tvEmpModAddDate.setCellFactory(TextFieldTableCell.forTableColumn());
/*    	tvEmpModAddDate.onEditCommit(new EventHandler<CellEditEvent<ModEmp, String>>() {

			@Override
			public void handle(CellEditEvent<ModEmp, String> arg0) {
				// TODO Auto-generated method stub
				((ModEmp) t)
			}
    		
    	})*/
    	tvEmpModAddAmount.setCellFactory(TextFieldTableCell.forTableColumn());
    	tvEmpModAddDescrip.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    
    public void comboBoxFill(ComboBox<String> box, ObservableList list) {
    	box.setItems(list);
    	box.getSelectionModel().clearAndSelect(0);
    }
    
    public void listViewFill(ListView lv, ObservableList list) {
    	lv.setItems(list);
    }
    
    public void lstEmployeeFill() {
    	lstEmployee.setItems(Employee.fillEmployeeName(Company.selectCompany(cbCompany.getValue())));
    	listViewFill(lstEmployee, Employee.fillEmployeeName(Company.selectCompany(cbCompany.getValue())));
    }
    
    public void tvEmployeeFill() {
    	tvEmployee.setItems(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
    }
    
    public void lstModTypeFill() {
    	lstModType.setItems(ModType.fill());
    	listViewFill(lstModType, ModType.fill());
    }
    
    public void clearTableData(TableView tv) {
    	for(int i = 0; i < tv.getItems().size(); i++)
    		tv.getItems().clear();
    }


}
