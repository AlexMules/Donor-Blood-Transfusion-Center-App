package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.AnalizaSange;
import org.alex.donor.model.enums.RezultatAnaliza;
import org.alex.donor.repository.AnalizaSangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BiologService {

    private final AnalizaSangeRepository analizaRepo;

    /**
     * CERINȚA: Vizualizare sânge donat care așteaptă rezultatul.
     * Returnează analizele cu status IN_ASTEPTARE.
     */
    public List<AnalizaSange> getAnalizeInAsteptare() {
        return analizaRepo.findAllByRezultat(RezultatAnaliza.IN_ASTEPTARE);
    }
}