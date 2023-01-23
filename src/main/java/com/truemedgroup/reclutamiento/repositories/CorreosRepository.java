package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Correo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorreosRepository extends CrudRepository<Correo, Integer> {

    Optional<Correo> findByCorreo(String correo );

    Boolean existsByCorreo(String correo);

}
