package domain.huellas;

import domain.BaseTest;
import domain.entities.huellaDeCarbono.HuellaDeCarbono;
import domain.entities.reportes.ReporteOrganizacion;
import domain.entities.reportes.ReporteSectorTerritorial;
import domain.entities.trayectos.TramoTransportePrivado;
import domain.entities.trayectos.Trayecto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class ReportesTest extends BaseTest {
    private ReporteOrganizacion reporteOrganizacion;
    private ReporteSectorTerritorial reporteSectorAdolfoAlsina;
    private Map<String, Double> composicionPorSector;

    @Before
    public void initReporte() {
        reporteOrganizacion = new ReporteOrganizacion(2020, 1, organizacionA);
        reporteSectorAdolfoAlsina = new ReporteSectorTerritorial(2020, 1, sectorAdolfoAlsina);
        composicionPorSector = new ReporteSectorTerritorial(2020, 1, sectorBuenosAires).composicionDeHCPorSector();
    }

    @Test
    public void huellaPorTipoDeOrganizacion() {
        reporteOrganizacion.setFecha(YearMonth.of(2022, 1));
        Assert.assertEquals(
                organizacionA.calcularHuellaMensual(2022, 1) + organizacionB.calcularHuellaMensual(2022, 1),
                reporteOrganizacion.calcularHCTotalPorCategoria(Arrays.asList(organizacionA, organizacionB)),
                0
        );
    }

    @Test
    public void composicionOrganizacionPorSector() {
        Map<String, Double> composicion = reporteSectorAdolfoAlsina.composicionDeHCPorSector();
        Assert.assertEquals(100.0, composicion.get(organizacionA.convertirADTO().getNombre()), 0);
    }

    @Test
    public void porcentajePorSectorMunicipal() {
        Assert.assertEquals(0, composicionPorSector.get(sectorAdolfoAlsina.getNombre()) - composicionPorSector.get(sectorMarChiquita.getNombre()), 0);
    }

    @Test
    public void composicionANivelPais() {
        Assert.assertEquals(100.0, reporteSectorAdolfoAlsina.composicionDeHCNivelPaisPorProvincia(Collections.singletonList(sectorBuenosAires)).get(sectorBuenosAires.getNombre()), 0);
    }

    @Test
    public void composicionPorActividad() {
        Map<String, Double> composicionPorActividad = reporteOrganizacion.composicionDeHCPorAct();
        composicionPorActividad.forEach((a, p) -> System.out.printf(a + ": %.2f%%\n", p));
        Assert.assertEquals(100.0, composicionPorActividad.values().stream().mapToDouble(Double::doubleValue).sum(), 0);
    }

    @Test
    public void composicionPorTipoDeActividad() {
        Map<String, Double> composicionPorTipoDeActividad = reporteOrganizacion.composicionDeHCPorTipoDeAct();
        composicionPorTipoDeActividad.forEach((a, p) -> System.out.printf(a + ": %.2f%%\n", p));
        Assert.assertEquals(100.0, composicionPorTipoDeActividad.values().stream().mapToDouble(Double::doubleValue).sum(), 0);
    }

    @Test
    public void composicionPorTipoDeActividadConTrayectos() {
        miembroA.agregarTrayecto(organizacionA, new Trayecto(YearMonth.now().withYear(2020), new TramoTransportePrivado(auto, direccion1, direccion2)));
        miembroC.agregarTrayecto(organizacionA, new Trayecto(YearMonth.now().withYear(2020), new TramoTransportePrivado(auto, direccion2, direccion3)));
        Map<String, Double> composicionPorTipoDeActividad = reporteOrganizacion.composicionDeHCPorTipoDeAct();
        composicionPorTipoDeActividad.forEach((a, p) -> System.out.printf(a + ": %.10f%%\n", p));
        Assert.assertEquals(100.0, composicionPorTipoDeActividad.values().stream().mapToDouble(Double::doubleValue).sum(), 0);
    }

    @Test
    public void composicionPorMiembro() {
        miembroA.agregarTrayecto(organizacionA, new Trayecto(YearMonth.now().withYear(2020), new TramoTransportePrivado(auto, direccion1, direccion2)));
        miembroC.agregarTrayecto(organizacionA, new Trayecto(YearMonth.now().withYear(2020), new TramoTransportePrivado(auto, direccion2, direccion3)));
        Map<String, Double> composicionPorMiembro = reporteOrganizacion.composicionDeHCPorMiembro();
        composicionPorMiembro.forEach((m, p) -> System.out.printf(m + ": %.2f%%\n", p));
        Assert.assertTrue(
                composicionPorMiembro.get(miembroA.convertirADTO().getNombreYApellido()) >
                        composicionPorMiembro.get(miembroC.convertirADTO().getNombreYApellido()));
    }

    @Test
    public void evolucionAnualOrganizacion() {
        Map<String, Double> evolucionOrganizacion = reporteOrganizacion.evolucionDeHCAnual(organizacionA);
        evolucionOrganizacion.forEach((a, h) -> System.out.printf("Año " + a + ", huella: %.2f kgCO2eq\n", h));
        double huellaDesde = organizacionA.huellasAnualesDesde(2020).stream().mapToDouble(HuellaDeCarbono::getValor).sum();
        Assert.assertEquals(huellaDesde, evolucionOrganizacion.values().stream().mapToDouble(Double::doubleValue).sum(), 0);
    }

    @Test
    public void evolucionMensualOrganizacion() {
        Map<String, Double> evolucionOrganizacion = reporteOrganizacion.evolucionDeHCMensual(organizacionA);
        evolucionOrganizacion.forEach((f, h) -> System.out.printf("Fecha " + f + ", huella: %.2f kgCO2eq\n", h));
        double huellaDesde = organizacionA.huellasMensualesDesde(YearMonth.of(2020, 1)).stream().mapToDouble(HuellaDeCarbono::getValor).sum();
        Assert.assertEquals(huellaDesde, evolucionOrganizacion.values().stream().mapToDouble(Double::doubleValue).sum(), 0);
    }

    @Test
    public void evolucionSectorMunicipal() {
        Map<String, Double> evolucionSector = reporteSectorAdolfoAlsina.evolucionDeHCAnual(sectorAdolfoAlsina);
        evolucionSector.forEach((a, h) -> System.out.printf("Año " + a + ", huella: %.2f kgCO2eq\n", h));
        double huellaDesde = organizacionA.huellasAnualesDesde(2020).stream().mapToDouble(HuellaDeCarbono::getValor).sum();
        Assert.assertEquals(huellaDesde, evolucionSector.values().stream().mapToDouble(Double::doubleValue).sum(), 0);
    }
}
