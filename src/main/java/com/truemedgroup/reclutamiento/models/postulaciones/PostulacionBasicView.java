package com.truemedgroup.reclutamiento.models.postulaciones;

import java.util.Date;

public interface PostulacionBasicView {

    Short getEstatus();

    Date getFecha();

    Integer getSueldoPretendido();

    EmpleoSummary getEmpleo();

    interface EmpleoSummary{
        String getPuesto();

        String getEstado();

        String getMunicipio();

        String getId();
    }
}
