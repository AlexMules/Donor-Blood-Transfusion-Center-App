package org.alex.donor;

import lombok.extern.slf4j.Slf4j;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j // Pentru log-uri mai frumoase
public class DonorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonorApplication.class, args);
    }

    @Bean
    CommandLineRunner runFullTest(
            DonatorService donatorService,
            AutentificareService authService,
            ProgramareService programareService) {

        return args -> {
            System.out.println("\n========== START SCENARIU TEST ==========");

            try {
                // PAS 1: Încercăm înregistrarea
                // Folosim un try-catch intern pentru a ignora eroarea dacă userul există deja
                Utilizator u = new Utilizator(null, "test.donator@gmail.com", "parola123", "0722123456", "Popescu", "Andrei", Rol.DONATOR);
                Adresa adr = new Adresa(null, "Cluj", "Cluj-Napoca", "Eroilor", 15, "400123");
                Donator don = new Donator();
                don.setCnp("1900101123456");
                don.setDataNasterii(LocalDateTime.of(1990, 1, 1, 0, 0));
                don.setSex(Sex.M);
                don.setGreutate(75.0f);

                try {
                    donatorService.inregistrareDonator(u, adr, don);
                    System.out.println("[OK] Înregistrare reușită.");
                } catch (Exception e) {
                    System.out.println("[INFO] Utilizatorul există deja, continuăm cu login.");
                }

                // PAS 2: Login
                Utilizator logat = authService.login("test.donator@gmail.com", "parola123");
                System.out.println("[OK] Login reușit pentru: " + logat.getNume());

                // PAS 3: Programare (Avem nevoie de obiectul Donator din DB)
                // Căutăm donatorul asociat utilizatorului logat
                Donator donatorDinDb = donatorService.getDonatorByUtilizator(logat);

                // Programare peste 2 zile (Anulabilă)
                Programare p1 = new Programare();
                p1.setDonator(donatorDinDb);
                p1.setDataOraProgramare(LocalDateTime.now().plusDays(2));
                p1 = programareService.creeazaProgramare(p1);
                System.out.println("[OK] Programare creată pentru poimâine.");

                // Încercăm anularea ei
                programareService.anuleazaProgramare(p1.getId());
                System.out.println("[OK] Anulare reușită pentru programarea de peste 2 zile.");

                // PAS 4: Test Regula 24h
                Programare p2 = new Programare();
                p2.setDonator(donatorDinDb);
                p2.setDataOraProgramare(LocalDateTime.now().plusHours(5));
                p2 = programareService.creeazaProgramare(p2);

                try {
                    System.out.println(">> Încercăm anularea programării de peste 5 ore...");
                    programareService.anuleazaProgramare(p2.getId());
                } catch (RuntimeException e) {
                    System.out.println("[SUCCES TEST] Regula 24h funcționează: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("[EROARE CRITICĂ TEST]: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("========== SFÂRȘIT SCENARIU TEST ==========\n");
        };
    }
}