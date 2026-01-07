package org.alex.donor.repository;

import org.alex.donor.model.Alerta;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {
    List<Alerta> findAllByGrupaSanguinaAndRhOrderByDataOraDesc(GrupaSanguina grupa, Rh rh);
}