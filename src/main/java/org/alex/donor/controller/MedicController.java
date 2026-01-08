package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.service.AutentificareService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MedicController {

    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private Button btnLogout;

    @FXML
    public void handleViewAppointments() {
        // Navigare către pagina de vizualizare programări
        loadScene("/fxml/medic_programari.fxml", "Vizualizare Programări");
    }

    @FXML
    public void handleViewStock() {
        // Navigare către pagina de vizualizare stoc sânge
        loadScene("/fxml/medic_stoc.fxml", "Vizualizare Stoc Sânge");
    }

    @FXML
    public void handleSendAlert() {
        // Navigare către pagina de trimitere alertă
        loadScene("/fxml/medic_trimitere_alerta.fxml", "Trimitere Alertă");
    }

    @FXML
    public void handleViewPersonalData() {
        // Navigare către pagina de date personale
        loadScene("/fxml/personal_data.fxml", "Date Personale Medic");
    }

    @FXML
    public void handleLogout() {
        // Realizează logout-ul prin serviciu conform cerinței
        autentificareService.logout();
        loadScene("/fxml/login.fxml", "Login");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            // Obținem Stage-ul curent folosind butonul de logout
            Stage stage = (Stage) btnLogout.getScene().getWindow();

            if (fxmlPath.contains("login")) {
                stage.setScene(new Scene(root, 800, 500));
                stage.setResizable(false);
            } else {
                stage.setScene(new Scene(root, 1000, 800));
                stage.setResizable(true);
            }

            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}