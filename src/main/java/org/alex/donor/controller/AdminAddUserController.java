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
import org.alex.donor.model.enums.Rol;
import org.alex.donor.service.AdministratorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AdminAddUserController implements Initializable {

    private final ApplicationContext springContext;
    private final AdministratorService adminService;

    @FXML private ComboBox<Rol> rolCombo;
    @FXML private TextField emailField, phoneField, numeField, prenumeField, codParafaField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolCombo.setItems(FXCollections.observableArrayList(Rol.MEDIC, Rol.BIOLOG));
        rolCombo.setValue(Rol.MEDIC);
    }

    @FXML
    public void handleSaveUser() {
        try {
            validateFields();

            String email = emailField.getText().trim();
            String parola = passwordField.getText();
            String nrTelefon = phoneField.getText().trim();
            String nume = numeField.getText().trim();
            String prenume = prenumeField.getText().trim();
            String codParafa = codParafaField.getText().trim();
            Rol rolSelectat = rolCombo.getValue();

            adminService.creeazaContPersonalMedical(
                    email, parola, nrTelefon, nume, prenume, codParafa, rolSelectat
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("Utilizatorul a fost adăugat cu succes în baza de date!");
            alert.showAndWait();

            clearFields();

        } catch (RuntimeException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void validateFields() throws Exception {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || numeField.getText().isEmpty() ||
                prenumeField.getText().isEmpty() || codParafaField.getText().isEmpty()) {
            throw new Exception("Toate câmpurile sunt obligatorii!");
        }

        // verificare format email
        if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new Exception("Formatul email-ului este invalid (ex: ion@email.com)!");
        }

        if (!phoneField.getText().matches("^\\d{10}$")) {
            throw new Exception("Numărul de telefon trebuie să aibă exact 10 cifre!");
        }

        if (!numeField.getText().matches("^[A-Z][a-z]*$")) {
            throw new Exception("Numele trebuie să înceapă cu majusculă!");
        }

        if (!prenumeField.getText().matches("^[A-Z][a-z]*$")) {
            throw new Exception("Prenumele trebuie să înceapă cu majusculă!");
        }
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
        phoneField.clear();
        numeField.clear();
        prenumeField.clear();
        codParafaField.clear();
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_main.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}