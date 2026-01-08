package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.AnalizaSange;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class BiologVizualizareDetaliiController implements Initializable {

    private final ApplicationContext springContext;

    @FXML private TextField txtNume, txtPrenume, txtDataDonare, txtDataRezultat, txtGrupa, txtRh, txtCantitate, txtRezultat;
    @FXML private TextArea txtMesaj;
    @FXML private Button btnBack;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inițializarea de bază (dacă e cazul)
    }

    public void initData(AnalizaSange analiza) {
        // Maparea datelor din obiectul AnalizaSange către interfață
        txtNume.setText(analiza.getDonare().getDonator().getUtilizator().getNume());
        txtPrenume.setText(analiza.getDonare().getDonator().getUtilizator().getPrenume());

        txtDataDonare.setText(analiza.getDonare().getDataDonare().format(formatter));
        txtDataRezultat.setText(analiza.getDataIntroducereRezultat().format(formatter));

        txtGrupa.setText(analiza.getGrupaSanguina().toString());
        txtRh.setText(analiza.getRh().toString());
        txtCantitate.setText(String.valueOf(analiza.getCantitateMl()));
        txtRezultat.setText(analiza.getRezultat().toString());
        txtMesaj.setText(analiza.getMesaj());
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biolog_rezultate_finalizate.fxml"));
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