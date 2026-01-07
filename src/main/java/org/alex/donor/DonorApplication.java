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
    CommandLineRunner testVizualizareBiolog(AutentificareService authService, BiologService biologService) {
        return args -> {
            System.out.println("\n========== TEST VIZUALIZARE ANALIZE (BIOLOG) ==========");

            try {
                // 1. Logare Biolog
                Utilizator biolog = authService.login("biolog.test@laborator.ro", "biolog123");
                System.out.println("[OK] Biolog logat: " + biolog.getNume());

                // 2. Obținerea analizelor în așteptare
                List<AnalizaSange> deLucru = biologService.getAnalizeInAsteptare();

                if (deLucru.isEmpty()) {
                    System.out.println("[-] Nu există analize în așteptare momentan.");
                } else {
                    System.out.println("[+] S-au găsit " + deLucru.size() + " analize de procesat:");
                    System.out.println("------------------------------------------------------------");
                    System.out.println(String.format("%-5s | %-20s | %-15s", "ID", "DATA DONARE", "DONATOR"));
                    System.out.println("------------------------------------------------------------");

                    for (AnalizaSange a : deLucru) {
                        // Accesăm data_donare din tabelul donare prin cheia străină (a.getDonare())
                        System.out.println(String.format("%-5d | %-20s | %-15s",
                                a.getId(),
                                a.getDonare().getDataDonare(),
                                a.getDonare().getDonator().getUtilizator().getNume()));
                    }
                }

                authService.logout();

            } catch (Exception e) {
                System.err.println("Eroare la testul de vizualizare biolog: " + e.getMessage());
            }

            System.out.println("========== SFÂRȘIT TEST VIZUALIZARE BIOLOG ==========\n");
        };
    }
}
