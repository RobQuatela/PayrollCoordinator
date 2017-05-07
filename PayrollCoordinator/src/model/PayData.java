package model;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class PayData {

	
	public static void insertOrUpdate(ObservableList<OriginPayData> payData, ObservableList<ModPayData> modData) {
		ObservableList<OriginPayData> dup = OriginPayData.searchForDup(payData);
		ObservableList<OriginPayData> originUpdate = OriginPayData.searchForUpdates(dup);
		ObservableList<ModPayData> modUpdate = FXCollections.observableArrayList();

		if(dup.isEmpty()) {
			OriginPayData.insert(payData);
			ModPayData.insert(modData);
		}
		else {
			for(int i = 0; i < dup.size(); i++) {
				for(int t = 0; t < payData.size(); t++) {
					if(payData.get(t).getEmpID() == dup.get(i).getEmpID()) {
						payData.remove(t);
						modData.remove(t);
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

				if (result.get() == alert.getButtonTypes().get(0)) {
					OriginPayData.update(originUpdate);
					
					for(OriginPayData data : originUpdate) {
						modUpdate.add(new ModPayData(data.getOriginID(), 
								data.getOriginHoursReg(), data.getOriginHoursOT(),
								data.getOriginRate()));
					}
					ModPayData.update(modUpdate);
					
					if (!payData.isEmpty()) {
						OriginPayData.insert(payData);
						ModPayData.insert(modData);
					}
				} else {
					AlertMessage test = new AlertMessage(AlertType.INFORMATION, "Duplicate data has been discarded...");
					test.showAndWait();

					OriginPayData.insert(payData);
					ModPayData.insert(modData);
				}
			}
		}
	}
}
