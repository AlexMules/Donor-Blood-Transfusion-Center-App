package org.alex.donor.repository;

import org.alex.donor.model.Utilizator;
import org.alex.donor.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilizatorRepository extends JpaRepository<Utilizator, Integer> {
    Optional<Utilizator> findByEmail(String email);

    List<Utilizator> findAllByRolIn(List<Rol> roluri);
}
