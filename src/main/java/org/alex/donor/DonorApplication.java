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
    CommandLineRunner testVizualizareProgramariMedic(
            AutentificareService authService,
            MedicService medicService,
            ProgramareService programareService,
            DonatorService donatorService) {

        return args -> {
            System.out.println("\n========== TEST VIZUALIZARE PROGRAMĂRI (MEDIC) ==========");

            try {
                // 1. NE ASIGURĂM CĂ AVEM O PROGRAMARE PENTRU AZI (pentru test)
                Utilizator uDonator = authService.login("test.donator@gmail.com", "parola123");
                Donator donator = donatorService.getDonatorByUtilizator(uDonator);

                Programare pAzi = new Programare();
                pAzi.setDonator(donator);
                // Programăm pentru ora actuală (care este în interiorul zilei de azi)
                pAzi.setDataOraProgramare(LocalDateTime.now());
                pAzi.setStatus(StatusProgramare.CONFIRMATA);
                programareService.creeazaProgramare(pAzi);

                System.out.println("[INFO] S-a creat o programare de test pentru astăzi.");
                authService.logout();

                // 2. LOGARE MEDIC
                Utilizator medic = authService.login("medic.test@spital.ro", "medic123");
                System.out.println("[OK] Medic logat: " + medic.getNume());

                // 3. VIZUALIZARE PROGRAMĂRI PENTRU DATA DE AZI
                LocalDate dataCautata = LocalDate.now();
                List<Programare> rezultate = medicService.getProgramariPentruZi(dataCautata);

                System.out.println(">> Rezultate pentru data: " + dataCautata);
                if (rezultate.isEmpty()) {
                    System.out.println("[-] Nu s-au găsit programări confirmate pentru această zi.");
                } else {
                    System.out.println("[+] S-au găsit " + rezultate.size() + " programări:");
                    for (Programare p : rezultate) {
                        System.out.println("    - Ora: " + p.getDataOraProgramare().toLocalTime() +
                                " | Donator: " + p.getDonator().getUtilizator().getNume() +
                                " " + p.getDonator().getUtilizator().getPrenume() +
                                " | Telefon: " + p.getDonator().getUtilizator().getNrTelefon());
                    }
                }

                authService.logout();

            } catch (Exception e) {
                System.err.println("[EROARE TEST]: " + e.getMessage());
            }

            System.out.println("========== SFÂRȘIT TEST VIZUALIZARE ==========\n");
        };
    }
}
