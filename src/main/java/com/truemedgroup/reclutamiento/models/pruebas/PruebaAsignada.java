package com.truemedgroup.reclutamiento.models.pruebas;

import org.springframework.beans.factory.annotation.Value;

public interface PruebaAsignada {

    Integer getId();

    @Value("#{target.calificacion != null}")
    Boolean getRealizada();

    NombrePrueba getPrueba();

    interface NombrePrueba{

        Integer getId();

        String getnombre();

        String getUrl();

    }

}
