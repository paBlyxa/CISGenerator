package com.we.cisgenerator.view.controller;

import com.we.cisgenerator.CISGenMainApp;
import com.we.cisgenerator.model.internal.DPFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class FilterController {

	private final static Logger logger = LoggerFactory.getLogger(FilterController.class);
	
	@FXML
	ListView<DPFilter> listViewFilters;
	
	@FXML
	ListView<DPFilter> listViewUsedFilters;
	
	private ObservableList<DPFilter> listFilters;
	private ObservableList<DPFilter> listUsedFilters;
	private boolean okClicked = false;
	private Stage dialogStage;
	
	/**
	 * Устанавливает сцену для этого окна.
	 * 
	 * @param dialogStage	
	 */
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	/**
	 * Инициализация класса-контроллера. Этот метод вызывается автоматически
	 * после того, как fxml-файл будет загружен.
	 */
	@FXML
	private void initialize() {
		listFilters = FXCollections.observableArrayList();
		listUsedFilters = FXCollections.observableArrayList();
		DPFilter f = new DPFilter("По идентификатору", "Идент", "AI3M*", DPFilter.FilterType.CONTAINS);
		listFilters.add(f);
		listViewFilters.setItems(listFilters);
		listViewUsedFilters.setItems(listUsedFilters);
	}
	
	@FXML
	private void handleAdd(){
		logger.debug("Handle Add, selected items [{}]", listViewFilters.getSelectionModel().getSelectedItems());
		listUsedFilters.addAll(listViewFilters.getSelectionModel().getSelectedItems());
	}
	
	@FXML
	private void handleDelete(){
		logger.debug("Handle Delete, selected items [{}]", listViewUsedFilters.getSelectionModel().getSelectedItems());
		listUsedFilters.removeAll(listViewUsedFilters.getSelectionModel().getSelectedItems());
	}
	
	@FXML
	private void handleNew(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CISGenMainApp.class.getResource("NewFilter.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage stage = new Stage();
			stage.setTitle("Новый фильтр для экспорта");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(dialogStage);
			Scene scene = new Scene(page);
			stage.setScene(scene);
			
			NewFilterController controller = loader.getController();
			controller.setDialogStage(stage);
		
			stage.showAndWait();
			
			if (controller.isOkClicked()){
				listUsedFilters.add(controller.getDPFilter());
			}
		} catch (IOException e){
			logger.error("An error occured on FilterExportField", e);
		}
	}
	
	@FXML
	private void handleOk(){
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
	
	public List<DPFilter> getFilters(){
		return listUsedFilters;
	}
}
