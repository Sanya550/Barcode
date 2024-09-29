package com.example.barcode;

import com.example.barcode.entity.Device;
import com.example.barcode.enums.Brand;
import com.example.barcode.enums.Type;
import com.example.barcode.service.DeviceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.barcode.helper.BarcodeHelper.*;
import static com.example.barcode.helper.GeneralHelper.*;

public class BarcodeController implements Initializable {
    public final String ROOT_DIRECTORY = "C:\\Users\\Alex\\IdeaProjects\\Barcode\\src\\main\\resources\\testData\\";

    @FXML
    private TextField nameOfModel;

    @FXML
    private ComboBox<String> comBoxForBrand;

    @FXML
    private ComboBox<String> comBoxForType;

    @FXML
    private TextArea textArea;

    @FXML
    private ImageView imageView;

    @FXML
    private TableView<Device> tableView;

    private final DeviceService dbService = new DeviceService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var brands = FXCollections.observableArrayList(Brand.getAllBrands());
        comBoxForBrand.setItems(brands);
        comBoxForBrand.setValue(brands.get(0));

        var types = FXCollections.observableArrayList(Type.getAllTypes());
        comBoxForType.setItems(types);
        comBoxForType.setValue(types.get(0));

        deleteFilesInFolder(ROOT_DIRECTORY);
        for (var dbDevice : dbService.getDevices()) {
            try {
                saveBarcode(ROOT_DIRECTORY + dbDevice.getName(), new StringBuilder(dbDevice.getCode()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TableColumn<Device, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        idColumn.setResizable(true);


        TableColumn<Device, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);
        nameColumn.setResizable(true);

        TableColumn<Device, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(150);
        typeColumn.setResizable(true);

        TableColumn<Device, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandColumn.setPrefWidth(150);
        brandColumn.setResizable(true);

        TableColumn<Device, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeColumn.setPrefWidth(150);
        codeColumn.setResizable(true);

        tableView.getColumns().addAll(idColumn, nameColumn, typeColumn, brandColumn, codeColumn);
    }

    @FXML
    public void saveItem() {
        var name = nameOfModel.getText().toUpperCase().trim();
        if (isPngFileExists(ROOT_DIRECTORY, name)) {
            JOptionPane.showMessageDialog(null, "Файл з таким ім'ям вже існує", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            var code = generateBarcode(name);
            try {
                saveBarcode(ROOT_DIRECTORY + name, code);
                JOptionPane.showMessageDialog(null, "Файл успішно збережений", "Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Помилка", "Error", JOptionPane.ERROR_MESSAGE);
            }

            var type = comBoxForType.getValue();
            var brand = comBoxForBrand.getValue();
            var newDevice = new Device();
            newDevice.setName(name);
            newDevice.setType(type);
            newDevice.setBrand(brand);
            newDevice.setCode(code.toString());
            dbService.create(newDevice);
        }
    }

    @FXML
    public void decodeItem() {
        var fileChooser = new JFileChooser(new File(ROOT_DIRECTORY));
        fileChooser.setDialogTitle("Оберіть PNG файл");
        int returnValue = fileChooser.showDialog(null, "OK");
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            var nameOfFile = fileChooser.getSelectedFile().getName();
            var image = loadImageFromFile(ROOT_DIRECTORY + nameOfFile);
            imageView.setImage(image);

            String decodedModelName = decodeBarcode(ROOT_DIRECTORY + nameOfFile);
            var device = dbService.getDeviceByName(decodedModelName);
            textArea.clear();
            textArea.setText("Назва моделі: " + device.getName() + "\nБренд: " + device.getBrand() + "\nТип: " + device.getType());
        }
    }

    @FXML
    public void checkActualDevices() {
        tableView.getItems().clear();
        ObservableList<Device> data = FXCollections.observableArrayList(dbService.getDevices());
        tableView.setItems(data);
    }
}