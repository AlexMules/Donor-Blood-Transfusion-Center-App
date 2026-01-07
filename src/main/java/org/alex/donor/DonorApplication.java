package org.alex.donor;

import lombok.extern.slf4j.Slf4j;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.repository.AnalizaSangeRepository;
import org.alex.donor.repository.DonareRepository;
import org.alex.donor.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@Slf4j
public class DonorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonorApplication.class, args);
    }

    @Bean
    CommandLineRunner runFullTest(
            DonatorService donatorService,
            AutentificareService authService,
            ProgramareService programareService,
            DonareRepository donareRepo,
            AnalizaSangeRepository analizaRepo) {

        return args -> {
            System.out.println("\n========== START SCENARIU TEST COMPLET ==========");

            try {
                // PAS 1: ÎNREGISTRARE
                Utilizator u = new Utilizator(null, "test.donator@gmail.com", "parola123", "0722123456", "Popescu", "Andrei", Rol.DONATOR);
                Adresa adr = new Adresa(null, "Cluj", "Cluj-Napoca", "Eroilor", 15, "400123");
                Donator don = new Donator();
                don.setCnp("1900101123456");
                don.setDataNasterii(LocalDateTime.of(1990, 1, 1, 0, 0));
                don.setSex(Sex.M);
                don.setGreutate(75.0f);
                don.setStatus(StatusDonator.ELIGIBIL); // Status inițial

                try {
                    donatorService.inregistrareDonator(u, adr, don);
                    System.out.println("[OK] Înregistrare reușită.");
                } catch (Exception e) {
                    System.out.println("[INFO] Utilizatorul există deja, continuăm.");
                }

                // PAS 2: LOGIN
                Utilizator logat = authService.login("test.donator@gmail.com", "parola123");
                System.out.println("[OK] Login reușit pentru: " + logat.getNume());
                Donator donatorDinDb = donatorService.getDonatorByUtilizator(logat);

                // PAS 3: TEST PROGRAMARE & ANULARE (REGULA 24H)
                Programare p1 = new Programare();
                p1.setDonator(donatorDinDb);
                p1.setDataOraProgramare(LocalDateTime.now().plusDays(2));
                p1 = programareService.creeazaProgramare(p1);
                programareService.anuleazaProgramare(p1.getId());
                System.out.println("[OK] Test anulare programare (>24h) reușit.");

                Programare p2 = new Programare();
                p2.setDonator(donatorDinDb);
                p2.setDataOraProgramare(LocalDateTime.now().plusHours(5));
                p2 = programareService.creeazaProgramare(p2);
                try {
                    programareService.anuleazaProgramare(p2.getId());
                } catch (RuntimeException e) {
                    System.out.println("[OK] Test blocare anulare (<24h) reușit: " + e.getMessage());
                }

                // PAS 4: TEST STATUS DONATOR
                System.out.println("\n>> VERIFICARE STATUS:");
                System.out.println("Status Donator: " + donatorService.getStatusDonator(donatorDinDb));
                System.out.println("Mesaj explicativ: " + donatorService.getMesajStatus(donatorDinDb));

                // PAS 5: TEST ISTORIC DONĂRI (Simulăm o donare și o analiză)
                System.out.println("\n>> SIMULARE ISTORIC:");
                // 5.1. Salvăm o donare
                Donare donareEfectuata = new Donare();
                donareEfectuata.setDonator(donatorDinDb);
                donareEfectuata.setDataDonare(LocalDateTime.now().minusDays(3));
                donareEfectuata = donareRepo.save(donareEfectuata);

                // 5.2. Salvăm analiza pentru acea donare
                AnalizaSange analiza = new AnalizaSange();
                analiza.setDonare(donareEfectuata);
                analiza.setDataIntroducereRezultat(LocalDateTime.now().minusDays(1));
                analiza.setRezultat(RezultatAnaliza.ADMIS);
                analiza.setGrupaSanguina(GrupaSanguina.A);
                analiza.setRh(Rh.POZITIV);
                analiza.setCantitateMl(450);
                analiza.setMesaj("Sânge de calitate excelentă. Mulțumim!");
                analizaRepo.save(analiza);

                // 5.3. Citim istoricul prin DonatorService
                List<AnalizaSange> istoric = donatorService.getIstoricAnalize(donatorDinDb);
                System.out.println("[OK] Istoric recuperat. Intrări găsite: " + istoric.size());
                for (AnalizaSange a : istoric) {
                    System.out.println("Donare la data: " + a.getDonare().getDataDonare() +
                            " | Rezultat: " + a.getRezultat() +
                            " | Mesaj: " + a.getMesaj());
                }

            } catch (Exception e) {
                System.err.println("[EROARE CRITICĂ TEST]: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("========== SFÂRȘIT SCENARIU TEST ==========\n");
        };
    }
}