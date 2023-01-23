package com.truemedgroup.reclutamiento.repositories.postulaciones;

import com.truemedgroup.commonsRecruit.postulaciones.Observacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservacionesRepository extends CrudRepository<Observacion, Integer> {


}
