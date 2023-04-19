package domain.entities.ubicaciones;

import domain.entities.actores.sectores.SectorProvincial;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "provincia")
public class Provincia {
    @Id
    @Getter
    public int id;

    @Column(name = "nombre")
    @Getter
    public String nombre;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @Setter
    private Pais pais;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "sector_provincial_id", referencedColumnName = "id")
    @Getter
    private SectorProvincial sectorProvincial;

    public Provincia() {
        this.sectorProvincial = new SectorProvincial();
        this.sectorProvincial.setProvincia(this);
    }

    public Provincia(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.sectorProvincial = new SectorProvincial();
        this.sectorProvincial.setProvincia(this);
    }

    public String getNombre(){
        return "Provincia de " + this.nombre;
    }
}