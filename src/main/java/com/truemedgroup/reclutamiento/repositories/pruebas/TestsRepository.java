package com.truemedgroup.reclutamiento.repositories.pruebas;

import com.truemedgroup.commonsRecruit.pruebas.TermanModel;
import com.truemedgroup.commonsRecruit.pruebas.TermanResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface TestsRepository extends CrudRepository<TermanModel, Integer> {

    @Query("SELECT u FROM TermanModel u WHERE u.idUsuario = ?1 and u.idPrueba = 1")
    TermanResult getTermanResult(Integer idUsuario);

}
