package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Estudio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudiosRepository extends CrudRepository<Estudio, Integer> {

    List<Estudio> findByIdUsuario(Integer id);

}
