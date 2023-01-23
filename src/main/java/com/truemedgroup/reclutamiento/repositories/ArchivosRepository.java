package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Archivo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivosRepository extends CrudRepository<Archivo,Long> {

    @Query(value = "SELECT COUNT(*) FROM Archivos WHERE Id_Resultado = ?1",nativeQuery = true)
    Integer countImagenes(Integer result);

    @Query(value = "SELECT * FROM Archivos WHERE Id_Resultado = ?1", nativeQuery = true)
    List<Archivo> findAllByidResulado(Integer id);

}
