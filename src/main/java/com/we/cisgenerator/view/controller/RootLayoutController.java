package com.we.cisgenerator.view.controller;

import com.we.cisgenerator.CISGenMainApp;
import com.we.cisgenerator.model.*;
import com.we.cisgenerator.model.Module;
import com.we.cisgenerator.model.internal.DPFilter;
import com.we.cisgenerator.model.winccoa.*;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import com.we.cisgenerator.service.DBService;
import com.we.cisgenerator.service.PLCService;
import com.we.cisgenerator.service.WCCOAasciiTask;
import com.we.cisgenerator.service.WinCCOAService;
import com.we.jackcess.core.exceptions.AccessException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Контроллер для корневого макета. Корневой макет предоставляет базовый макет
 * приложения, содержащий строку меню и место, где будут размещены остальные
 * элементы JavaFX.
 * 
 * @author fakadey
 *
 */
public class RootLayoutController {

	Logger logger = LoggerFactory.getLogger(RootLayoutController.class);

	@FXML
	private TextField filePath;
	@FXML
	private Label status;

	@FXML
	private TableView<PLC> plcTable;
	@FXML
	private TableColumn<PLC, String> posPLCCol;
	@FXML
	private TableColumn<PLC, String> cabinetCol;
	@FXML
	private TableColumn<PLC, String> addrCol;
	@FXML
	private CheckBox configCheckBox;
	@FXML
	private CheckBox codeCheckBox;
	@FXML
	private CheckBox paramCheckBox;
	@FXML
	private CheckBox checkBoxSDS_AI;
	@FXML
	private CheckBox checkBoxSDS_DI;
	@FXML
	private CheckBox checkBoxSDS_Calc;
	@FXML
	private CheckBox checkBoxSDS_BO;
	@FXML
	private CheckBox checkBoxDrivers;
	@FXML
	private CheckBox checkBoxSDS_QS;
	@FXML
	private CheckBox checkBoxSDS_XR;

	// Ссылка на главное приложение
	private CISGenMainApp mainApp;

	private File file;

	/**
	 * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(CISGenMainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void init() {
	}

	/**
	 * Вызывается, когда пользователь кликнул по кнопке Экспорт оффлайн.
	 * Генерирует конфигурацию, программу и сохраняет в файл.
	 */
	@FXML
	private void handleExportOffline() {
		ObservableList<PLC> plcList = plcTable.getSelectionModel().getSelectedItems();
		logger.debug("Export offline plcList.size = {}", plcList.size());
		if (plcList != null && !plcList.isEmpty() && !((plcList.size() == 1) && plcList.get(0).isInterfaceModule())) {
			if (!configCheckBox.isSelected() && !codeCheckBox.isSelected() && !paramCheckBox.isSelected()) {
				logger.warn("No checkbox is selected");
				status.setText("Выберите пункты для экспорта");
				return;
			}
			for (PLC plc : plcList) {
				// Для интерфейсного модуля нечего генерировать
				if (!plc.isInterfaceModule()){
					FileChooser fileChooser = new FileChooser();
					File path = mainApp.getPath("exportWagoFilePath");
					if (path != null && path.isDirectory()){
						fileChooser.setInitialDirectory(path);
					}
					File exportFile = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
					if (exportFile != null) {
						mainApp.setPath(exportFile.getParentFile(), "exportWagoFilePath");
						
						boolean append = false;
						List<AI> listAI = null;
						try {
							if (codeCheckBox.isSelected()) {
								if (listAI == null) {
									listAI = DBService.list(file, AI.class, plc);
								}
								PLCService.calcComParameters(listAI, plc);
								PLCService.writeProgram(plc, exportFile, listAI, append);
								append = true;
							}
							if (paramCheckBox.isSelected()) {
								if (listAI == null) {
									listAI = DBService.list(file, AI.class, plc);
								}
								PLCService.calcComParameters(listAI, plc);
								PLCService.writeInitProgram(plc, exportFile, append);
								append = true;
							}
							if (configCheckBox.isSelected()) {
								PLCService.writeConfig(exportFile, plc, append);
								append = true;
							}
							status.setText("Экспорт в файл \"" + exportFile.getName() + "\" завершен");
						} catch (AccessException e) {
							status.setText(e.getMessage());
						} catch (NullPointerException e) {
							logger.error(e.getMessage(), e);
							status.setText(e.toString());
						}
					} else {
						logger.warn("File hasn't been choosen");
					}
				}
			}
		} else {
			logger.debug("No plc is selected");
			status.setText("Выберите контроллер из списка");
		}
	}

	/**
	 * Действие когда нажали кнопку онлайн.
	 */
	@FXML
	private void handleOnline() {
		PLC plc = plcTable.getSelectionModel().getSelectedItem();
		if ((plc != null) && !plc.isInterfaceModule()) {
			logger.debug("Online {}", plc.getPosition());
			boolean okClicked = mainApp.showOnlinePLCDialog(plc, file);
			if (okClicked) {
				status.setText("Экспорт успешно завершен");
			}
		} else {
			logger.debug("No plc is selected");
			status.setText("Выберите контроллер из списка");
		}
	}

	/**
	 * Вызывается, когда пользователь кликнул по кнопке Экспорт WinCC OA.
	 * Генерирует данные сигналов и сохраняет в файл.
	 */
	@FXML
	private void handleExportWinCC() {
		logger.debug("WinCCOA Offline Export");
		exportToWinCC(false);
		
	}

	/**
	 * Вызывается, когда пользователь кликнул по кнопке Онлайн WinCC OA.
	 * Генерирует данные сигналов и сохраняет в файл, затем вызывает WCCOAascii
	 */
	@FXML
	private void handleExportOnlineWinCC() {
		logger.debug("WinCCOA Online Export");
		exportToWinCC(true);
	}

	/**
	 * Генерирует данные сигналов.
	 * @param online - если True, то сгенерированные данные сохраняются во временный
	 * файл, а затем вызывается приложение WCCOAascii для импорта данных.
	 * Если False, то данные сохраняются в выбранный файл.
	 */
	private void exportToWinCC(boolean online){
		File exportFile = null;
		if (file != null) {
			ObservableList<PLC> plcList = plcTable.getSelectionModel().getSelectedItems();
			if (checkBoxDrivers.isSelected() || checkBoxSDS_QS.isSelected() || checkBoxSDS_Calc.isSelected()
					|| checkBoxSDS_BO.isSelected() || checkBoxSDS_XR.isSelected() ||(plcList != null && !plcList.isEmpty())) {
				if (checkBoxSDS_AI.isSelected() || checkBoxSDS_DI.isSelected() || checkBoxSDS_Calc.isSelected()
						|| checkBoxDrivers.isSelected() || checkBoxSDS_QS.isSelected() || checkBoxSDS_BO.isSelected() || checkBoxSDS_XR.isSelected()) {
					exportFile = online ? new File("TempExport.dpl") : showDialog();
					if (exportFile != null) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Подтверждение");
						alert.setContentText("Выгрузить всю информацию или использовать фильтр?");
						ButtonType buttonTypeAll = new ButtonType("Все");
						ButtonType buttonTypeFilter = new ButtonType("Фильтр");
						ButtonType buttonTypeCancel = new ButtonType("Отмена");
						
						alert.getButtonTypes().setAll(buttonTypeAll, buttonTypeFilter, buttonTypeCancel);
						
						Optional<ButtonType> result = alert.showAndWait();
						Collection<AsciiExportField> fields;
						List<DPFilter> filters = null;
						if (result.get() == buttonTypeAll){
							fields = Arrays.asList(AsciiExportField.values());
						} else if (result.get() == buttonTypeFilter){
							fields = mainApp.showFilterExportFieldDialog();
							if (fields == null){
								logger.info("Cancel export - no fields");
								return;
							}
							filters = mainApp.showFilterDialog();
						} else {
							logger.info("Cancel export");
							return;
						}
						
						boolean append = false;
						List<Dp> listDp = new ArrayList<>();
						try {
							if (checkBoxSDS_AI.isSelected()) {
								listDp.addAll(DBService.list(file, SDS_AI.class, plcList, filters));
								listDp.addAll(DBService.list(file, SDS_AI_Internal.class, plcList, filters));
							}
							if (checkBoxSDS_DI.isSelected()) {
								List<DI> listDI = DBService.list(file, DI.class, plcList, filters);
								for (DI di : listDI){
									listDp.add(new SDS_DI(di));
								}
								List<DI_Internal> listDI_Internal = DBService.list(file, DI_Internal.class, plcList, filters);
								for (DI_Internal di : listDI_Internal){
									listDp.add(new SDS_DI_Internal(di));
								}
								listDp.addAll(DBService.list(file, SDS_DQ.class, plcList, filters));
							}
							if (checkBoxSDS_XR.isSelected()){
								List<XR> listXR = DBService.list(file, XR.class, filters);
								for (XR xr : listXR) {
									switch(xr.getType()) {
										case BOOL:
											if (xr.isOut()) {
												listDp.add(new SDS_XR_DO(xr));
											} else {
												listDp.add(new SDS_XR_DI(xr));
											}
											break;
										case REAL:
											if (xr.isOut()) {
												listDp.add(new SDS_XR_AO(xr));
											} else {
												listDp.add(new SDS_XR(xr));
											}
											break;
										case UINT:
											if (xr.isOut()) {
												listDp.add(new SDS_XR_AO(xr));
											} else {
												listDp.add(new SDS_XR(xr));
											}
											break;
										case WORD:
											break;
										default:
											break;
												
										}
									
								}
							}
							if (checkBoxSDS_Calc.isSelected()) {
								listDp.addAll(DBService.list(file, SDS_PA.class, filters));
								
							}
							if (checkBoxSDS_BO.isSelected()) {
								listDp.addAll(DBService.list(file, SDS_BO.class, filters));
							}
							if (checkBoxSDS_QS.isSelected()) {
								listDp.addAll(DBService.list(file, SDS_QS.class, filters));

							}
							if (checkBoxDrivers.isSelected()) {
								List<PLC> listPLC = new ArrayList<>();
								for (PLC plc : plcTable.getItems()) {
									if (!plc.isInterfaceModule()){
										if (plc.isReserved() && (!listPLC.contains(plc.getReservPLC()))){
											listPLC.add(plc);
										}
									}
								}
								for (PLC plc : listPLC){
									List<Module> listModule = plc.getModules();
									for (Module m : listModule){
										if (m.getName().equals("0750-0652#24 RS-232/RS-485 Interface adjustable (24 Bytes)")){
											listDp.add(new SDS_comPLC(plc));
											break;
										}
									}
								}
							}
							WinCCOAService.writeToFile(exportFile, listDp, fields, append);
							status.setText("Экспорт в файл \'" + exportFile.getName() + "\' успешно завершен");
							if (online){
								WCCOAasciiTask task = new WCCOAasciiTask("-proj", "TestProject", "-in", exportFile.toString());
								Thread thread = new Thread(task);
								thread.setDaemon(true);
								task.setOnFailed((value) -> {
									logger.warn("An error occured while running WCCOAascii",
											task.exceptionProperty().getValue());
									status.setText("Не найдено установленного ПО WinCC OA");
								});
								thread.start();
							}
						} catch (AccessException e) {
							status.setText(e.getMessage());
						}
					}
				} else {
					logger.debug("No checkbox are selected");
					status.setText("Выберите пункт");
				}
			} else {
				logger.debug("No plc is selected");
				status.setText("Выберите контроллер из списка");
			}
		}
	}
	
	
	/**
	 * Действие когда нажали кнопку "Выбрать файл". Открывает диалоговое окно
	 * выбора файла.
	 */
	@FXML
	private void handleChooseFile() {

		FileChooser fileChooser = new FileChooser();
		file = fileChooser.showOpenDialog(null);
		// file = new File("E:\\Projects\\Армянская АЭС\\База данных (Армянская
		// АЭС) v.3.2.accdb");
		if (file != null) {
			loadDataFromFile(file);
			mainApp.setFilePath(file);
		}
	}

	/**
	 * Загружает данные из файла в таблицу контроллеров.
	 * @param file - файл с БД.
	 */
	public void loadDataFromFile(File file) {
		this.file = file;
		logger.info("Selected file {}", file);
		filePath.setText(file.getName());
		initTable();
	}

	/**
	 * Загружаем данные в таблицу контроллеров.
	 * 
	 * @param newFile
	 */
	private void initTable() {
		if (file != null) {
			posPLCCol.setCellValueFactory(new PropertyValueFactory<PLC, String>("position"));
			cabinetCol.setCellValueFactory(cellData -> {
				return new SimpleStringProperty(
						cellData.getValue().getCabinet() != null ? cellData.getValue().getCabinet().getName() : "");
			});
			addrCol.setCellValueFactory(new PropertyValueFactory<PLC, String>("ipAddress"));
			try {
				ObservableList<PLC> listPLC = FXCollections.observableArrayList(DBService.getListPLC(file));
				plcTable.setItems(listPLC);
				plcTable.getSortOrder().add(posPLCCol);
				plcTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				status.setText("Файл \"" + file.getName() + "\" успешно прочитан");

			} catch (IOException e) {
				logger.error("An error occured while reading file", e);
				status.setText("Ошибка при чтении файла");
			}
		}
	}

	/**
	 * Открытие окна для выбора файла
	 */
	private File showDialog() {
		FileChooser fileChooser = new FileChooser();
		File path = mainApp.getPath("exportFilePath");
		if (path != null && path.isDirectory()){
			fileChooser.setInitialDirectory(path);
		}
		File newFile =  fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		if (newFile != null){
			mainApp.setPath(newFile.getParentFile(), "exportFilePath");
		}
		return newFile;
	}

	/**
	 * Закрывает приложение.
	 */
	@FXML
	private void handleExit() {
		Platform.exit();
	}
}
