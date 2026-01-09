package org.alex.donor.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.DonatorService;
import org.alex.donor.service.ProgramareService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProgramareController implements Initializable {

    private final ProgramareService programareService;
    private final DonatorService donatorService;
    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private VBox boxGreen, boxPurple;
    @FXML private DatePicker datePickerZiua;
    @FXML private ComboBox<String> comboOra;
    @FXML private Label lblInfoZiua, lblInfoOra;
    @FXML private Button btnBack;

    private Donator donatorLogat;
    private Programare programareActivaCurenta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        donatorLogat = donatorService.getDonatorByUtilizator(autentificareService.getUtilizatorLogat());

        configurareRestrictiiCalendar();

        datePickerZiua.valueProperty().addListener((obs, vechi, nou) -> {
            if (nou != null) {
                actualizeazaOreDisponibile(nou);
            }
        });

        actualizeazaStareInterfata();
    }

    private void actualizeazaStareInterfata() {
        if (donatorLogat.getStatus() != StatusDonator.ELIGIBIL) {
            String statusFormatat = donatorLogat.getStatus().toString().replace("_", " ");
            dezactiveazaPanouProgramare("Nu te poți programa deoarece statusul tău actual este: " + statusFormatat);
            return;
        }

        Optional<Programare> activaOpt = programareService.getProgramareActiva(donatorLogat);

        if (activaOpt.isPresent()) {
            programareActivaCurenta = activaOpt.get();

            boxGreen.setDisable(true);
            boxGreen.setOpacity(0.5);
            boxPurple.setDisable(false);
            boxPurple.setOpacity(1.0);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            lblInfoZiua.setText("Ziua: " + programareActivaCurenta.getDataOraProgramare().toLocalDate().format(dtf));
            lblInfoOra.setText("Ora: " + programareActivaCurenta.getDataOraProgramare().toLocalTime().toString());
        } else {
            boxGreen.setDisable(false);
            boxGreen.setOpacity(1.0);
            boxPurple.setDisable(true);
            boxPurple.setOpacity(0.5);

            lblInfoZiua.setText("Ziua: --.--.----");
            lblInfoOra.setText("Ora: --:--");
            programareActivaCurenta = null;
        }
    }

    private void actualizeazaOreDisponibile(LocalDate data) {
        List<String> toateOrele = List.of("08:00", "09:00", "10:00", "11:00", "12:00");

        List<String> disponibile = toateOrele.stream()
                .filter(ora -> {
                    LocalDateTime ldt = LocalDateTime.of(data, LocalTime.parse(ora));
                    return !programareService.esteOraOcupata(ldt);
                })
                .collect(Collectors.toList());

        comboOra.setItems(FXCollections.observableArrayList(disponibile));
    }

    @FXML
    public void handleSchedule() {
        LocalDate data = datePickerZiua.getValue();
        String oraStr = comboOra.getValue();

        if (data == null || oraStr == null) {
            new Alert(Alert.AlertType.WARNING, "Vă rugăm să selectați atât ziua cât și ora!").show();
            return;
        }

        try {
            LocalDateTime dataOra = LocalDateTime.of(data, LocalTime.parse(oraStr));
            programareService.creeazaProgramare(donatorLogat, dataOra); //
            new Alert(Alert.AlertType.INFORMATION, "Programarea a fost realizată cu succes!").show();
            actualizeazaStareInterfata();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void handleCancel() {
        if (programareActivaCurenta == null) return;

        try {
            programareService.anuleazaProgramare(programareActivaCurenta.getId());
            new Alert(Alert.AlertType.INFORMATION, "Programarea a fost anulată.").show();
            actualizeazaStareInterfata();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void dezactiveazaPanouProgramare(String mesaj) {
        boxGreen.setDisable(true);
        boxGreen.setOpacity(0.5);
        boxPurple.setDisable(true);
        boxPurple.setOpacity(0.5);
        new Alert(Alert.AlertType.WARNING, mesaj).show();
    }

    private void configurareRestrictiiCalendar() {
        datePickerZiua.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now().plusDays(1)) ||
                        item.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                }
            }
        });
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