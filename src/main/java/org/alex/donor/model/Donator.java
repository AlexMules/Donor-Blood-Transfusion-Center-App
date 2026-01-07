package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import org.alex.donor.model.enums.Sex;
import org.alex.donor.model.enums.StatusDonator;

import java.time.LocalDateTime;

@Entity
@Table(name = "donator")
@Data
public class Donator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_utilizator", nullable = false)
    private Utilizator utilizator;

    @ManyToOne
    @JoinColumn(name = "id_adresa")
    private Adresa adresa;

    @Column(unique = true, nullable = false)
    private String cnp;

    private LocalDateTime dataNasterii;
    private Integer varsta;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Float greutate;
    private Float inaltime;

    @Enumerated(EnumType.STRING)
    private GrupaSanguina grupaSanguina;

    @Enumerated(EnumType.STRING)
    private Rh rh;

    @Enumerated(EnumType.STRING)
    private StatusDonator status = StatusDonator.ELIGIBIL;
}
