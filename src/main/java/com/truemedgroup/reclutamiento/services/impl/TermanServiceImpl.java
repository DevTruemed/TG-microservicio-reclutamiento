package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.usuario.*;
import com.truemedgroup.reclutamiento.repositories.UsuarioPruebasRepository;
import com.truemedgroup.reclutamiento.repositories.pruebas.TestsRepository;
import com.truemedgroup.reclutamiento.services.interfaces.TermanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TermanServiceImpl implements TermanService {

    private static final Logger logger = LoggerFactory.getLogger(TermanServiceImpl.class);

    @Autowired
    private UsuarioPruebasRepository usuarioPruebasRepository;

    @Autowired
    private TestsRepository testRepository;

    public UsuarioPrueba CalficarTest(UsuarioPrueba test){
        List<SeccionResultado> secciones = test.getSecciones();
        Integer calificacion = 0;
        for (SeccionResultado seccion :
                secciones) {
            int preguntas = 0, aciertos = 0, errores = 0;

            if (seccion.getNumeroSeccion() == 2 || seccion.getNumeroSeccion() == 5 || seccion.getNumeroSeccion() == 10 )
                seccion.setCalificacion(seccion.getCalificacion() * 2);
            else if (seccion.getNumeroSeccion() == 3 || seccion.getNumeroSeccion() == 6 || seccion.getNumeroSeccion() == 8){
                if (seccion.getNumeroSeccion() == 3)
                    preguntas = 30;
                else if (seccion.getNumeroSeccion() == 6)
                    preguntas = 20;
                else
                    preguntas = 17;

                errores = preguntas - seccion.getPreguntas().size();
                for (PreguntaResultado pregunta :
                        seccion.getPreguntas()) {
                    if (pregunta.getCorrecta())
                        aciertos ++;
                    else
                        errores ++;
                }
                seccion.setCalificacion(aciertos - errores);
            }

            calificacion += seccion.getCalificacion();
        }
        test.setCalificacion(calificacion);

        return test;

    }

    public HashMap<String, Object> getResultado (Integer idUsuario){

        HashMap<String, Object> resultado = new HashMap<>();

        resultado.put("resultado", testRepository.getTermanResult(idUsuario));
        return resultado;
    }

}
