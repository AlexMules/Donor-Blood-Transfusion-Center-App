package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.StatusProgramare;
import org.alex.donor.repository.ProgramareRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicService {

    private final ProgramareRepository programareRepo;

    public List<Programare> getProgramariPentruZi(LocalDate data) {
        LocalDateTime startZi = data.atStartOfDay();
        LocalDateTime sfarsitZi = data.atTime(LocalTime.MAX);

        return programareRepo.findAllByDataOraProgramareBetween(startZi, sfarsitZi)
                .stream()
                .filter(p -> p.getStatus() == StatusProgramare.CONFIRMATA)
                .collect(Collectors.toList());
    }
}