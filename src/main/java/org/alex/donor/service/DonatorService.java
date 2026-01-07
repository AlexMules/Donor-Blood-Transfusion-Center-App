package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class DonatorService {

    private final UtilizatorRepository utilizatorRepo;
    private final AdresaRepository adresaRepo;
    private final DonatorRepository donatorRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void inregistrareDonator(Utilizator u, Adresa a, Donator d) {
        // validari unicitate donator
        if (utilizatorRepo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Există deja un cont cu această adresă de e-mail!");
        }

        if (donatorRepo.existsByCnp(d.getCnp())) {
            throw new RuntimeException("Acest CNP este deja înregistrat!");
        }

        // procesare utilizator
        u.setParola(passwordEncoder.encode(u.getParola())); // criptare parola
        u.setRol(Rol.DONATOR);
        Utilizator utilizatorSalvat = utilizatorRepo.save(u);

        // procesare adresa
        Adresa adresaSalvata = adresaRepo.save(a);

        // procesare donator
        d.setUtilizator(utilizatorSalvat);
        d.setAdresa(adresaSalvata);
        d.setStatus(StatusDonator.ELIGIBIL);
        d.setVarsta(Period.between(d.getData_nasterii().toLocalDate(), LocalDate.now()).getYears());

        // salvare finala
        donatorRepo.save(d);
    }

    public Donator getDonatorByUtilizator(Utilizator u) {
        return donatorRepo.findByUtilizator(u)
                .orElseThrow(() -> new RuntimeException("Profilul de donator nu a fost găsit!"));
    }
}
