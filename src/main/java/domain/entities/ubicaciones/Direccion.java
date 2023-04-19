package domain.entities.ubicaciones;

import db.EntidadPersistente;
import domain.entities.actores.sectores.SectorMunicipal;
import domain.entities.actores.organizaciones.Organizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "direccion")
@Getter @Setter
public class Direccion extends EntidadPersistente {
    @ManyToOne
    @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    private Localidad localidad;

    @Column(name = "calle")
    private String calle;

    @Column(name = "altura")
    private String altura;

    public Direccion() {
    }

    public Direccion(Localidad localidad, String calle, String altura) {
        this.localidad = localidad;
        this.calle = calle;
        this.altura = altura;
    }

    public int getLocalidadId() {
        return localidad.getId();
    }

    public void agregarASectorMunicipal(Organizacion organizacion) {
        getSectorMunicipal().agregarOrganizacion(organizacion);
    }

    public SectorMunicipal getSectorMunicipal() {
        return localidad.getMunicipio().getSectorMunicipal();
    }
}