package com.truemedgroup.reclutamiento.services.interfaces;

import com.truemedgroup.commonsRecruit.usuario.Empleo;
import com.truemedgroup.commonsRecruit.usuario.PostRapTest;
import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * @author Carlos Arturo Guzmán García
 * @version 1.0.0
 * Servicio que contiene funciones especiales para los
 * administradores del sistema
 **/
public interface AdminService {

    /**
     * Metodo para obtener la informacón del dasboard de adminstración
     *
     * @return Hashmap con la información solicitada
     *
     **/
    public HashMap<String, Object> dashboard();

    public HashMap<String, Object> dashboardInactivo(Integer idVacante);

    public Boolean asignarPrueba(UsuarioPrueba usuarioPrueba);

    public List<Empleo> empleosActivos();

    public void postPostulacionRapida(String nombreCompleto, String archivo, String type, Integer idEmpleo, String telefono, String bodyPreguntas, String inlineImages);

    public List<PostRapTest> getTestsRapidos();

    public List<String> getCorreosNotificacion();
}
