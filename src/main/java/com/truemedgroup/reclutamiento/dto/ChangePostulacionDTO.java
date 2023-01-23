package com.truemedgroup.reclutamiento.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePostulacionDTO {

    private Integer idUsuario;
    private Integer idEmpleo;
    private Short estatus;


}
