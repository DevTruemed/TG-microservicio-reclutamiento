package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.ExperienciasLaborales;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienciasLaboralesRepository extends CrudRepository<ExperienciasLaborales,Integer> {

    List<ExperienciasLaborales> findByIdUsuario(Integer id);

}
