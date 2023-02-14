package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.usuario.*;
import com.truemedgroup.reclutamiento.repositories.*;
import com.truemedgroup.reclutamiento.repositories.postulaciones.PostulacionesRapidasRepository;
import com.truemedgroup.reclutamiento.services.interfaces.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Carlos Arturo Guzmán García
 * @version 1.0.0
 * Implementación del AdminService
 **/
@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    UsuariosRepository usuariosRepository;

    @Autowired
    PostulacionesRepository postulacionesRepository;

    @Autowired
    EmpleosRepository empleosRepository;

    @Autowired
    HabilidadesRepository habilidadesRepository;

    @Autowired
    UsuarioPruebasRepository usuarioPruebasRepository;

    @Autowired
    PostulacionesRapidasRepository postulacionesRapidasRepository;

    @Autowired
    private MailServiceImpl mailService;

    @Override
    public HashMap<String, Object> dashboard() {

        HashMap<String, Object> result = new HashMap<String, Object>();

//        result.put("noUsuarios", usuariosRepository.count()-1);

//        result.put("noPostulaciones", postulacionesRepository.count());

        result.put("habilidades", ((List<Habilidad>)habilidadesRepository.getHabilidades())
                .stream()
                .map(Habilidad::getHabilidad)
                .collect(Collectors.toList()));

        List<Object> vacantes = new ArrayList<Object>();
        List<Object> postulaciones = new ArrayList<Object>();

        for (Empleo empleo: empleosRepository.findByActivoTrue()) {
            HashMap<String,Object> vacante = new HashMap<String, Object>();
            vacante.put("vacante", empleo);
            vacante.put("noPostulaciones", postulacionesRepository.countByidEmpleo(empleo.getId()));
            vacantes.add(vacante);

            List<Habilidad> empleoHabilidades = empleo.getHabilidades();

            for (Postulacion postulacion : postulacionesRepository.findByIdEmpleo(empleo.getId())) {
                HashMap<String, Object> aux = new HashMap<String, Object>();

                Usuario usuario = usuariosRepository.findById(postulacion.getIdUsuario()).get();

                aux.put("id", postulacion.getId());
                aux.put("revisado", postulacion.getRevisado());
                aux.put("estatus", postulacion.getEstatus());
                aux.put("idUsuario", postulacion.getIdUsuario());
                aux.put("idEmpleo", postulacion.getIdEmpleo());
                aux.put("fecha", postulacion.getFecha());
                aux.put("nombre", usuario.getNombre());
                aux.put("apellidoPaterno", usuario.getApellidoPaterno());
                aux.put("apellidoMaterno", usuario.getApellidoMaterno());
                aux.put("fechaNacimiento", usuario.getFechaNacimiento());
                aux.put("telefonos", usuario.getTelefonos()
                        .stream()
                        .map(Telefono::getNumero)
                        .collect(Collectors.toList()));
                aux.put("correos", usuario.getCorreos()
                        .stream()
                        .map(Correo::getCorreo)
                        .collect(Collectors.toList()));
                aux.put("genero", usuario.getSexo());

                aux.put("palbras", usuario.getHabilidades().stream()
                        .filter(hablidad -> !hablidad.getHabilidad().getIdioma())
                        .filter(habilidad -> empleoHabilidades.contains( habilidad.getHabilidad()) )
                        .map(Usuario_Habilidad::getHabilidad)
                        .map(Habilidad::getHabilidad)
                        .collect(Collectors.toList()));


                aux.put("trabajos", usuario.getExperienciasLaborales().stream()
                        .map(ExperienciasLaborales::getPuesto)
                        .collect(Collectors.toList()));
                aux.put("empleo", empleo.getPuesto());
                aux.put("sueldoPretendido", postulacion.getSueldoPretendido());
                Optional<UsuarioPrueba> usuarioPrueba = usuarioPruebasRepository.findByIdUsuario(postulacion.getIdUsuario());
                if (usuarioPrueba.isPresent()) {
                    aux.put("terman", usuarioPrueba.get().getCalificacion());
                    aux.put("idResultados", usuarioPrueba.get().getId());
                } else {
                    aux.put("terman", null);
                    aux.put("idResultados", null);
                }
                postulaciones.add(aux);
            }
        }

        result.put("vacantes", vacantes);
        result.put("postulaciones", postulaciones);

        return result;
    }

    @Override
    public HashMap<String, Object> dashboardInactivo(Integer idVacante) {

        HashMap<String, Object> result = new HashMap<String, Object>();

        result.put("habilidades", ((List<Habilidad>)habilidadesRepository.getHabilidades())
                .stream()
                .map(Habilidad::getHabilidad)
                .collect(Collectors.toList()));

        List<Object> vacantes = new ArrayList<Object>();
        List<Object> postulaciones = new ArrayList<Object>();

        Optional<Empleo> empleoOpt = empleosRepository.findById(idVacante);
        if (empleoOpt.get() != null){
            Empleo empleo = empleoOpt.get();

            HashMap<String,Object> vacante = new HashMap<String, Object>();
            vacante.put("vacante", empleo);
            vacante.put("noPostulaciones", postulacionesRepository.countByidEmpleo(empleo.getId()));
            vacantes.add(vacante);
            List<Habilidad> empleoHabilidades = empleo.getHabilidades();
            for (Postulacion postulacion : postulacionesRepository.findByIdEmpleo(empleo.getId())) {
                HashMap<String, Object> aux = new HashMap<String, Object>();

                Usuario usuario = usuariosRepository.findById(postulacion.getIdUsuario()).get();

                aux.put("id", postulacion.getId());
                aux.put("revisado", postulacion.getRevisado());
                aux.put("estatus", postulacion.getEstatus());
                aux.put("idUsuario", postulacion.getIdUsuario());
                aux.put("idEmpleo", postulacion.getIdEmpleo());
                aux.put("fecha", postulacion.getFecha());
                aux.put("nombre", usuario.getNombre());
                aux.put("apellidoPaterno", usuario.getApellidoPaterno());
                aux.put("apellidoMaterno", usuario.getApellidoMaterno());
                aux.put("fechaNacimiento", usuario.getFechaNacimiento());
                aux.put("telefonos", usuario.getTelefonos()
                        .stream()
                        .map(Telefono::getNumero)
                        .collect(Collectors.toList()));
                aux.put("correos", usuario.getCorreos()
                        .stream()
                        .map(Correo::getCorreo)
                        .collect(Collectors.toList()));
                aux.put("genero", usuario.getSexo());

                aux.put("palbras", usuario.getHabilidades().stream()
                        .filter(hablidad -> !hablidad.getHabilidad().getIdioma())
                        .filter(habilidad -> empleoHabilidades.contains( habilidad.getHabilidad()) )
                        .map(Usuario_Habilidad::getHabilidad)
                        .map(Habilidad::getHabilidad)
                        .collect(Collectors.toList()));


                aux.put("trabajos", usuario.getExperienciasLaborales().stream()
                        .map(ExperienciasLaborales::getPuesto)
                        .collect(Collectors.toList()));
                aux.put("empleo", empleo.getPuesto());
                aux.put("sueldoPretendido", postulacion.getSueldoPretendido());
                Optional<UsuarioPrueba> usuarioPrueba = usuarioPruebasRepository.findByIdUsuario(postulacion.getIdUsuario());
                if (usuarioPrueba.isPresent()) {
                    aux.put("terman", usuarioPrueba.get().getCalificacion());
                    aux.put("idResultados", usuarioPrueba.get().getId());
                } else {
                    aux.put("terman", null);
                    aux.put("idResultados", null);
                }
                postulaciones.add(aux);
            }
        }
        result.put("vacantes", vacantes);
        result.put("postulaciones", postulaciones);
        return result;
    }

    @Override
    public Boolean asignarPrueba(UsuarioPrueba usuarioPrueba){

        logger.info("asignarPrueba(" + usuarioPrueba +")");

        try{

            if ( !usuarioPruebasRepository.existsByidUsuarioAndIdPrueba(usuarioPrueba.getIdUsuario(), usuarioPrueba.getIdPrueba()) ) {
                usuarioPrueba.setId(null);
                usuarioPrueba.setCalificacion(null);
                usuarioPrueba.setSecciones(null);
                usuarioPruebasRepository.save(usuarioPrueba);
                return true;
            } else
                return false;

        }catch (Exception e){

            logger.error(e.getMessage());

            return false;

        }

    }

    public List<Empleo> empleosActivos() {
//        Test Empleo Prueba Local
        /*
        List<Empleo> list = new ArrayList<>();
        Optional<Empleo> data = empleosRepository.findById(28040);
        if (data.isPresent()) {
            Empleo emp = data.get();
            emp.setPruebas(new ArrayList<>());
            emp.setActivo(true);
            if (emp.getTest() != null) {
                PostRapTest test = emp.getTest();
                Collections.shuffle(test.getPreguntas(), new Random());
                test.setPreguntas(test.getPreguntas().subList(0, 4));
            }
            list.add(emp);
        }
        return list;
        */


        List<Empleo> data = empleosRepository.findByActivoTrue();
        for (Empleo empleo: data) {
            empleo.setPruebas(new ArrayList<>());
            if (empleo.getTest() != null) {
                PostRapTest test = empleo.getTest();
                Collections.shuffle(test.getPreguntas(), new Random());
                test.setPreguntas(test.getPreguntas().subList(0, 4));
            }
        }
        return data;
    }

    public void postPostulacionRapida(String nombreCompleto, String archivo, String type, Integer idEmpleo, String telefono, String bodyPreguntas, String inlineImages) {
        Optional<Empleo> optional = empleosRepository.findById(idEmpleo);

        if ( !optional.isPresent() )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleo no encontrado");

        Empleo empleo = optional.get();

        if (!empleo.getActivo())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleo inactivo");

        Integer consecutivo = postulacionesRapidasRepository.getTopPostulacionRapida() + 1;
        Integer cantidadPostulaciones = postulacionesRapidasRepository.getTotalPostulaciones(telefono, empleo.getId());


        String desempeno = "";
        if (bodyPreguntas.contains(">DESTACADO<")) {
            desempeno = "DESTACADO";
        }
        if (bodyPreguntas.contains(">BUENO<")) {
            desempeno = "BUENO";
        }
        if (bodyPreguntas.contains(">REGULAR<")) {
            desempeno = "REGULAR";
        }
        if (bodyPreguntas.contains(">MALO<")) {
            desempeno = "MALO";
        }
        String correo = empleo.getCorreoNotificacion();
        String subject = "NUEVA POSTULACION PARA " + empleo.getPuesto() + " - " + desempeno;
        String body = "<html><body>" + "<p># Postulacion: \"" + consecutivo.toString() + "\"</p><p>Vacante: \"" + empleo.getPuesto() +
                "\"</p><p>Aspirante: \"" + nombreCompleto + "\"</p><p>Número telefónico: \"" + telefono + "\".</p>" +
                (cantidadPostulaciones >= 2 ? "<p>Este número se ha postulado " + cantidadPostulaciones + " veces a esta vacante.</p>" : "")
                + "<br />" + bodyPreguntas + "</html></body>";
        postulacionesRapidasRepository.insertPostulacionRapida(nombreCompleto, idEmpleo, archivo, type, telefono, body, desempeno);

        String nombreArchivo = "cv_" + nombreCompleto.replaceAll(" ", "_") + ".pdf";

        if (correo.length() != 0) {
            mailService.sendMailWithAttachment(correo, subject,  body, archivo, nombreArchivo, inlineImages);
        }
    }

    @Override
    public List<PostRapTest> getTestsRapidos() {
        return (List<PostRapTest>) postulacionesRapidasRepository.findAll();
    }

    @Override
    public List<String> getCorreosNotificacion() {
        return postulacionesRapidasRepository.getCorreosNotificacion();
    }
}
