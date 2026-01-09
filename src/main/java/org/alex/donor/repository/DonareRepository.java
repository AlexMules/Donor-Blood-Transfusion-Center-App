package org.alex.donor.repository;

import org.alex.donor.model.Donare;
import org.alex.donor.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonareRepository extends JpaRepository<Donare, Integer> {

    List<Donare> findAllByDonatorOrderByDataDonareDesc(Donator donator);

    @Query("SELECT MIN(d.dataDonare) FROM Donare d " +
            "JOIN AnalizaSange a ON a.donare.id = d.id " +
            "WHERE d.donator.id = :idDonator AND a.rezultat = 'ADMIS'")
    LocalDateTime findDataPrimaDonareAdmisa(@Param("idDonator") Integer idDonator);

    @Query("SELECT MAX(d.dataDonare) FROM Donare d " +
            "JOIN AnalizaSange a ON a.donare.id = d.id " +
            "WHERE d.donator.id = :idDonator AND a.rezultat = 'ADMIS'")
    LocalDateTime findDataUltimaDonareAdmisa(@Param("idDonator") Integer idDonator);
}