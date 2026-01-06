package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.RezultatAnaliza;
import org.alex.donor.model.enums.Rh;

import java.time.LocalDateTime;

@Entity
@Table(name = "analiza_sange")
@Data
public class AnalizaSange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_donare", nullable = false)
    private Donare donare;

    private LocalDateTime data_introducere_rezultat;
    private Integer cantitate_ml;

    @Enumerated(EnumType.STRING)
    private GrupaSanguina grupa_sanguina;

    @Enumerated(EnumType.STRING)
    private Rh rh;

    @Enumerated(EnumType.STRING)
    private RezultatAnaliza rezultat = RezultatAnaliza.IN_ASTEPTARE;

    private String mesaj;
}
