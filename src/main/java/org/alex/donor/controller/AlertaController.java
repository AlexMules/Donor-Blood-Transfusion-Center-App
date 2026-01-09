package org.alex.donor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Alerta;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.DonatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AlertaController implements Initializable {

    private final DonatorService donatorService;
    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private ListView<Alerta> alertaListView;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareCeluleCard();
        alertaListView.setSelectionModel(new NoSelectionModel<>());

        var utilizator = autentificareService.getUtilizatorLogat();
        var donator = donatorService.getDonatorByUtilizator(utilizator);
        var status = donatorService.getStatusDonator(donator);

        if (status != StatusDonator.ELIGIBIL) {
            String statusFormatat = status.toString().replace("_", " ");

            Label placeholder = new Label("Momentan nu poți vizualiza alertele deoarece statusul tău actual este:\n\n" + statusFormatat);

            placeholder.setStyle("-fx-text-fill: white; " +
                    "-fx-font-size: 26px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-alignment: center;");

            placeholder.setWrapText(true);
            placeholder.setMaxWidth(700);

            alertaListView.setPlaceholder(placeholder);
        } else {
            try {
                alertaListView.setItems(FXCollections.observableArrayList(donatorService.getAlertePersonale()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void configurareCeluleCard() {
        alertaListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Alerta alerta, boolean empty) {
                super.updateItem(alerta, empty);
                if (empty || alerta == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    VBox card = new VBox(10);
                    card.getStyleClass().add("alert-card");

                    Label titluLabel = new Label(alerta.getTitluMesaj());
                    titluLabel.getStyleClass().add("alert-card-title");

                    Label continutLabel = new Label(alerta.getContinutMesaj());
                    continutLabel.getStyleClass().add("alert-card-content");
                    continutLabel.setWrapText(true);
                    continutLabel.setMaxWidth(800.0);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, 'ora' HH:mm", new Locale("ro"));
                    Label dataLabel = new Label(alerta.getDataOra().format(formatter));
                    dataLabel.getStyleClass().add("alert-card-date");

                    card.getChildren().addAll(titluLabel, continutLabel, dataLabel);
                    setGraphic(card);
                    setStyle("-fx-background-color: transparent;");
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
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class NoSelectionModel<T> extends MultipleSelectionModel<T> {
        @Override public ObservableList<Integer> getSelectedIndices() { return FXCollections.emptyObservableList(); }
        @Override public ObservableList<T> getSelectedItems() { return FXCollections.emptyObservableList(); }
        @Override public void selectIndices(int index, int... indices) {}
        @Override public void selectAll() {}
        @Override public void selectFirst() {}
        @Override public void selectLast() {}
        @Override public void clearAndSelect(int index) {}
        @Override public void select(int index) {}
        @Override public void select(T obj) {}
        @Override public void clearSelection(int index) {}
        @Override public void clearSelection() {}
        @Override public boolean isSelected(int index) { return false; }
        @Override public boolean isEmpty() { return true; }
        @Override public void selectPrevious() {}
        @Override public void selectNext() {}
    }
}