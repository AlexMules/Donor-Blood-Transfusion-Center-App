package org.alex.donor;

import org.alex.donor.model.Adresa;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import org.alex.donor.model.enums.Sex;
import org.alex.donor.service.DonatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class DonorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonorApplication.class, args);
    }

    @Bean
    CommandLineRunner testRegister(DonatorService donatorService) {
        return args -> {
            System.out.println(">>> INCEPERE TEST INREGISTRARE DONATOR <<<");

            try {
                // 1. Pregătim datele pentru Utilizator
                Utilizator u = new Utilizator();
                u.setEmail("test.donator@gmail.com");
                u.setParola("parola123"); // Va fi criptată de service
                u.setNume("Popescu");
                u.setPrenume("Andrei");
                u.setNr_telefon("0722123456");

                // 2. Pregătim datele pentru Adresa
                Adresa a = new Adresa();
                a.setJudet("Cluj");
                a.setLocalitate("Cluj-Napoca");
                a.setStrada("Strada Eroilor");
                a.setNumar(15);
                a.setCod_postal("400123");

                // 3. Pregătim datele pentru Donator
                Donator d = new Donator();
                d.setCnp("1900101123456");
                d.setSex(Sex.M);
                d.setGreutate(75.0f);
                d.setInaltime(180.0f);
                d.setGrupa_sanguina(GrupaSanguina.A);
                d.setRh(Rh.POZITIV);
                // Setăm o dată de naștere validă (ex: 1 ianuarie 1990)
                d.setData_nasterii(LocalDateTime.of(1990, 1, 1, 0, 0));

                // 4. Apelăm metoda de înregistrare
                donatorService.inregistrareDonator(u, a, d);

                System.out.println(">>> TEST REUSIT! Verifica baza de date.");

            } catch (Exception e) {
                System.out.println(">>> EROARE TEST: " + e.getMessage());
            }
        };
    }

}
