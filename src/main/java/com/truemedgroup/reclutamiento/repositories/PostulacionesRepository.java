package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Postulacion;
import com.truemedgroup.reclutamiento.dto.TelefonoDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostulacionesRepository extends CrudRepository<Postulacion, Integer> {

    List<Postulacion> findFirst10ByOrderByIdDesc();

    List<Postulacion> findByidUsuario(Integer idUsuario);

    List<Postulacion> findByIdEmpleo(Integer idEmpleo);

    Optional<Postulacion> findByid(Integer id);

    Integer countByidEmpleo(Integer id);

}
