package org.alex.donor.repository;

import org.alex.donor.model.Donator;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.StatusProgramare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramareRepository extends JpaRepository<Programare, Integer> {

    List<Programare> findAllByDonatorOrderByDataOraProgramareDesc(Donator donator);

    List<Programare> findAllByDataOraProgramareBetween(LocalDateTime start, LocalDateTime end);

    Optional<Programare> findFirstByDonatorAndStatusAndDataOraProgramareAfter(
            Donator donator, StatusProgramare status, LocalDateTime dataOra);

    boolean existsByDataOraProgramareAndStatus(LocalDateTime dataOra, StatusProgramare status);
}
