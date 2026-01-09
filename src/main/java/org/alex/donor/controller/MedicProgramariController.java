package org.alex.donor.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Programare;
import org.alex.donor.service.MedicService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MedicProgramariController implements Initializable {

    private final MedicService medicService;
    private final ApplicationContext springContext;

    @FXML private DatePicker datePickerZi;
    @FXML private TableView<Programare> tabelProgramari;

    @FXML private TableColumn<Programare, Number> colNr;
    @FXML private TableColumn<Programare, String> colNume;
    @FXML private TableColumn<Programare, String> colPrenume;
    @FXML private TableColumn<Programare, String> colOra;
    @FXML private TableColumn<Programare, Void> colActiuni;

    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareRestrictiiCalendar();
        configurareColoane();

        datePickerZi.setValue(LocalDate.now());
        handleDateChange();
    }

    private void configurareRestrictiiCalendar() {
        datePickerZi.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now()) ||
                        item.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                }
            }
        });
    }

    private void configurareColoane() {
        colNr.setCellValueFactory(column ->
                new ReadOnlyObjectWrapper<>(tabelProgramari.getItems().indexOf(column.getValue()) + 1));
        colNr.setStyle("-fx-alignment: CENTER;");

        colNume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonator().getUtilizator().getNume()));
        colNume.setStyle("-fx-alignment: CENTER;");

        colPrenume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonator().getUtilizator().getPrenume()));
        colPrenume.setStyle("-fx-alignment: CENTER;");

        colOra.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDataOraProgramare().toLocalTime().toString()));
        colOra.setStyle("-fx-alignment: CENTER;");

        configurareButonDetalii();
    }

    private void configurareButonDetalii() {
        colActiuni.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetails = new Button("Vezi detalii");
            {
                btnDetails.getStyleClass().add("btn-action-white-small");
                btnDetails.setOnAction(event -> {
                    Programare p = getTableView().getItems().get(getIndex());
                    handleViewDetails(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDetails);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    @FXML
    public void handleDateChange() {
        LocalDate dataSelectata = datePickerZi.getValue();
        if (dataSelectata != null) {
            tabelProgramari.setItems(FXCollections.observableArrayList(
                    medicService.getProgramariPentruZi(dataSelectata)));
            tabelProgramari.refresh();
        }
    }

    private void handleViewDetails(Programare p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/medic_detalii_donator.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            MedicDetaliiDonatorController detailsController = loader.getController();
            detailsController.initData(p);

            Stage stage = (Stage) tabelProgramari.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen();
        } catch (IOException e) {
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

    public void incarcaDataSpecifica(LocalDate data) {
        datePickerZi.setValue(data);
        handleDateChange();
    }
}