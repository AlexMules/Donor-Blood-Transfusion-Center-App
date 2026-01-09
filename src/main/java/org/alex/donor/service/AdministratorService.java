package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.Rol;
import org.alex.donor.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Utilizator> getPersonalMedical() {
        return utilizatorRepo.findAllByRolIn(List.of(Rol.MEDIC, Rol.BIOLOG));
    }

    @Transactional
    public void stergeUtilizator(Integer id) {
        if (!utilizatorRepo.existsById(id)) {
            throw new RuntimeException("Utilizatorul nu există!");
        }
        utilizatorRepo.deleteById(id);
    }

    @Transactional
    public void actualizeazaDatePersonal(Integer id, String nouEmail, String nouaParola) {
        // 1. Căutăm utilizatorul în baza de date
        Utilizator user = utilizatorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        // 2. Actualizăm Email-ul dacă a fost completat și este diferit de cel actual
        if (nouEmail != null && !nouEmail.trim().isEmpty() && !nouEmail.equals(user.getEmail())) {
            // Verificăm dacă noul email este deja ocupat de altcineva
            if (utilizatorRepo.findByEmail(nouEmail).isPresent()) {
                throw new RuntimeException("Eroare: Email-ul " + nouEmail + " este deja utilizat de alt cont!");
            }
            user.setEmail(nouEmail);
        }

        // 3. Actualizăm Parola dacă a fost completată (o criptăm înainte de salvare)
        if (nouaParola != null && !nouaParola.trim().isEmpty()) {
            user.setParola(passwordEncoder.encode(nouaParola));
        }

        // 4. Salvăm modificările
        utilizatorRepo.save(user);
    }
}