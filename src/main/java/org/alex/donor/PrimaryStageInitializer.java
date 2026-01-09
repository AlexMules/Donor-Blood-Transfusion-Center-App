package org.alex.donor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final ApplicationContext context;

    @Value("classpath:/fxml/login.fxml")
    private Resource loginFxml;

    public PrimaryStageInitializer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(loginFxml.getURL());
            fxmlLoader.setControllerFactory(context::getBean);

            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 800, 500));
            stage.setTitle("Sistem Gestiune Donare Sânge");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut încărca fișierul FXML!", e);
        }
    }
}