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
        // 1. Configurăm mesajul de tip Placeholder
        Label placeholder = new Label("Momentan nu există nicio analiză înregistrată în sistem.");

        // Stil similar cu cel folosit la alerte pentru consistență
        placeholder.setStyle("-fx-text-fill: white; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-alignment: center;");
        placeholder.setWrapText(true);
        placeholder.setMaxWidth(600); // Limităm lățimea pentru a forța ruperea rândului dacă e nevoie

        // 2. Setăm placeholder-ul pe listă
        listaRezultate.setPlaceholder(placeholder);

        // 3. Continuăm cu restul configurărilor
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
                    int nrOrdine = getListView().getItems().size() - getIndex();

                    // 1. Header (Data donării este mereu prezentă, deci e sigură)
                    Label lblHeader = new Label("Analiza #" + nrOrdine + " | Data donării: " +
                            analiza.getDonare().getDataDonare().format(formatter));
                    lblHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                    Label lblDataRez = new Label();
                    Label lblVerdict = new Label();
                    Label lblMesaj = new Label();
                    lblMesaj.setWrapText(true);
                    lblMesaj.setStyle("-fx-font-style: italic; -fx-padding: 10 0 0 0; -fx-font-size: 16px;");

                    // --- VERIFICAREA CRITICĂ PENTRU EVITAREA NULLPOINTEREXCEPTION ---
                    if (analiza.getDataIntroducereRezultat() == null) {
                        // Cazul: ÎN AȘTEPTARE
                        lblDataRez.setText("Status: Proba este în curs de analizare...");
                        lblVerdict.setText("ÎN AȘTEPTARE");
                        lblVerdict.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f1c40f;"); // Galben
                        lblMesaj.setText("Mesaj: Rezultatele vor fi afișate imediat ce sunt validate de laborator.");
                    } else {
                        // Cazul: FINALIZAT (ADMIS/RESPINS)
                        // Doar aici este sigur să apelăm .format() pe dataIntroducereRezultat
                        lblDataRez.setText("Rezultat procesat la: " + analiza.getDataIntroducereRezultat().format(formatter));

                        String rezultatStr = analiza.getRezultat() != null ? analiza.getRezultat().toString() : "NECUNOSCUT";
                        lblVerdict.setText(rezultatStr);

                        // Culori pentru ADMIS/RESPINS
                        if ("ADMIS".equals(rezultatStr)) {
                            lblVerdict.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;"); // Verde
                        } else {
                            lblVerdict.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;"); // Roșu
                        }
                        lblMesaj.setText("Mesaj: " + (analiza.getMesaj() != null ? analiza.getMesaj() : "Fără observații suplimentare."));
                    }

                    card.getChildren().addAll(lblHeader, lblDataRez, lblVerdict, lblMesaj);
                    setGraphic(card);
                    setStyle("-fx-background-color: transparent; -fx-padding: 10;");
                }
            }
        });
    }

    private void incarcaDate() {
        // 1. Preluăm donatorul logat curent
        Donator d = donatorService.getDonatorByUtilizator(autentificareService.getUtilizatorLogat());

        // 2. Obținem lista de analize (atât cele finalizate, cât și cele în așteptare)
        List<AnalizaSange> analize = donatorService.getIstoricAnalize(d);

        // 3. Aplicăm sortarea ierarhică
        analize.sort((a1, a2) -> {
            // Obținem datele de finalizare pentru a verifica statusul "În așteptare"
            var d1 = a1.getDataIntroducereRezultat();
            var d2 = a2.getDataIntroducereRezultat();

            // CAZUL A: Ambele analize sunt în așteptare
            if (d1 == null && d2 == null) {
                // Le sortăm descrescător după data donării (cea mai recentă donare prima)
                return a2.getDonare().getDataDonare().compareTo(a1.getDonare().getDataDonare());
            }

            // CAZUL B: Doar una este în așteptare
            if (d1 == null) return -1; // Analiza 1 urcă sus (în așteptare)
            if (d2 == null) return 1;  // Analiza 2 urcă sus (în așteptare)

            // CAZUL C: Ambele sunt finalizate
            // Le sortăm descrescător după data la care a fost introdus rezultatul
            return d2.compareTo(d1);
        });

        // 4. Actualizăm ListView-ul cu noua listă sortată
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