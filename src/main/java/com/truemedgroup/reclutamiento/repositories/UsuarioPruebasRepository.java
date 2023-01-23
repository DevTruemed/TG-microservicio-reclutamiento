package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioPruebasRepository extends CrudRepository<UsuarioPrueba,Integer> {

    Boolean existsByidUsuarioAndIdPrueba(Integer idUsuario, Integer idPrueba);

    @Query(nativeQuery = true, value = "SELECT TOP 1 * FROM Usuarios_Pruebas WHERE Id_Usuario = ?1 AND Id_Prueba = ?2")
    Optional<UsuarioPrueba> findByidUsuarioAndidPrueba (Integer idUsuario, Integer idPrueba);

    @Query(nativeQuery = true, value = "SELECT TOP 1 * FROM Usuarios_Pruebas WHERE Id_Usuario = ?1")
    Optional<UsuarioPrueba> findByIdUsuario(Integer idUsuario);

}
