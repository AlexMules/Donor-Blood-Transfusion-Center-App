package org.alex.donor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@Slf4j
@EnableScheduling
public class DonorApplication {

    public static void main(String[] args) {
        javafx.application.Application.launch(JavaFxApplication.class, args);
    }
}
