package domain.entities.usuarios;

import db.EntidadPersistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol extends EntidadPersistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Permiso permiso;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rol_permiso", joinColumns = @JoinColumn(name = "rol_id"))
    @Enumerated(EnumType.STRING)
    private Set<Permiso> permisos;

    public Rol() {
        this.permisos = new LinkedHashSet<>();
    }

    public Rol(Permiso permiso) {
        this.permiso = permiso;
        this.permisos = new LinkedHashSet<>();
    }

    public void agregarPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    public Boolean tenesPermiso(Permiso permiso) {
        return this.permisos.contains(permiso);
    }

    public Boolean tenesTodosLosPermisos(Permiso ... permisos) {
        return Arrays.stream(permisos).allMatch(p -> this.permisos.contains(p));
    }
}
