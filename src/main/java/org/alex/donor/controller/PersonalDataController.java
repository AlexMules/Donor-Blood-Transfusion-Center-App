package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.service.AutentificareService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PersonalDataController implements Initializable {

    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private TextField numeField, prenumeField, rolField, emailField;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Obținem utilizatorul curent din sesiune
        Utilizator userLogat = autentificareService.getUtilizatorLogat();

        if (userLogat != null) {
            numeField.setText(userLogat.getNume());
            prenumeField.setText(userLogat.getPrenume());
            rolField.setText(userLogat.getRol().toString());
            emailField.setText(userLogat.getEmail());
        }
    }

    @FXML
    public void handleBack() {
        Utilizator userLogat = autentificareService.getUtilizatorLogat();
        String fxmlPath = "";

        // Redirecționare în funcție de rolul utilizatorului
        if (userLogat.getRol() == Rol.ADMIN) {
            fxmlPath = "/fxml/admin_main.fxml";
        } else if (userLogat.getRol() == Rol.MEDIC) {
            fxmlPath = "/fxml/medic_main.fxml";
        } // Adaugă și restul rolurilor aici

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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