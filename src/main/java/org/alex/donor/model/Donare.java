package org.alex.donor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "donare")
@Data
public class Donare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_donator", nullable = false)
    private Donator donator;

    private LocalDateTime dataDonare = LocalDateTime.now();
}
