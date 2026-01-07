package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.AnalizaSange;
import org.alex.donor.model.Donare;
import org.alex.donor.model.Donator;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.RezultatAnaliza;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.model.enums.StatusProgramare;
import org.alex.donor.repository.AnalizaSangeRepository;
import org.alex.donor.repository.DonareRepository;
import org.alex.donor.repository.DonatorRepository;
import org.alex.donor.repository.ProgramareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicService {

    private final ProgramareRepository programareRepo;
    private final DonareRepository donareRepo;
    private final AnalizaSangeRepository analizaRepo;
    private final DonatorRepository donatorRepo;

    public List<Programare> getProgramariPentruZi(LocalDate data) {
        LocalDateTime startZi = data.atStartOfDay();
        LocalDateTime sfarsitZi = data.atTime(LocalTime.MAX);

        return programareRepo.findAllByDataOraProgramareBetween(startZi, sfarsitZi)
                .stream()
                .filter(p -> p.getStatus() == StatusProgramare.CONFIRMATA)
                .collect(Collectors.toList());
    }

    public Programare getDetaliiProgramare(Integer idProgramare) {
        return programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));
    }

    /**
     * FLUX VALIDARE (Donatorul este apt)
     */
    @Transactional
    public void valideazaDonare(Integer idProgramare) {
        Programare p = programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));

        // 1. Programarea devine FINALIZATA
        p.setStatus(StatusProgramare.FINALIZATA);
        programareRepo.save(p);

        // 2. Creăm o intrare în tabela 'donare' (NOW)
        Donare donare = new Donare();
        donare.setDonator(p.getDonator());
        donare.setDataDonare(LocalDateTime.now());
        Donare donareSalvata = donareRepo.save(donare);

        // 3. Creăm o intrare în 'analiza_sange' (IN_ASTEPTARE)
        AnalizaSange analiza = new AnalizaSange();
        analiza.setDonare(donareSalvata);
        analiza.setRezultat(RezultatAnaliza.IN_ASTEPTARE);
        analizaRepo.save(analiza);

        // 4. Donatorul devine INELIGIBIL_TEMPORAR
        Donator d = p.getDonator();
        d.setStatus(StatusDonator.INELIGIBIL_TEMPORAR);
        donatorRepo.save(d);
    }

    /**
     * FLUX INVALIDARE (Donatorul este respins de medic)
     */
    @Transactional
    public void respingeDonare(Integer idProgramare) {
        Programare p = programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));

        // 1. Programarea devine RESPINSA
        p.setStatus(StatusProgramare.RESPINSA);
        programareRepo.save(p);
    }
}