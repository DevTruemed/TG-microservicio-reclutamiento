package com.truemedgroup.reclutamiento.models.address;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Ciudades")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class Ciudad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Ciudad",
            nullable = false,
            updatable = false)
    private Integer id;

    @Column(name = "Ciudad",
            nullable = false,
            updatable = false,
            length = 100)
    private String ciudad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Municipio",
                nullable = false)
    private Municipio municipio;

}
