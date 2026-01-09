package org.alex.donor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.repository.BiologRepository;
import org.alex.donor.repository.MedicRepository;
import org.alex.donor.service.AutentificareService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PersonalDataController implements Initializable {

    private final AutentificareService autentificareService;
    private final ApplicationContext springContext;

    private final MedicRepository medicRepo;
    private final BiologRepository biologRepo;

    @FXML private TextField numeField, prenumeField, rolField, emailField, phoneField, codParafaField;
    @FXML private VBox containerParafa;
    @FXML private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilizator userLogat = autentificareService.getUtilizatorLogat();

        if (userLogat != null) {
            // date comune tuturor utilizatorilor
            numeField.setText(userLogat.getNume());
            prenumeField.setText(userLogat.getPrenume());
            rolField.setText(userLogat.getRol().toString());
            emailField.setText(userLogat.getEmail());
            phoneField.setText(userLogat.getNrTelefon());

            // logica specifica pentru medic/biolog
            if (userLogat.getRol() == Rol.MEDIC) {
                medicRepo.findByUtilizator(userLogat).ifPresent(medic -> {
                    activareCampParafa(medic.getCodParafa());
                });
            } else if (userLogat.getRol() == Rol.BIOLOG) {
                biologRepo.findByUtilizator(userLogat).ifPresent(biolog -> {
                    activareCampParafa(biolog.getCodParafa());
                });
            }
        }
    }

    private void activareCampParafa(String cod) {
        containerParafa.setVisible(true);
        containerParafa.setManaged(true);
        codParafaField.setText(cod);
    }

    @FXML
    public void handleBack() {
        Utilizator userLogat = autentificareService.getUtilizatorLogat();
        if (userLogat == null) return;

        String fxmlPath = switch (userLogat.getRol()) {
            case ADMIN   -> "/fxml/admin_main.fxml";
            case DONATOR -> "/fxml/donator_main.fxml";
            case MEDIC   -> "/fxml/medic_main.fxml";
            case BIOLOG  -> "/fxml/biolog_main.fxml";
        };

        String titlu = switch (userLogat.getRol()) {
            case ADMIN   -> "Administrator - Menu";
            case DONATOR -> "Donator - Dashboard";
            case MEDIC   -> "Medic - Dashboard";
            case BIOLOG  -> "Biolog - Dashboard";
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle(titlu);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}