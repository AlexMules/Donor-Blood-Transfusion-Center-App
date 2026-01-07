package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;

@Entity
@Table(name = "stoc_sange")
@Data
public class StocSange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private GrupaSanguina grupaSanguina;

    @Enumerated(EnumType.STRING)
    private Rh rh;

    private Integer cantitateMl = 0;
}
