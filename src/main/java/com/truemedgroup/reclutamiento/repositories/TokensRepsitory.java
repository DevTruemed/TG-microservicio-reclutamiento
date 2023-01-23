package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.commonsRecruit.usuario.Token;
import com.truemedgroup.commonsRecruit.usuario.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepsitory extends CrudRepository<Token,Long> {

    Boolean existsByToken(String token);

    Boolean existsByUsuario(Usuario usuario);

    Token findByUsuario(Usuario usuario);

    Token findByToken(String token);

}
