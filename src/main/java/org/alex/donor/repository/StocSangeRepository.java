package org.alex.donor.repository;

import org.alex.donor.model.StocSange;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StocSangeRepository extends JpaRepository<StocSange, Integer> {
    Optional<StocSange> findByGrupaSanguinaAndRh(GrupaSanguina grupa, Rh rh);
}