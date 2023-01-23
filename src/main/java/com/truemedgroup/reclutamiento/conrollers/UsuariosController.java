package com.truemedgroup.reclutamiento.conrollers;

import com.truemedgroup.commonsRecruit.postulaciones.ClaveCompuestaPostulacion;
import com.truemedgroup.commonsRecruit.usuario.Prueba;
import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;
import com.truemedgroup.reclutamiento.dto.CodigoSimpleDTO;
import com.truemedgroup.reclutamiento.models.address.Asentamiento;
import com.truemedgroup.reclutamiento.models.address.CodigoPostal;
import com.truemedgroup.reclutamiento.models.address.Estado;
import com.truemedgroup.reclutamiento.models.address.Municipio;
import com.truemedgroup.reclutamiento.models.postulaciones.PostulacionBasicView;
import com.truemedgroup.reclutamiento.models.pruebas.PruebaAsignada;
import com.truemedgroup.reclutamiento.repositories.CPRepository;
import com.truemedgroup.reclutamiento.repositories.EstadosRepository;
import com.truemedgroup.reclutamiento.repositories.postulaciones.PostulacionRepository;
import com.truemedgroup.reclutamiento.repositories.pruebas.ResultadosPruebasRepository;
import com.truemedgroup.reclutamiento.services.impl.CleaverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UsuariosController {

    @Autowired
    CleaverService cleaverService;

    @Autowired
    PostulacionRepository postulacionRepository;

    @Autowired
    ResultadosPruebasRepository resultadosPruebasRepository;

    @GetMapping("pruebas/cleaver")
    public Prueba getCleaver(){
        return cleaverService.getTest();
    }

    @PostMapping("pruebas/cleaver")
    public Boolean postCleaver(@RequestBody UsuarioPrueba resultado) { return cleaverService.CalificarPrueba(resultado); }

    @GetMapping("postulaciones/usuario")
    public List<PostulacionBasicView> getPostulaciones(@RequestParam("idUsuario") Integer idUsuario){
        return postulacionRepository.findByidUsuario(idUsuario);
    }

    @DeleteMapping("postulaciones")
    @ResponseStatus(HttpStatus.OK)
    public void deletePostulaciones(@RequestParam("idUsuario") Integer idUsuario,
                                    @RequestParam("idEmpleo") Integer idEmpleo){

        if ( postulacionRepository.existsById( new ClaveCompuestaPostulacion(idUsuario, idEmpleo)) )
            postulacionRepository.deleteById( new ClaveCompuestaPostulacion(idUsuario, idEmpleo));

        else
            throw new ResourceNotFoundException("La postulación no existe");

    }

    @GetMapping("pruebas/asignadas")
    public List<PruebaAsignada> getPruebaUsuario(@RequestParam("idUsuario") Integer idUsuario){
        return resultadosPruebasRepository.findByidUsuario(idUsuario);
    }

    @Autowired
    CPRepository cpRepository;

    @Autowired
    EstadosRepository estadosRepository;

    @GetMapping("info_cp/{cp}")
    public CodigoSimpleDTO getInfoCP(@PathVariable("cp") Integer cp){

        Optional<CodigoPostal> codigoPostal = cpRepository.findById(cp);

        if ( !codigoPostal.isPresent() )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CP No encontrado");

        CodigoPostal codigo = codigoPostal.get();

        CodigoSimpleDTO respuesta = new CodigoSimpleDTO();

        respuesta.setCp(codigo.getCp());

        respuesta.setAsentamiento(codigo.getAsentamientos().stream().map(Asentamiento::getAsentamiento).collect(Collectors.toList()));

        Asentamiento asentamiento = codigo.getAsentamientos().get(0);

        if ( asentamiento.getCiudad() != null )
            respuesta.setCiudad(asentamiento.getCiudad().getCiudad());

        respuesta.setEstado(asentamiento.getMunicipio().getEstado().getEstado());

        respuesta.setMunicipio(asentamiento.getMunicipio().getMunicipio());

        return respuesta;
    }

    @GetMapping("get_estados")
    public List<String> getEstados(){
        return ((List<Estado>) estadosRepository.findAll()).stream().map(Estado::getEstado).collect(Collectors.toList());
    }

    @GetMapping("get_municipio_por_estado/{estado}")
    public List<String> getMunicipioEstados(@PathVariable("estado") String estado){

        Optional<Estado> estadoOptional = estadosRepository.findByEstado(estado);

        if ( !estadoOptional.isPresent() )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado");

        return estadoOptional.get().getMunicipios().stream().map(Municipio::getMunicipio).collect(Collectors.toList());
    }

    @GetMapping("search_cp/{cp}")
    public List<String> getCP(@PathVariable("cp") Integer cp){
        return cpRepository.getCP(cp).stream().map(codigoPostal -> codigoPostal.getCp().toString()).collect(Collectors.toList());
    }

}
