package org.alex.donor.repository;

import org.alex.donor.model.Adresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresaRepository extends JpaRepository<Adresa, Integer> {
}
