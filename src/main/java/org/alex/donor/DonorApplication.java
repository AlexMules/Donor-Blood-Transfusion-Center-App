package org.alex.donor;

import lombok.extern.slf4j.Slf4j;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.repository.AnalizaSangeRepository;
import org.alex.donor.repository.DonareRepository;
import org.alex.donor.repository.UtilizatorRepository;
import org.alex.donor.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@Slf4j
public class DonorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonorApplication.class, args);
    }

    @Bean
    CommandLineRunner testVizualizareStoc(MedicService medicService) {
        return args -> {
            System.out.println("\n========== TEST MEDIC: VIZUALIZARE STOC SÂNGE ==========");

            try {
                List<StocSange> stoc = medicService.getStocSangeComplet();

                if (stoc.isEmpty()) {
                    System.out.println("[-] Stocul este gol în baza de date.");
                } else {
                    System.out.println(String.format("%-15s | %-10s | %-15s", "GRUPĂ", "RH", "CANTITATE (ml)"));
                    System.out.println("----------------------------------------------");
                    for (StocSange s : stoc) {
                        System.out.println(String.format("%-15s | %-10s | %-15s",
                                s.getGrupaSanguina(),
                                s.getRh(),
                                s.getCantitateMl()));
                    }
                }
            } catch (Exception e) {
                System.err.println("Eroare la citirea stocului: " + e.getMessage());
            }

            System.out.println("========================================================\n");
        };
    }
}
