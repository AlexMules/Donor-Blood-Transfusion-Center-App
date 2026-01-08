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
import org.alex.donor.model.AnalizaSange;
import org.alex.donor.service.BiologService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class BiologRezultateFinalizateController implements Initializable {

    private final BiologService biologService;
    private final ApplicationContext springContext;

    @FXML private TableView<AnalizaSange> tabelRezultate;
    @FXML private TableColumn<AnalizaSange, Number> colNr;
    @FXML private TableColumn<AnalizaSange, String> colNume;
    @FXML private TableColumn<AnalizaSange, String> colPrenume;
    @FXML private TableColumn<AnalizaSange, String> colDataRezultat; // Coloana nouă injectată
    @FXML private TableColumn<AnalizaSange, String> colRezultat;
    @FXML private TableColumn<AnalizaSange, Void> colActiuni;
    @FXML private Button btnBack;

    // Formator pentru afișarea datei (zi-lună-an oră:minut)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareColoane();
        incarcaDate();
    }

    private void configurareColoane() {
        // 1. Număr de ordine (index + 1)
        colNr.setCellValueFactory(column ->
                new ReadOnlyObjectWrapper<>(tabelRezultate.getItems().indexOf(column.getValue()) + 1));
        colNr.setStyle("-fx-alignment: CENTER;");

        // 2. Nume Donator
        colNume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDonator().getUtilizator().getNume()));
        colNume.setStyle("-fx-alignment: CENTER;");

        // 3. Prenume Donator
        colPrenume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDonator().getUtilizator().getPrenume()));
        colPrenume.setStyle("-fx-alignment: CENTER;");

        // 4. Data Rezultat (Formatare LocalDateTime -> String)
        colDataRezultat.setCellValueFactory(data -> {
            if (data.getValue().getDataIntroducereRezultat() != null) {
                return new SimpleStringProperty(data.getValue().getDataIntroducereRezultat().format(formatter));
            }
            return new SimpleStringProperty("-");
        });
        colDataRezultat.setStyle("-fx-alignment: CENTER;");

        // 5. Rezultat (ADMIS / RESPINS)
        colRezultat.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRezultat().toString()));
        colRezultat.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");

        configurareButonDetalii();
    }

    private void configurareButonDetalii() {
        colActiuni.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetalii = new Button("Vezi detalii");
            {
                btnDetalii.getStyleClass().add("btn-action-white-small");
                btnDetalii.setOnAction(event -> {
                    AnalizaSange analiza = getTableView().getItems().get(getIndex());
                    handleVeziDetalii(analiza);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDetalii);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void incarcaDate() {
        // Preluăm analizele finalizate din service
        List<AnalizaSange> rezultate = biologService.getAnalizeFinalizate();

        // Sortăm crescător după data introducerii rezultatului (cele mai vechi primele)
        rezultate.sort(Comparator.comparing(AnalizaSange::getDataIntroducereRezultat));

        tabelRezultate.setItems(FXCollections.observableArrayList(rezultate));
        tabelRezultate.refresh(); // Reîmprospătare pentru a actualiza corect coloana Nr.
    }

    private void handleVeziDetalii(AnalizaSange analiza) {
        System.out.println("Vizualizare detalii pentru analiza ID: " + analiza.getId());
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biolog_main.fxml"));
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