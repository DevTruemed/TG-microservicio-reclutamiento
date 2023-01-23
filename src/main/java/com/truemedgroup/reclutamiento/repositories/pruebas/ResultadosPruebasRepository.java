package com.truemedgroup.reclutamiento.repositories.pruebas;

import com.truemedgroup.reclutamiento.models.pruebas.PruebaAsignada;
import com.truemedgroup.reclutamiento.models.pruebas.ResultadoPrueba;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadosPruebasRepository extends CrudRepository<ResultadoPrueba, Integer> {
    List<PruebaAsignada> findByidUsuario(Integer idUsuario);
}
