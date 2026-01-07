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
    CommandLineRunner testValidareRespingere(MedicService medicService, ProgramareService programareService) {
        return args -> {
            System.out.println("\n--- TEST FINAL MEDIC: VALIDARE VS RESPINGERE ---");

            // Presupunem că avem două programări de test în DB
            // 1. Test Validare
            try {
                System.out.println("Testăm VALIDARE pentru programarea ID: 1");
                medicService.valideazaDonare(1);
            } catch (Exception e) { System.out.println(e.getMessage()); }

            // 2. Test Respingere
            try {
                System.out.println("Testăm RESPINGERE pentru programarea ID: 2");
                medicService.respingeDonare(2);
            } catch (Exception e) { System.out.println(e.getMessage()); }
        };
    }
}
