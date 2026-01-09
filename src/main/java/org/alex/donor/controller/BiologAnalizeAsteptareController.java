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
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class BiologAnalizeAsteptareController implements Initializable {

    private final BiologService biologService;
    private final ApplicationContext springContext;

    @FXML private TableView<AnalizaSange> tabelAnalize;
    @FXML private TableColumn<AnalizaSange, Number> colNr;
    @FXML private TableColumn<AnalizaSange, String> colNume, colPrenume, colDataDonare;
    @FXML private TableColumn<AnalizaSange, Void> colActiuni;
    @FXML private Button btnBack;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareColoane();
        incarcaAnalize();
    }

    private void configurareColoane() {
        colNr.setCellValueFactory(column ->
                new ReadOnlyObjectWrapper<>(tabelAnalize.getItems().indexOf(column.getValue()) + 1));
        colNr.setStyle("-fx-alignment: CENTER;");

        colNume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDonator().getUtilizator().getNume()));

        colPrenume.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDonator().getUtilizator().getPrenume()));

        colDataDonare.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDonare().getDataDonare().format(formatter)));

        configurareButonActiune();
    }

    private void configurareButonActiune() {
        colActiuni.setCellFactory(param -> new TableCell<>() {
            private final Button btnIntro = new Button("Introducere rezultat");
            {
                btnIntro.getStyleClass().add("btn-action-white-small");
                btnIntro.setOnAction(event -> handleIntroducereRezultat(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(btnIntro);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void incarcaAnalize() {
        tabelAnalize.setItems(FXCollections.observableArrayList(biologService.getAnalizeInAsteptare()));
        tabelAnalize.refresh();
    }

    private void handleIntroducereRezultat(AnalizaSange analiza) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biolog_detalii_analiza.fxml"));

            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            BiologDetaliiAnalizaController controller = loader.getController();
            controller.initData(analiza);

            Stage stage = (Stage) tabelAnalize.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle("Introducere Rezultat Analiză");
            stage.centerOnScreen();

        } catch (IOException e) {
            System.err.println("Eroare la deschiderea formularului de rezultate: " + e.getMessage());
            e.printStackTrace();
        }
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
        } catch (IOException e) { e.printStackTrace(); }
    }
}