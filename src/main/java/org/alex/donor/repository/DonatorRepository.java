package org.alex.donor.repository;

import org.alex.donor.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonatorRepository extends JpaRepository<Donator, Integer> {
    boolean existsByCnp(String cnp);
}
