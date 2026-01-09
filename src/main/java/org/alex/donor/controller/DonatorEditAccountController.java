package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.DonatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DonatorEditAccountController implements Initializable {

    private final DonatorService donatorService; // Serviciul unde am mutat logica de actualizare
    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private TextField emailCurentField, emailNouField;
    @FXML private PasswordField parolaActualaField, parolaNouaField;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // La deschiderea ferestrei, afișăm email-ul actual al donatorului
        Utilizator userLogat = autentificareService.getUtilizatorLogat();
        if (userLogat != null) {
            emailCurentField.setText(userLogat.getEmail());
        }
    }

    @FXML
    private void handleSave() {
        String parolaActuala = parolaActualaField.getText();
        String emailNou = emailNouField.getText().trim();
        String parolaNoua = parolaNouaField.getText().trim();

        // 1. Verificăm dacă parola actuală a fost introdusă (este obligatorie pentru securitate)
        if (parolaActuala.isEmpty()) {
            afiseazaAlerta(Alert.AlertType.WARNING, "Atenție", "Câmp obligatoriu",
                    "Trebuie să introduceți parola actuală pentru a confirma modificările.");
            return;
        }

        // 2. Verificăm dacă există cel puțin o modificare de făcut
        if (emailNou.isEmpty() && parolaNoua.isEmpty()) {
            afiseazaAlerta(Alert.AlertType.WARNING, "Atenție", "Nicio modificare",
                    "Introduceți un email nou sau o parolă nouă.");
            return;
        }

        // 3. Validare format Email Nou (dacă a fost completat)
        if (!emailNou.isEmpty() && !isValidEmail(emailNou)) {
            afiseazaAlerta(Alert.AlertType.ERROR, "Eroare Validare", "Format Email Invalid",
                    "Vă rugăm să introduceți o adresă de email validă.");
            return;
        }

        try {
            // 4. Apelăm metoda din DonatorService care verifică parola și face update-ul
            donatorService.actualizeazaContDonator(
                    autentificareService.getUtilizatorLogat().getId(),
                    parolaActuala,
                    emailNou,
                    parolaNoua
            );

            // 5. Mesaj de succes și întoarcere la meniul principal
            afiseazaAlerta(Alert.AlertType.INFORMATION, "Succes", "Date Actualizate",
                    "Contul dumneavoastră a fost modificat cu succes.");
            handleBack();

        } catch (RuntimeException e) {
            // Prindem erorile (ex: parolă actuală greșită sau email duplicat) și le afișăm
            afiseazaAlerta(Alert.AlertType.ERROR, "Eroare la Salvare", "Operațiune eșuată", e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void afiseazaAlerta(Alert.AlertType tip, String titlu, String antet, String mesaj) {
        Alert alerta = new Alert(tip);
        alerta.setTitle(titlu);
        alerta.setHeaderText(antet);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/donator_main.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle("Donator - Dashboard");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}