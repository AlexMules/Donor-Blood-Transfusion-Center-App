package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Programare;
import org.alex.donor.service.MedicService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MedicDetaliiDonatorController {

    private final MedicService medicService;
    private final ApplicationContext springContext;

    @FXML private Label lblNume, lblPrenume, lblVarsta, lblSex, lblGreutate, lblInaltime, lblGrupa, lblRh;
    @FXML private Button btnBack;

    private Programare programareCurenta;
    private LocalDate dataProvenienta;

    public void initData(Programare p) {
        this.programareCurenta = p;
        this.dataProvenienta = p.getDataOraProgramare().toLocalDate();

        lblNume.setText(p.getDonator().getUtilizator().getNume());
        lblPrenume.setText(p.getDonator().getUtilizator().getPrenume());
        lblVarsta.setText(String.valueOf(p.getDonator().getVarsta()));
        lblSex.setText(p.getDonator().getSex().toString());
        lblGreutate.setText(p.getDonator().getGreutate() + " kg");
        lblInaltime.setText(p.getDonator().getInaltime() + " m");
        lblGrupa.setText(p.getDonator().getGrupaSanguina().toString());
        lblRh.setText(p.getDonator().getRh().toString());
    }

    @FXML
    public void handleValideaza() {
        if (programareCurenta == null) return;

        try {
            medicService.valideazaDonare(programareCurenta.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("Donarea a fost validată. Programarea este finalizată, iar analizele au fost trimise către biolog.");
            alert.showAndWait();

            handleBack();

        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Eroare");
            errorAlert.setHeaderText("Nu s-a putut valida donarea");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void handleRespinge() {
        if (programareCurenta == null) return;

        try {
            medicService.respingeDonare(programareCurenta.getId());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Donare Respinsă");
            alert.setHeaderText(null);
            alert.setContentText("Donatorul a fost declarat inapt pentru donare în această sesiune. Programarea a fost marcată ca respinsă.");
            alert.showAndWait();

            handleBack();

        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Eroare");
            errorAlert.setHeaderText("Nu s-a putut respinge donarea");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/medic_programari.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            MedicProgramariController controller = loader.getController();
            controller.incarcaDataSpecifica(dataProvenienta);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}