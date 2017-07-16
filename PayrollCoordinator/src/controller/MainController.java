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
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import model.PaycomTimecard;

public class MainController implements Initializable {

    private ObservableList<EmployeeOriginal> originPayData = FXCollections.observableArrayList();
    private ObservableList<String> payrollTypes = FXCollections.observableArrayList();
    private ObservableList<String> payrollRules = FXCollections.observableArrayList("7(i) Exemption", "Commission Overtime", "Traditional Overtime");
    private ObservableList<String> addToPayTypes = FXCollections.observableArrayList("<All>", "Commission Modifications", "Separate Modifications");

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
    private TableColumn<ModEmp, String> tvEmployeeModDetailEarn;
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
    @FXML
    private Button btnAddEmployee;
    @FXML
    private Button btnUpdateEmployee;
    @FXML
    private TableView<PaycomTimecard> tvPaycomTimecard;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardEmployee;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardDept;
    @FXML
    private TableColumn<PaycomTimecard, Date> tvPaycomTimecardDate;
    @FXML
    private TableColumn<PaycomTimecard, Time> tvPaycomTimecardPunch;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardType;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardEarning;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardTax;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardComments;
    @FXML
    private TableColumn<PaycomTimecard, String> tvPaycomTimecardLabor;
    @FXML
    private TableColumn<PaycomTimecard, Double> tvPaycomTimecardHours;
    @FXML
    private TableColumn<PaycomTimecard, Double> tvPaycomTimecardDollars;
    @FXML
    private TableColumn<PaycomTimecard, Double> tvPaycomTimecardRate;
    @FXML
    private TableColumn<PaycomTimecard, Double> tvPaycomTimecardUnits;
    @FXML
    private DatePicker dpPaycomTimecard;
    @FXML
    private Button btnGenerateTimecard;
    @FXML
    private Button btnExportPaycom;
    @FXML
    private DatePicker dpEditModDate;
    @FXML
    private ComboBox<String> cbEditModType;
    @FXML
    private TextField txtEditModAmount;
    @FXML
    private TextField txtEditModHours;
    @FXML
    private TextArea taEditModDescrip;
    @FXML
    private Button btnEditMod;
    @FXML
    private Button btnDeleteMod;
    @FXML
    private TitledPane tpEditModification;
    @FXML
    private TableView<ModType> tvModTypes;
    @FXML
    private TableColumn<ModType, String> tvModTypesID;
    @FXML
    private TableColumn<ModType, String> tvModTypesEarningCode;
    @FXML
    private TableColumn<ModType, String> tvModTypesName;
    @FXML
    private ComboBox<String> cbAddPayType;
    @FXML
    private CheckBox ckAddModRate;
    @FXML
    private CheckBox ckEditModRate;
    @FXML
    private Button btnDeleteTimecard;
    @FXML
    private TextField txtSearchEmployee;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comboBoxFill(cbCompany, Company.fillCompanyName());
		comboBoxFill(cbOTRule, payrollRules);
		comboBoxFill(cbEditModType, ModType.fill());
		comboBoxFill(cbAddPayType, addToPayTypes);
		setTvModType(ModType.fillModTypes());
		setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		dpDateEndingPrev.setValue(LocalDate.now());
		dpExportDateEnding.setValue(LocalDate.now());
		dpPaycomTimecard.setValue(LocalDate.now());
		setTvOriginPayDataPrev(OriginPayData.fillOriginPayData(Company.selectCompany(cbCompany.getValue()), dpDateEndingPrev.getValue()));
		setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue()));
    	dpExportEndDate.setValue(dpExportDateEnding.getValue());
    	dpExportStartDate.setValue(dpExportDateEnding.getValue().minusDays(6));
		tvEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				tvEmployeeToggle();
			}
			
		});
		tvEmployee.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				tvEmployeeToggle();
			}
			
		});
		
		tvExportPayData.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				tvExportPayDataToggle();
			}
			
		});
		tvExportPayData.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				tvExportPayDataToggle();
			}
			
		});
		
		tvEmployeeModDetail.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				tvEmployeeModDetailToggle();
			}
			
		});
		tvEmployeeModDetail.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				tvEmployeeModDetailToggle();
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
		ModPayData modData = ModPayData.getModPayData(tvExportPayData.getSelectionModel().getSelectedItem().getModID());
		double rate = oldData.getOriginRate();
		double reg = oldData.getOriginHoursReg();
		double ot = oldData.getOriginHoursOT();
		double oldGross;
		if(modData.getModPayrollRule().equals("7(i) Exemption"))
			oldGross = Calc.grossCalc7i(rate, reg, ot);
		else
			oldGross = Calc.grossCalcOT(rate, reg, ot);
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
		double newGross;
		ModPayData modData = ModPayData.getModPayData(tvExportPayData.getSelectionModel().getSelectedItem().getModID());
		
		if(modData.getModPayrollRule().equals("7(i) Exemption"))
			newGross = Calc.grossCalc7i(rate, reg, ot);
		else
			newGross = Calc.grossCalcOT(rate, reg, ot);
		
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
			double rate;
			try {
				rate = tvOriginPayData.getItems().get(i).getOriginRate();
			} catch(Exception e) {
				rate = 0;
			}
			double ot;
			try {
				ot = tvOriginPayData.getItems().get(i).getOriginHoursOT();
			} catch(Exception e) {
				ot = 0;
			}
			
			payData.add(new OriginPayData(
					Date.valueOf(dpOriginDateEnding.getValue()),
					company.getCoID(),
					tvOriginPayData.getItems().get(i).getEmpID(),
					tvOriginPayData.getItems().get(i).getOriginHoursReg(),
					ot,
					rate
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
    	
    	
/*    	try {
			Parent parent = FXMLLoader.load(getClass().getResource("/view/AddEmployee.fxml"));
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Add Employee(s)");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    
    public void btnClearOriginPayData_Clicked(ActionEvent event) {
    	clearTableData(tvOriginPayData);
    }
    
    public void btnGenerateTimecard_Clicked(ActionEvent event) {
    	Company company = Company.selectCompany(cbCompany.getValue().toString());
    	ObservableList<ModPayData> modData = ModPayData.getModPayDataAll(company, dpPaycomTimecard.getValue());
    	ObservableList<ModEmp> modEmps = ModEmp.getModEmpPassive(company, dpPaycomTimecard.getValue().minusDays(6), dpPaycomTimecard.getValue());
    	final DecimalFormat df = new DecimalFormat("###.##");
    	
    	for(ModPayData data : modData) {
    		if(data.getModPayrollRule().equals("Traditional Overtime")) {
    			if(data.getModHoursReg() <= 40) {
    				PaycomTimecard.insertOrUpdate(new PaycomTimecard(data.getEmpID(), dpPaycomTimecard.getValue(),
    						"R", "", data.getModHoursReg(), 0, 0));
    			}
    			else {
    				PaycomTimecard.insertOrUpdate(new PaycomTimecard(data.getEmpID(), dpPaycomTimecard.getValue(),
    						"R", "", data.getModHoursReg(), 0, 0));
    				PaycomTimecard.insertOrUpdate(new PaycomTimecard(data.getEmpID(), dpPaycomTimecard.getValue(),
    						"O", "", data.getModHoursOT(), 0, 0));
    			}
    		}
    		else {
				if (data.getModHoursReg() <= 40) {
					PaycomTimecard.insertOrUpdate(new PaycomTimecard(data.getEmpID(), dpPaycomTimecard.getValue(),
							"RGT", "", data.getModHoursReg(), 0, data.getModRate()));
				} 
				else {
					//double newHoursReg = data.getModHoursReg() - (data.getModHoursReg() - 40);
					double ot = data.getModHoursReg() - 40;
					double otRate = Double.valueOf(df.format(data.getModRate() * 0.5));
					PaycomTimecard.insertOrUpdate(new PaycomTimecard(data.getEmpID(), dpPaycomTimecard.getValue(),
							"RGT", "", data.getModHoursReg(), 0, data.getModRate()));
					PaycomTimecard.insertOrUpdate(
							new PaycomTimecard(data.getEmpID(), dpPaycomTimecard.getValue(), "OTT", "", ot, 0, otRate));
				}
    		}
    	}
    	
    	for(ModEmp modEmp : modEmps) {
    		PaycomTimecard.insertOrUpdate(new PaycomTimecard(modEmp.getEmpID(), modEmp.getModEmpDate().toLocalDate(), modEmp.getEarningCode(), modEmp.getModEmpDescrip(), modEmp.getModEmpHours(), 
    				modEmp.getModEmpAmount(), 0));
    	}
    	
    	setTvPaycomTimecard(PaycomTimecard.getPaycomTimecard(dpPaycomTimecard.getValue(), Company.selectCompany(cbCompany.getValue().toString())));
    }
    
    public void btnDeleteTimecard_Clicked(ActionEvent event) {
    	PaycomTimecard timecard = new PaycomTimecard(
    			tvPaycomTimecard.getSelectionModel().getSelectedItem().getTimecardID());
    	timecard.delete();
    	setTvPaycomTimecard(PaycomTimecard.getPaycomTimecard(dpPaycomTimecard.getValue(), Company.selectCompany(cbCompany.getValue().toString())));
    }
    
    public void dpPaycomTimecard_ValueChanged(ActionEvent event) {
    	setTvPaycomTimecard(PaycomTimecard.getPaycomTimecard(dpPaycomTimecard.getValue(), Company.selectCompany(cbCompany.getValue().toString())));
    }
    
    public void cbAddPayType_ValueChanged(ActionEvent event) {
    	setTvModType(ModType.fillModTypes(cbAddPayType.getValue().toString()));
    }
    
    public void cbCompany_ValueChanged(ActionEvent event) {
    	txtEditModAmount.clear();
    	txtEditModHours.clear();
    	taEditModDescrip.clear();
    	txtEditEmpID.clear();
    	txtEditEmpNameFirst.clear();
    	txtEditEmpNameLast.clear();
    	
    	tvEmployeeFill();
    	setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
		setTvOriginPayDataPrev(OriginPayData.fillOriginPayData(Company.selectCompany(cbCompany.getValue()), dpDateEndingPrev.getValue()));
		setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue()));
		setTvPaycomTimecard(PaycomTimecard.getPaycomTimecard(dpPaycomTimecard.getValue(), Company.selectCompany(cbCompany.getValue().toString())));
		clearTableData(tvEmployeeModDetail);
		clearAddModPanel();
		clearEditModPanel();
    }
    
    public void btnModInsert_Clicked(ActionEvent event) {
    	ModType mod = new ModType(txtModInsert.getText());
    	ModType.insert(mod);
    	lstModTypeFill();
    	comboBoxFill(cbAddModType, ModType.fill());
    	txtModInsert.clear();
    }
    
    public void btnAddMod_Clicked(ActionEvent event) {
    	double hours;
    	if(txtAddModHours.getText().isEmpty())
    		hours = 0;
    	else
    		hours = Double.parseDouble(txtAddModHours.getText());
    	
    	ModEmp.insert(new ModEmp(
    			ModType.searchModTypeID(cbAddModType.getSelectionModel().getSelectedItem().toString()),
    			tvEmployee.getItems().get(tvEmployee.getSelectionModel().getSelectedIndex()).getEmpID(),
    			dpAddModDate.getValue(),
    			Double.parseDouble(txtAddModAmount.getText()), hours,
    			taAddModDescrip.getText()
    			));
    	
    	setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString()));
    	txtAddModAmount.clear();
    	txtAddModHours.clear();
    	taAddModDescrip.clear();
    	
    }
    
    public void btnExportUpdatePayroll_Clicked(ActionEvent event) {
    	ObservableList<ModPayData> modData = ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue());
    	ModHistory.insertOrUpdate(modData, dpExportStartDate.getValue(), dpExportEndDate.getValue(), cbCompany.getValue());
    	setTvExportPayData(ModPayData.getModPayData(Company.selectCompany(cbCompany.getValue()), dpExportDateEnding.getValue()));
    }
    
/*    public void btnExportPayroll_Clicked(ActionEvent event) {
    	ObservableList<ModPayData> modDatas = FXCollections.observableArrayList();
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setHeaderText("QDRIVE - Payroll Coordinator");
    	dialog.setContentText("What would you like to name the file?");
    	
    	Optional<String> result = dialog.showAndWait();
    	if(result.isPresent()) {
    		String fileName = result.get();
			try {
				CSVWriter writer = new CSVWriter(
						new FileWriter(System.getProperty("user.home") + "/Desktop/" + fileName + ".csv"), '\t');
				for (int i = 0; i < tvExportPayData.getItems().size(); i++) {
					modDatas.add(new ModPayData(tvExportPayData.getItems().get(i).getEmpID(),
							tvExportPayData.getItems().get(i).getEmpName(),
							tvExportPayData.getItems().get(i).getModHoursReg(),
							tvExportPayData.getItems().get(i).getModHoursOT(),
							tvExportPayData.getItems().get(i).getModRate()));
				}

				for (ModPayData modData : modDatas) {
					String line = "" + modData.getEmpID() + "," + modData.getEmpName() + "," + modData.getModHoursReg()
							+ "," + modData.getModHoursOT() + "," + modData.getModRate();
					String[] lines = line.split(",");
					writer.writeNext(lines);
				}

				writer.close();

				AlertMessage confirmation = new AlertMessage(AlertType.CONFIRMATION,
						"Your file has successfully exported to your desktop!");
				confirmation.showAndWait();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AlertMessage error = new AlertMessage(AlertType.ERROR, "Your file did not export successfully...");
				error.showAndWait();
			}
    	}
    }*/
    
    public void btnExportPaycom_Clicked(ActionEvent event) {
    	ObservableList<PaycomTimecard> timecard = FXCollections.observableArrayList();
    	//String fileName = dpPaycomTimecard.getValue().toString();
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setHeaderText("QDRIVE - Payroll Coordinator");
    	dialog.setContentText("What would you like to name the file?");
    	
    	Optional<String> result = dialog.showAndWait();
    	if(result.isPresent()) {
			try {
				String fileName = result.get();
				CSVWriter writer = new CSVWriter(
						new FileWriter(System.getProperty("user.home") + "/Desktop/" + fileName + ".csv"), '\t');
				for (int i = 0; i < tvPaycomTimecard.getItems().size(); i++) {
					timecard.add(new PaycomTimecard(tvPaycomTimecard.getItems().get(i).getEmpID(),
							tvPaycomTimecard.getItems().get(i).getDeptCode(),
							tvPaycomTimecard.getItems().get(i).getDate().toLocalDate(),
							tvPaycomTimecard.getItems().get(i).getPunchtime(),
							tvPaycomTimecard.getItems().get(i).getPunchtype(),
							tvPaycomTimecard.getItems().get(i).getModTypeID(),
							tvPaycomTimecard.getItems().get(i).getTaxCode(),
							tvPaycomTimecard.getItems().get(i).getComments(),
							tvPaycomTimecard.getItems().get(i).getLaborAllocation(),
							tvPaycomTimecard.getItems().get(i).getHours(),
							tvPaycomTimecard.getItems().get(i).getDollars(),
							tvPaycomTimecard.getItems().get(i).getTempRate(),
							tvPaycomTimecard.getItems().get(i).getUnits()));
				}

				for (PaycomTimecard card : timecard) {
					String hours, dollars, rate;
					if (card.getHours() == 0)
						hours = "";
					else
						hours = String.valueOf(card.getHours());
					if (card.getDollars() == 0)
						dollars = "";
					else
						dollars = String.valueOf(card.getDollars());
					if (card.getTempRate() == 0)
						rate = "";
					else
						rate = String.valueOf(card.getTempRate());

					String line = "" + card.getEmpID().toString() + "," + card.getDeptCode() + "," + card.getDate().toLocalDate()
							+ "," + card.getPunchtime() + "," + card.getPunchtype() + "," + card.getModTypeID() + ","
							+ card.getTaxCode() + "," + card.getComments() + "," + card.getLaborAllocation() + ","
							+ hours + "," + dollars + "," + rate + "," + "";
					String[] lines = line.split(",");
					writer.writeNext(lines);
				}

				writer.close();

				AlertMessage confirmation = new AlertMessage(AlertType.CONFIRMATION,
						"Your file has successfully exported to your desktop!");
				confirmation.showAndWait();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AlertMessage error = new AlertMessage(AlertType.ERROR, "Your file did not export successfully...");
				error.showAndWait();
			}
    	}
    }
    
    public void btnAddEmployee_Clicked(ActionEvent event) {
    	StringBuilder name = new StringBuilder();
    	name.append(txtNewEmpNameLast.getText() + ", " + txtNewEmpNameFirst.getText());
    	Company company = Company.selectCompany(cbCompany.getValue().toString());
    	Employee emp = new Employee(txtNewEmpID.getText(), name.toString(), company.getCoID());
    	emp.insert(emp);
    	setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
    	txtNewEmpNameLast.clear();
    	txtNewEmpNameFirst.clear();
    	txtNewEmpID.clear();
    }
    
    public void btnEditEmployee_Clicked(ActionEvent event) {
    	StringBuilder name = new StringBuilder();
    	name.append(txtEditEmpNameLast.getText() + ", " + txtEditEmpNameFirst.getText());
    	Company company = Company.selectCompany(cbCompany.getValue().toString());
    	String orgEmp = tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString();
    	Employee emp = new Employee(txtEditEmpID.getText(), name.toString(), company.getCoID());
    	emp.update(emp, orgEmp);
    	setTvEmployee(Employee.fillEmployee(Company.selectCompany(cbCompany.getValue())));
    }

    public void btnEditMod_Clicked(ActionEvent event) {
    	ModEmp modEmp = new ModEmp(
    			tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModEmpID(),
    			ModType.searchModTypeID(cbEditModType.getValue().toString()),
    			tvEmployee.getSelectionModel().getSelectedItem().getEmpID(),
    			Date.valueOf(dpEditModDate.getValue()),
    			Double.valueOf(txtEditModAmount.getText()),
    			Double.valueOf(txtEditModHours.getText()),
    			taEditModDescrip.getText()
    			);
    	ModEmp.update(modEmp);
    	setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString()));
    }
    
    public void btnDeleteMod_Clicked(ActionEvent event) {
    	ModEmp modEmp = new ModEmp(tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModEmpID());
    	modEmp.delete();
    	txtEditModAmount.clear();
    	txtEditModHours.clear();
    	taEditModDescrip.clear();
    	setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString()));
    }
    
    private ObservableList<EmployeeOriginal> importOriginData(File file) {
    	String[] nextLine;
    	try {
    		CSVReader reader = new CSVReader(new FileReader(file.getAbsolutePath()));
    		while((nextLine = reader.readNext()) != null) {
    			String id = nextLine[0].substring(nextLine[0].length() - 4);
    			if(nextLine.length == 5)
    				originPayData.add(new EmployeeOriginal(id, nextLine[1], 
    						Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3]), 
    						Double.parseDouble(nextLine[4])));
    			if(nextLine.length == 4)
    				originPayData.add(new EmployeeOriginal(id, nextLine[1], 
        					Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3])));
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
    
    public void ckAddModRate_isChecked(ActionEvent event) {
    	boolean checked = modRateChanged(ckAddModRate);
    	ObservableList<ModType> modTypes = FXCollections.observableArrayList();
    	ObservableList<String> modTypeStrings = FXCollections.observableArrayList();
    	if(checked)
    		modTypes = ModType.fillModTypes("Commission Modifications");
    	else 
    		modTypes = ModType.fillModTypes("Separate Modifications");
    	
		for(ModType modType : modTypes)
			modTypeStrings.add(modType.getModTypeName());
		comboBoxFill(cbAddModType, modTypeStrings);
    }
    
    public void ckEditModRate_isChecked(ActionEvent event) {
    	boolean checked = modRateChanged(ckEditModRate);
    	ObservableList<ModType> modTypes = FXCollections.observableArrayList();
    	ObservableList<String> modTypeStrings = FXCollections.observableArrayList();
    	if(checked)
    		modTypes = ModType.fillModTypes("Commission Modifications");
    	else 
    		modTypes = ModType.fillModTypes("Separate Modifications");
    	
		for(ModType modType : modTypes)
			modTypeStrings.add(modType.getModTypeName());
		comboBoxFill(cbEditModType, modTypeStrings);
    }
    
    public void setValues(ObservableList<EmployeeOriginal> imports) {
    	tvOriginPayDataCOLid.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empID"));
    	tvOriginPayDataCOLname.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empName"));
    	tvOriginPayDataCOLregHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursReg"));
    	tvOriginPayDataCOLotHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursOT"));
    	Object test;
    	//test = Calc.testObj((Object)imports.get(0).getOriginRate());
    	try {
    		test = Calc.testObj((Object)imports.get(0).getOriginRate());
    	} catch(NullPointerException e) {
    		test = null;
    	}
    	if(test != null )
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
    	FilteredList<Employee> filterEmployees = new FilteredList<>(employees, p -> true);
    	txtSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> {
    		filterEmployees.setPredicate(employee -> {
    			//if filter is empty, display all
    			if(newValue == null || newValue.isEmpty()) {
    				return true;
    			}
    			
    			String lowerCaseFilter = newValue.toLowerCase();
    			
    			if(employee.getEmpName().toLowerCase().contains(lowerCaseFilter)) {
    				return true;
    			}
    			else if(employee.getEmpID().toLowerCase().contains(lowerCaseFilter)) {
    				return true;
    			}
    			
    			return false;
    		});
    	});
    	
    	SortedList<Employee> sortedEmployees = new SortedList<>(filterEmployees);
    	sortedEmployees.comparatorProperty().bind(tvEmployee.comparatorProperty());
    	tvEmployee.setItems(sortedEmployees);
    }
    
    public void setTvEmployeeModDetail(ObservableList<ModEmp> modEmps) {
    	tvEmployeeModDetailDate.setCellValueFactory(new PropertyValueFactory<ModEmp, Date>("modEmpDate"));
    	tvEmployeeModDetailEarn.setCellValueFactory(new PropertyValueFactory<ModEmp, String>("earningCode"));
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
    
    public void setTvPaycomTimecard(ObservableList<PaycomTimecard> timecard) {
    	tvPaycomTimecardEmployee.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, String>("empID"));
    	tvPaycomTimecardDept.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, String>("deptCode"));
    	tvPaycomTimecardDate.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, Date>("date"));
    	tvPaycomTimecardPunch.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, Time>("punchtime"));
    	tvPaycomTimecardEarning.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, String>("modTypeID"));
    	tvPaycomTimecardTax.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, String>("taxCode"));
    	tvPaycomTimecardComments.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, String>("comments"));
    	tvPaycomTimecardLabor.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, String>("laborAllocation"));
    	tvPaycomTimecardHours.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, Double>("hours"));
    	tvPaycomTimecardDollars.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, Double>("dollars"));
    	tvPaycomTimecardRate.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, Double>("tempRate"));
    	tvPaycomTimecardUnits.setCellValueFactory(new PropertyValueFactory<PaycomTimecard, Double>("units"));
    	tvPaycomTimecard.setItems(timecard);
    }
    
    public void setTvModType(ObservableList<ModType> modTypes) {
    	tvModTypesID.setCellValueFactory(new PropertyValueFactory<ModType, String>("modTypeID"));
    	tvModTypesEarningCode.setCellValueFactory(new PropertyValueFactory<ModType, String>("earningCode"));
    	tvModTypesName.setCellValueFactory(new PropertyValueFactory<ModType, String>("modTypeName"));
    	tvModTypes.setItems(modTypes);
    }
    
    public void tvEmployeeToggle() {
		String empName = tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString();
		tpAddModification.setText(
				"Add Modification (" + empName + ")");
		setTvEmployeeModDetail(ModEmp.fillByEmployee(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString()));
		tpAddModification.setDisable(false);
		tpAddModification.setExpanded(true);
		ArrayList<String> name = new ArrayList<>();
		for(String val : empName.split(", ")) {
			name.add(val);
		}
		txtEditEmpNameLast.setText(name.get(0));
		txtEditEmpNameFirst.setText(name.get(1));
		txtEditEmpID.setText(tvEmployee.getSelectionModel().getSelectedItem().getEmpID().toString());
		clearEditModPanel();
    }
    
    public void tvExportPayDataToggle() {
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
    
    public void tvEmployeeModDetailToggle() {
		tpEditModification.setText(
				"Update Modification (" + tvEmployee.getSelectionModel().getSelectedItem().getEmpName().toString() + ")");
		dpEditModDate.setValue(tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModEmpDate().toLocalDate());
		txtEditModAmount.setText(String.valueOf(tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModEmpAmount()));
		txtEditModHours.setText(String.valueOf(tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModEmpHours()));
		taEditModDescrip.setText(String.valueOf(tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModEmpDescrip()));
		cbEditModType.setValue(String.valueOf(tvEmployeeModDetail.getSelectionModel().getSelectedItem().getModTypeName()));
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
    
    public boolean modRateChanged(CheckBox ckbox) {
    	if(ckbox.isSelected())
    		return true;
    	else
    		return false;
    }
    
    public void clearTableData(TableView tv) {
    	for(int i = 0; i < tv.getItems().size(); i++)
    		tv.getItems().clear();
    }
    
    public void clearAddModPanel() {
    	tpAddModification.setText("Add Modification");
    }
    
    public void clearEditModPanel() {
    	tpEditModification.setText("Update Modification");
    	dpEditModDate.setValue(null);
    	txtEditModAmount.clear();
    	txtEditModHours.clear();
    	comboBoxFill(cbEditModType, ModType.fill());
    	taEditModDescrip.clear();
    }


}
