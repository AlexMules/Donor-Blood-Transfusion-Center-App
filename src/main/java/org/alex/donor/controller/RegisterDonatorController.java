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
import org.alex.donor.model.Adresa;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import org.alex.donor.model.enums.Sex;
import org.alex.donor.service.DonatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class RegisterDonatorController implements Initializable {

    private final DonatorService donatorService;
    private final ApplicationContext springContext;

    @FXML private TextField emailField, phoneField, numeField, prenumeField, cnpField, dataNasteriiField;
    @FXML private PasswordField passwordField;
    @FXML private TextField greutateField, inaltimeField, judetField, localitateField, stradaField, numarField, codPostalField;
    @FXML private ComboBox<String> sexCombo, grupaCombo, rhCombo;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sexCombo.setItems(FXCollections.observableArrayList("M", "F"));
        grupaCombo.setItems(FXCollections.observableArrayList("0", "A", "B", "AB"));
        rhCombo.setItems(FXCollections.observableArrayList("+", "-"));
    }

    @FXML
    public void handleRegister() {
        try {
            validateFields(); // Execută toate restricțiile cerute

            // 1. Construim obiectul Utilizator
            Utilizator u = new Utilizator();
            u.setEmail(emailField.getText().trim());
            u.setParola(passwordField.getText());
            u.setNume(numeField.getText().trim());
            u.setPrenume(prenumeField.getText().trim());
            u.setNrTelefon(phoneField.getText().trim());

            // 2. Construim obiectul Adresa
            Adresa a = new Adresa();
            a.setJudet(judetField.getText().trim());
            a.setLocalitate(localitateField.getText().trim());
            a.setStrada(stradaField.getText().trim());
            a.setNumar(Integer.parseInt(numarField.getText().trim()));
            a.setCodPostal(codPostalField.getText().trim());

            // 3. Construim obiectul Donator
            Donator d = new Donator();
            d.setCnp(cnpField.getText().trim());
            d.setSex(mapSex(sexCombo.getValue()));
            d.setGreutate(Float.valueOf(greutateField.getText().trim()));
            String inaltime = inaltimeField.getText().trim().replace(",", ".");
            d.setInaltime(Float.valueOf(inaltime));

            // Conversie Data: adăugăm automat ora 00:00:00
            LocalDate date = LocalDate.parse(dataNasteriiField.getText());
            d.setDataNasterii(LocalDateTime.of(date, LocalTime.MIDNIGHT));

            // Conversie Enums pentru Sânge (presupunând denumirile standard din modelele tale)
            d.setGrupaSanguina(mapGrupa(grupaCombo.getValue()));
            d.setRh(mapRh(rhCombo.getValue()));

            // 4. Apelăm DonatorService pentru salvare (include criptarea parolei și salvarea în lanț)
            donatorService.inregistrareDonator(u, a, d);

            // Succes! Mergem înapoi la login
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cont creat cu succes!");
            alert.showAndWait();
            handleBack();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void validateFields() throws Exception {
        if (isAnyEmpty()) throw new Exception("Toate câmpurile sunt obligatorii!");

        // Validări Regex de bază
        if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) throw new Exception("Email invalid!");
        if (!phoneField.getText().matches("\\d{10}")) throw new Exception("Telefonul trebuie să aibă 10 cifre!");

        // Greutatea: Doar cifre (număr întreg)
        if (!greutateField.getText().trim().matches("\\d+")) {
            throw new Exception("Greutatea trebuie să fie un număr întreg!");
        }

        // Înălțimea: Permitem cifre și opțional un punct/virgulă pentru zecimale
        if (!inaltimeField.getText().trim().replace(",", ".").matches("\\d+(\\.\\d+)?")) {
            throw new Exception("Înălțimea trebuie să fie un număr (ex: 1.75 sau 175)!");
        }

        if (!cnpField.getText().matches("\\d{13}")) throw new Exception("CNP-ul trebuie să aibă 13 cifre!");
        if (!codPostalField.getText().matches("\\d{6}")) throw new Exception("Codul poștal trebuie să aibă 6 cifre!");

        // Validare format dată (AAAA-LL-ZZ)
        try { LocalDate.parse(dataNasteriiField.getText()); }
        catch (Exception e) { throw new Exception("Data nașterii invalidă! Folosiți formatul AAAA-LL-ZZ."); }
    }

    private boolean isAnyEmpty() {
        return emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                numeField.getText().isEmpty() || prenumeField.getText().isEmpty() ||
                cnpField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                judetField.getText().isEmpty() || sexCombo.getValue() == null;
    }

    private GrupaSanguina mapGrupa(String val) {
        return switch (val) {
            case "0" -> GrupaSanguina.ZERO;
            case "A" -> GrupaSanguina.A;
            case "B" -> GrupaSanguina.B;
            case "AB" -> GrupaSanguina.AB;
            default -> null;
        };
    }

    private Rh mapRh(String val) {
        return val.equals("+") ? Rh.POZITIV : Rh.NEGATIV;
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    private Sex mapSex(String val) {
        if (val == null) return null;
        return val.equals("M") ? Sex.M : Sex.F;
    }
}