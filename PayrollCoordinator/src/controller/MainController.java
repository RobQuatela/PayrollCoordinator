package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import com.opencsv.CSVWriter;

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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.AlertMessage;
import model.Calc;
import model.Company;
import model.Employee;
import model.EmployeeOriginal;
import model.ModEmp;
import model.ModHistory;
import model.ModPayData;
import model.ModType;
import model.OriginPayData;
import model.PayData;

public class MainController implements Initializable {

    private ObservableList<EmployeeOriginal> originPayData = FXCollections.observableArrayList();
    private ObservableList<String> payrollTypes = FXCollections.observableArrayList();
    private ObservableList<String> payrollRules = FXCollections.observableArrayList("7(i) Exemption", "Traditional Overtime");

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
    @FXML
    private TitledPane tpAddModification;
    @FXML
    private DatePicker dpAddModDate;
    @FXML
    private ComboBox<String> cbAddModType;
    @FXML
    private TextField txtAddModAmount;
    @FXML
    private TextField txtAddModHours;
    @FXML
    private TextArea taAddModDescrip;
    @FXML
    private TableView<ModEmp> tvEmployeeModDetail;
    @FXML
    private TableColumn<ModEmp, Date> tvEmployeeModDetailDate;
    @FXML
    private TableColumn<ModEmp, String> tvEmployeeModDetailMod;
    @FXML
    private TableColumn<ModEmp, Double> tvEmployeeModDetailAmount;
    @FXML
    private TableColumn<ModEmp, Double> tvEmployeeModDetailHours;
    @FXML
    private TableColumn<ModEmp, String> tvEmployeeModDetailDescrip;
    @FXML
    private TableView<ModPayData> tvExportPayData;
    @FXML
    private TableColumn<ModPayData, String> tvExportPayDataEmpID;
    @FXML
    private TableColumn<ModPayData, String> tvExportPayDataName;
    @FXML
    private TableColumn<ModPayData, Double> tvExportPayDataRegHours;
    @FXML
    private TableColumn<ModPayData, Double> tvExportPayDataOTHours;
    @FXML
    private TableColumn<ModPayData, Double> tvExportPayDataRate;
    @FXML
    private DatePicker dpExportDateEnding;
    @FXML
    private DatePicker dpExportStartDate;
    @FXML
    private DatePicker dpExportEndDate;
    @FXML
    private Button btnExportUpdatePayroll;
    @FXML
    private Button btnExportPayroll;
    @FXML
    private TableView<ModEmp> tvModDetail;
    @FXML
    private TableColumn<ModEmp, String> tvModDetailType;
    @FXML
    private TableColumn<ModEmp, Double> tvModDetailAmount;
    @FXML
    private TableColumn<ModEmp, Double> tvModDetailHours;
    @FXML
    private TableColumn<ModEmp, String> tvModDetailDescrip;
    @FXML
    private Label lblExpOldGross;
    @FXML
    private Label lblExpMod;
    @FXML
    private Label lblExpNewGross;
    @FXML
    private Label lblExpOriginRate;
    @FXML
    private Label lblExpModRate;
    @FXML
    private TitledPane tpModDetail;
    @FXML
    private ComboBox cbOTRule;
    @FXML
    private TextField txtNewEmpID;
    @FXML
    private TextField txtNewEmpNameFirst;
    @FXML
    private TextField txtNewEmpNameLast;
    @FXML
    private TextField txtEditEmpID;
    @FXML
    private TextField txtEditEmpNameFirst;
    @FXML
    private TextField txtEditEmpNameLast;
    
    
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comboBoxFill(cbCompany, Company.fillCompanyName());
		comboBoxFill(cbOTRule, payrollRules);
		setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		dpDateEndingPrev.setValue(LocalDate.of(2017, 4, 22));
		dpExportDateEnding.setValue(LocalDate.of(2017, 4, 22));
		setTvOriginPayDataPrev(OriginPayData.fillOriginPayData(Company.selectCompany(cbCompany.getValue()), dpDateEndingPrev.getValue()));
		setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue()));
    	dpExportEndDate.setValue(dpExportDateEnding.getValue());
    	dpExportStartDate.setValue(dpExportDateEnding.getValue().minusDays(6));
		listViewFill(lstModType, ModType.fill());
		tvEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				String empName = tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString();
				tpAddModification.setText(
						"Add Modification (" + empName + ")");
				setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString()));
				tpAddModification.setDisable(false);
				tpAddModification.setExpanded(true);
				for(String val : empName.split(", ")) {
					txtEditEmpNameLast.setText(val);
					txtEditEmpNameFirst.setText(val);
				}
				txtEditEmpID.setText(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString());
			}
			
		});
		
		tvExportPayData.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				tpModDetail.setText(
						"Modification Detail (" + tvExportPayData.getSelectionModel().getSelectedItem().getEmpName().toString() + ")");
				setTvModDetail(
						ModEmp.fillByEmployee(
								tvExportPayData.getSelectionModel().getSelectedItem().getEmpID().toString(), 
								dpExportStartDate.getValue(), dpExportEndDate.getValue()));
				setLblExpOldGross();
				setLblExpMod();
				setLblExpNewGross();
				setLblExpOriginRate();
				setLblExpModRate();
			}
			
		});
		
		payrollTypes.add("Original");
		payrollTypes.add("Modified");
		comboBoxFill(cbPayrollType, payrollTypes);
		comboBoxFill(cbAddModType, ModType.fill());
		//setTvEmpModAdd();
		//setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString()));
	}
	
	public void setLblExpOldGross() {
		OriginPayData oldData = OriginPayData.getOriginPayData(tvExportPayData.getSelectionModel().getSelectedItem().getOriginID());
		double rate = oldData.getOriginRate();
		double reg = oldData.getOriginHoursReg();
		double ot = oldData.getOriginHoursOT();
		double oldGross = Calc.grossCalc7i(rate, reg, ot);
		lblExpOldGross.setText(String.valueOf(oldGross));
	}
	
	public void setLblExpMod() {
		double mod = 0;
		for(int i = 0; i < tvModDetail.getItems().size(); i++) {
			mod += tvModDetail.getItems().get(i).getModEmpAmount();
		}
		lblExpMod.setText(String.valueOf(mod));
	}
	
	public void setLblExpNewGross() {
		double rate = tvExportPayData.getSelectionModel().getSelectedItem().getModRate();
		double reg = tvExportPayData.getSelectionModel().getSelectedItem().getModHoursReg();
		double ot = tvExportPayData.getSelectionModel().getSelectedItem().getModHoursOT();
		double newGross = Calc.grossCalc7i(rate, reg, ot);
		lblExpNewGross.setText(String.valueOf(newGross));
	}
	
	public void setLblExpOriginRate() {
		OriginPayData oldData = OriginPayData.getOriginPayData(tvExportPayData.getSelectionModel().getSelectedItem().getOriginID());
		lblExpOriginRate.setText(String.valueOf(oldData.getOriginRate()));
	}
	
	public void setLblExpModRate() {
		ModPayData modData = ModPayData.getModPayData(tvExportPayData.getSelectionModel().getSelectedItem().getModID());
		lblExpModRate.setText(String.valueOf(modData.getModRate()));
	}
    
	public void btnSaveImport_Clicked(ActionEvent event) {
		Employee.Insert(originPayData, Company.selectCompany(cbCompany.getValue()));
		setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		ObservableList<OriginPayData> payData = FXCollections.observableArrayList();
		Company company = Company.selectCompany(cbCompany.getValue().toString());
		
		for(int i = 0; i < tvOriginPayData.getItems().size(); i++) {
			payData.add(new OriginPayData(
					Date.valueOf(dpOriginDateEnding.getValue()),
					company.getCoID(),
					tvOriginPayData.getItems().get(i).getEmpID(),
					tvOriginPayData.getItems().get(i).getOriginHoursReg(),
					tvOriginPayData.getItems().get(i).getOriginHoursOT(),
					tvOriginPayData.getItems().get(i).getOriginRate()
					));
		}
		PayData.insertOrUpdate(payData, cbOTRule.getValue().toString());
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
    	setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		setTvOriginPayDataPrev(OriginPayData.fillOriginPayData(Company.selectCompany(cbCompany.getValue()), dpDateEndingPrev.getValue()));
		setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue()));
    }
    
    public void btnModInsert_Clicked(ActionEvent event) {
    	ModType mod = new ModType(txtModInsert.getText());
    	ModType.insert(mod);
    	lstModTypeFill();
    	comboBoxFill(cbAddModType, ModType.fill());
    	txtModInsert.clear();
    }
    
    public void btnAddMod_Clicked(ActionEvent event) {
    	ModEmp.insert(new ModEmp(
    			ModType.searchModTypeID(cbAddModType.getSelectionModel().getSelectedItem().toString()),
    			tvEmployee.getItems().get(tvEmployee.getSelectionModel().getSelectedIndex()).getEmpID(),
    			dpAddModDate.getValue(),
    			Double.parseDouble(txtAddModAmount.getText()), Double.parseDouble(txtAddModHours.getText()),
    			taAddModDescrip.getText()
    			));
    	
    	setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString()));
    	txtAddModAmount.clear();
    	txtAddModHours.clear();
    	taAddModDescrip.clear();
    	
    }
    
    public void btnExportUpdatePayroll_Clicked(ActionEvent event) {
    	ObservableList<ModPayData> modData = ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue());
    	ModHistory.insert(modData, dpExportStartDate.getValue(), dpExportEndDate.getValue(), cbCompany.getValue());
    	setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue()));
    }
    
    public void btnExportPayroll_Clicked(ActionEvent event) {
    	ObservableList<ModPayData> modDatas = FXCollections.observableArrayList();
    	
    	try {
			CSVWriter writer = new CSVWriter(new FileWriter(System.getProperty("user.home") + "/Desktop/testfile.csv"), '\t');
			for(int i = 0; i < tvExportPayData.getItems().size(); i++) {
				modDatas.add(new ModPayData(
						tvExportPayData.getItems().get(i).getEmpID(),
						tvExportPayData.getItems().get(i).getEmpName(),
						tvExportPayData.getItems().get(i).getModHoursReg(),
						tvExportPayData.getItems().get(i).getModHoursOT(),
						tvExportPayData.getItems().get(i).getModRate()
						));
			}
			
			for(ModPayData modData : modDatas ) {
				String line = "" + modData.getEmpID() + "," + modData.getEmpName() + "," + modData.getModHoursReg() +
						"," + modData.getModHoursOT() + "," + modData.getModRate();
				String[] lines = line.split(",");
				writer.writeNext(lines);
			}
			
			writer.close();
			
			AlertMessage confirmation = new AlertMessage(AlertType.CONFIRMATION, "Your file has successfully exported to your desktop!");
			confirmation.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AlertMessage error = new AlertMessage(AlertType.ERROR, "Your file did not export successfully...");
			error.showAndWait();
		}
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
    
    public void dpExportDateEnding_ValueChanged(ActionEvent event) {
    	setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()),
    			dpExportDateEnding.getValue()));		
    	dpExportEndDate.setValue(dpExportDateEnding.getValue());
    	dpExportStartDate.setValue(dpExportDateEnding.getValue().minusDays(6));
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
    
    public void setTvEmployeeModDetail(ObservableList<ModEmp> modEmps) {
    	tvEmployeeModDetailDate.setCellValueFactory(new PropertyValueFactory<ModEmp, Date>("modEmpDate"));
    	tvEmployeeModDetailMod.setCellValueFactory(new PropertyValueFactory<ModEmp, String>("modTypeName"));
    	tvEmployeeModDetailAmount.setCellValueFactory(new PropertyValueFactory<ModEmp, Double>("modEmpAmount"));
    	tvEmployeeModDetailHours.setCellValueFactory(new PropertyValueFactory<ModEmp, Double>("modEmpHours"));
    	tvEmployeeModDetailDescrip.setCellValueFactory(new PropertyValueFactory<ModEmp, String>("modEmpDescrip"));
    	tvEmployeeModDetail.setItems(modEmps);
    }
    
    public void setTvExportPayData(ObservableList<ModPayData> modData) {
    	tvExportPayDataEmpID.setCellValueFactory(new PropertyValueFactory<ModPayData, String>("empID"));
    	tvExportPayDataName.setCellValueFactory(new PropertyValueFactory<ModPayData, String>("empName"));
    	tvExportPayDataRegHours.setCellValueFactory(new PropertyValueFactory<ModPayData, Double>("modHoursReg"));
    	tvExportPayDataOTHours.setCellValueFactory(new PropertyValueFactory<ModPayData, Double>("modHoursOT"));
    	tvExportPayDataRate.setCellValueFactory(new PropertyValueFactory<ModPayData, Double>("modRate"));
    	tvExportPayData.setItems(modData);
    }
    
    public void setTvModDetail(ObservableList<ModEmp> modEmp) {
    	tvModDetailType.setCellValueFactory(new PropertyValueFactory<ModEmp, String>("modTypeName"));
    	tvModDetailAmount.setCellValueFactory(new PropertyValueFactory<ModEmp, Double>("modEmpAmount"));
    	tvModDetailHours.setCellValueFactory(new PropertyValueFactory<ModEmp, Double>("modEmpHours"));
    	tvModDetailDescrip.setCellValueFactory(new PropertyValueFactory<ModEmp, String>("modEmpDescrip"));
    	tvModDetail.setItems(modEmp);
    }
    
    public void comboBoxFill(ComboBox box, ObservableList list) {
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
