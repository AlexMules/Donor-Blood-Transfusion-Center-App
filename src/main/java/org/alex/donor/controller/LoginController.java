package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.service.AutentificareService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginController {

    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String parola = passwordField.getText();

        if (email.isEmpty() || parola.isEmpty()) {
            showError("Te rugăm să completezi toate câmpurile!");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Formatul email-ului este invalid!");
            return;
        }

        try {
            autentificareService.login(email, parola);
            Utilizator user = autentificareService.getUtilizatorLogat();
            navigateToDashboard(user.getRol());

        } catch (Exception e) {
            showError("Date de logare incorecte!");
        }
    }

    @FXML
    public void handleRegister() {
        loadScene("/fxml/register_donator.fxml", "Înregistrare Donator");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void navigateToDashboard(Rol rol) {
        String fxmlPath = switch (rol) {
            case DONATOR -> "/fxml/donator_main.fxml";
            case MEDIC -> "/fxml/medic_main.fxml";
            case BIOLOG -> "/fxml/biolog_main.fxml";
            case ADMIN -> "/fxml/admin_main.fxml";
        };
        loadScene(fxmlPath, "Dashboard " + rol);
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();

            if (fxmlPath.contains("dashboard") || fxmlPath.contains("main") ||
                    fxmlPath.contains("register") || fxmlPath.contains("admin")) {

                stage.setScene(new Scene(root, 1000, 800));
                stage.setMinWidth(1000);
                stage.setMinHeight(750);
                stage.setResizable(true);
            } else {
                stage.setScene(new Scene(root, 800, 500));
                stage.setResizable(false); // Login-ul rămâne fix
            }

            stage.setTitle(title);
            stage.centerOnScreen();

        } catch (IOException e) {
            showError("Eroare la încărcarea ferestrei: " + title);
            e.printStackTrace();
        }
    }
}