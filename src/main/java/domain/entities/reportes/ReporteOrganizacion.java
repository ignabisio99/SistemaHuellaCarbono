package domain.entities.reportes;

import domain.entities.importacionDeDatos.actividades.Actividad;
import domain.entities.importacionDeDatos.actividades.TipoDeActividad;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.actores.organizaciones.TipoDeOrganizacion;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteOrganizacion extends Reporte {
    private Organizacion organizacion;
    private double huellaTotalOrganizacion;

    public ReporteOrganizacion(int anio, int mes, Organizacion organizacion) {
        this.fecha = YearMonth.of(anio, mes);
        this.organizacion = organizacion;
        this.huellaTotalOrganizacion = organizacion.calcularHuellaTotalDesde(fecha);
    }

    // HC total por tipo de Organización (según la clasificación de la Organización)

    public double calcularHCTotalPorCategoria(List<Organizacion> organizaciones) {
        return organizaciones.stream()
                .filter(o -> o.esDelTipo(organizacion.getTipoDeOrganizacion()))
                .mapToDouble(o -> o.calcularHuellaTotalDesde(fecha)).sum();
    }

    // Composición de HC total de una determinada Organización

    public Map<String, Double> composicionDeHCPorAct() {
        Map<String, Double> coleccionDeAct = new HashMap<>();
        organizacion.getListadoDeImportaciones()
                .forEach(a ->
                        coleccionDeAct.put(a.getTipoDeActividad().toString(),
                                obtenerPorcentaje(a.getValorHC(), huellaTotalOrganizacion))
                );
        return coleccionDeAct;
    }

    public Map<String, Double> composicionDeHCPorTipoDeAct() {
        List<Actividad> actividades = organizacion.getListadoDeImportaciones();
        Map<String, Double> coleccionDeAct = new HashMap<>();

        for(TipoDeActividad tipoDeActividad : TipoDeActividad.values()) {
            if(tipoDeActividad.equals(TipoDeActividad.TRAYECTO)) continue;
            coleccionDeAct.put(tipoDeActividad.name(), obtenerPorcentaje(
                    actividades.stream()
                            .filter(a -> a.getTipoDeActividad().equals(tipoDeActividad))
                            .mapToDouble(Actividad::getValorHC)
                            .sum(),
                    huellaTotalOrganizacion)
            );
        }

        double porcetajeTrayectos = 100 - coleccionDeAct.values().stream().mapToDouble(Double::doubleValue).sum();
        coleccionDeAct.put(TipoDeActividad.TRAYECTO.toString(), obtenerPorcentaje(porcetajeTrayectos, huellaTotalOrganizacion));

        return coleccionDeAct;
    }

    public Map<String, Double> composicionDeHCPorMiembro() {
        Map<String, Double> composicionPorMiembro = new HashMap<>();
        organizacion.getMiembrosPorOrganizacion().forEach(m -> composicionPorMiembro.put(
                m.getMiembro().convertirADTO().getNombreYApellido(),
                obtenerPorcentaje(m.calcularHuellaTotalDesde(fecha), huellaTotalOrganizacion))
        );
        return composicionPorMiembro;
    }

    // Evolución de HC total de una determinada Organización

    public Map<String, Double> evolucionDeHCAnual() {
        return super.evolucionDeHCAnual(organizacion);
    }

    public Map<String, Double> evolucionDeHCMensual() {
        return super.evolucionDeHCMensual(organizacion);
    }
}
