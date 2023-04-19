package domain.entities.actores.sectores;

import domain.entities.ubicaciones.Provincia;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sector_provincial")
public class SectorProvincial extends SectorTerritorial {
    @OneToOne
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    @Setter
    private Provincia provincia;

    @OneToMany(mappedBy = "sectorProvincial", fetch = FetchType.LAZY)
    @Getter
    private List<SectorMunicipal> sectoresMunicipales;

    public SectorProvincial() {
        this.sectoresMunicipales = new ArrayList<>();
        this.huellasDeCarbono = new ArrayList<>();
    }

    public void agregarSectorMunicipal(SectorMunicipal sectorMunicipal) {
        this.sectoresMunicipales.add(sectorMunicipal);
    }

    @Override
    public String getNombre() {
        return provincia.getNombre();
    }

    @Override
    public Map<String, Double> composicion(YearMonth fecha) {
        double huellaTotalSector = calcularHuellaTotalDesde(fecha);
        Map<String, Double> composicionPorSector = new HashMap<>();
        sectoresMunicipales.forEach(s ->
                composicionPorSector.put(s.getNombre(), s.calcularHuellaTotalDesde(fecha) / huellaTotalSector * 100)
        );
        return composicionPorSector;
    }
}