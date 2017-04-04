package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.opencsv.CSVReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import model.EmployeeOriginal;
import model.OriginPayData;

public class MainController implements Initializable {

    private ObservableList<EmployeeOriginal> originPayData = FXCollections.observableArrayList();
	
    @FXML
    private Button btnImportOriginData;
    @FXML
    private TableView<EmployeeOriginal> tvOriginPayData;
    @FXML
    private TextField txtFile;
    @FXML
    private TableColumn<EmployeeOriginal, Double> tvOriginPayDataCOLrate;
    @FXML
    private TableColumn<EmployeeOriginal, String> tvOriginPayDataCOLname;
    @FXML
    private TableColumn<EmployeeOriginal, Double> tvOriginPayDataCOLregHours;
    @FXML
    private TableColumn<EmployeeOriginal, Double> tvOriginPayDataCOLotHours;
    @FXML
    private ComboBox<String> cbWeek;
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	cbWeek = new ComboBox<>();
/*    	cbWeek.getItems().addAll(
    			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
    			13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
    			23, 24, 25, 26
    			);*/
    	cbWeek.getItems().addAll(
    			"test",
    			"test1"
    			);
	}
    
    
    public void btnImportOriginData_Clicked(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
    		setValues(importOriginData(selectedFile));
    		//txtFile.setText(selectedFile.getAbsolutePath());
    	}
    }
    
    private ObservableList<EmployeeOriginal> importOriginData(File file) {
    	String[] nextLine;
    	try {
    		CSVReader reader = new CSVReader(new FileReader(file.getAbsolutePath()));
    		while((nextLine = reader.readNext()) != null) {
    			originPayData.add(new EmployeeOriginal(nextLine[0], Double.parseDouble(nextLine[1]),
    					Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3])));
    		}
    	}catch(FileNotFoundException e) {
    		e.printStackTrace();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	return originPayData;
    }
    
    public void setValues(ObservableList<EmployeeOriginal> imports) {
    	tvOriginPayDataCOLname.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, String>("empName"));
    	tvOriginPayDataCOLregHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursReg"));
    	tvOriginPayDataCOLotHours.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originHoursOT"));
    	tvOriginPayDataCOLrate.setCellValueFactory(new PropertyValueFactory<EmployeeOriginal, Double>("originRate"));
    	tvOriginPayData.setItems(imports);
    }


}
