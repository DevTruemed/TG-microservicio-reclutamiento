package com.truemedgroup.reclutamiento.models.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Codigospostales")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CodigoPostal implements Serializable {

    @Id
    @Column(name = "CP",
            nullable = false,
            unique = true,
            updatable = false)
    private Integer cp;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cp")
    private List<Asentamiento> asentamientos;

    public void addAsentamiento(Asentamiento asentamiento){

        if (asentamientos == null)
            asentamientos = new ArrayList<>();

        getAsentamientos().add(asentamiento);

    }

}
