package org.alex.donor.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.service.AdministratorService;
import org.alex.donor.service.AutentificareService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AdminAddUserController implements Initializable {

    private final ApplicationContext springContext;
    private final AdministratorService adminService; // Injectăm serviciul tău

    @FXML private ComboBox<String> rolCombo;
    @FXML private TextField emailField, phoneField, numeField, prenumeField, codParafaField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button btnBack;
    @FXML private HBox codParafaContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolCombo.setItems(FXCollections.observableArrayList("Medic", "Biolog"));
        rolCombo.setValue("Medic");

        rolCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            codParafaContainer.setVisible(newVal.equals("Medic"));
            if (newVal.equals("Biolog")) codParafaField.clear();
        });
    }

    @FXML
    public void handleSaveUser() {
        try {
            validateFields(); // Verifică email, telefon, majuscule și câmpuri goale

            // 1. Pregătim obiectul Utilizator
            Utilizator u = new Utilizator();
            u.setEmail(emailField.getText().trim());
            u.setParola(passwordField.getText()); // Serviciul ar trebui să o cripteze
            u.setNrTelefon(phoneField.getText().trim());
            u.setNume(numeField.getText().trim());
            u.setPrenume(prenumeField.getText().trim());

            // Mapăm string-ul din combo la Enum-ul Rol
            Rol rolSelesctat = rolCombo.getValue().equals("Medic") ? Rol.MEDIC : Rol.BIOLOG;
            u.setRol(rolSelesctat);

            // 2. Apelăm serviciul pentru salvare
            // Notă: Dacă serviciul tău are o metodă specifică, adaptează aici
            String codParafa = rolSelesctat == Rol.MEDIC ? codParafaField.getText().trim() : null;
            adminService.creeazaContPersonalMedical(u, codParafa);

            // 3. Afișăm mesaj de succes
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("Utilizatorul " + u.getNume() + " a fost adăugat cu succes!");
            alert.showAndWait();

            // 4. Golim câmpurile pentru o nouă adăugare
            clearFields();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void validateFields() throws Exception {
        if (isAnyEmpty()) throw new Exception("Toate câmpurile sunt obligatorii!");

        // Restricție: email de forma ion@email.com
        if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new Exception("Format email invalid (ex: ion@email.com)!");

        // Restricție: nr telefon din 10 cifre
        if (!phoneField.getText().matches("\\d{10}"))
            throw new Exception("Numărul de telefon trebuie să aibă fix 10 cifre!");

        // Restricție: Nume și prenume să înceapă cu majusculă
        if (!numeField.getText().matches("[A-Z][a-z]*") || !prenumeField.getText().matches("[A-Z][a-z]*"))
            throw new Exception("Numele și prenumele trebuie să înceapă cu majusculă!");

        if (rolCombo.getValue().equals("Medic") && codParafaField.getText().isEmpty())
            throw new Exception("Codul de parafă este obligatoriu pentru medici!");
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

    private boolean isAnyEmpty() {
        return emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                numeField.getText().isEmpty() || prenumeField.getText().isEmpty();
    }
}