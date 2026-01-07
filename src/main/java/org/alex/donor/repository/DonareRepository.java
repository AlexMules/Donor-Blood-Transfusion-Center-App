package org.alex.donor.repository;

import org.alex.donor.model.Donare;
import org.alex.donor.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonareRepository extends JpaRepository<Donare, Integer> {

    /**
     * Găsește toate donările unui anumit donator.
     * Folosim 'OrderByDataDonareDesc' pentru ca cele mai recente donări să apară primele în listă.
     */
    List<Donare> findAllByDonatorOrderByDataDonareDesc(Donator donator);
}