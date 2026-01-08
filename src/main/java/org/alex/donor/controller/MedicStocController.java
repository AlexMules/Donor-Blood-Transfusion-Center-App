package org.alex.donor.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.StocSange;
import org.alex.donor.service.MedicService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MedicStocController implements Initializable {

    private final MedicService medicService;
    private final ApplicationContext springContext;

    @FXML private TableView<StocSange> tabelStoc;
    @FXML private TableColumn<StocSange, String> colGrupa;
    @FXML private TableColumn<StocSange, String> colRh;
    @FXML private TableColumn<StocSange, Integer> colCantitate;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareColoane();
        incarcaDateStoc();
    }

    private void configurareColoane() {
        // Mapăm datele conform modelului tău StocSange
        colGrupa.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGrupaSanguina().toString()));

        colRh.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRh().toString()));

        colCantitate.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getCantitateMl()));
    }

    private void incarcaDateStoc() {
        // Preluăm lista completă din MySQL prin MedicService
        try {
            tabelStoc.setItems(FXCollections.observableArrayList(medicService.getStocSangeComplet()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/medic_main.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}