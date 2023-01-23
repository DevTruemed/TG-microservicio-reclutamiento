package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.SeccionResultado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeccionesResultadosRepository extends CrudRepository<SeccionResultado,Integer> {
}
