package com.truemedgroup.reclutamiento.models.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Estados")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Estado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Estado",
            nullable = false,
            updatable = false)
    private Short id;

    @Column(name = "Estado",
            nullable = false,
            unique = true,
            length = 20)
    private String estado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estado")
    List<Municipio> municipios;

}
