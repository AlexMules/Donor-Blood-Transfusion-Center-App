package org.alex.donor.repository;

import org.alex.donor.model.AnalizaSange;
import org.alex.donor.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalizaSangeRepository extends JpaRepository<AnalizaSange, Integer> {

    @Query("SELECT a FROM AnalizaSange a WHERE a.donare.donator = :donator ORDER BY a.donare.dataDonare DESC")
    List<AnalizaSange> findAllByDonator(@Param("donator") Donator donator);
}