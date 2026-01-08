package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.model.enums.StatusProgramare;
import org.alex.donor.repository.ProgramareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramareService {

    private final ProgramareRepository programareRepo;
    private final DonatorService donatorService;

    @Transactional
    public void creeazaProgramare(Donator d, LocalDateTime dataOra) {
        // 1. Verificare Eligibilitate
        if (donatorService.getStatusDonator(d) != StatusDonator.ELIGIBIL) {
            throw new RuntimeException("Doar donatorii eligibili se pot programa!");
        }

        // 2. Verificare dacă are deja o programare activă
        if (getProgramareActiva(d).isPresent()) {
            throw new RuntimeException("Ai deja o programare activă confirmată!");
        }

        // 3. Verificare disponibilitate slot (pentru siguranță)
        if (esteOraOcupata(dataOra)) {
            throw new RuntimeException("Acest interval orar tocmai a fost ocupat!");
        }

        Programare p = new Programare();
        p.setDonator(d);
        p.setDataOraProgramare(dataOra);
        p.setStatus(StatusProgramare.CONFIRMATA);
        programareRepo.save(p);
    }

    @Transactional
    public void anuleazaProgramare(Integer idProgramare) {
        Programare p = programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));

        // Regula ta: se poate anula doar cu 24h înainte
        if (p.getDataOraProgramare().isBefore(LocalDateTime.now().plusDays(1))) {
            throw new RuntimeException("O programare poate fi anulată cu cel puțin 24 de ore înainte!");
        }

        p.setStatus(StatusProgramare.ANULATA);
        programareRepo.save(p);
    }

    public Optional<Programare> getProgramareActiva(Donator d) {
        // Căutăm o programare CONFIRMATA care are data în viitor (începând de azi)
        return programareRepo.findFirstByDonatorAndStatusAndDataOraProgramareAfter(
                d, StatusProgramare.CONFIRMATA, LocalDateTime.now());
    }

    public boolean esteOraOcupata(LocalDateTime dataOra) {
        return programareRepo.existsByDataOraProgramareAndStatus(dataOra, StatusProgramare.CONFIRMATA);
    }
}