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


    @Transactional
    public void creeazaContPersonalMedical(String email, String parola, String nrTelefon,
                                    String nume, String prenume, String codParafa, Rol rolSelectat) {

        if (utilizatorRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Eroare: Email-ul " + email + " este deja înregistrat!");
        }

        Utilizator user = new Utilizator();
        user.setEmail(email);
        user.setParola(passwordEncoder.encode(parola));
        user.setNrTelefon(nrTelefon);
        user.setNume(nume);
        user.setPrenume(prenume);
        user.setRol(rolSelectat);

        Utilizator userSalvat = utilizatorRepo.save(user);

        if (rolSelectat == Rol.MEDIC) {
            Medic medic = new Medic();
            medic.setUtilizator(userSalvat);
            medic.setCodParafa(codParafa);
            medicRepo.save(medic);
        } else if (rolSelectat == Rol.BIOLOG) {
            Biolog biolog = new Biolog();
            biolog.setUtilizator(userSalvat);
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
        Utilizator user = utilizatorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        if (nouEmail != null && !nouEmail.trim().isEmpty() && !nouEmail.equals(user.getEmail())) {
            if (utilizatorRepo.findByEmail(nouEmail).isPresent()) {
                throw new RuntimeException("Eroare: Email-ul " + nouEmail + " este deja utilizat de alt cont!");
            }
            user.setEmail(nouEmail);
        }

        if (nouaParola != null && !nouaParola.trim().isEmpty()) {
            user.setParola(passwordEncoder.encode(nouaParola));
        }

        utilizatorRepo.save(user);
    }
}