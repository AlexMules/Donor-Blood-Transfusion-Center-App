package org.alex.donor.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.service.AdministratorService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AdminDeleteUserController implements Initializable {

    private final AdministratorService adminService;
    private final ApplicationContext springContext;

    @FXML private TableView<Utilizator> userTable;
    @FXML private TableColumn<Utilizator, String> colNume;
    @FXML private TableColumn<Utilizator, String> colPrenume;
    @FXML private TableColumn<Utilizator, Rol> colRol;
    @FXML private TableColumn<Utilizator, Void> colActiune;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colPrenume.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        setupActionColumn();
        refreshTable();
    }

    private void refreshTable() {
        userTable.setItems(FXCollections.observableArrayList(adminService.getPersonalMedical()));
    }

    private void setupActionColumn() {
        Callback<TableColumn<Utilizator, Void>, TableCell<Utilizator, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnDelete = new Button("Șterge cont");
            {
                btnDelete.getStyleClass().add("btn-delete-row");

                btnDelete.setPrefWidth(130);

                btnDelete.setOnAction(event -> {
                    Utilizator user = getTableView().getItems().get(getIndex());
                    confirmAndDelete(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(btnDelete);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        };

        colActiune.setCellFactory(cellFactory);
    }

    private void confirmAndDelete(Utilizator user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare Ștergere");
        alert.setHeaderText(null);
        alert.setContentText("Ești sigur că vrei să ștergi contul utilizatorului: "
                + user.getNume() + " " + user.getPrenume() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    adminService.stergeUtilizator(user.getId());
                    refreshTable();
                } catch (RuntimeException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_main.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen(); //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }
}