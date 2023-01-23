package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Familiares;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamiliaresRepository extends CrudRepository<Familiares,Integer> {

    List<Familiares> findByIdUsuario(Integer id);

}
