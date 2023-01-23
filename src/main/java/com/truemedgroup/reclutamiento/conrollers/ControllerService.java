package com.truemedgroup.reclutamiento.conrollers;

import com.truemedgroup.commonsRecruit.usuario.*;
import com.truemedgroup.reclutamiento.repositories.*;
import com.truemedgroup.reclutamiento.services.impl.TermanServiceImpl;
import com.truemedgroup.reclutamiento.services.interfaces.UsuariosService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class ControllerService {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private TermanServiceImpl termanServiceImpl;

    @Autowired
    private EmpleosRepository empleosRepository;

    @Autowired
    private PostulacionesRepository postulacionesRepository;

    @Autowired
    private PruebasRepository pruebasRepository;

    @Autowired
    private UsuarioPruebasRepository usuarioPruebasRepository;

    @Autowired
    private SeccionesResultadosRepository seccionesResultadosRepository;

    @Autowired
    private PreguntasResultadosRepository preguntasResultadosRepository;

    @Autowired
    private IntentosRepository intentosRepository;

    @Autowired
    private CorreosRepository correosRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Usuario> getUsuarios(@PageableDefault(page = 0, size = 50) Pageable pageable){

        return usuariosService.list(pageable);

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario getUsuario(@PathVariable("id") Integer id)
            throws ResourceNotFoundException{
        return usuariosService.getUsuario(id);
    }

    @GetMapping("/search/correo")
    @ResponseStatus(HttpStatus.OK)
    public Usuario getUsuario(@RequestParam(value = "correo")  String correo)
            throws ResourceNotFoundException{

        return usuariosService.getUsuarioByCorreo(correo);

    }

    @GetMapping("/exist/correo")
    public Boolean existCorreo(@RequestParam(value = "correo")  String correo)
            throws ResourceNotFoundException{

        return correosRepository.existsByCorreo(correo);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario PostUsuario( @RequestBody Usuario usuario ){ ;

        return usuariosService.postUsuario(usuario);

    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario PutUsuario( @RequestBody Usuario usuario,
                               @PathVariable("id") Integer id)
            throws ResourceNotFoundException{
       return usuariosService.putUsuario(usuario,id);

    }

    @GetMapping("/empleos")
    public List<Empleo> getEmpleos(){
        return (List<Empleo>) empleosRepository.findAll();
    }

    @GetMapping("/empleos/{id}")
    public Empleo getEmpleoById(@PathVariable("id") Integer id){
        return empleosRepository.findById(id).get();
    }

    @Autowired
    private HabilidadesRepository habilidadesRepository;

    @PostMapping("/empleos")
    public Empleo postEmpleo(@RequestBody Empleo empleo){
        empleo.setFechaPublicacion(new Date());

        empleo.setHabilidades(empleo.getHabilidades().stream().map( habilidad ->{
                if (habilidad.getId() == null){
                    return habilidadesRepository.save(habilidad);
                }
                return habilidad;
        }).collect(Collectors.toList()));

        Empleo result = empleosRepository.save(empleo);

        return result;
    }

    @GetMapping("/habilidades")
    public List<Habilidad> getHabilidades(){
        return (List<Habilidad>) habilidadesRepository.findAll();
    }

    @PutMapping("/empleos/{id}")
    public  Empleo putEmpleo(@RequestBody Empleo empleo, @PathVariable("id") Integer id){

        empleo.setId(id);

        empleo.setHabilidades(empleo.getHabilidades().stream().map( habilidad ->{
            if (habilidad.getId() == null){
                return habilidadesRepository.save(habilidad);
            }
            return habilidad;
        }).collect(Collectors.toList()));

        empleo.setFechaPublicacion(empleosRepository.findById(id).get().getFechaPublicacion());

        return empleosRepository.save(empleo);
    }

    @DeleteMapping("/empleos/{id}")
    public void deleteEmpleo(@PathVariable("id") Integer id){

        Empleo empleo = empleosRepository.findById(id).get();
        empleo.setActivo(false);
        empleosRepository.save(empleo);

    }

    @GetMapping("/postulaciones")
    public List<Postulacion> getPostulaciones(){
        return (List<Postulacion>) postulacionesRepository.findAll();
    }

    @GetMapping("/postulaciones/{id}")
    public Postulacion getPostulacionById(@PathVariable("id") Integer id){
        return postulacionesRepository.findByid(id).get();
    }

    @PostMapping("/postulaciones")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,Boolean> postPostulaciones(@RequestBody Postulacion postulacion ){
        postulacion.setFecha(new Date());
        postulacion.setEstatus((short)0);
        postulacionesRepository.save(postulacion);
        HashMap<String,Boolean> result = new HashMap<String, Boolean>();
//        if (usuarioPruebasRepository.findByIdUsuario(postulacion.getIdUsuario()).isPresent())
//            result.put("existTest",true);
//        else
//            result.put("existTest",false);
        result.put("existTest",false);
        result.put("existTestCleaver",false);
        for (Prueba prueba: empleosRepository.findById(postulacion.getIdEmpleo()).get().getPruebas()) {
//            if (prueba.getId() == 1 ){
//                result.put("existTest",true);
//            }
//            if (usuarioPruebasRepository.findByidUsuarioAndidPrueba(postulacion.getIdUsuario(), prueba.getId()).isPresent()){
//                result.put("existTestCleaver",true);
//            }
//            if (prueba.getId() == 1 && usuarioPruebasRepository.findByidUsuarioAndidPrueba(postulacion.getIdUsuario(), prueba.getId()).isPresent())
//                result.put("existTest",true);
            if (prueba.getId() == 1) {
                if (usuarioPruebasRepository.findByidUsuarioAndidPrueba(postulacion.getIdUsuario(), prueba.getId()).isPresent()) {
                    result.put("existTest",true);
                } else {
                    // Ingresa el nuevo
                    UsuarioPrueba usuarioPrueba = new UsuarioPrueba();
                    if ( !usuarioPruebasRepository.existsByidUsuarioAndIdPrueba(postulacion.getIdUsuario(), prueba.getId()) ) {
                        usuarioPrueba.setIdUsuario(postulacion.getIdUsuario());
                        usuarioPrueba.setIdPrueba(prueba.getId());
                        usuarioPrueba.setId(null);
                        usuarioPrueba.setCalificacion(null);
                        usuarioPrueba.setSecciones(null);
                        usuarioPruebasRepository.save(usuarioPrueba);
                        result.put("existTest",true);
                    }
                }
            }

            if (prueba.getId() == 2  ) {
                if(usuarioPruebasRepository.findByidUsuarioAndidPrueba(postulacion.getIdUsuario(), prueba.getId()).isPresent()){
                    result.put("existTestCleaver",true);
                }
                else {
                    // Ingresa el nuevo
                    UsuarioPrueba usuarioPrueba = new UsuarioPrueba();
                    if ( !usuarioPruebasRepository.existsByidUsuarioAndIdPrueba(postulacion.getIdUsuario(), prueba.getId()) ) {
                        usuarioPrueba.setIdUsuario(postulacion.getIdUsuario());
                        usuarioPrueba.setIdPrueba(prueba.getId());
                        usuarioPrueba.setId(null);
                        usuarioPrueba.setCalificacion(null);
                        usuarioPrueba.setSecciones(null);
                        usuarioPruebasRepository.save(usuarioPrueba);
                        result.put("existTestCleaver",true);
                    }
                }
            }

            if (prueba.getId() == 1002){
                if(usuarioPruebasRepository.findByidUsuarioAndidPrueba(postulacion.getIdUsuario(), prueba.getId()).isPresent()){
                    result.put("existTestInglesB2",true);
                }
                else {
                    // Ingresa el nuevo
                    UsuarioPrueba usuarioPrueba = new UsuarioPrueba();
                    if ( !usuarioPruebasRepository.existsByidUsuarioAndIdPrueba(postulacion.getIdUsuario(), prueba.getId()) ) {
                        usuarioPrueba.setIdUsuario(postulacion.getIdUsuario());
                        usuarioPrueba.setIdPrueba(prueba.getId());
                        usuarioPrueba.setId(null);
                        usuarioPrueba.setCalificacion(null);
                        usuarioPrueba.setSecciones(null);
                        usuarioPruebasRepository.save(usuarioPrueba);
                        result.put("existTestInglesB2",true);
                    }
                }
            }

        }
        return result;
    }

    @GetMapping("/pruebas/{id}")
    public Prueba getPruebas(@PathVariable("id") Integer id){
        return pruebasRepository.findById(id).get();
    }

    @GetMapping("/resultados")
    public List<HashMap<String,Object>> getResultados(){
        List<HashMap<String,Object>> result = new ArrayList<>();
       return usuariosService.list(PageRequest.of(0,5000)).get().map( user -> {
               HashMap<String,Object> aux = new HashMap<String, Object>();
               aux.put("usuario",user);
               Optional<UsuarioPrueba> usuarioPrueba = usuarioPruebasRepository.findByIdUsuario(user.getId());
               if (usuarioPrueba.isPresent()){
                   aux.put("calificacion",usuarioPrueba.get().getCalificacion());
                   aux.put("id", usuarioPrueba.get().getId());
               }else {
                   aux.put("calificacion","NO APLICA");
                   aux.put("id", null);
               }
               aux.put("postulaciones", postulacionesRepository.findByidUsuario(user.getId()));
               return aux;
        }).collect(Collectors.toList());
    }

    @GetMapping("/resultados/{id}/{idPostulacion}")
    public HashMap<String, Object> getResultado(@PathVariable("id") String id, @PathVariable(value = "idPostulacion") String idPostulacion){
        HashMap<String, Object> resultado = new HashMap<>();
        UsuarioPrueba result = null;
        Usuario usuario = null;
        if (!id.equals("null")){
             result = usuarioPruebasRepository.findById(Integer.parseInt(id)).get();
             usuario = result.getUsuario();
        }else {
             usuario = usuariosService.getUsuario(postulacionesRepository.findByid(Integer.parseInt(idPostulacion)).get().getIdUsuario());
        }

        usuario.setCorreos(usuario.getCorreos().stream().map(correo -> {
            correo.setUsuario(null);
            return correo;
        }).collect(Collectors.toList()));

        if (result != null)
        result.setUsuario(usuario);

        if (idPostulacion.equals("null")){
            List<Postulacion> postulacions = postulacionesRepository.findByidUsuario(result.getIdUsuario());
            postulacions.stream().map(postulacion -> {
                postulacion.setRevisado(true);
                return postulacion;
            }).collect(Collectors.toList());
            postulacionesRepository.saveAll(postulacions);
        }else {
            Optional<Postulacion> postulacion = postulacionesRepository.findByid(Integer.parseInt(idPostulacion));
            if (postulacion.isPresent()){
                Postulacion postulacion1 = postulacion.get();
                postulacion1.setRevisado(true);
                postulacion1.setId(null);
                postulacionesRepository.save(postulacion1);
            }
        }
        resultado.put("resultado", result);
        resultado.put("usuario", usuario);
        return resultado;
    }

    @GetMapping("/test/{id}")
    public Boolean testId(@PathVariable("id") Integer id){
        return usuarioPruebasRepository.findByIdUsuario(id).isPresent();
    }

    @PostMapping("/resultados")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioPrueba postPruebas(@RequestBody UsuarioPrueba usuarioPrueba){


            if (usuarioPrueba.getIdPrueba() == 1){
                // No trae calificacion, lo califica
                usuarioPrueba = termanServiceImpl.CalficarTest(usuarioPrueba);
            }

            // Procede a la validacion y generacion
            if (usuarioPruebasRepository.findByidUsuarioAndidPrueba(usuarioPrueba.getIdUsuario(), usuarioPrueba.getIdPrueba()).isPresent()){
                usuarioPrueba.setId(usuarioPruebasRepository.findByidUsuarioAndidPrueba(usuarioPrueba.getIdUsuario(), usuarioPrueba.getIdPrueba()).get().getId());
            }

            List<SeccionResultado> secciones = usuarioPrueba.getSecciones();
            usuarioPrueba.setSecciones(null);

            usuarioPrueba = usuarioPruebasRepository.save(usuarioPrueba);

            for (SeccionResultado seccion : secciones) {

                List<PreguntaResultado> preguntas = seccion.getPreguntas();

                seccion.setPreguntas(null);
                seccion.setIdUsuarioPrueba(usuarioPrueba.getId());

                seccion = seccionesResultadosRepository.save(seccion);

                for (PreguntaResultado pregunta : preguntas) {
                    List <Intentos> intentos = pregunta.getIntentos();
                    pregunta.setIntentos(null);
                    pregunta.setIdSeccionesResultados(seccion.getId());
                    pregunta = preguntasResultadosRepository.save(pregunta);

                    for (Intentos intento : intentos) {
                        intento.setIdPreguntaresultado(pregunta.getId());
                        intentosRepository.save(intento);
                    }

                }

            }



        return usuarioPruebasRepository.findById(usuarioPrueba.getId()).get();
    }

    @PostMapping("/recuperar/password")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean recuperarPassword(@RequestParam("correo") String correo){
        return usuariosService.requestPasswordChange(correo);
    }

    @GetMapping("/recuperar/password")
    @ResponseStatus(HttpStatus.OK)
    public String checkToken(@RequestParam("token") String token){
        return usuariosService.checkToken(token);
    }

    @PutMapping("/restorePassword")
    @ResponseStatus(HttpStatus.CREATED)
    public void restorePassword(@RequestParam("token") String token, @RequestParam String password){
        usuariosService.restorePassword(token,password);
    }

    @Autowired
    ArchivosRepository archivosRepository;

    @PostMapping("/uploadTestImageBase64")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadImage2(@RequestParam("image") String imageValue,
                               @RequestParam("formato") String formato,
                               @RequestParam("UsuarioId") String idUsuario,
                               @RequestParam("Prueba") String prueba)
    {
        try
        {
            byte[] imageByte=Base64.decodeBase64(imageValue);
            String directory="/reclutamiento/"+prueba+idUsuario+archivosRepository.countImagenes(Integer.parseInt(idUsuario));

            if (formato.equals("image/jpg"))
                directory+=".jpg";
            else if (formato.equals("image/jpeg"))
                directory+=".jpeg";
            else if (formato.equals("image/png"))
                directory+=".png";

            OutputStream out = new FileOutputStream(directory);
            out.write(imageByte);
            out.close();

            Archivo imagen = new Archivo();
            imagen.setRuta(directory);
            imagen.setTipo(formato);
            imagen.setIdResultado(Integer.parseInt(idUsuario));

            archivosRepository.save(imagen);

            return "success";
        }
        catch(Exception e)
        {
            return "error = "+e;
        }

    }


    @GetMapping(value = "/getTestImageBase64")
    public @ResponseBody
    List<HashMap<String,String>> getImage(@RequestParam("idUsuario") Integer id) throws IOException {
        List<HashMap<String,String>>  result = new ArrayList<HashMap<String,String>>();
        List<Archivo> imagenes   = archivosRepository.findAllByidResulado(id);
        for (Archivo archivo :
                imagenes) {
            HashMap<String,String> image = new HashMap<String, String>();
            byte[] fileContent = FileUtils.readFileToByteArray(new File(archivo.getRuta()));
            String encodedString = Base64.encodeBase64String(fileContent);
            image.put("image",encodedString);
            image.put("type", archivo.getTipo());
            result.add(image);
        }

        return result;
    }

}
