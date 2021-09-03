package com.we.cisgenerator.view.controller;

import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class FilterExportFieldController {
	
	@FXML
	private VBox vbox;
	
	private Stage dialogStage;
	private boolean okClicked = false;
	private final Set<AsciiExportField> fields = new HashSet<>();
	
	/**
	 * Устанавливает сцену для этого окна.
	 * 
	 * @param dialogStage	
	 */
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	public void init(){
		for (AsciiExportField field : AsciiExportField.values()){
			CheckBox cb = new CheckBox(field.name());
			if (field.equals(AsciiExportField.DpType) || field.equals(AsciiExportField.Datapoint)){
				cb.setSelected(true);
				cb.setDisable(true);
			}
			vbox.getChildren().add(cb);
		}
		CheckBox cb = new CheckBox("Все");
		// По изменению выбираем либо сбрасываем все поля, кроме неактивных
		cb.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				vbox.getChildren().forEach(node -> {
					CheckBox ch = (CheckBox)node;
					if (!ch.isDisable()){
						ch.setSelected(newValue);
					}
				});
			}
		});
		vbox.getChildren().add(cb);
	}
	
	@FXML
	private void handleOk(){
		vbox.getChildren().forEach(node -> {
			CheckBox ch = (CheckBox)node;
			if (ch.isSelected() && !ch.getText().equals("Все")){
				fields.add(AsciiExportField.valueOf(ch.getText()));
			}
		});
		okClicked = true;
		dialogStage.close();
	}
	
	@FXML
	private void handleCancel(){
		dialogStage.close();
	}
	
	public boolean isOkClicked(){
		return okClicked;
	}
	
	public Set<AsciiExportField> getFields(){
		return fields;
	}
}
