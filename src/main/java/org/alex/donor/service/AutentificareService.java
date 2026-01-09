package org.alex.donor.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.repository.UtilizatorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutentificareService {

    private final UtilizatorRepository utilizatorRepo;
    private final PasswordEncoder passwordEncoder;
    /**
     * -- GETTER --
     *  Getter pentru a verifica cine este logat în orice moment al aplicației
     */
    @Getter
    private Utilizator utilizatorLogat;

    public Utilizator login(String email, String parola) {
        Optional<Utilizator> optUser = utilizatorRepo.findByEmail(email);

        if (optUser.isPresent()) {
            Utilizator u = optUser.get();
            if (passwordEncoder.matches(parola, u.getParola())) {
                this.utilizatorLogat = u;
                return u;
            }
        }

        throw new RuntimeException("Email sau parolă incorectă!");
    }

    /**
     * Metoda de deautentificare
     */
    public void logout() {
        this.utilizatorLogat = null;
    }

    /**
     * Verifică dacă există cineva logat
     */
    public boolean isLogged() {
        return utilizatorLogat != null;
    }

    public void refreshSesiune(Utilizator utilizatorActualizat) {
        this.utilizatorLogat = utilizatorActualizat;
    }
}
