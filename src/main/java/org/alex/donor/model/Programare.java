package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.alex.donor.model.enums.StatusProgramare;

import java.time.LocalDateTime;

@Entity
@Table(name = "programare")
@Data
public class Programare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_donator", nullable = false)
    private Donator donator;

    private LocalDateTime dataOraProgramare;

    @Enumerated(EnumType.STRING)
    private StatusProgramare status = StatusProgramare.CONFIRMATA;
}
