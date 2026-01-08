package org.alex.donor.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.StocSange;
import org.alex.donor.service.BiologService;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class BiologModificaStocController implements Initializable {

    private final BiologService biologService;

    // Injectăm containerul principal pentru a-i putea cere focusul
    @FXML private VBox mainContainer;
    @FXML private TextField txtCantitateActuala, txtCantitateTrimisa;

    private StocSange stocCurent;
    private Runnable onSuccess;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Platform.runLater asigură că mutarea focusului are loc după ce fereastra e complet desenată
        Platform.runLater(() -> {
            if (mainContainer != null) {
                mainContainer.requestFocus();
            }
        });
    }

    public void initData(StocSange stoc, Runnable onSuccess) {
        this.stocCurent = stoc;
        this.onSuccess = onSuccess;
        txtCantitateActuala.setText(String.valueOf(stoc.getCantitateMl()));
    }

    @FXML
    public void handleSalveaza() {
        try {
            String input = txtCantitateTrimisa.getText().trim();
            if (input.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Introduceți cantitatea trimisă!").show();
                return;
            }

            int trimis = Integer.parseInt(input);

            if (trimis <= 0) {
                new Alert(Alert.AlertType.WARNING, "Cantitatea trebuie să fie un număr pozitiv!").show();
                return;
            }

            // Verificăm dacă nu se trimite mai mult decât există în stoc
            if (trimis > stocCurent.getCantitateMl()) {
                new Alert(Alert.AlertType.ERROR, "Nu puteți trimite " + trimis + " mL. În stoc sunt doar " + stocCurent.getCantitateMl() + " mL!").show();
                return;
            }

            // Apelăm serviciul corect pentru scăderea stocului
            biologService.scadeCantitateStoc(stocCurent.getId(), trimis);

            new Alert(Alert.AlertType.INFORMATION, "Stocul a fost actualizat cu succes!").showAndWait();

            if (onSuccess != null) onSuccess.run();
            handleAnuleaza();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Vă rugăm să introduceți un număr valid (ex: 450).").show();
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void handleAnuleaza() {
        // Închidem fereastra curentă (Stage-ul)
        ((Stage) mainContainer.getScene().getWindow()).close();
    }
}