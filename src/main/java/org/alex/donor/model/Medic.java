package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medic")
@Data
public class Medic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_utilizator", nullable = false)
    private Utilizator utilizator;

    private String cod_parafa;
}
