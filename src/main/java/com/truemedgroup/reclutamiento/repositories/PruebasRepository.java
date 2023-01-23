package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Prueba;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PruebasRepository extends CrudRepository<Prueba, Integer> {

    Optional<Prueba> findByNombre(String nombre);

}
