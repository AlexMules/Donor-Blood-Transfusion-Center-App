package org.alex.donor.controller;

import javafx.beans.property.SimpleObjectProperty;
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
import org.alex.donor.model.StocSange;
import org.alex.donor.model.Utilizator;
import org.alex.donor.service.AutentificareService;
import org.alex.donor.service.MedicService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class BiologStocController implements Initializable {

    private final MedicService medicService;
    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    @FXML private TableView<StocSange> tabelStoc;
    @FXML private TableColumn<StocSange, String> colGrupa, colRh;
    @FXML private TableColumn<StocSange, Integer> colCantitate;
    @FXML private TableColumn<StocSange, Void> colActiuni;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurareColoane();
        incarcaDateStoc();
    }

    private void configurareColoane() {
        // Mapare conform entității StocSange
        colGrupa.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGrupaSanguina().toString()));
        colRh.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRh().toString()));
        colCantitate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCantitateMl()));

        // Setăm politica de redimensionare programatic (sau în FXML) pentru a umple tabelul
        tabelStoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        colActiuni.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifica = new Button("Modifică Cantitatea");
            {
                btnModifica.getStyleClass().add("btn-action-white-small");
                btnModifica.setOnAction(event -> {
                    StocSange s = getTableView().getItems().get(getIndex());
                    handleModificaCantitate(s);
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

    private void incarcaDateStoc() {
        // MedicService returnează lista din repo
        tabelStoc.setItems(FXCollections.observableArrayList(medicService.getStocSangeComplet()));
    }

    private void handleModificaCantitate(StocSange s) {
        try {
            // 1. Încărcăm FXML-ul pentru pop-up
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biolog_modifica_stoc.fxml"));

            // 2. Setăm factory-ul pentru Spring
            loader.setControllerFactory(springContext::getBean);

            Parent root = loader.load();

            // 3. Obținem controller-ul ferestrei pop-up
            BiologModificaStocController controller = loader.getController();

            // 4. Transmitem datele și o metodă de refresh (callback)
            // Îi trimitem obiectul de stoc selectat și metoda incarcaDateStoc pentru a reîmprospăta tabelul după salvare
            controller.initData(s, this::incarcaDateStoc);

            // 5. Creăm o scenă nouă pentru fereastra pop-up
            Stage stage = new Stage();
            stage.setTitle("Modificare Cantitate Stoc");
            stage.setScene(new Scene(root));

            // 6. Setăm fereastra ca MODALĂ (blochează interacțiunea cu tabelul până e închisă)
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Eroare la deschiderea ferestrei de modificare stoc: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        Utilizator userLogat = autentificareService.getUtilizatorLogat();

        // Acum navigarea este dinamică bazată pe cine este la tastatură
        String fxmlPath = switch (userLogat.getRol()) {
            case MEDIC -> "/fxml/medic_main.fxml";
            case BIOLOG -> "/fxml/biolog_main.fxml";
            default -> "/fxml/login.fxml";
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.centerOnScreen();
        } catch (IOException e) { e.printStackTrace(); }
    }
}