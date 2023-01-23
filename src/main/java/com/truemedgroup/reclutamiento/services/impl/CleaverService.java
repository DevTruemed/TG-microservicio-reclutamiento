package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.usuario.Prueba;
import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;
import com.truemedgroup.reclutamiento.repositories.PruebasRepository;
import com.truemedgroup.reclutamiento.repositories.UsuarioPruebasRepository;
import com.truemedgroup.reclutamiento.services.interfaces.PruebasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CleaverService implements PruebasService {

    private static final Logger logger = LoggerFactory.getLogger(CleaverService.class);

    // HashMap que mapea la relación de una palabra con Dominio (D), Influencia (I), Constancia (S) y Apego (C) de cleaver
    private static final Map<String, List<String>> DISC = new HashMap<>();

    // Variable que mapea la relacion de Dominio (D) cuando el candiato está motivado (M) con respecto a una gráfica
    private static final Map<Integer, Integer> DM = new HashMap<>();

    // Variable que mapea la relacion de Influencia (I) cuando el candiato está motivado (M) con respecto a una gráfica
    private static final Map<Integer, Integer> IM = new HashMap<>();

    // Variable que mapea la relacion de Constancia (S) cuando el candiato está motivado (M) con respecto a una gráfica
    private static final Map<Integer, Integer> SM = new HashMap<>();

    // Variable que mapea la relacion de Apego (C) cuando el candiato está motivado (M) con respecto a una gráfica
    private static final Map<Integer, Integer> CM = new HashMap<>();

    // Variable que mapea la relacion de Dominio (D) cuando el candiato está bajo presión (L) con respecto a una gráfica
    private static final Map<Integer, Integer> DL = new HashMap<>();

    // Variable que mapea la relacion de Influencia (I) cuando el candiato está bajo presión (L) con respecto a una gráfica
    private static final Map<Integer, Integer> IL = new HashMap<>();

    // Variable que mapea la relacion de Constancia (S) cuando el candiato está bajo presión (L) con respecto a una gráfica
    private static final Map<Integer, Integer> SL = new HashMap<>();

    // Variable que mapea la relacion de Apego (C) cuando el candiato está bajo presión (L) con respecto a una gráfica
    private static final Map<Integer, Integer> CL = new HashMap<>();

    // Variable que mapea la relacion de Dominio (D) del total (M-L) con respecto a una gráfica
    private static final Map<Integer, Integer> DT = new HashMap<>();

    // Variable que mapea la relacion de Influencia (I) del total (M-L) con respecto a una gráfica
    private static final Map<Integer, Integer> IT = new HashMap<>();

    // Variable que mapea la relacion de Constancia (S) del total (M-L) con respecto a una gráfica
    private static final Map<Integer, Integer> ST = new HashMap<>();

    // Variable que mapea la relacion de Apego (C) del total (M-L) con respecto a una gráfica
    private static final Map<Integer, Integer> CT = new HashMap<>();

    //Bloque de inicialización de todas las varbles private static final
    static {

        DISC.put("PERSUASIVO",          getListEquivalencia("I", ""));
        DISC.put("GENTIL",              getListEquivalencia("S", "S"));
        DISC.put("HUMILDE",             getListEquivalencia("C", "C"));
        DISC.put("ORIGINAL",            getListEquivalencia("", "D"));

        DISC.put("AGRESIVO",            getListEquivalencia("D", ""));
        DISC.put("ALMA DE LA FIESTA",   getListEquivalencia("I", "I"));
        DISC.put("COMODINO",            getListEquivalencia("S", "S"));
        DISC.put("TEMEROSO",            getListEquivalencia("", "C"));

        DISC.put("AGRADABLE",           getListEquivalencia("", "S"));
        DISC.put("TEMEROSO DE DIOS",    getListEquivalencia("C", "C"));
        DISC.put("TENAZ",               getListEquivalencia("D", "D"));
        DISC.put("ATRACTIVO",           getListEquivalencia("I", "I"));

        DISC.put("CAUTELOSO",           getListEquivalencia("C", "C"));
        DISC.put("DETERMINADO",         getListEquivalencia("D", ""));
        DISC.put("CONVINCENTE",         getListEquivalencia("I", "I"));
        DISC.put("BONACHÓN",            getListEquivalencia("S", ""));

        DISC.put("DÓCIL",               getListEquivalencia("", "C"));
        DISC.put("ATREVIDO",            getListEquivalencia("D", "D"));
        DISC.put("LEAL",                getListEquivalencia("S", ""));
        DISC.put("ENCANTADOR",          getListEquivalencia("I", "I"));

        DISC.put("DISPUESTO",           getListEquivalencia("S", ""));
        DISC.put("DESEOSO",             getListEquivalencia("", ""));
        DISC.put("CONSECUENTE",         getListEquivalencia("C", "C"));
        DISC.put("ENTUSIASTA",          getListEquivalencia("", "D"));

        DISC.put("FUERZA DE VOLUNTAD",  getListEquivalencia("", "D"));
        DISC.put("MENTE ABIERTA",       getListEquivalencia("C", ""));
        DISC.put("COMPLACIENTE",        getListEquivalencia("S", "S"));
        DISC.put("ANIMOSO",             getListEquivalencia("I", "I"));

        DISC.put("CONFIADO",            getListEquivalencia("I", ""));
        DISC.put("SIMPATIZADOR",        getListEquivalencia("", "S"));
        DISC.put("TOLERANTE",           getListEquivalencia("", "C"));
        DISC.put("AFIRMATIVO",          getListEquivalencia("D", "D"));

        DISC.put("ECUÁNIME",            getListEquivalencia("S", "S"));
        DISC.put("PRECISO",             getListEquivalencia("C", "C"));
        DISC.put("NERVIOSO",            getListEquivalencia("", "D"));
        DISC.put("JOVIAL",              getListEquivalencia("", "I"));

        DISC.put("DISCIPLINADO",        getListEquivalencia("C", ""));
        DISC.put("GENEROSO",            getListEquivalencia("S", "S"));
        DISC.put("ANIMOSO1",             getListEquivalencia("", "I"));
        DISC.put("PERSISTENTE",         getListEquivalencia("D", "D"));

        DISC.put("COMPETITIVO",         getListEquivalencia("D", "D"));
        DISC.put("ALEGRE",              getListEquivalencia("", "I"));
        DISC.put("CONSIDERADO",         getListEquivalencia("S", "S"));
        DISC.put("ARMONIOSO",           getListEquivalencia("", "C"));

        DISC.put("ADMIRABLE",           getListEquivalencia("I", ""));
        DISC.put("BONDADOSO",           getListEquivalencia("S", ""));
        DISC.put("RESIGNADO",           getListEquivalencia("", "C"));
        DISC.put("CARÁCTER FIRME",      getListEquivalencia("D", "D"));

        DISC.put("OBEDIENTE",           getListEquivalencia("S", ""));
        DISC.put("QUISQUILLOSO",        getListEquivalencia("", "C"));
        DISC.put("INCONQUISTABLE",      getListEquivalencia("D", "D"));
        DISC.put("JUGUETÓN",            getListEquivalencia("I", "I"));

        DISC.put("RESPETUOSO",          getListEquivalencia("C", ""));
        DISC.put("EMPRENDEDOR",         getListEquivalencia("D", "D"));
        DISC.put("OPTIMISTA",           getListEquivalencia("I", "I"));
        DISC.put("SERVICIAL",           getListEquivalencia("S", "S"));

        DISC.put("VALIENTE",            getListEquivalencia("D", ""));
        DISC.put("INSPIRADOR",          getListEquivalencia("I", ""));
        DISC.put("SUMISO",              getListEquivalencia("", "S"));
        DISC.put("TÍMIDO",              getListEquivalencia("", "C"));

        DISC.put("ADAPTABLE",           getListEquivalencia("C", ""));
        DISC.put("DISPUTADOR",          getListEquivalencia("D", "D"));
        DISC.put("INDIFERENTE",         getListEquivalencia("", "S"));
        DISC.put("SANGRE LIVIANA",      getListEquivalencia("I", "I"));

        DISC.put("AMIGUERO",            getListEquivalencia("I", "I"));
        DISC.put("PACIENTE",            getListEquivalencia("S", "S"));
        DISC.put("CONFIANZA EN SÍ MISMO",getListEquivalencia("D", "D"));
        DISC.put("MESURADO PARA HABLAR",getListEquivalencia("C", ""));

        DISC.put("CONFORME",            getListEquivalencia("", "S"));
        DISC.put("CONFIABLE",           getListEquivalencia("S", "I"));
        DISC.put("PACÍFICO",            getListEquivalencia("C", "C"));
        DISC.put("POSITIVO",            getListEquivalencia("D", "D"));

        DISC.put("AVENTURERO",          getListEquivalencia("D", "D"));
        DISC.put("RECEPTIVO",           getListEquivalencia("C", ""));
        DISC.put("CORDIAL",             getListEquivalencia("", "I"));
        DISC.put("MODERADO",            getListEquivalencia("S", "S"));

        DISC.put("INDULGENTE",          getListEquivalencia("S", "S"));
        DISC.put("ESTETA",              getListEquivalencia("", "C"));
        DISC.put("VIGOROSO",            getListEquivalencia("D", "D"));
        DISC.put("SOCIABLE",            getListEquivalencia("I", "I"));

        DISC.put("PARLANCHÍN",          getListEquivalencia("I", "I"));
        DISC.put("CONTROLADO",          getListEquivalencia("S", "S"));
        DISC.put("CONVENCIONAL",        getListEquivalencia("", "C"));
        DISC.put("DECISIVO",            getListEquivalencia("D", "D"));

        DISC.put("COHIBIDO",            getListEquivalencia("", "S"));
        DISC.put("EXACTO",              getListEquivalencia("C", ""));
        DISC.put("FRANCO",              getListEquivalencia("D", "D"));
        DISC.put("BUEN COMPAÑERO",      getListEquivalencia("I", "I"));

        DISC.put("DIPLOMÁTICO",         getListEquivalencia("C", ""));
        DISC.put("AUDAZ",               getListEquivalencia("D", "D"));
        DISC.put("REFINADO",            getListEquivalencia("", "I"));
        DISC.put("SATISFECHO",          getListEquivalencia("S", "S"));

        DISC.put("INQUIETO",            getListEquivalencia("D", "D"));
        DISC.put("POPULAR",             getListEquivalencia("I", "I"));
        DISC.put("BUEN VECINO",         getListEquivalencia("S", "S"));
        DISC.put("DEVOTO",              getListEquivalencia("C", "C"));

        DM.put(0,1);
        DM.put(1,5);
        DM.put(2,10);
        DM.put(3,20);
        DM.put(4,30);
        DM.put(5,40);
        DM.put(6,50);
        DM.put(7,60);
        DM.put(8,65);
        DM.put(9,75);
        DM.put(10,84);
        DM.put(11,87);
        DM.put(12,90);
        DM.put(13,93);
        DM.put(14,95);
        DM.put(15,97);
        DM.put(16,97);
        DM.put(17,98);
        DM.put(18,98);
        DM.put(19,98);
        DM.put(20,99);

        IM.put(0,4);
        IM.put(1,10);
        IM.put(2,25);
        IM.put(3,40);
        IM.put(4,55);
        IM.put(5,70);
        IM.put(6,82);
        IM.put(7,90);
        IM.put(8,95);
        IM.put(9,96);
        IM.put(10,97);
        IM.put(11,97);
        IM.put(12,97);
        IM.put(13,97);
        IM.put(14,97);
        IM.put(15,97);
        IM.put(16,97);
        IM.put(17,99);

        SM.put(0,5);
        SM.put(1,10);
        SM.put(2,16);
        SM.put(3,30);
        SM.put(4,40);
        SM.put(5,55);
        SM.put(6,63);
        SM.put(7,75);
        SM.put(8,84);
        SM.put(9,90);
        SM.put(10,95);
        SM.put(11,96);
        SM.put(12,97);
        SM.put(13,97);
        SM.put(14,97);
        SM.put(15,97);
        SM.put(16,98);
        SM.put(17,98);
        SM.put(18,98);
        SM.put(19,99);

        CM.put(0,1);
        CM.put(1,5);
        CM.put(2,16);
        CM.put(3,30);
        CM.put(4,55);
        CM.put(5,70);
        CM.put(6,84);
        CM.put(7,93);
        CM.put(8,95);
        CM.put(9,97);
        CM.put(10,97);
        CM.put(11,97);
        CM.put(12,98);
        CM.put(13,98);
        CM.put(14,98);
        CM.put(15,99);

        DL.put(0,99);
        DL.put(1,95);
        DL.put(2,87);
        DL.put(3,80);
        DL.put(4,65);
        DL.put(5,55);
        DL.put(6,50);
        DL.put(7,35);
        DL.put(8,30);
        DL.put(9,20);
        DL.put(10,18);
        DL.put(11,15);
        DL.put(12,10);
        DL.put(13,6);
        DL.put(14,5);
        DL.put(15,4);
        DL.put(16,3);
        DL.put(17,2);
        DL.put(18,2);
        DL.put(19,2);
        DL.put(20,2);
        DL.put(21,1);

        IL.put(0,99);
        IL.put(1,95);
        IL.put(2,87);
        IL.put(3,75);
        IL.put(4,55);
        IL.put(5,40);
        IL.put(6,25);
        IL.put(7,16);
        IL.put(8,10);
        IL.put(9,5);
        IL.put(10,4);
        IL.put(11,4);
        IL.put(12,3);
        IL.put(13,3);
        IL.put(14,3);
        IL.put(15,2);
        IL.put(16,2);
        IL.put(17,2);
        IL.put(18,2);
        IL.put(19,1);

        SL.put(0,99);
        SL.put(1,97);
        SL.put(2,95);
        SL.put(3,87);
        SL.put(4,80);
        SL.put(5,65);
        SL.put(6,55);
        SL.put(7,35);
        SL.put(8,28);
        SL.put(9,18);
        SL.put(10,10);
        SL.put(11,5);
        SL.put(12,4);
        SL.put(13,3);
        SL.put(14,3);
        SL.put(15,3);
        SL.put(16,2);
        SL.put(17,2);
        SL.put(18,2);
        SL.put(19,1);

        CL.put(0,99);
        CL.put(1,97);
        CL.put(2,95);
        CL.put(3,90);
        CL.put(4,84);
        CL.put(5,70);
        CL.put(6,55);
        CL.put(7,40);
        CL.put(8,38);
        CL.put(9,23);
        CL.put(10,10);
        CL.put(11,5);
        CL.put(12,4);
        CL.put(13,3);
        CL.put(14,2);
        CL.put(15,2);
        CL.put(16,1);

        DT.put(-21,1);
        DT.put(-20,2);
        DT.put(-19,2);
        DT.put(-18,2);
        DT.put(-17,2);
        DT.put(-16,2);
        DT.put(-15,2);
        DT.put(-14,2);
        DT.put(-13,4);
        DT.put(-12,5);
        DT.put(-11,5);
        DT.put(-10,9);
        DT.put(-9,13);
        DT.put(-8,15);
        DT.put(-7,16);
        DT.put(-6,20);
        DT.put(-5,25);
        DT.put(-4,29);
        DT.put(-3,35);
        DT.put(-2,40);
        DT.put(-1,45);
        DT.put(0,50);
        DT.put(1,55);
        DT.put(2,60);
        DT.put(3,65);
        DT.put(4,67);
        DT.put(5,70);
        DT.put(6,75);
        DT.put(7,80);
        DT.put(8,84);
        DT.put(9,85);
        DT.put(10,90);
        DT.put(11,91);
        DT.put(12,94);
        DT.put(13,95);
        DT.put(14,96);
        DT.put(15,97);
        DT.put(16,97);
        DT.put(17,98);
        DT.put(18,98);
        DT.put(19,98);
        DT.put(20,99);

        IT.put(-19,1);
        IT.put(-18,2);
        IT.put(-17,2);
        IT.put(-16,2);
        IT.put(-15,2);
        IT.put(-14,2);
        IT.put(-13,2);
        IT.put(-12,2);
        IT.put(-11,2);
        IT.put(-10,3);
        IT.put(-9,4);
        IT.put(-8,5);
        IT.put(-7,6);
        IT.put(-6,10);
        IT.put(-5,16);
        IT.put(-4,20);
        IT.put(-3,29);
        IT.put(-2,35);
        IT.put(-1,45);
        IT.put(0,55);
        IT.put(1,60);
        IT.put(2,70);
        IT.put(3,75);
        IT.put(4,85);
        IT.put(5,90);
        IT.put(6,95);
        IT.put(7,96);
        IT.put(8,97);
        IT.put(9,97);
        IT.put(10,98);
        IT.put(11,98);
        IT.put(12,98);
        IT.put(13,98);
        IT.put(14,98);
        IT.put(15,98);
        IT.put(16,98);
        IT.put(17,99);

        ST.put(-19,1);
        ST.put(-18,2);
        ST.put(-17,2);
        ST.put(-16,2);
        ST.put(-15,2);
        ST.put(-14,2);
        ST.put(-13,2);
        ST.put(-12,3);
        ST.put(-11,4);
        ST.put(-10,5);
        ST.put(-9,8);
        ST.put(-8,10);
        ST.put(-7,15);
        ST.put(-6,20);
        ST.put(-5,25);
        ST.put(-4,30);
        ST.put(-3,35);
        ST.put(-2,40);
        ST.put(-1,50);
        ST.put(0,57);
        ST.put(1,60);
        ST.put(2,70);
        ST.put(3,75);
        ST.put(4,80);
        ST.put(5,84);
        ST.put(6,87);
        ST.put(7,91);
        ST.put(8,94);
        ST.put(9,96);
        ST.put(10,97);
        ST.put(11,97);
        ST.put(12,98);
        ST.put(13,98);
        ST.put(14,98);
        ST.put(15,98);
        ST.put(16,98);
        ST.put(17,98);
        ST.put(18,98);
        ST.put(19,99);

        CT.put(-16,1);
        CT.put(-15,2);
        CT.put(-14,2);
        CT.put(-13,2);
        CT.put(-12,2);
        CT.put(-11,3);
        CT.put(-10,4);
        CT.put(-9,6);
        CT.put(-8,9);
        CT.put(-7,13);
        CT.put(-6,20);
        CT.put(-5,25);
        CT.put(-4,35);
        CT.put(-3,40);
        CT.put(-2,55);
        CT.put(-1,60);
        CT.put(0,70);
        CT.put(1,75);
        CT.put(2,84);
        CT.put(3,90);
        CT.put(4,95);
        CT.put(5,96);
        CT.put(6,97);
        CT.put(7,97);
        CT.put(8,98);
        CT.put(9,98);
        CT.put(10,98);
        CT.put(11,98);
        CT.put(12,98);
        CT.put(13,98);
        CT.put(14,98);
        CT.put(15,99);

    }

    @Autowired
    PruebasRepository pruebasRepository;

    @Autowired
    UsuarioPruebasRepository usuarioPruebasRepository;

    @Override
    public Prueba getTest() {
        return pruebasRepository.findByNombre("Cleaver").get();
    }

    @Override
    public Boolean CalificarPrueba(UsuarioPrueba resultado) {

        if ( usuarioPruebasRepository.findByidUsuarioAndidPrueba(resultado.getIdUsuario(), resultado.getIdPrueba()).isPresent() ){
            resultado.setId(usuarioPruebasRepository.findByidUsuarioAndidPrueba(resultado.getIdUsuario(), resultado.getIdPrueba()).get().getId());
        }

        usuarioPruebasRepository.save(resultado);

        return true;
    }

    @Transactional
    public Map<String, Object> getResultado(Integer idUsuario) throws ResourceNotFoundException{

        logger.info("getResultado(" + idUsuario + ")");

        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> M = new HashMap<>();
        Map<String, Integer> L = new HashMap<>();

        M.put("D",0);
        M.put("I",0);
        M.put("S",0);
        M.put("C",0);
        L.put("D",0);
        L.put("I",0);
        L.put("S",0);
        L.put("C",0);

        Optional<UsuarioPrueba> consulta = usuarioPruebasRepository.findByidUsuarioAndidPrueba(idUsuario, 2);

        if(consulta.isPresent()){

            UsuarioPrueba usuarioPrueba = consulta.get();

            usuarioPrueba.getSecciones().get(0).getPreguntas().forEach(pregunta -> {

                String aux[] = pregunta.getIntentos().get(0).getRespuesta().split("//");

                if ( DISC.get(aux[0].toUpperCase(Locale.ROOT)) == null )
                    logger.error("Error MAP DISC -> " + aux[0].toUpperCase(Locale.ROOT));
                else if ( !DISC.get(aux[0].toUpperCase(Locale.ROOT)).get(0).equals("") ){
                    M.put( DISC.get(aux[0].toUpperCase(Locale.ROOT)).get(0), M.get(DISC.get(aux[0].toUpperCase(Locale.ROOT)).get(0)) + 1);
                }

                if ( DISC.get(aux[1].toUpperCase(Locale.ROOT)) == null )
                    logger.error("Error MAP DISC -> " + aux[1].toUpperCase(Locale.ROOT));
                else if ( !DISC.get(aux[1].toUpperCase(Locale.ROOT)).get(1).equals("") ){
                    L.put( DISC.get(aux[1].toUpperCase(Locale.ROOT)).get(1), L.get(DISC.get(aux[1].toUpperCase(Locale.ROOT)).get(1)) + 1);
                }

            });

        }else {
            throw new ResourceNotFoundException("Resultado no encontrado");
        }

        result.put("M",M);
        result.put("L",L);

        Map<String, Integer> TOTAL = new HashMap<>();

        TOTAL.put("D", M.get("D") - L.get("D"));
        TOTAL.put("I", M.get("I") - L.get("I"));
        TOTAL.put("S", M.get("S") - L.get("S"));
        TOTAL.put("C", M.get("C") - L.get("C"));

        result.put("TOTAL", TOTAL);

        Map<String, Integer> gm = new HashMap<>();

        gm.put("D", DM.get(M.get("D")));
        gm.put("I", IM.get(M.get("I")));
        gm.put("S", SM.get(M.get("S")));
        gm.put("C", CM.get(M.get("C")));

        result.put("GM", gm);

        Map<String, Integer> gl = new HashMap<>();

        gl.put("D", DL.get(L.get("D")));
        gl.put("I", IL.get(L.get("I")));
        gl.put("S", SL.get(L.get("S")));
        gl.put("C", CL.get(L.get("C")));

        result.put("GL", gl);

        Map<String, Integer> gt = new HashMap<>();
        gt.put("D", DT.get(TOTAL.get("D")));
        gt.put("I", IT.get(TOTAL.get("I")));
        gt.put("S", ST.get(TOTAL.get("S")));
        gt.put("C", CT.get(TOTAL.get("C")));

        result.put("GT", gt);

        return result;
    }


    /**
     * Método creado para crear un arraylst con 2 campos para inicializar
     * el mapa de equivalecias
     * */
    private static List<String> getListEquivalencia(String valor1, String valor2){
        List<String> result = new ArrayList<>();
        result.add(valor1);
        result.add(valor2);
        return result;
    }

}
