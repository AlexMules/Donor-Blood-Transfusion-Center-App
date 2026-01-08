package org.alex.donor.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.AnalizaSange;
import org.alex.donor.model.Donator;
import org.alex.donor.model.enums.RezultatAnaliza;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.DonatorService;
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
public class DonatorRezultateController implements Initializable {

    private final DonatorService donatorService;
    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private ListView<AnalizaSange> listaRezultate;
    @FXML private Button btnBack;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareCelule();
        incarcaDate();
    }

    private void configurareCelule() {
        listaRezultate.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AnalizaSange analiza, boolean empty) {
                super.updateItem(analiza, empty);
                if (empty || analiza == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    VBox card = new VBox(10);
                    card.getStyleClass().add("result-card");

                    // Calculăm numărul de ordine (descrescător)
                    int nrOrdine = getListView().getItems().size() - getIndex();

                    // 1. Header card: Nr și Schimbarea etichetei în "Data donarii:"
                    Label lblHeader = new Label("Analiza #" + nrOrdine + " | Data donării: " +
                            analiza.getDonare().getDataDonare().format(formatter));
                    lblHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                    // Data rezultat
                    Label lblDataRez = new Label("Rezultat procesat la: " + analiza.getDataIntroducereRezultat().format(formatter));
                    lblDataRez.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");

                    // Verdict (ADMIS/RESPINS)
                    Label lblVerdict = new Label(analiza.getRezultat().toString());
                    lblVerdict.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                    if (analiza.getRezultat() == RezultatAnaliza.ADMIS) {
                        lblVerdict.setStyle(lblVerdict.getStyle() + "-fx-text-fill: #2ecc71;"); // Verde
                    } else {
                        lblVerdict.setStyle(lblVerdict.getStyle() + "-fx-text-fill: #e74c3c;"); // Roșu
                    }

                    // 2. Mesaj Biolog cu font mai mare (16px)
                    Label lblMesaj = new Label("Mesaj: " + analiza.getMesaj());
                    lblMesaj.setWrapText(true);
                    // Am adăugat -fx-font-size: 16px;
                    lblMesaj.setStyle("-fx-font-style: italic; -fx-padding: 10 0 0 0; -fx-font-size: 16px;");

                    card.getChildren().addAll(lblHeader, lblDataRez, lblVerdict, lblMesaj);
                    setGraphic(card);
                    setStyle("-fx-background-color: transparent; -fx-padding: 10;");
                }
            }
        });
    }

    private void incarcaDate() {
        Donator d = donatorService.getDonatorByUtilizator(autentificareService.getUtilizatorLogat());
        List<AnalizaSange> analize = donatorService.getIstoricAnalize(d);

        // Sortare DESCRESCĂTOARE după data introducerii
        analize.sort(Comparator.comparing(AnalizaSange::getDataIntroducereRezultat).reversed());

        listaRezultate.setItems(FXCollections.observableArrayList(analize));
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/donator_main.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen();
        } catch (IOException e) { e.printStackTrace(); }
    }
}