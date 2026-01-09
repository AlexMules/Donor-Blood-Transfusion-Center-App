package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // ADĂUGAT
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser; // ADĂUGAT
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.DonatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File; // ADĂUGAT
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files; // ADĂUGAT
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
        Utilizator u = autentificareService.getUtilizatorLogat();
        if (u != null) {
            Donator donator = donatorService.getDonatorByUtilizator(u);
            setareAfisareStatus(donator);
        }
    }

    private void setareAfisareStatus(Donator donator) {
        StatusDonator status = donatorService.getStatusDonator(donator);
        String textStatus = status.toString().replace("_", " ");
        statusLabel.setText(textStatus);

        if (status == StatusDonator.ELIGIBIL) {
            statusLabel.setStyle("-fx-text-fill: #00FF00;"); // Verde
        } else {
            statusLabel.setStyle("-fx-text-fill: #FFD700;"); // Galben
        }
    }

    /**
     * Gestionează descărcarea certificatului de donator în format PDF.
     */
    @FXML
    public void handleDownloadCertificat() {
        Utilizator u = autentificareService.getUtilizatorLogat();
        Donator d = donatorService.getDonatorByUtilizator(u);

        // 1. Verificare Eligibilitate (doar ELIGIBIL poate descărca)
        if (d.getStatus() != StatusDonator.ELIGIBIL) {
            afiseazaAlerta(Alert.AlertType.WARNING, "Acces Refuzat", "Ineligibil",
                    "Certificatul este disponibil doar pentru donatorii eligibili.");
            return;
        }

        // 2. Configurare dialog salvare fișier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvează Certificat Donator");
        fileChooser.setInitialFileName("Certificat_" + u.getNume() + "_" + u.getPrenume() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documente PDF", "*.pdf"));

        File file = fileChooser.showSaveDialog(btnLogout.getScene().getWindow());

        if (file != null) {
            try {
                // 3. Generare PDF prin serviciu
                byte[] pdfBytes = donatorService.genereazaCertificatComplet(d);

                // 4. Salvare fișier pe disk
                Files.write(file.toPath(), pdfBytes);

                afiseazaAlerta(Alert.AlertType.INFORMATION, "Succes", "Fișier Salvat",
                        "Certificatul a fost generat cu succes!");
            } catch (Exception e) {
                afiseazaAlerta(Alert.AlertType.ERROR, "Eroare", "Eroare la descărcare",
                        "Nu s-a putut genera fișierul: " + e.getMessage());
            }
        }
    }

    // --- METODE NAVIGARE EXISTENTE ---

    @FXML public void handleAlerte() { loadScene("/fxml/donator_alerte.fxml", "Alerte Stoc Sânge"); }
    @FXML public void handleProgramare() { loadScene("/fxml/donator_programare.fxml", "Programare Donare"); }
    @FXML public void handleIstoric() { loadScene("/fxml/donator_rezultate_analize.fxml", "Rezultate Analize"); }
    @FXML public void handleViewPersonalData() { loadScene("/fxml/personal_data.fxml", "Date Personale"); }
    @FXML public void handleEditAccount() { loadScene("/fxml/donator_edit_account.fxml", "Modificare Date Cont"); }
    @FXML public void handleGhid() { loadScene("/fxml/ghid_donator.fxml", "Ghidul Donatorului"); }

    @FXML
    public void handleLogout() {
        autentificareService.logout();
        loadScene("/fxml/login.fxml", "Login");
    }

    // --- METODE UTALITARE ---

    private void afiseazaAlerta(Alert.AlertType tip, String t, String h, String c) {
        Alert alert = new Alert(tip);
        alert.setTitle(t);
        alert.setHeaderText(h);
        alert.setContentText(c);
        alert.showAndWait();
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