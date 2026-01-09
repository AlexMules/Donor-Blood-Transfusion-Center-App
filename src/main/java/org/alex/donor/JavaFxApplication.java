package org.alex.donor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.InputStream;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // Pornește contextul Spring
        this.context = new SpringApplicationBuilder()
                .sources(DonorApplication.class)
                .run();
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Încărcăm fluxul de date al imaginii
        InputStream iconStream = getClass().getResourceAsStream("/images/logo_app.png");

        // 2. Verificăm dacă imaginea a fost găsită și o adăugăm la iconițele ferestrei
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        } else {
            System.err.println("Atenție: Imaginea /images/logo_app.png nu a fost găsită!");
        }

        // 3. Trimitem semnalul către Spring
        context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        // Închide Spring-ul când închizi fereastra
        this.context.close();
        Platform.exit();
    }
}