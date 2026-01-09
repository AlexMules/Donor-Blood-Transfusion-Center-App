package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.service.AdministratorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AdminEditUserController {

    private final AdministratorService administratorService; // Serviciul care deține logica de actualizare
    private final ApplicationContext springContext;

    @FXML private TextField numeField, prenumeField, rolField, emailActualField, emailNouField;
    @FXML private PasswordField parolaNouaField;
    @FXML private Label errorLabel;
    @FXML private Button btnBack;

    private Utilizator utilizatorSelectat;

    /**
     * Metodă apelată din tabelul de gestionare pentru a trimite datele utilizatorului selectat.
     */
    public void initData(Utilizator user) {
        this.utilizatorSelectat = user;
        numeField.setText(user.getNume());
        prenumeField.setText(user.getPrenume());
        rolField.setText(user.getRol().toString());
        emailActualField.setText(user.getEmail());
    }

    @FXML
    private void handleSaveModifications() {
        errorLabel.setVisible(false);
        String nouEmail = emailNouField.getText().trim();
        String nouaParola = parolaNouaField.getText().trim();

        // 1. Validări de bază
        if (nouEmail.isEmpty() && nouaParola.isEmpty()) {
            afiseazaAlerta(Alert.AlertType.WARNING, "Atenție", "Nicio modificare",
                    "Vă rugăm să introduceți un email nou sau o parolă nouă.");
            return;
        }

        if (!nouEmail.isEmpty() && !isValidEmail(nouEmail)) {
            afiseazaAlerta(Alert.AlertType.ERROR, "Eroare Validare", "Format Email Invalid",
                    "Adresa de email introdusă nu are un format corect.");
            return;
        }

        try {
            // 2. Apelăm serviciul pentru actualizare
            administratorService.actualizeazaDatePersonal(utilizatorSelectat.getId(), nouEmail, nouaParola);

            // 3. Mesaj de SUCCES (Information)
            afiseazaAlerta(Alert.AlertType.INFORMATION, "Succes", "Actualizare Reușită",
                    "Datele utilizatorului " + utilizatorSelectat.getNume() + " au fost salvate.");

            // 4. Revenim la tabel DOAR după ce utilizatorul apasă OK pe alertă
            handleBack();

        } catch (RuntimeException e) {
            // 5. Mesaj de EROARE (Error)
            afiseazaAlerta(Alert.AlertType.ERROR, "Eroare Bază de Date", "Nu s-a putut efectua salvarea",
                    e.getMessage());
        }
    }

    private void afiseazaAlerta(Alert.AlertType tip, String titlu, String antet, String mesaj) {
        Alert alerta = new Alert(tip);
        alerta.setTitle(titlu);
        alerta.setHeaderText(antet);
        alerta.setContentText(mesaj);

        // showAndWait() oprește execuția codului până când utilizatorul închide fereastra
        alerta.showAndWait();
    }

    /**
     * Verifică formatul email-ului folosind o expresie regulată standard.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_view_users.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle("Gestionare Conturi Personal");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}