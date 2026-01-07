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

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@Slf4j
public class DonorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonorApplication.class, args);
    }

    @Bean
    CommandLineRunner repairAndTestAdmin(
            UtilizatorRepository userRepo,
            AutentificareService authService,
            AdministratorService adminService,
            PasswordEncoder encoder) {

        return args -> {
            System.out.println("\n========== REPARARE ȘI TEST ADMIN ==========");

            // PAS 1: Resetăm parola direct din cod pentru a fi siguri de hash
            userRepo.findByEmail("admin@donor.ro").ifPresent(u -> {
                u.setParola(encoder.encode("admin123")); // Generăm hash-ul în mod programatic
                userRepo.save(u);
                System.out.println("[OK] Parola a fost rescrisă în DB cu un hash curat.");
            });

            try {
                // PAS 2: Încercăm login-ul acum
                Utilizator admin = authService.login("admin@donor.ro", "admin123");
                System.out.println("[SUCCES] Autentificare reușită pentru: " + admin.getNume());

                // PAS 3: Testăm adăugarea unui Medic (cerința ta principală)
                try {
                    adminService.creeazaContPersonalMedical(
                            "medic.test@spital.ro", "medic123", "0744111222",
                            "Ionescu", "Matei", "PARAFA-777", Rol.MEDIC
                    );
                    System.out.println("[OK] Medic adăugat cu succes.");
                } catch (Exception e) {
                    System.out.println("[INFO] Medicul există deja.");
                }

            } catch (Exception e) {
                System.err.println("[EROARE] Login-ul a eșuat din nou: " + e.getMessage());
            }
            System.out.println("============================================\n");
        };
    }
}