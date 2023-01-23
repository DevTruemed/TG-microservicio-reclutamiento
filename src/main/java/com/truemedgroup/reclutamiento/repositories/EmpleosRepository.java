package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Empleo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleosRepository extends CrudRepository<Empleo, Integer> {

    List<Empleo> findByActivoTrue();

    List<Empleo> findByActivoFalse();

    @Query(value = "EXEC cambiarEstadoVacante ?1, ?2", nativeQuery = true)
    Integer cambiarEstatus(Short estatus, Integer idEmpleo);

}
