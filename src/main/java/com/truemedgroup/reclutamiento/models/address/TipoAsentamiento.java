package com.truemedgroup.reclutamiento.models.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Tipoasentamientos")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TipoAsentamiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Tipo",
            nullable = false,
            unique = true,
            updatable = false)
    private Short id;

    @Column(name = "Tipo",
            nullable = false,
            length = 50)
    private String tipo;

}
