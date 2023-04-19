package domain.entities.actores.sectores;

import domain.entities.huellaDeCarbono.HuellaDeCarbono;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.ubicaciones.Municipio;
import lombok.Setter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sector_municipal")
public class SectorMunicipal extends SectorTerritorial {
    @OneToOne
    @JoinColumn(name = "municipio_id", referencedColumnName = "id")
    @Setter
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "sector_provincial_id", referencedColumnName = "id")
    private SectorProvincial sectorProvincial;

    @OneToMany(mappedBy = "sectorMunicipal")
    private List<Organizacion> organizaciones;

    public SectorMunicipal() {
        this.organizaciones = new ArrayList<>();
        this.huellasDeCarbono = new ArrayList<>();
    }

    public void agregarOrganizacion(Organizacion organizacion) {
        this.organizaciones.add(organizacion);
    }

    @Override
    public String getNombre() {
        return municipio.getNombre();
    }

    @Override
    public void agregarHuella(HuellaDeCarbono huellaDeCarbono) {
        super.agregarHuella(huellaDeCarbono);
        sectorProvincial.agregarHuella(new HuellaDeCarbono(huellaDeCarbono));
    }

    @Override
    public Map<String, Double> composicion(YearMonth fecha) {
        double huellaTotalSector = calcularHuellaTotalDesde(fecha);
        Map<String, Double> composicionPorSector = new HashMap<>();
        organizaciones.forEach(o ->
                composicionPorSector.put(o.convertirADTO().getNombre(),
                        o.calcularHuellaTotalDesde(fecha) / huellaTotalSector * 100)
        );
        return composicionPorSector;
    }

    public void setSectorProvincial(SectorProvincial sectorProvincial) {
        this.sectorProvincial = sectorProvincial;
        sectorProvincial.agregarSectorMunicipal(this);
    }
}