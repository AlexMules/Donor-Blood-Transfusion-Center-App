package org.alex.donor.repository;

import org.alex.donor.model.Donator;
import org.alex.donor.model.Programare;
import org.alex.donor.model.enums.StatusProgramare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE Programare p SET p.status = 'ANULATA' " +
            "WHERE p.status = 'CONFIRMATA' AND p.dataOraProgramare < :limita")
    int anuleazaProgramariNeprezentate(@Param("limita") LocalDateTime limita);
}
