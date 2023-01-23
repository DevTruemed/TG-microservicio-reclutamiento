package com.truemedgroup.reclutamiento.services.interfaces;

import com.truemedgroup.commonsRecruit.usuario.Prueba;
import com.truemedgroup.commonsRecruit.usuario.UsuarioPrueba;

public interface PruebasService{

    public Prueba getTest();

    public Boolean CalificarPrueba(UsuarioPrueba resultado);

}
