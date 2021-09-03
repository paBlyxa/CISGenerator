package com.we.cisgenerator.view.controller;


import com.we.cisgenerator.model.internal.DPFilter;
import com.we.cisgenerator.model.internal.DPFilter.FilterType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class NewFilterController {

	@FXML
	TextField textFieldName;
	@FXML
	TextField textFieldColumn;
	@FXML
	TextField textFieldValue;
	@FXML
	ComboBox<FilterType> comboBoxType;
	
	private boolean okClicked = false;
	private Stage dialogStage;
	private DPFilter dpFilter = null;
	
	/**
	 * Инициализация класса-контроллера. Этот метод вызывается автоматически
	 * после того, как fxml-файл будет загружен.
	 */
	@FXML
	private void initialize() {
		comboBoxType.setConverter(new StringConverter<FilterType>(){

			@Override
			public String toString(FilterType object) {
				return object.name();
			}

			@Override
			public FilterType fromString(String string) {
				return FilterType.valueOf(string);
			}
			
		});
		comboBoxType.getItems().addAll(FilterType.values());
	}
	
	/**
	 * Устанавливает сцену для этого окна.
	 * 
	 * @param dialogStage	
	 */
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	protected DPFilter getDPFilter(){
		return dpFilter;
	}
	
	@FXML
	private void handleAdd(){
		if ((textFieldName.getText() != null) && (textFieldColumn.getText() != null)
				&& (textFieldValue != null) && (comboBoxType.getValue() != null)) {
			dpFilter = new DPFilter(textFieldName.getText(), textFieldColumn.getText(),
					textFieldValue.getText(), comboBoxType.getValue());
			okClicked = true;	
		}
		
		dialogStage.close();
	}
	
	@FXML
	private void handleCancel(){
		dialogStage.close();
	}
	
	public boolean isOkClicked(){
		return okClicked;
	}
}
