package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Utilizator;
import org.alex.donor.repository.UtilizatorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UtilizatorService {

    private final UtilizatorRepository utilizatorRepo;
    private final PasswordEncoder passwordEncoder;

    public Utilizator getDatePersonale(Integer idUtilizator) {
        return utilizatorRepo.findById(idUtilizator)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));
    }

    @Transactional
    public void resetareParolaDonator(String email, String parolaNoua) {
        Utilizator u = utilizatorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nu există cont cu acest email!"));

        if (!u.getRol().name().equals("DONATOR")) {
            throw new RuntimeException("Acces interzis! Doar donatorii își pot reseta parola prin această metodă!");
        }

        u.setParola(passwordEncoder.encode(parolaNoua));
        utilizatorRepo.save(u);
    }
}
