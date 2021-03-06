package model;

import java.time.LocalDate;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public interface PayData {
	
	public static void insertOrUpdate(ObservableList<OriginPayData> payData, String payrollRule) {
		ObservableList<OriginPayData> dup = OriginPayData.searchForDup(payData);
		ObservableList<OriginPayData> originUpdate = OriginPayData.searchForUpdates(dup);
		ObservableList<ModPayData> modUpdate = FXCollections.observableArrayList();

		if(dup.isEmpty()) {
			for(int i = 0; i < payData.size(); i++) {
				
				OriginPayData.insert(payData.get(i));
				OriginPayData data = OriginPayData.getOriginPayData(OriginPayData.searchLastID());
				//need to create modpaydata objects here instead of the main controller
				ModPayData.insert(new ModPayData(data.getOriginID(), data.getOriginHoursReg(),
						data.getOriginHoursOT(), data.getOriginRate(), payrollRule));
			}
			
			AlertMessage newData = new AlertMessage(AlertType.CONFIRMATION, "The following new data has been inserted!");
			newData = newData.originPayDataInfo(payData);
			newData.getButtonTypes().remove(0,2);
			newData.getButtonTypes().add(ButtonType.OK);
			newData.showAndWait();
		}
		else {
			for(int i = 0; i < dup.size(); i++) {
				for(int t = 0; t < payData.size(); t++) {
					if(payData.get(t).getEmpID() == dup.get(i).getEmpID()) {
						payData.remove(t);
						//modData.remove(t);
					}
				}
			}
			if(!originUpdate.isEmpty()) {
				AlertMessage alert = new AlertMessage(AlertType.INFORMATION,
						"The following employee(s) data has already been created for "
								+ originUpdate.get(0).getOriginEndDate()
								+ ". Would you like to update these values with the new values below?");
				alert = alert.originPayDataInfo(originUpdate);
				Optional<ButtonType> result = alert.showAndWait();

				//NEED TO CREATE AN UPDATE FOR INDIVIDUAL ORIGINPAYDATA OBJECT
				if (result.get() == alert.getButtonTypes().get(0)) {
					OriginPayData.update(originUpdate);
					for(OriginPayData update : originUpdate) {
						//OriginPayData.insert(update);
						//OriginPayData data = OriginPayData.getOriginPayData(OriginPayData.searchLastID());
						//ModPayData.insert(new ModPayData(data.getOriginID(), data.getOriginHoursReg(),
								//data.getOriginHoursOT(), data.getOriginRate(), payrollRule));
						ModPayData.update(new ModPayData(update.getOriginID(), update.getOriginHoursReg(),
								update.getOriginHoursOT(), update.getOriginRate(), payrollRule));
					}
/*					for(OriginPayData data : originUpdate) {
						modUpdate.add(new ModPayData(data.getOriginID(), 
								data.getOriginHoursReg(), data.getOriginHoursOT(),
								data.getOriginRate()));
					}
					ModPayData.update(modUpdate);*/
					
					if (!payData.isEmpty()) {
						for(OriginPayData data : payData) {
							OriginPayData.insert(data);
							OriginPayData insert = OriginPayData.getOriginPayData(OriginPayData.searchLastID());
							ModPayData.insert(new ModPayData(insert.getOriginID(), insert.getOriginHoursReg(),
									insert.getOriginHoursOT(), insert.getOriginRate(), payrollRule));
						}
/*						OriginPayData.insert(payData);
						ModPayData.insert(modData);*/
					}
				} else {
					AlertMessage test = new AlertMessage(AlertType.INFORMATION, "Duplicate data has been discarded...");
					test.showAndWait();

					for(OriginPayData data : payData) {
						OriginPayData.insert(data);
						OriginPayData insert = OriginPayData.getOriginPayData(OriginPayData.searchLastID());
						ModPayData.insert(new ModPayData(insert.getOriginID(), insert.getOriginHoursReg(),
								insert.getOriginHoursOT(), insert.getOriginRate(), payrollRule));
					}
				}
			}
			else {
				ObservableList<ModPayData> updateModPayData = FXCollections.observableArrayList();
				AlertMessage alert = new AlertMessage(AlertType.INFORMATION,
						"The following employee(s) data has already been created for "
								+ dup.get(0).getOriginEndDate()
								+ ". Would you like to replace modified values with the new values below?");
				alert = alert.originPayDataInfo(dup);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == alert.getButtonTypes().get(0)) {
					for(OriginPayData update : dup) {
						ModPayData.update(new ModPayData(update.getOriginID(), update.getOriginHoursReg(),
								update.getOriginHoursOT(), update.getOriginRate(), payrollRule));
						ModPayData delMod = ModPayData.getModPayDataByOrigin(update.getOriginID());
						updateModPayData.add(delMod);
					} 
					
					for(ModPayData del : updateModPayData) {
						ModHistory.delete(del);
					}
				}
				else {
					AlertMessage test = new AlertMessage(AlertType.INFORMATION, "Import data has been discarded...");
					test.showAndWait();
				}
			}
		}
	}
	
	public static void insert(ObservableList<OriginPayData> payData, ObservableList<ModPayData> modData) {
		
	}
}
