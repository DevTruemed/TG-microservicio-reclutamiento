package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Habilidad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabilidadesRepository extends CrudRepository<Habilidad, Integer> {

    @Query(nativeQuery = true, value = "SELECT TOP 1 * FROM Habilidades WHERE Habilidad = ?")
    Optional<Habilidad> findByHabilidad(String habilidad);

    @Query(nativeQuery = true, value = "SELECT * FROM Habilidades WHERE Idioma = 0")
    Iterable<Habilidad> getHabilidades();

}
