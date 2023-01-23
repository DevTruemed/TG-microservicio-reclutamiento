package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.reclutamiento.models.address.Estado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadosRepository extends CrudRepository<Estado, Integer> {

    Optional<Estado> findByEstado(String estado);

}
