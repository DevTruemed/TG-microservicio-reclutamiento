package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.usuario.SeccionResultado;
import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;
import com.truemedgroup.reclutamiento.repositories.SeccionesResultadosRepository;
import com.truemedgroup.reclutamiento.repositories.UsuarioPruebasRepository;
import com.truemedgroup.reclutamiento.repositories.pruebas.TestsRepository;
import com.truemedgroup.reclutamiento.services.interfaces.Inglesb2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class Inglesb2ServiceImpl implements Inglesb2Service {

    private static final Logger logger = LoggerFactory.getLogger(Inglesb2Service.class);

    @Autowired
    private UsuarioPruebasRepository usuarioPruebasRepository;

    @Autowired
    private SeccionesResultadosRepository seccionesResultadosRepository;


    public HashMap<String, Object> getResultado (Integer idUsuario){

        HashMap<String, Object> resultado = new HashMap<>();
        Integer idPrueba = 1002;

        Optional<UsuarioPrueba> usuarioPruebaOpt = usuarioPruebasRepository.findByidUsuarioAndidPrueba(idUsuario , idPrueba);
        UsuarioPrueba usuarioPrueba;
        if (usuarioPruebaOpt.isPresent()){
            usuarioPrueba  = usuarioPruebaOpt.get();
            if (usuarioPrueba != null){
                resultado.put("calificacionFinal" , usuarioPrueba.getCalificacion());
                // Busca la info dentro de las secciones
                List<SeccionResultado> seccionesResultado = usuarioPrueba.getSecciones();
                if  (seccionesResultado.size() >   0 ) {
                    resultado.put("realizado" , true);
                    for (SeccionResultado seccion:
                            seccionesResultado
                    ) {
                        resultado.put("seccion" + seccion.getNumeroSeccion() , seccion.getCalificacion());
                    }
                }else{
                    resultado.put("realizado" , false);
                }
            }
        }else{
            resultado.put("realizado" , false);
        }
        return resultado;
    }



}
