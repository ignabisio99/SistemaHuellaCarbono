package domain.entities.reportes;

import domain.entities.actores.Actor;
import lombok.Setter;

import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class Reporte {
    @Setter
    protected YearMonth fecha;

    protected double obtenerPorcentaje(double valor, double HCTotal) {
        return valor/HCTotal * 100;
    }

    public double calcularHCTotal(Actor entidad) {
        return entidad.calcularHuellaTotalDesde(fecha);
    }

    public Map<String, Double> evolucionDeHCAnual(Actor sector) {
        Map<String, Double> evolucionAnual = new HashMap<>();
        sector.huellasAnualesDesde(fecha.getYear()).forEach(h -> evolucionAnual.put(
                Year.of(h.getYear()).toString(),
                h.getValor()
        ));
        return evolucionAnual;
    }

    public Map<String, Double> evolucionDeHCMensual(Actor sector) {
        Map<String, Double> evolucionMensual = new HashMap<>();
        sector.huellasMensualesDesde(fecha).forEach(h -> evolucionMensual.put(
                h.getPerDeImputacion().format(DateTimeFormatter.ofPattern("MM/yyyy")),
                h.getValor()
        ));
        return evolucionMensual;
    }
}
