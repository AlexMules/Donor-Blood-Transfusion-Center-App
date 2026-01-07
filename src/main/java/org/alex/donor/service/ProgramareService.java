package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.StatusProgramare;
import org.alex.donor.repository.ProgramareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProgramareService {

    private final ProgramareRepository programareRepo;

    @Transactional
    public Programare creeazaProgramare(Programare p) {
        p.setStatus(StatusProgramare.CONFIRMATA);
        return programareRepo.save(p);
    }

    @Transactional
    public void anuleazaProgramare(Integer idProgramare) {
        Programare p = programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));

        if (p.getDataOraProgramare().isBefore(LocalDateTime.now().plusDays(1))) {
            throw new RuntimeException("O programare poate fi anulată cu cel puțin 24 de ore înainte!");
        }

        p.setStatus(StatusProgramare.ANULATA);
        programareRepo.save(p);
    }
}