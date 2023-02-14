package com.truemedgroup.reclutamiento.repositories.postulaciones;

import com.truemedgroup.commonsRecruit.postulaciones.Observacion;
import com.truemedgroup.commonsRecruit.usuario.Empleo;
import com.truemedgroup.commonsRecruit.usuario.PostRapArchivo;
import com.truemedgroup.commonsRecruit.usuario.PostRapTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostulacionesRapidasRepository extends CrudRepository<PostRapTest, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Postulaciones_Rapidas(nombre_completo, idEmpleo, archivo, myme_type, telefono, bodyResponse, desempeno) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    public void insertPostulacionRapida(String nombreCompleto, Integer idEmpleo, String archivo, String type, String telefono, String body, String desempeno);

    @Query(value = "select TOP 1 id FROM Postulaciones_Rapidas ORDER BY id DESC", nativeQuery = true)
    public Integer getTopPostulacionRapida();

    @Query(value = "SELECT COUNT(*) FROM Postulaciones_Rapidas WHERE telefono = ?1 AND idEmpleo = ?2 ", nativeQuery = true)
    public Integer getTotalPostulaciones(String telefono, Integer idEmpleo);

    @Query(value = "SELECT Correo FROM PostRap_Correos_Notificacion", nativeQuery = true)
    public List<String> getCorreosNotificacion();

    @Query(value = "SELECT * FROM PostRap_Archivos WHERE Id_Record = ?1 AND Es_Pregunta = 1", nativeQuery = true)
    public List<PostRapArchivo> getImagenesPregunta(Integer idPregunta);

    @Query(value = "SELECT * FROM PostRap_Archivos WHERE Id_Record = ?1 AND Es_Respuesta = 1", nativeQuery = true)
    public List<PostRapArchivo> getImagenesRespuesta(Integer idRespuesta);

    @Query(value = "SELECT data FROM PostRap_Archivos WHERE Id = ?1", nativeQuery = true)
    public String getArchivoById(Integer idArchivo);
}
