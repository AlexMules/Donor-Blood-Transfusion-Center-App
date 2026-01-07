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
    private final ApplicationContext springContext; // Avem nevoie de el pentru a schimba scenele

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String parola = passwordField.getText();

        // 1. Validare: Câmpuri goale
        if (email.isEmpty() || parola.isEmpty()) {
            showError("Te rugăm să completezi toate câmpurile!");
            return;
        }

        // 2. Validare: Format Email (ceva@ceva.com)
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Formatul email-ului este invalid!");
            return;
        }

        try {
            // 3. Încercare autentificare prin Service
            autentificareService.login(email, parola);
            Utilizator user = autentificareService.getUtilizatorLogat();

            // 4. Navigare în funcție de rol
            navigateToDashboard(user.getRol());

        } catch (Exception e) {
            showError("Date de logare incorecte sau eroare server.");
        }
    }

    @FXML
    public void handleRegister() {
        // Metoda pentru butonul "Înregistrează-te"
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
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            showError("Eroare la încărcarea ferestrei următoare.");
            e.printStackTrace();
        }
    }
}