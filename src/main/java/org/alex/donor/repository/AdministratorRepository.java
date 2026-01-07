package org.alex.donor.repository;

import org.alex.donor.model.Administrator;
import org.alex.donor.model.Utilizator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findByUtilizator(Utilizator u);
}
