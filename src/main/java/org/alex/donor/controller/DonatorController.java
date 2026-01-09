package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.DonatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class DonatorController implements Initializable {

    private final DonatorService donatorService;
    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private Label statusLabel;
    @FXML private Button btnLogout, btnAlerte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Obținem utilizatorul logat și profilul său de donator
        Utilizator u = autentificareService.getUtilizatorLogat();
        if (u != null) {
            Donator donator = donatorService.getDonatorByUtilizator(u);
            setareAfisareStatus(donator);
        }
    }

    private void setareAfisareStatus(Donator donator) {
        StatusDonator status = donatorService.getStatusDonator(donator);

        // Formatare text: INELIGIBIL_TEMPORAR -> INELIGIBIL TEMPORAR
        String textStatus = status.toString().replace("_", " ");
        statusLabel.setText(textStatus);

        // Aplicare culori conform cerinței
        if (status == StatusDonator.ELIGIBIL) {
            statusLabel.setStyle("-fx-text-fill: #00FF00;"); // Verde
        } else {
            statusLabel.setStyle("-fx-text-fill: #FFD700;"); // Galben pentru ineligibil_temporar/permanent
        }
    }

    @FXML
    public void handleAlerte() {
        // Aici vei deschide fereastra de alerte care folosește getAlertePersonale()
        loadScene("/fxml/donator_alerte.fxml", "Alerte Stoc Sânge");
    }

    @FXML
    public void handleProgramare() {
        loadScene("/fxml/donator_programare.fxml", "Programare Donare");
    }

    @FXML
    public void handleIstoric() {
        // Schimbăm calea de la donator_istoric.fxml la donator_rezultate_analize.fxml
        loadScene("/fxml/donator_rezultate_analize.fxml", "Rezultate Analize");
    }

    @FXML
    public void handleViewPersonalData() {
        loadScene("/fxml/personal_data.fxml", "Date Personale");
    }

    @FXML
    public void handleEditAccount() {
        loadScene("/fxml/donator_edit_account.fxml", "Modificare Date Cont");
    }

    @FXML
    public void handleLogout() {
        autentificareService.logout();
        loadScene("/fxml/login.fxml", "Login");
    }

    @FXML
    public void handleGhid() {
        loadScene("/fxml/ghid_donator.fxml", "Ghidul Donatorului");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();

            if (fxmlPath.contains("login")) {
                stage.setScene(new Scene(root, 800, 500));
            } else {
                stage.setScene(new Scene(root, 1000, 800));
            }

            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}