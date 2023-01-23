package com.truemedgroup.reclutamiento.models.address;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Municipios")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class Municipio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Municipio",
            nullable = false,
            updatable = false)
    private Integer id;

    @Column(name = "Municipio",
            nullable = false,
            updatable = false,
            length = 100)
    private String municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Estado",
                nullable = false)
    private Estado estado;

}
