package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "adresa")
@Data
public class Adresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String judet;
    private String localitate;
    private String strada;
    private Integer numar;
    private String cod_postal;
}
