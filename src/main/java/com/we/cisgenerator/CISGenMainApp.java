package com.we.cisgenerator;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.we.cisgenerator.model.PLC;
import com.we.cisgenerator.model.internal.DPFilter;
import com.we.cisgenerator.model.winccoa.ascii.AsciiExportField;
import com.we.cisgenerator.view.controller.FilterController;
import com.we.cisgenerator.view.controller.FilterExportFieldController;
import com.we.cisgenerator.view.controller.PLCOnlineController;
import com.we.cisgenerator.view.controller.RootLayoutController;
import com.we.jackcess.core.exceptions.AccessException;

public class CISGenMainApp extends Application {

    private final static Logger logger = LoggerFactory.getLogger(CISGenMainApp.class);
    private final static String DB_PATH = "filePath";

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Конструктор
     */
    public CISGenMainApp(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.debug("Start CIS Generator");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CIS Generator");

        //set icon of the application
        ClassLoader classLoader = CISGenMainApp.class.getClassLoader();
        Image applicationIcon = new Image(classLoader.getResourceAsStream("logo.png"));
        primaryStage.getIcons().add(applicationIcon);

        initRootLayout();
    }

    @Override
    public void stop(){
        // TODO stop all threads
    }

    /**
     * Возвращает preference файла адресатов, то есть, последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     *
     * @return
     */
    public File getFilePath(){
        return getPath(DB_PATH);
    }

    /**
     * Возвращает preference файла адресатов, то есть, последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     *
     * @param path - название свойства в реестре
     *
     * @return
     */
    public File getPath(String path){
        Preferences prefs = Preferences.userNodeForPackage(CISGenMainApp.class);
        String filePath = prefs.get(path, null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    public void setFilePath(File file){
        setPath(file, DB_PATH);
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     * @param path - название свойства в реестре
     */
    public void setPath(File file, String path){
        Preferences prefs = Preferences.userNodeForPackage(CISGenMainApp.class);
        if (file != null){
            prefs.put(path, file.getPath());

        } else {
            prefs.remove(path);
        }
    }

    /**
     * Инициализирует корневой макет.
     */
    private void initRootLayout(){
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CISGenMainApp.class.getResource("RootLayout.fxml"));
            rootLayout = loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даем контроллеру доступ к главному приложению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            controller.init();

            primaryStage.show();

            File file = getFilePath();
            if (file != null){
                controller.loadDataFromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает главную сцену.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


    public boolean showOnlinePLCDialog(PLC plc, File file){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CISGenMainApp.class.getResource("PLCOnlinePane.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Online WAGO PLC");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PLCOnlineController controller = loader.getController();
            controller.setFile(file);
            controller.setPLC(plc);
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    logger.debug("Close request");
                    controller.shutdown();
                }
            });
            try {
                controller.start();
            } catch (AccessException e) {
                logger.error(e.getMessage(), e.getCause());
            }

            // Отображаем диалоговое окно и ждем, пока пользователь его не
            // закроет
            dialogStage.showAndWait();

            return false;
        } catch (IOException e){
            logger.error("An error occured while online WAGO PLC", e);
            return false;
        }
    }

    public Set<AsciiExportField> showFilterExportFieldDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CISGenMainApp.class.getResource("FilterExportField.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор полей для экспорта");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FilterExportFieldController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.init();

            dialogStage.showAndWait();

            return controller.getFields();
        } catch (IOException e){
            logger.error("An error occured on FilterExportField", e);
        }
        return null;
    }

    public List<DPFilter> showFilterDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CISGenMainApp.class.getResource("Filter.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор фильтров для экспорта");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FilterController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked())
                return controller.getFilters();
            else
                return null;
        } catch (IOException e){
            logger.error("An error occured on FilterExportField", e);
        }
        return null;
    }

}