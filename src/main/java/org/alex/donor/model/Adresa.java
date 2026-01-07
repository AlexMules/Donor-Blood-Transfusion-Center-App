package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String judet;
    private String localitate;
    private String strada;
    private Integer numar;
    private String codPostal;
}
