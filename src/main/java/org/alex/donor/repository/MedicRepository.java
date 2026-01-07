package org.alex.donor.repository;

import org.alex.donor.model.Medic;
import org.alex.donor.model.Utilizator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicRepository extends JpaRepository<Medic, Integer> {
    Optional<Medic> findByUtilizator(Utilizator u);
}
