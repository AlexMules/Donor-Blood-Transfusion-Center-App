package org.alex.donor;

import lombok.extern.slf4j.Slf4j;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.repository.*;
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
    @org.springframework.core.annotation.Order(6)
    CommandLineRunner testFluxCompletResetareSiAutentificare(
            UtilizatorService utilizatorService,
            AutentificareService autentificareService) {

        return args -> {
            System.out.println("\n=== TEST COMPLET: RESETARE -> LOGOUT -> LOGIN -> LOGOUT ===");

            // MODIFICĂ AICI: Folosește email-ul care există în baza ta de date
            String email = "test.donator@gmail.com";
            String parolaNoua = "SangeleSalveazaVieti2026!";

            // 1. RESETARE
            System.out.println("Step 1: Resetare parolă donator pentru " + email);
            try {
                utilizatorService.resetareParolaDonator(email, parolaNoua);

                // 2. LOGOUT
                autentificareService.logout();
                System.out.println("Step 2: Logout efectuat.");

                // 3. LOGIN
                System.out.println("Step 3: Încercare login cu parola nouă...");
                Utilizator u = autentificareService.login(email, parolaNoua);
                System.out.println("   SUCCES! Logat ca: " + u.getNume() + " " + u.getPrenume());

                // 4. LOGOUT FINAL
                autentificareService.logout();
                System.out.println("Step 4: Logout final efectuat.");

            } catch (RuntimeException e) {
                System.err.println("   EROARE în timpul testului: " + e.getMessage());
            }

            System.out.println("==========================================================\n");
        };
    }
}
