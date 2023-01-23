package com.truemedgroup.reclutamiento.models.pruebas;

import com.truemedgroup.commonsRecruit.usuario.Prueba;
import com.truemedgroup.commonsRecruit.usuario.SeccionResultado;
import com.truemedgroup.commonsRecruit.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Usuarios_Pruebas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoPrueba implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @Column(name ="Id_Usuario")
    private Integer idUsuario;

    @Column(name = "Id_Prueba")
    private Integer idPrueba;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Prueba", insertable = false, updatable = false)
    private Prueba prueba;

    @Column(name = "Calificacion")
    private Integer calificacion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "Id_Usuariopruebas")
    private List<SeccionResultado> secciones;

}
