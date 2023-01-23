package com.truemedgroup.reclutamiento.conrollers;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.truemedgroup.commonsRecruit.postulaciones.Observacion;
import com.truemedgroup.commonsRecruit.postulaciones.PostulacionesView;
import com.truemedgroup.commonsRecruit.usuario.Empleo;
import com.truemedgroup.commonsRecruit.usuario.PostRapTest;
import com.truemedgroup.commonsRecruit.usuario.Usuario;
import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;
import com.truemedgroup.reclutamiento.clients.OauthFeignClient;
import com.truemedgroup.reclutamiento.dto.ChangePostulacionDTO;
import com.truemedgroup.reclutamiento.services.impl.CleaverService;
import com.truemedgroup.reclutamiento.services.impl.Inglesb2ServiceImpl;
import com.truemedgroup.reclutamiento.services.impl.PostulacionesServiceImpl;
import com.truemedgroup.reclutamiento.services.impl.TermanServiceImpl;
import com.truemedgroup.reclutamiento.services.interfaces.AdminService;
import com.truemedgroup.reclutamiento.services.interfaces.UsuariosService;
import com.truemedgroup.reclutamiento.services.interfaces.VacantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin")
public class AdminController {
    
    @Autowired
    AdminService adminService;

    @Autowired
    PostulacionesServiceImpl postulacionesService;

    @Autowired
    TermanServiceImpl termanServiceImpl;

    @Autowired
    Inglesb2ServiceImpl inglesb2ServiceImp;

    @Autowired
    VacantesService vacantesService;

    @Autowired
    CleaverService cleaverService;

    @Autowired
    UsuariosService usuariosService;

    @GetMapping("/dashboard")
    public HashMap<String, Object> getDashboard()  {
          return adminService.dashboard();
    }

    @GetMapping("/dashboard/{idVacante}")
    public HashMap<String, Object> getDashboard(@PathVariable("idVacante") Integer idVacante)  {
        return adminService.dashboardInactivo(idVacante);
    }

    @PutMapping("postulacion")
    public Integer getPostulacion(@RequestBody ChangePostulacionDTO postulacionDTO){
        if (postulacionDTO != null)
            return postulacionesService.changeEstatus(postulacionDTO.getIdUsuario(), postulacionDTO.getIdEmpleo(), postulacionDTO.getEstatus());
        else
            return null;
    }

    @Autowired
    OauthFeignClient oauthFeignClient;

    @PostMapping("postulacion/observacion/{idUsuario}/{idEmpleo}")
    @ResponseStatus(HttpStatus.CREATED)
    public Observacion postObservacion(@RequestHeader Map<String, String> headers,
                                       @RequestBody Observacion observacion,
                                       @PathVariable ("idUsuario") Integer idUsuario,
                                       @PathVariable ("idEmpleo") Integer idEmpleo){

        Usuario usuario = usuariosService.getUsuarioByCorreo(oauthFeignClient.getInfoToken(headers).get("user").toString());

        observacion.setIdUsuarioCreador(usuario.getId());

        return postulacionesService.postObservacion(idUsuario, idEmpleo, observacion);

    }

    @PutMapping("postulacion/observacion/{idObservacion}")
    @ResponseStatus(HttpStatus.CREATED)
    public Observacion putObservacion(@RequestHeader Map<String, String> headers,
                                      @RequestBody Observacion observacion,
                                      @PathVariable ("idObservacion") Integer idObservacion){

        Usuario usuario = usuariosService.getUsuarioByCorreo(oauthFeignClient.getInfoToken(headers).get("user").toString());

        observacion.setIdUsuarioCreador(usuario.getId());

        return postulacionesService.putObservacion(observacion, idObservacion);

    }

    @PutMapping("postulacion/reasignar")
    @ResponseStatus(HttpStatus.OK)
    public Object putPostulacion(@RequestHeader Map<String, String> headers,
                                      @RequestParam("oldEmpleo") Integer oldEmpleo,
                                      @RequestParam("newEmpleo") Integer newEmpleo,
                                      @RequestParam("observacion") String observacion,
                                      @RequestParam("postulante") String correo){

        Usuario usuario = usuariosService.getUsuarioByCorreo(oauthFeignClient.getInfoToken(headers).get("user").toString());
        Usuario postulante = usuariosService.getUsuarioByCorreo(correo);

        return postulacionesService.putPostulacionReasignar(usuario, oldEmpleo, newEmpleo, observacion, postulante);
    }

    @DeleteMapping("postulacion/observacion/{idObservacion}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteObservacion (@RequestHeader Map<String, String> headers,
                                      @PathVariable ("idObservacion") Integer idObservacion){

        Usuario usuario = usuariosService.getUsuarioByCorreo(oauthFeignClient.getInfoToken(headers).get("user").toString());

        return postulacionesService.deleteObservacion(idObservacion, usuario.getId());
    }

    @GetMapping("postulacion/{idUsuario}/{idEmpleo}")
    public PostulacionesView getPostulacion(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idEmpleo") Integer idEmpleo){

        if (idEmpleo == null || idUsuario == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los parametros no pueden ser null");

        return postulacionesService.getPostulacion(idUsuario, idEmpleo);
    }


    @GetMapping("/resultados/terman/{idUsuario}")
    public HashMap<String, Object> getResultado(@PathVariable("idUsuario") Integer idUsuario){
        return termanServiceImpl.getResultado(idUsuario);
    }

    @GetMapping("/resultados/inglesb2/{idUsuario}")
    public HashMap<String, Object> getResultadoIngles(@PathVariable("idUsuario") Integer idUsuario){


        return inglesb2ServiceImp.getResultado(idUsuario);
    }

    @GetMapping("/resultados/cleaver/{idUsuario}")
    public Map<String, Object> getResultadoClaver(@PathVariable("idUsuario") Integer idUsuario){
        return cleaverService.getResultado(idUsuario);
    }

    @PutMapping("vacante/estatus/{idVacante}")
    public Integer cambiarEstatusVacante(@PathVariable("idVacante") Integer vacante,
                                         @RequestParam("estatus") Short estatus){
        return vacantesService.cambiarEstado(vacante, estatus);
    }

    @PostMapping("asignarPrueba")
    public Boolean asignarPrueba(@RequestBody UsuarioPrueba usuarioPrueba){
       return adminService.asignarPrueba(usuarioPrueba);
    }

    @GetMapping("vacantes")
    public List<Empleo> empleosActivos() {
        return adminService.empleosActivos();
    }

    @PostMapping("postulacion_rapida")
    @ResponseStatus(HttpStatus.OK)
    public void postPostulacionRapida(@RequestParam("nombre_completo") String nombreCompleto,
                                      @RequestParam("archivo") String archivo,
                                      @RequestParam("type") String type,
                                      @RequestParam("idEmpleo") Integer idEmpleo,
                                      @RequestParam("telefono") String telefono,
                                      @RequestParam("bodyPreguntas") String bodyPreguntas,
                                      @RequestParam("inlineImages") String inlineImagesString) {
        adminService.postPostulacionRapida(nombreCompleto, archivo, type, idEmpleo, telefono, bodyPreguntas, inlineImagesString);
    }

    @GetMapping("tests_rapidos")
    public List<PostRapTest> getTestsRapidos() {
        return adminService.getTestsRapidos();
    }

    @GetMapping("correos_notificacion")
    public List<String> getCorreosNotificacion() {
        return adminService.getCorreosNotificacion();
    }

}
