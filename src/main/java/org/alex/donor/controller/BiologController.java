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
public class BiologController {

    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private Button btnLogout;

    @FXML
    public void handleIntroducereRezultate() {
        loadScene("/fxml/biolog_analize_asteptare.fxml", "Vizualizare Analize In Asteptare");
    }

    @FXML
    public void handleVizualizareRezultate() {
        loadScene("/fxml/biolog_rezultate_finalizate.fxml", "Vizualizare Rezultate Analize");
    }

    @FXML
    public void handleVizualizareStoc() {
        loadScene("/fxml/biolog_stoc.fxml", "Gestiune Stoc Sânge");
    }

    @FXML
    public void handleVizualizareDatePersonale() {
        loadScene("/fxml/personal_data.fxml", "Date Personale Biolog");
    }

    @FXML
    public void handleLogout() {
        autentificareService.logout();
        loadScene("/fxml/login.fxml", "Login");
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