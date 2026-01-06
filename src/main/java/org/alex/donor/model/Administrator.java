package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "administrator")
@Data
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_utilizator", nullable = false)
    private Utilizator utilizator;
}
