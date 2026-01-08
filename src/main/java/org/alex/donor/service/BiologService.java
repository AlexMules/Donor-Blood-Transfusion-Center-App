package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.AnalizaSange;
import org.alex.donor.model.Donator;
import org.alex.donor.model.StocSange;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.RezultatAnaliza;
import org.alex.donor.model.enums.Rh;
import org.alex.donor.model.enums.StatusDonator;
import org.alex.donor.repository.AnalizaSangeRepository;
import org.alex.donor.repository.DonatorRepository;
import org.alex.donor.repository.StocSangeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BiologService {

    private final AnalizaSangeRepository analizaRepo;
    private final StocSangeRepository stocRepo;
    private final DonatorRepository donatorRepo;

    /**
     * CERINȚA: Vizualizare sânge donat care așteaptă rezultatul.
     * Returnează analizele cu status IN_ASTEPTARE.
     */
    public List<AnalizaSange> getAnalizeInAsteptare() {
        return analizaRepo.findAllByRezultat(RezultatAnaliza.IN_ASTEPTARE)
                .stream()
                // Sortăm crescător după data donării din obiectul Donare
                .sorted(Comparator.comparing(a -> a.getDonare().getDataDonare()))
                .collect(Collectors.toList());
    }

    public List<AnalizaSange> getAnalizeFinalizate() {
        return analizaRepo.findAllByRezultatIn(List.of(RezultatAnaliza.ADMIS, RezultatAnaliza.RESPINS));
    }

    @Transactional
    public void introducereRezultatAnaliza(Integer idAnaliza, Integer cantitate,
                                           GrupaSanguina grupa, Rh rh,
                                           RezultatAnaliza rezultat, String mesaj) {

        // 1. Găsim analiza în așteptare
        AnalizaSange analiza = analizaRepo.findById(idAnaliza)
                .orElseThrow(() -> new RuntimeException("Analiza nu a fost găsită!"));

        // 2. Actualizăm datele analizei
        analiza.setCantitateMl(cantitate);
        analiza.setGrupaSanguina(grupa);
        analiza.setRh(rh);
        analiza.setRezultat(rezultat);
        analiza.setMesaj(mesaj);
        analiza.setDataIntroducereRezultat(LocalDateTime.now());
        analizaRepo.save(analiza);

        // 3. Obținem donatorul
        Donator donator = analiza.getDonare().getDonator();

        // 4. Logica de Verdict (Stoc + Status Donator)
        if (rezultat == RezultatAnaliza.ADMIS) {
            // --- CAZ: SÂNGE BUN ---
            // Actualizăm stocul
            StocSange stoc = stocRepo.findByGrupaSanguinaAndRh(grupa, rh)
                    .orElseThrow(() -> new RuntimeException("Combinația de stoc nu există!"));

            stoc.setCantitateMl(stoc.getCantitateMl() + cantitate);
            stocRepo.save(stoc);

            // Donatorul devine ELIGIBIL din nou
            donator.setStatus(StatusDonator.ELIGIBIL);
        } else {
            // --- CAZ: SÂNGE RĂU / CONTAMINAT ---
            // Nu modificăm stocul

            // Donatorul devine INELIGIBIL_PERMANENT (nu mai poate face programări niciodată)
            donator.setStatus(StatusDonator.INELIGIBIL_PERMANENT);
        }

        // Salvăm modificarea statusului donatorului
        donatorRepo.save(donator);
    }

    @Transactional
    public void scadeCantitateStoc(Integer idStoc, Integer cantitateDeScazut) {
        StocSange stoc = stocRepo.findById(idStoc)
                .orElseThrow(() -> new RuntimeException("Eroare: Grupa de sânge nu a fost găsită în stoc!"));

        int nouaCantitate = stoc.getCantitateMl() - cantitateDeScazut;

        // Validarea logică: nu putem avea stoc negativ
        if (nouaCantitate < 0) {
            throw new RuntimeException("Cantitatea trimisă nu poate fi mai mare decât cea existentă!");
        }

        stoc.setCantitateMl(nouaCantitate);
        stocRepo.save(stoc);
    }


}