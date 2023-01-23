package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Intentos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntentosRepository extends CrudRepository<Intentos,Integer> {
}
