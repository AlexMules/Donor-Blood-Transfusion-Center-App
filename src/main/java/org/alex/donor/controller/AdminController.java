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

    // Folosim un buton existent pentru a obține referința către Stage (fereastră)
    @FXML private Button btnLogout;

    @FXML
    public void handleAddAccount() {
        // Aceasta va deschide fereastra de adăugare medic/biolog
        loadScene("/fxml/admin_add_user.fxml", "Adăugare Utilizator Nou");
    }

    @FXML
    public void handleDeleteAccount() {
        // Aici vei implementa logica de ștergere ulterior
        System.out.println("Navigare către Ștergere Cont...");
    }

    @FXML
    public void handleViewPersonalData() {
        // Aici vei implementa vizualizarea datelor adminului
        System.out.println("Vizualizare date personale administrator");
    }

    @FXML
    public void handleLogout() {
        // 1. Apelăm serviciul pentru a șterge sesiunea
        autentificareService.logout();

        // 2. Încărcăm pagina de Login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

            // FOARTE IMPORTANT: Folosim contextul Spring pentru a injecta LoginController
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            // 3. Obținem Stage-ul curent și schimbăm scena
            Stage stage = (Stage) btnLogout.getScene().getWindow();

            // Resetăm dimensiunea pentru Login
            stage.setScene(new Scene(root, 800, 500));
            stage.setResizable(false); // Login-ul nu ar trebui să fie redimensionabil
            stage.setTitle("Login - Donor System");

            // Recentrăm fereastra deoarece dimensiunea s-a micșorat
            stage.centerOnScreen();

        } catch (IOException e) {
            System.err.println("Eroare la întoarcerea la pagina de login!");
            e.printStackTrace();
        }
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            // Obținem Stage-ul curent
            Stage stage = (Stage) btnLogout.getScene().getWindow();

            // Ajustăm dimensiunea în funcție de destinație
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
            System.err.println("Eroare la încărcarea scenei: " + fxmlPath);
            e.printStackTrace();
        }
    }


}