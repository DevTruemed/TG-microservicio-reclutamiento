package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.reclutamiento.repositories.EmpleosRepository;
import com.truemedgroup.reclutamiento.repositories.postulaciones.PostulacionRepository;
import com.truemedgroup.reclutamiento.services.interfaces.VacantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class VacantesServiceImpl implements VacantesService {

    @Autowired
    EmpleosRepository empleosRepository;

    @Autowired
    PostulacionRepository postulacionRepository;

    @Override
    @Transactional
    public Integer cambiarEstado(Integer idVacante, Short estatus) {
        postulacionRepository.findByidEmpleo(idVacante).forEach(postulacion -> {
            postulacion.setActivo(estatus == 1);
            postulacionRepository.save(postulacion);
        });

        return empleosRepository.cambiarEstatus(estatus, idVacante);
    }

}
