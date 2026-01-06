package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alex.donor.model.enums.Rol;

@Entity
@Table(name = "utilizator")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilizator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String parola;

    private String nr_telefon;
    private String nume;
    private String prenume;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
