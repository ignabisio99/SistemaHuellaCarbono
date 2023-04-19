package domain.entities.reportes;

import domain.entities.actores.Actor;
import domain.entities.actores.sectores.SectorMunicipal;
import domain.entities.actores.sectores.SectorProvincial;
import domain.entities.actores.sectores.SectorTerritorial;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReporteSectorTerritorial extends Reporte {
    private SectorTerritorial sectorTerritorial;

    public ReporteSectorTerritorial(int anio, int mes, SectorTerritorial sectorTerritorial) {
        this.fecha = YearMonth.of(anio, mes);
        this.sectorTerritorial = sectorTerritorial;
    }

    // HC total por sector territorial

    public double calcularHCTotal() {
        return super.calcularHCTotal(sectorTerritorial);
    }

    // Composición de HC total de un determinado sector territorial

    public Map<String, Double> composicionDeHCPorSector() {
        return sectorTerritorial.composicion(fecha);
    }

    // Composición de HC total a nivel país (discriminando provincias)

    public Map<String, Double> composicionDeHCNivelPaisPorProvincia(List<SectorProvincial> sectoresProvinciales) {
        double huellaNivelPais = sectoresProvinciales.stream().mapToDouble(s -> s.calcularHuellaTotalDesde(fecha)).sum();
        Map<String, Double> composicionPorSector = new HashMap<>();
        sectoresProvinciales.forEach(s -> composicionPorSector.put(s.getNombre(), obtenerPorcentaje(s.calcularHuellaTotalDesde(fecha), huellaNivelPais)));
        return composicionPorSector;
    }

    // Composición de HC total a nivel país (discriminando municipios)

    public Map<String, Double> composicionDeHCNivelPaisPorMunicipio(List<SectorProvincial> sectoresProvinciales) {
        List<SectorMunicipal> sectoresMunicipales = sectoresProvinciales.stream().flatMap(s -> s.getSectoresMunicipales().stream()).collect(Collectors.toList());
        double huellaNivelPais = sectoresMunicipales.stream().mapToDouble(s -> s.calcularHuellaTotalDesde(fecha)).sum();
        Map<String, Double> composicionPorSector = new HashMap<>();
        sectoresMunicipales.forEach(s -> composicionPorSector.put(s.getNombre(), obtenerPorcentaje(s.calcularHuellaTotalDesde(fecha), huellaNivelPais)));
        return composicionPorSector;
    }

    // Evolución de HC total de un determinado sector territorial

    public Map<String, Double> evolucionDeHCAnual() {
        return super.evolucionDeHCAnual(sectorTerritorial);
    }

    public Map<String, Double> evolucionDeHCMensual() {
        return super.evolucionDeHCMensual(sectorTerritorial);
    }
}
