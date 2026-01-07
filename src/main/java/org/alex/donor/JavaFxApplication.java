package org.alex.donor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

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
        // Trimite un semnal către Spring că interfața este gata să apară
        context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        // Închide Spring-ul când închizi fereastra
        this.context.close();
        Platform.exit();
    }
}