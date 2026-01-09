package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.repository.*;
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
    private final StocSangeRepository stocRepo;
    private final AlertaRepository alertaRepo;

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


    @Transactional
    public void valideazaDonare(Integer idProgramare) {
        Programare p = programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));

        p.setStatus(StatusProgramare.FINALIZATA);
        programareRepo.save(p);

        Donator donatorManaged = donatorRepo.findById(p.getDonator().getId())
                .orElseThrow(() -> new RuntimeException("Donatorul nu a fost găsit în baza de date!"));

        Donare donare = new Donare();
        donare.setDonator(donatorManaged);
        LocalDateTime dataSimulata = p.getDataOraProgramare().plusMinutes(15);
        donare.setDataDonare(dataSimulata);

        Donare donareSalvata = donareRepo.save(donare);

        AnalizaSange analiza = new AnalizaSange();
        analiza.setDonare(donareSalvata);
        analiza.setRezultat(RezultatAnaliza.IN_ASTEPTARE);
        analizaRepo.save(analiza);

        donatorManaged.setStatus(StatusDonator.INELIGIBIL_TEMPORAR);
        donatorRepo.save(donatorManaged);
    }


    @Transactional
    public void respingeDonare(Integer idProgramare) {
        Programare p = programareRepo.findById(idProgramare)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită!"));

        p.setStatus(StatusProgramare.RESPINSA);
        programareRepo.save(p);
    }

    public List<StocSange> getStocSangeComplet() {
        return stocRepo.findAll();
    }

    @Transactional
    public void trimiteAlertaUrgenta(GrupaSanguina grupa, Rh rh, String titlu, String continut) {
        Alerta alerta = new Alerta();
        alerta.setGrupaSanguina(grupa);
        alerta.setRh(rh);
        alerta.setTitluMesaj(titlu);
        alerta.setContinutMesaj(continut);
        alerta.setDataOra(LocalDateTime.now());

        alertaRepo.save(alerta);
    }
}