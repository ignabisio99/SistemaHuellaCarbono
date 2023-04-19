package domain.entities.usuarios;

import db.EntidadPersistente;
import domain.entities.actores.Actor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "usuario")
@Getter @Setter
public class Usuario extends EntidadPersistente {
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nombre_de_usuario", unique = true)
    private String nombreDeUsuario;

    @Column(name = "contrasenia")
    private String contrasenia;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "actor_id", referencedColumnName = "id")
    private Actor actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Rol rol;

    public Usuario() {

    }

    public Usuario(String nombreDeUsuario, String contrasenia, Actor actor, Rol rol) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contrasenia = contrasenia;
        this.actor = actor;
        actor.setUsuario(this);
        this.rol = rol;
    }

    public Permiso getRol() {
        return rol.getPermiso();
    }
}