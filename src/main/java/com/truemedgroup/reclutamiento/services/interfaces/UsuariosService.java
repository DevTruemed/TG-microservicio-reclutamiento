package com.truemedgroup.reclutamiento.services.interfaces;

import com.truemedgroup.commonsRecruit.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

/**
 * @author Carlos Arturo Guzmán García
 * @version 1.0.0
 * Servicio que valida y define todos los métodos relacionados
 * con el usuario
 **/
public interface UsuariosService {

    /**
     * Método que lista los usuarios de la
     * base de datos en grupos de X cantidad
     *
     * @param pageable Objeto que contiene instrucciones para saber
     *                 la cantidad de usuarios a retornar y el orden
     *
     * @return Lista de Usuarios
     *
     **/
    public Page<Usuario> list(Pageable pageable);

    /**
     * Metodo que obtiene el usuario por Id
     *
     * @param id Id del usuario solicitado
     *
     * @throws ResourceNotFoundException Expcepcion disparadacuando el usuario solicitado no se encuentra
     *
     * @return Usuario solictado
     *
     **/
    public Usuario getUsuario(Integer id) throws ResourceNotFoundException;

    public Usuario getUsuarioByCorreo( String correo ) throws ResourceNotFoundException;

    /**
     * Método que guarda un usuario en la base de datos
     *
     * @param usuario Id del usuario solicitado
     *
     * @throws ResourceNotFoundException Expcepcion disparadacuando el usuario solicitado no se encuentra
     *
     * @return Usuario creado con el id asignado
     *
     **/
    public Usuario postUsuario(Usuario usuario);

    /**
     * Método que actualiza los datos de un usuario
     *
     * @param id Id del usuario a actualizar
     * @param usuario Usuario a actualizar (solo actualiza los campos que no son null)
     *
     * @throws ResourceNotFoundException Expcepcion disparadacuando el usuario solicitado no se encuentra
     *
     * @return Usuario creado con el id asignado
     *
     **/
    public Usuario putUsuario(Usuario usuario, Integer id) throws ResourceNotFoundException;

    public Boolean requestPasswordChange(String correo);

    public String checkToken(String token);

    public void restorePassword(String token,String password);

}
