package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.postulaciones.ClaveCompuestaPostulacion;
import com.truemedgroup.commonsRecruit.postulaciones.Observacion;
import com.truemedgroup.commonsRecruit.postulaciones.Postulaciones;
import com.truemedgroup.commonsRecruit.postulaciones.PostulacionesView;
import com.truemedgroup.commonsRecruit.usuario.Usuario;
import com.truemedgroup.reclutamiento.dto.TelefonoDTO;
import com.truemedgroup.reclutamiento.repositories.postulaciones.ObservacionesRepository;
import com.truemedgroup.reclutamiento.repositories.postulaciones.PostulacionRepository;
import com.truemedgroup.reclutamiento.services.interfaces.PostulacionesService;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class PostulacionesServiceImpl implements PostulacionesService {

    private static final Logger logger = LoggerFactory.getLogger(PostulacionesServiceImpl.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-mm-dd HH:mm:ss");

    @Autowired
    PostulacionRepository postulacionRepository;

    @Autowired
    ObservacionesRepository observacionesRepository;

    @Value("${TWILIO_ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${TWILIO_MESSAGING_SID}")
    private String MESSAGING_SID;

    public Integer changeEstatus(Integer idUsuario, Integer idEmpleo, Short estatus){

        if (idEmpleo == null || idUsuario == null || estatus == null || !postulacionRepository.existsById(new ClaveCompuestaPostulacion(idUsuario, idEmpleo)))
            return 0;
        else
            return postulacionRepository.changeEstatusPostulacion(idUsuario, idEmpleo, estatus);

    }

    public PostulacionesView getPostulacion(Integer idUsuario, Integer idEmpleo) throws ResourceNotFoundException {

        logger.info("getPostulacion - idUsuario: " + idUsuario + " idEmpleo: " + idEmpleo);

        PostulacionesView postulacion = postulacionRepository.findByidUsuarioAndIdEmpleo(idUsuario,idEmpleo);

        if (postulacion.getEstatus() == 0)
            setRevisado(idUsuario, idEmpleo);

        return postulacion;

    }

    public Object putPostulacionReasignar(Usuario usuario, Integer oldEmpleo, Integer newEmpleo, String observacion, Usuario postulante) {
        postulacionRepository.reasignarPostulacion(newEmpleo, postulante.getId(), oldEmpleo);
        if (observacion.length() > 0) {
            Observacion observacionModel = new Observacion();
            observacionModel.setIdUsuarioCreador(usuario.getId());
            observacionModel.setIdUsuario(postulante.getId());
            observacionModel.setIdEmpleo(newEmpleo);
            observacionModel.setObservacion(observacion);
            observacionModel.setFecha(new Date());
            observacionesRepository.save(observacionModel);
        }
        return true;
    }

    public Observacion postObservacion (Integer idUsuario, Integer idEmpleo, Observacion observacion) throws ResponseStatusException{

        if (idUsuario == null || idEmpleo == null || observacion == null || observacion.getIdUsuarioCreador() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ningún parámetro puede ser null");

        if (observacion.getObservacion() == null || observacion.getObservacion().equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La observación no puede esar vacía");

        observacion.setIdUsuario(idUsuario);
        observacion.setIdEmpleo(idEmpleo);
        observacion.setId(null);
        observacion.setFecha(new Date());
        observacion = observacionesRepository.save(observacion);

        return observacion;

    }

    public Observacion putObservacion (Observacion observacion, Integer idObservacion) throws ResponseStatusException, ResourceNotFoundException{

        if (idObservacion == null || observacion == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ningún parámetro puede ser null");

        Optional<Observacion> original = observacionesRepository.findById(idObservacion);

        if (!original.isPresent())
            throw new ResourceNotFoundException("Observacion no encontrada");

        if ( observacion.getIdUsuarioCreador() != original.get().getIdUsuarioCreador() )
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes los privilegios para modficar esta observación");

        observacion.setId(idObservacion);
        observacion.setIdEmpleo(original.get().getIdEmpleo());
        observacion.setIdUsuario(original.get().getIdUsuario());
        observacion.setIdUsuarioCreador(original.get().getIdUsuarioCreador());
        observacion.setFecha(original.get().getFecha());

        return observacionesRepository.save(observacion);

    }

    public Boolean deleteObservacion (Integer idObservacion, Integer idUsuario){

        if (idObservacion == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ningún parámetro puede ser null");

        if (!observacionesRepository.existsById(idObservacion))
            return false;

        if ( !(observacionesRepository.findById(idObservacion).get().getIdUsuarioCreador() == idUsuario) )
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes los privilegios para eliminar esta observación");

        observacionesRepository.deleteById(idObservacion);

        return true;

    }

    @Transactional
    private void setRevisado(Integer idUsuario, Integer idEmpleo){
        Postulaciones postulacion = postulacionRepository.findById(new ClaveCompuestaPostulacion(idUsuario,idEmpleo)).get();
        postulacion.setRevisado(true);
        postulacion.setEstatus((short)1);
        postulacionRepository.save(postulacion);
    }

    //    @Scheduled(cron = "*/20 * * * * ?")
    @Scheduled(cron = "0 0 12,18 ? * *")
    private void reminderPushNotification() throws IOException, URISyntaxException, InterruptedException {
        logger.info("Cron Task '':: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        List<TelefonoDTO> telefonos = postulacionRepository.getTelefonosPruebasFaltantes();
//        TelefonoDTO pruebaGenaro = new TelefonoDTO() {
//            @Override
//            public Integer getId() {
//                return 92560;
//            }
//
//            @Override
//            public String getNumero() {
//                return "4426066284";
//            }
//
//            @Override
//            public Integer getId_Usuario() {
//                return 91654;
//            }
//
//            @Override
//            public String getNombre() {
//                return "Genaro Esaú";
//            }
//        };
//        List<TelefonoDTO> telefonos = new ArrayList<>();
//        telefonos.add(pruebaGenaro);

        for (TelefonoDTO number: telefonos) {
            try {
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                Message message = Message.creator(
                                new com.twilio.type.PhoneNumber("+52" + number.getNumero()),
                                MESSAGING_SID,
                                "Hola " + number.getNombre() + ", Somos Truemed Group y queremos agradecer tu interés y postulación  en nuestra vacante. \n" +
                                        "Hemos detectado que aún no realizas las pruebas las cuales son indispensables para dar continuidad a tu proceso . \n" +
                                        "Te enviamos nuevamente el link https://reclutamiento.truemedgroup.com")
                        .create();

                System.out.println(message.getSid());
                if (message.getStatus().toString().equals("accepted")) {
                    postulacionRepository.actualizarNumeroRecordatoriosPruebas(number.getId_Usuario());
                }
            } catch (ApiException ex) {
                System.err.println(ex);
            }
        }
    }

}
