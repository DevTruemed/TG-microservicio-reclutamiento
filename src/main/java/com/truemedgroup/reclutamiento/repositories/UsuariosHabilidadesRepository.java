package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Habilidad;
import com.truemedgroup.commonsRecruit.usuario.Usuario_Habilidad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuariosHabilidadesRepository extends CrudRepository<Usuario_Habilidad, Integer> {

    List<Usuario_Habilidad> findAllByidUsuario(Integer id);

}
