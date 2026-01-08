package org.alex.donor.controller;

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
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.RezultatAnaliza;
import org.alex.donor.model.enums.Rh;
import org.alex.donor.service.BiologService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class BiologDetaliiAnalizaController implements Initializable {

    private final BiologService biologService;
    private final ApplicationContext springContext;

    @FXML private Label lblTitluAnaliza;
    @FXML private ComboBox<GrupaSanguina> comboGrupa;
    @FXML private ComboBox<Rh> comboRh;
    @FXML private ComboBox<RezultatAnaliza> comboRezultat;
    @FXML private TextField txtCantitate;
    @FXML private TextArea txtMesaj;
    @FXML private Button btnBack;

    private AnalizaSange analizaCurenta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populăm ComboBox-urile cu valorile din Enums definite în model
        comboGrupa.setItems(FXCollections.observableArrayList(GrupaSanguina.values()));
        comboRh.setItems(FXCollections.observableArrayList(Rh.values()));

        // Permitem doar ADMIS sau RESPINS pentru rezultat (fără IN_ASTEPTARE)
        comboRezultat.setItems(FXCollections.observableArrayList(RezultatAnaliza.ADMIS, RezultatAnaliza.RESPINS));
    }

    /**
     * Primește obiectul AnalizaSange din tabelul anterior pentru a-l popula în interfață.
     */
    public void initData(AnalizaSange analiza) {
        this.analizaCurenta = analiza;
    }

    @FXML
    public void handleSalveaza() {
        try {
            // 1. Preluăm datele selectate de biolog
            GrupaSanguina grupa = comboGrupa.getValue();
            Rh rh = comboRh.getValue();
            RezultatAnaliza rezultat = comboRezultat.getValue();
            String cantitateStr = txtCantitate.getText().trim();
            String mesaj = txtMesaj.getText();

            // 2. Validare: toate câmpurile de selecție și cantitatea sunt obligatorii
            if (grupa == null || rh == null || rezultat == null || cantitateStr.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vă rugăm să completați toate câmpurile obligatorii!").show();
                return;
            }

            Integer cantitate = Integer.parseInt(cantitateStr);

            // 3. Apelăm serviciul care gestionează tranzacția (Analiza + Stoc + Status Donator)
            biologService.introducereRezultatAnaliza(
                    analizaCurenta.getId(),
                    cantitate,
                    grupa,
                    rh,
                    rezultat,
                    mesaj
            );

            // 4. Confirmare succes și întoarcere la listă
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rezultatul a fost salvat cu succes!");
            alert.showAndWait();
            handleBack();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Cantitatea trebuie să fie un număr întreg valid!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Eroare la salvare: " + e.getMessage()).show();
        }
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biolog_analize_asteptare.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle("Analize în Așteptare");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}