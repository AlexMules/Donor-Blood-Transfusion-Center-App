package org.alex.donor.repository;

import org.alex.donor.model.Biolog;
import org.alex.donor.model.Utilizator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiologRepository extends JpaRepository<Biolog, Integer> {
    Optional<Biolog> findByUtilizator(Utilizator u);
}
