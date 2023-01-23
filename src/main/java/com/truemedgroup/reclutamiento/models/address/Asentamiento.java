package com.truemedgroup.reclutamiento.models.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Asentamientos")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Asentamiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Asentamiento",
            nullable = false,
            unique = true,
            updatable = false)
    private Integer id;

    @Column(name = "Asentamiento",
            nullable = false,
            length = 200)
    private String asentamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Tipo",
            nullable = false)
    private TipoAsentamiento tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Ciudad",
            nullable = false)
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Municipio",
            nullable = false)
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CP",
            nullable = false)
    private CodigoPostal cp;

}
