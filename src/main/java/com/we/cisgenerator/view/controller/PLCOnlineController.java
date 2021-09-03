package com.we.cisgenerator.view.controller;

import com.we.cisgenerator.model.AI;
import com.we.cisgenerator.model.CCDCom;
import com.we.cisgenerator.model.CCDParameter;
import com.we.cisgenerator.model.PLC;
import com.we.cisgenerator.service.DBService;
import com.we.cisgenerator.service.ModbusReadWriteService;
import com.we.cisgenerator.service.PLCService;
import com.we.jackcess.core.exceptions.AccessException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PLCOnlineController {

	private final static Logger logger = LoggerFactory.getLogger(PLCOnlineController.class);
	
	@FXML
	private Label title;
	@FXML
	private TableView<CCDParameter> paramTable;
	@FXML
	private TableColumn<CCDParameter, String> nameColumn;
	@FXML
	private TableColumn<CCDParameter, String> valOfflineColumn;
	@FXML
	private TableColumn<CCDParameter, String> valOnlineColumn;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Button buttonLoadAll;
	@FXML
	private Button buttonRefresh;
	
	private File file;
	private PLC plc;
	private final ExecutorService executor;
	
	public PLCOnlineController(){
		executor = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Инициализация класса-контроллера. Этот метод вызывается автоматически
	 * после того, как fxml-файл будет загружен.
	 */
	@FXML
	private void initialize() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<CCDParameter, String>("name"));
		valOfflineColumn.setCellValueFactory(new PropertyValueFactory<CCDParameter, String>("valueOffline"));
		valOnlineColumn.setCellValueFactory(new PropertyValueFactory<CCDParameter, String>("valueOnline"));
		
		paramTable.setRowFactory(new Callback<TableView<CCDParameter>, TableRow<CCDParameter>>() {
			@Override
			public TableRow<CCDParameter> call(TableView<CCDParameter> tableView){
				final TableRow<CCDParameter> row = new TableRow<CCDParameter>(){
					@Override
					public void updateItem(CCDParameter item, boolean empty){
						super.updateItem(item, empty);
						if (item == null){
							setStyle("");
						} else 	if (item.isDifferent()){
							setStyle("-fx-text-background-color: red;");
						} else {
							setStyle("");
						}
					}
					
				};
				// TODO create context Menu
				return row;
			}
		});
	}
	
	@FXML
	private void handleUpdateAll(){
		if (paramTable.getItems().size() > 0){
			Task<Void> task = ModbusReadWriteService.getWriteParametersTask(plc);
			task.setOnSucceeded((value) -> {
				logger.debug("Write parameters succeeded");
				unbind();
				try {
					start();
				} catch (AccessException e){
					logger.error(e.getMessage(), e);
				}
			});
			progressBar.progressProperty().bind(task.progressProperty());
			title.textProperty().bind(task.messageProperty());
			executor.submit(task);
		} else{
			logger.error("Data hasn't been loaded");
		}
	}
	
	@FXML
	private void handleRefresh(){
		try {
			start();
		} catch (AccessException e){
			logger.error(e.getMessage(), e);
		}
	}
	
	public void start() throws AccessException{
		List<AI> listAI = DBService.list(file, AI.class, plc);
		PLCService.calcComParameters(listAI, plc);
		Task<Map<Integer, CCDCom>> task = ModbusReadWriteService.getReadParametersTask(plc);
		task.setOnSucceeded((value) -> {
			try {
				Map<Integer, CCDCom> comParameters = task.get();
				logger.debug("Get comParameters size {}", comParameters.size());
				if (comParameters.size() > 0){
					paramTable.setItems(PLCService.getParameterList(plc, comParameters));
				}
				unbind();
			} catch (InterruptedException | ExecutionException e) {
				logger.error("An exception occurred", e);
			}
		});
		buttonLoadAll.disableProperty().bind(task.runningProperty());
		buttonRefresh.disableProperty().bind(task.runningProperty());
		progressBar.progressProperty().bind(task.progressProperty());
		title.textProperty().bind(task.messageProperty());
		executor.submit(task);
	}
	
	private void unbind(){
		title.textProperty().unbind();
		progressBar.progressProperty().unbind();
		buttonLoadAll.disableProperty().unbind();
		buttonRefresh.disableProperty().unbind();
	}
	
	public void shutdown() {
	    executor.shutdownNow();
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	public void setPLC(PLC plc){
		this.plc = plc;
	}
	
}
