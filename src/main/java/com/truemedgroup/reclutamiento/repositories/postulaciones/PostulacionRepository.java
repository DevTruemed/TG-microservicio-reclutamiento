package com.truemedgroup.reclutamiento.repositories.postulaciones;

import com.truemedgroup.commonsRecruit.postulaciones.ClaveCompuestaPostulacion;
import com.truemedgroup.commonsRecruit.postulaciones.Postulaciones;
import com.truemedgroup.commonsRecruit.postulaciones.PostulacionesView;
import com.truemedgroup.reclutamiento.dto.TelefonoDTO;
import com.truemedgroup.reclutamiento.models.postulaciones.PostulacionBasicView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostulacionRepository extends CrudRepository<Postulaciones, ClaveCompuestaPostulacion> {

    @Query(value = "EXEC changeEstatusPostulacion ?1, ?2, ?3", nativeQuery = true)
    Integer changeEstatusPostulacion(@Param("id_usuario") Integer idUsuario,
                                     @Param("id_empleo")Integer idEmpleo,
                                     @Param("estatus")Short estatus);

    PostulacionesView findByidUsuarioAndIdEmpleo(Integer idUsuario, Integer idEmpleo);

    List<Postulaciones> findByidEmpleo(Integer idEmpleo);

    List<PostulacionBasicView> findByidUsuario(Integer idUsuario);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Postulaciones SET Id_Empleo = ?1, Estatus = 0, Revisado = 0 WHERE Id_Usuario = ?2 AND Id_Empleo = ?3", nativeQuery = true)
    void reasignarPostulacion(Integer newEmpleo, Integer idPostulante, Integer oldEmpleo);

    @Query(value="SELECT Telefonos.*, U.Nombre FROM Telefonos \n" +
            "INNER JOIN Usuarios U ON Telefonos.Id_Usuario = U .Id\n" +
            "WHERE Id_Usuario IN (\n" +
            "\tSELECT DISTINCT U.Id FROM Postulaciones P\n" +
            "\tINNER JOIN Usuarios U ON P.Id_Usuario = U.Id\n" +
            "\tINNER JOIN Usuarios_Pruebas UP ON P.Id_Usuario = UP.Id_Usuario\n" +
            "\tWHERE Fecha >= '2022-11-30' AND (Calificacion IS NULL)\n" +
            "\tAND (U.No_Recordatorio_Pruebas_Env IS NULL OR U.No_Recordatorio_Pruebas_Env = 0)\n" +
            ")", nativeQuery = true)
    List<TelefonoDTO> getTelefonosPruebasFaltantes();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Usuarios SET No_Recordatorio_Pruebas_Env = ISNULL(No_Recordatorio_Pruebas_Env, 0) + 1 WHERE Id = ?1", nativeQuery = true)
    void actualizarNumeroRecordatoriosPruebas(Integer idUsuario);

}
