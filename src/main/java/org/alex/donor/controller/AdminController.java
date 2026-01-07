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
public class AdminController {

    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private Button btnLogout;

    @FXML
    public void handleAddAccount() {
        loadScene("/fxml/admin_add_user.fxml", "Adăugare Utilizator Nou");
    }

    @FXML
    public void handleDeleteAccount() {
        loadScene("/fxml/admin_delete_user.fxml", "Ștergere Personal Medical");
    }

    @FXML
    public void handleViewPersonalData() {
        // ACTUALIZARE: Schimbăm calea către noul fișier FXML de date personale
        loadScene("/fxml/personal_data.fxml", "Vizualizare Date Personale");
    }

    @FXML
    public void handleLogout() {
        autentificareService.logout();
        loadScene("/fxml/login.fxml", "Login - Donor System");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnLogout.getScene().getWindow();

            if (fxmlPath.contains("login")) {
                stage.setScene(new Scene(root, 800, 500));
                stage.setResizable(false);
            } else {
                // Dimensiunea de 1000x800 asigură spațiu suficient pentru datele personale
                stage.setScene(new Scene(root, 1000, 800));
                stage.setResizable(true);
            }

            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Eroare la încărcarea scenei: " + fxmlPath);
            e.printStackTrace();
        }
    }
}