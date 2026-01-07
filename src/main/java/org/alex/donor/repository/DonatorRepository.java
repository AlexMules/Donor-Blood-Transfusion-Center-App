package org.alex.donor.repository;

import org.alex.donor.model.Donator;
import org.alex.donor.model.Utilizator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonatorRepository extends JpaRepository<Donator, Integer> {
    Optional<Donator> findByUtilizator(Utilizator u);
    boolean existsByCnp(String cnp);
}
