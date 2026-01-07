package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.alex.donor.model.enums.GrupaSanguina;
import org.alex.donor.model.enums.Rh;
import java.time.LocalDateTime;

@Entity
@Data
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private GrupaSanguina grupaSanguina;

    @Enumerated(EnumType.STRING)
    private Rh rh;

    private String titluMesaj;

    @Column(columnDefinition = "TEXT")
    private String continutMesaj;

    private LocalDateTime dataOra;
}