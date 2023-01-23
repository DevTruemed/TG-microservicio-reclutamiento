package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.usuario.*;
import com.truemedgroup.reclutamiento.clients.OauthFeignClient;
import com.truemedgroup.reclutamiento.repositories.*;
import com.truemedgroup.reclutamiento.services.interfaces.MailService;
import com.truemedgroup.reclutamiento.services.interfaces.UsuariosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Carlos Arturo Guzmám García
 * @version 1.0.0
 * @see UsuariosService
 *
 * Implementacion de la interfaz Usuario Service
 **/
@Service
public class UsuariosServiceImpl implements UsuariosService {

    private static final Logger logger = LoggerFactory.getLogger(UsuariosService.class);

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private CorreosRepository correosRepository;

    @Autowired
    private OauthFeignClient oauthFeignClient;

    @Autowired
    private DireccionesRepository direccionesRepository;

    @Autowired
    private EstudiosRepository estudiosRepository;

    @Autowired
    private HabilidadesRepository habilidadesRepository;

    @Autowired
    private UsuariosHabilidadesRepository usuariosHabilidadesRepository;

    @Autowired
    private ExperienciasLaboralesRepository experienciasLaboralesRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private FamiliaresRepository familiaresRepository;

    @Autowired
    private TelefonosRepository telefonosRepository;

    @Autowired
    private TokensRepsitory tokensRepsitory;

    @Override
    public Page<Usuario> list(Pageable pageable) {

        Page<Usuario> usuarios = usuariosRepository.findAll(pageable);

        usuarios.map(usuario -> {
            usuario.setCorreos(usuario.getCorreos()
                    .stream()
                    .map(correo -> {
                        correo.setUsuario(null);
                        return correo;
                    })
                    .collect(Collectors.toList()));
            return usuario;
        });

        return usuarios;
    }

    @Override
    public Usuario getUsuario(Integer id) throws ResourceNotFoundException {

        Optional<Usuario> usuario = usuariosRepository.findById(id);

        if (!usuario.isPresent()) throw new ResourceNotFoundException("Usuario no encontrado");

        Usuario aux = usuario.get();

        aux.setCorreos(aux.getCorreos()
                          .stream()
                          .map( correo -> {
                              correo.setUsuario(null);
                              return correo;
                              })
                          .collect(Collectors.toList()));

        return aux;

    }

    @Override
    public Usuario getUsuarioByCorreo(String correo) throws ResourceNotFoundException {

        Optional<Correo> correoEntity = correosRepository.findByCorreo(correo);

        if (!correoEntity.isPresent()) throw new ResourceNotFoundException("El correo no existe");

        Usuario usuario = correoEntity.get().getUsuario();

        usuario.setCorreos(usuario.getCorreos().stream().map(element -> {
            element.setUsuario(null);
            return element;
        }).collect(Collectors.toList()));

        return usuario;
    }

    @Override
    @Transactional
    public Usuario postUsuario(Usuario usuario) {

        usuario.setCvCompleto(false);

        Direccion direccion = new Direccion();
        direccion.setCalle("");
        direccion.setNoExterior("");
        direccion.setEstado("");
        direccion.setCodigoPostal(0);
        direccion.setActivo(true);

        usuario.setDireccion(direccionesRepository.save(direccion));

        List<Correo> correos = usuario.getCorreos();

        List<Telefono> telefonos = usuario.getTelefonos();

        usuario.setCorreos(new ArrayList<Correo>());
        usuario.setTelefonos(new ArrayList<Telefono>());
        usuario.setPassword(oauthFeignClient.encodePassword(usuario.getPassword()));

        usuario.setDependeConyuge(false);
        usuario.setDependenHijos(false);
        usuario.setDependenPadres(false);
        usuario.setDependenOtros(false);

        usuario = usuariosRepository.save(usuario);

        for (Correo correo :
                correos) {
            correo.setUsuario(usuario);
            List <Correo> aux = usuario.getCorreos();
            aux.add(correosRepository.save(correo));
            usuario.setCorreos(aux);
        }

        if (telefonos != null) {
        for (Telefono telefono :
                telefonos) {
            telefono.setIdUsuario(usuario.getId());
            List <Telefono> aux = usuario.getTelefonos();
            aux.add(telefonosRepository.save(telefono));
            usuario.setTelefonos(aux);
        }}

        if (usuario.getCorreos() != null)
        usuario.setCorreos(usuario.getCorreos()
                .stream()
                .map( correo -> {
                    correo.setUsuario(null);
                    return correo;
                })
                .collect(Collectors.toList()));

        return usuario;

    }

    @Override
    @Transactional
    public Usuario putUsuario(Usuario usuario, Integer id) throws ResourceNotFoundException{
        Optional<Usuario> optionalUsuario = usuariosRepository.findById(id);

        Usuario usuarioActualizado = null;

        if (optionalUsuario.isPresent())
            usuarioActualizado = optionalUsuario.get();
        else
            throw new ResourceNotFoundException("Usuario no encontrado");

        if (usuario.getNombre() != null) usuarioActualizado.setNombre(usuario.getNombre());
        if (usuario.getPassword() != null) {
            usuarioActualizado.setPassword(oauthFeignClient.encodePassword(usuario.getPassword()));
        }
        if (usuario.getDescripcion() != null) {
            usuarioActualizado.setDescripcion(usuario.getDescripcion());
        }
        if (usuario.getSexo() != null) usuarioActualizado.setSexo(usuario.getSexo());
        if (usuario.getApellidoMaterno() != null) usuarioActualizado.setApellidoMaterno(usuario.getApellidoMaterno());
        if (usuario.getApellidoPaterno() != null) usuarioActualizado.setApellidoPaterno(usuario.getApellidoPaterno());
        if (usuario.getFechaNacimiento() != null) usuarioActualizado.setFechaNacimiento(usuario.getFechaNacimiento());
        if (usuario.getEstadoCivil() != null) usuarioActualizado.setEstadoCivil(usuario.getEstadoCivil());
        if (usuario.getViveCon() != null) usuarioActualizado.setViveCon(usuario.getViveCon());
        if (usuario.getDependenPadres() != null) usuarioActualizado.setDependenPadres(usuario.getDependenPadres());
        if (usuario.getDependenHijos() != null) usuarioActualizado.setDependenHijos(usuario.getDependenHijos());
        if (usuario.getDependeConyuge() != null) usuarioActualizado.setDependeConyuge(usuario.getDependeConyuge());
        if (usuario.getDependenOtros() != null) usuarioActualizado.setDependenOtros(usuario.getDependenOtros());
        if (usuario.getDireccion() != null) {
            direccionesRepository.save(usuario.getDireccion());
        }
        if (usuario.getFacebook() != null) usuarioActualizado.setFacebook(usuario.getFacebook());
        if (usuario.getRazonAplicacion() != null) usuarioActualizado.setRazonAplicacion(usuario.getRazonAplicacion());
        if (usuario.getLinkedin() != null) usuarioActualizado.setLinkedin(usuario.getLinkedin());
        if (usuario.getGoogle() != null) usuarioActualizado.setGoogle(usuario.getGoogle());
        if (usuario.getTwitter() != null) usuarioActualizado.setTwitter(usuario.getTwitter());
        if (usuario.getActivo() != null) usuarioActualizado.setActivo(usuario.getActivo());
        if (usuario.getPassTemporal() != null) usuarioActualizado.setPassTemporal(usuario.getPassTemporal());

        usuarioActualizado.setCvCompleto(checkCvCompleto( usuario ));

        usuarioActualizado = usuariosRepository.save(usuarioActualizado);

        if (usuario.getCorreos().size() > 0){
            Correo correo = usuario.getCorreos().get(0);
            correo.setUsuario(usuarioActualizado);
            correosRepository.save(correo);
        }
        if (usuario.getTelefonos().size() > 0){
            Telefono telefono = usuario.getTelefonos().get(0);
            telefono.setIdUsuario(usuarioActualizado.getId());
            telefonosRepository.save(telefono);
        }

        List<Estudio> estudios = estudiosRepository.findByIdUsuario(usuarioActualizado.getId());
        if (usuario.getEstudios() != null){
            for (Estudio estudio: usuario.getEstudios()) {
                estudio.setIdUsuario(usuarioActualizado.getId());
                estudiosRepository.save(estudio);
                estudios.remove(estudio);
            }
        }

        estudiosRepository.deleteAll(estudios);

        List<Familiares> familiares = familiaresRepository.findByIdUsuario(usuarioActualizado.getId());

        if (usuario.getFamiliares() != null){
            for (Familiares familiar: usuario.getFamiliares()) {
                familiar.setIdUsuario(usuarioActualizado.getId());
                familiaresRepository.save(familiar);
                familiares.remove(familiar);
            }
        }

        familiaresRepository.deleteAll(familiares);

        List<ExperienciasLaborales> experienciasLaborales = experienciasLaboralesRepository.findByIdUsuario(usuarioActualizado.getId());


        if ( usuario.getExperienciasLaborales() != null) {
            for (ExperienciasLaborales el : usuario.getExperienciasLaborales()) {
                el.setIdUsuario(usuarioActualizado.getId());
                experienciasLaboralesRepository.save(el);
                experienciasLaborales.remove(el);
            }
        }

        experienciasLaboralesRepository.deleteAll(experienciasLaborales);

        if (usuarioActualizado.getCorreos() != null) {
            usuarioActualizado.setCorreos(usuarioActualizado
                    .getCorreos()
                    .stream()
                    .map(correo -> {
                        correo.setUsuario(null);
                        return correo;
                    }).collect(Collectors.toList()));
        }

        List<Usuario_Habilidad> habilidades = usuariosHabilidadesRepository.findAllByidUsuario(usuarioActualizado.getId());

        usuariosHabilidadesRepository.deleteAll(usuariosHabilidadesRepository.findAllByidUsuario(usuarioActualizado.getId()));
        List<Usuario_Habilidad> habilidadesList = foundHabilidades(usuarioActualizado);
        for (Usuario_Habilidad hab :
                usuario.getHabilidades()) {
            if (hab.getHabilidad().getIdioma()){
                if (habilidadesRepository.findByHabilidad(hab.getHabilidad().getHabilidad()).isPresent()) {
                    hab.setHabilidad(habilidadesRepository.findByHabilidad(hab.getHabilidad().getHabilidad()).get());
                    hab.setId(new EntityPropertyPK(usuario.getId(),hab.getHabilidad().getId()));
                }else {
                    hab.setHabilidad(habilidadesRepository.save(hab.getHabilidad()));
                    hab.setId(new EntityPropertyPK(usuario.getId(),hab.getHabilidad().getId()));
                }
                habilidadesList.add(hab);
                }
            }
        usuariosHabilidadesRepository.saveAll(habilidadesList);

        return usuarioActualizado;
    }


    private Boolean checkCvCompleto(Usuario usuario){
        boolean result = false;
        if (usuario.getNombre()  != null  && usuario.getFechaNacimiento() != null && usuario.getSexo()        != null   &&
            usuario.getCorreos() != null  && usuario.getTelefonos()       != null && usuario.getEstadoCivil() != null   &&
            usuario.getViveCon() != null  && usuario.getEstadoCivil()     != null && usuario.getDescripcion() != null){

            Direccion direccion = usuario.getDireccion();
            if (direccion.getCalle()     != null && direccion.getNoExterior() != null && direccion.getCodigoPostal() != null &&
                direccion.getMunicipio() != null && direccion.getEstado()     != null && direccion.getColonia() != null){
                if (usuario.getEstudios() != null && usuario.getEstudios().size() > 0 ){

                    result = true;

                }

            }
        }

        return result;
    }

    @Override
    public Boolean requestPasswordChange(String correo) {

        Optional<Correo> correoEntity = correosRepository.findByCorreo(correo);

        if (!correoEntity.isPresent())
            return false;

        Token token = new Token();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(correoEntity.get().getUsuario());
        token.setFechaExpiracion(calcularFechaExpiracionToken(1440));

        if (tokensRepsitory.existsByUsuario(token.getUsuario()))
            token.setId(tokensRepsitory.findByUsuario(token.getUsuario()).getId());

            token = tokensRepsitory.save(token);

        return mailService.sentEmailForgotPassword(correo,token.getToken());
    }

    @Override
    @Transactional
    public String checkToken(String token) {

        if (tokensRepsitory.existsByToken(token)){
            Token tokenEntity = tokensRepsitory.findByToken(token);
            if ((tokenEntity.getFechaExpiracion().getTime() - Calendar.getInstance().getTime().getTime()) >= 0){
                tokenEntity.setToken(UUID.randomUUID().toString());
                tokensRepsitory.save(tokenEntity);
                return tokenEntity.getToken();
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"El token que estas intentando usar ya no es válido");
            }
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Token no valido" );
        }
    }

    private Date calcularFechaExpiracionToken(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public void restorePassword(String token,String password) {
        token = checkToken(token);
        Token tokenEntity = tokensRepsitory.findByToken(token);
        Usuario usuario = usuariosRepository.findById(tokenEntity.getUsuario().getId()).get();
        usuario.setPassword(password = oauthFeignClient.encodePassword(password));
        usuariosRepository.save(usuario);
        tokensRepsitory.delete(tokenEntity);
    }

    private List<Usuario_Habilidad> foundHabilidades(Usuario usuario){
        Iterable<Habilidad> habilidades = habilidadesRepository.findAll();

        List<Usuario_Habilidad> result = new ArrayList<>();

        habilidades.forEach(habilidad -> {
            if ((usuario.getDescripcion() != null && usuario.getDescripcion().toLowerCase().indexOf(habilidad.getHabilidad().toLowerCase()) >= 0) ||
                 usuario.getRazonAplicacion() != null && usuario.getRazonAplicacion().toLowerCase().indexOf(habilidad.getHabilidad().toLowerCase()) >= 0 ||
                 usuario.getExperienciasLaborales().stream().map(experienciasLaborales -> {
                     return (experienciasLaborales.getDescripcion().toLowerCase().indexOf(habilidad.getHabilidad().toLowerCase()) >= 0 ||
                             experienciasLaborales.getDescripcionProblema().toLowerCase().indexOf(habilidad.getHabilidad().toLowerCase()) >= 0 ||
                             experienciasLaborales.getSolucionProblema().toLowerCase().indexOf(habilidad.getHabilidad().toLowerCase()) >= 0);
                 }).collect(Collectors.toList()).contains(true)){
                Usuario_Habilidad aux = new Usuario_Habilidad();
                aux.setId(new EntityPropertyPK(usuario.getId(),habilidad.getId()));
                aux.setPorcentaje(0);
                aux.setHabilidad(habilidad);
                aux.setIdUsuario(usuario.getId());
                result.add(aux);
            }
        });

        return result;

    }
}
