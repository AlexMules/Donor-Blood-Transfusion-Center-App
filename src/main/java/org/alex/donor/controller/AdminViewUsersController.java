package org.alex.donor.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AdminViewUsersController implements Initializable {

    private final AdministratorService administratorService; // Serviciul tău
    private final ApplicationContext springContext;

    @FXML private TableView<Utilizator> tabelUtilizatori;
    @FXML private TableColumn<Utilizator, String> colNume, colPrenume, colRol;
    @FXML private TableColumn<Utilizator, Void> colActiuni;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareColoane();
        incarcaUtilizatori();
    }

    private void configurareColoane() {
        // Mapăm coloanele către atributele modelului Utilizator
        colNume.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNume()));
        colPrenume.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenume()));
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRol().toString()));

        // Configurăm butonul "Modifică date" pentru fiecare rând
        colActiuni.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifica = new Button("Modifică date");

            {
                btnModifica.getStyleClass().add("btn-action-white-small");
                btnModifica.setOnAction(event -> {
                    Utilizator user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(btnModifica);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void incarcaUtilizatori() {
        // Apelăm metoda din serviciul tău care returnează doar MEDICI și BIOLOGI
        List<Utilizator> personal = administratorService.getPersonalMedical();
        tabelUtilizatori.setItems(FXCollections.observableArrayList(personal));
    }

    private void handleEditUser(Utilizator user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_edit_user.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            // Obținem controllerul ferestrei noi și îi pasăm utilizatorul selectat
            AdminEditUserController controller = loader.getController();
            controller.initData(user);

            Stage stage = (Stage) tabelUtilizatori.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}