package org.alex.donor.repository;

import org.alex.donor.model.Donator;
import org.alex.donor.model.Programare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgramareRepository extends JpaRepository<Programare, Integer> {
    List<Programare> findAllByDonatorOrderByDataOraProgramareDesc(Donator donator);
}
