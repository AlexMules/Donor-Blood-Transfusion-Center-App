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
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.service.AutentificareService;
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
    private final AutentificareService autentificareService; // Injectăm serviciul de autentificare
    private final ApplicationContext springContext;

    @FXML private TableView<AnalizaSange> tabelRezultate;
    @FXML private TableColumn<AnalizaSange, Number> colNr;
    @FXML private TableColumn<AnalizaSange, String> colNume, colPrenume, colDataRezultat, colRezultat;
    @FXML private TableColumn<AnalizaSange, Void> colActiuni;
    @FXML private Button btnBack;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareColoane();
        incarcaDate();
    }

    private void configurareColoane() {
        colNr.setCellValueFactory(column ->
                new ReadOnlyObjectWrapper<>(tabelRezultate.getItems().indexOf(column.getValue()) + 1));

        colNume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDonator().getUtilizator().getNume()));

        colPrenume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDonator().getUtilizator().getPrenume()));

        colDataRezultat.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDataIntroducereRezultat().format(formatter)));

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
                btnDetalii.setOnAction(event -> handleVeziDetalii(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(btnDetalii);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void incarcaDate() {
        List<AnalizaSange> rezultate = biologService.getAnalizeFinalizate();
        rezultate.sort(Comparator.comparing(AnalizaSange::getDataIntroducereRezultat));
        tabelRezultate.setItems(FXCollections.observableArrayList(rezultate));
    }

    private void handleVeziDetalii(AnalizaSange analiza) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biolog_detalii_analiza_vizualizare.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            BiologVizualizareDetaliiController controller = loader.getController();
            controller.initData(analiza);
            Stage stage = (Stage) tabelRezultate.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleBack() {
        // Logica de redirecționare dinamică
        Utilizator user = autentificareService.getUtilizatorLogat();
        String fxmlPath = "/fxml/biolog_main.fxml"; // Default
        String titlu = "Biolog - Dashboard";

        if (user.getRol() == Rol.MEDIC) {
            fxmlPath = "/fxml/medic_main.fxml";
            titlu = "Medic - Dashboard";
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle(titlu);
            stage.centerOnScreen();
        } catch (IOException e) { e.printStackTrace(); }
    }
}