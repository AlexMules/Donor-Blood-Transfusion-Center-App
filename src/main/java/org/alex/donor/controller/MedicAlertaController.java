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
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import org.alex.donor.service.MedicService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MedicAlertaController implements Initializable {

    // Folosim MedicService conform codului tău
    private final MedicService medicService;
    private final ApplicationContext springContext;

    @FXML private ComboBox<GrupaSanguina> comboGrupa;
    @FXML private ComboBox<Rh> comboRh;
    @FXML private TextField fieldTitlu;
    @FXML private TextArea fieldContinut;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populăm selecțiile pentru Grupă și RH din Enum-uri
        comboGrupa.setItems(FXCollections.observableArrayList(GrupaSanguina.values()));
        comboRh.setItems(FXCollections.observableArrayList(Rh.values()));
    }

    @FXML
    public void handleSendAlert() {
        // 1. Preluare date din UI
        GrupaSanguina grupa = comboGrupa.getValue();
        Rh rh = comboRh.getValue();
        String titlu = fieldTitlu.getText().trim();
        String continut = fieldContinut.getText().trim();

        // 2. Validare câmpuri
        if (grupa == null || rh == null || titlu.isEmpty() || continut.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Vă rugăm să completați toate câmpurile!").show();
            return;
        }

        try {
            // 3. Apelăm metoda din MedicService
            medicService.trimiteAlertaUrgenta(grupa, rh, titlu, continut);

            // 4. Notificare succes
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("Alerta a fost salvată și trimisă donatorilor compatibili!");
            alert.showAndWait();

            // 5. Resetare câmpuri
            fieldTitlu.clear();
            fieldContinut.clear();
            comboGrupa.setValue(null);
            comboRh.setValue(null);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Eroare la trimiterea alertei: " + e.getMessage()).show();
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