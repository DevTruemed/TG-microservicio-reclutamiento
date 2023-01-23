package com.truemedgroup.reclutamiento.dto;

import com.truemedgroup.reclutamiento.models.address.Asentamiento;
import lombok.Data;

import java.util.List;

@Data
public class CodigoSimpleDTO {

    private Integer cp;
    private List<String> asentamiento;
    private String tipo_asentamiento;
    private String municipio;
    private String estado;
    private String ciudad;

}
