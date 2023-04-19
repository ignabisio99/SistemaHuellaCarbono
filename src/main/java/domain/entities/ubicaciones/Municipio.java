package domain.entities.ubicaciones;

import domain.entities.actores.sectores.SectorMunicipal;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "municipio")
public class Municipio {
    @Id
    @Getter
    public int id;

    @Column(name = "nombre")
    @Getter
    public String nombre;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    private Provincia provincia;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "sector_municipal_id", referencedColumnName = "id")
    @Getter
    private SectorMunicipal sectorMunicipal;

    public Municipio() {
        this.sectorMunicipal = new SectorMunicipal();
        this.sectorMunicipal.setMunicipio(this);
    }

    public Municipio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.sectorMunicipal = new SectorMunicipal();
        this.sectorMunicipal.setMunicipio(this);
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
        this.sectorMunicipal.setSectorProvincial(provincia.getSectorProvincial());
    }

    public String getNombre(){
        return "Municipio de " + this.nombre;
    }
}