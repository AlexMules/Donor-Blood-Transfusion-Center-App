package org.alex.donor.repository;

import org.alex.donor.model.StocSange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocSangeRepository extends JpaRepository<StocSange, Integer> {
}