package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final UtilizatorRepository utilizatorRepo;
    private final MedicRepository medicRepo;
    private final BiologRepository biologRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Metoda apelată la apăsarea butonului "Adaugă" din interfața de Admin
     */
    @Transactional
    public void creeazaContPersonalMedical(String email, String parola, String nrTelefon,
                                    String nume, String prenume, String codParafa, Rol rolSelectat) {

        // 1. Validare email unic (Cerința ta: "daca exista deja email-ul, nu se poate adauga")
        if (utilizatorRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Eroare: Email-ul " + email + " este deja înregistrat!");
        }

        // 2. Creăm obiectul Utilizator (pentru tabela utilizatori)
        Utilizator user = new Utilizator();
        user.setEmail(email);
        user.setParola(passwordEncoder.encode(parola)); // Criptăm parola
        user.setNrTelefon(nrTelefon);
        user.setNume(nume);
        user.setPrenume(prenume);
        user.setRol(rolSelectat);

        // SALVARE ÎN TABELA UTILIZATORI
        // Spring returnează obiectul salvat care are acum și ID-ul generat automat de DB
        Utilizator userSalvat = utilizatorRepo.save(user);

        // 3. Creăm profilul specific (pentru tabela medic sau biolog)
        if (rolSelectat == Rol.MEDIC) {
            Medic medic = new Medic();
            medic.setUtilizator(userSalvat); // Aici se face automat legătura id_utilizator -> id
            medic.setCodParafa(codParafa);
            medicRepo.save(medic);
        } else if (rolSelectat == Rol.BIOLOG) {
            Biolog biolog = new Biolog();
            biolog.setUtilizator(userSalvat); // Legătura automată pentru id_utilizator
            biolog.setCodParafa(codParafa);
            biologRepo.save(biolog);
        }
    }
}