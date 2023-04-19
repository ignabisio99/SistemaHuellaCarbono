package domain.huellas;

import domain.BaseTest;
import domain.entities.trayectos.Tramo;
import domain.entities.trayectos.TramoTransportePrivado;
import domain.entities.trayectos.Trayecto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.YearMonth;

public class CalculoHuellaTest extends BaseTest {
    private YearMonth fechaA = YearMonth.now().withYear(2020);

    @Before
    public void initTrayectos() {
        Tramo tramoMiembroA = new TramoTransportePrivado(auto, direccion1, direccion2);

        miembroA.agregarTrayecto(organizacionA, new Trayecto(fechaA, tramoMiembroA));
        miembroA.agregarTrayecto(organizacionA, new Trayecto(YearMonth.of(2021, 7), tramoMiembroA));

        Tramo tramoCompartido = new TramoTransportePrivado(
                auto, direccion1, direccion2, fechaA, miembroA.getMiembroPorOrganizacion(organizacionA)
        );

        miembroC.agregarTrayecto(organizacionA, new Trayecto(fechaA, tramoCompartido));

        miembroB.agregarTrayecto(organizacionB, new Trayecto(
                        YearMonth.of(2021, 8),
                        new TramoTransportePrivado(auto, direccion3, direccion4))
        );
    }

    @Test
    public void calcularHCMiembroA() {
        Assert.assertEquals(2 * 123456 * 0.4, miembroA.calcularHuellaAnual(2020), 0);
    }

    @Test
    public void calcularHCMiembroC() {
        Assert.assertEquals(123456 * 0.4, miembroC.calcularHuellaAnual(2020), 0);
    }

    @Test
    public void calcularHCOrganizacionAnual() {
        Assert.assertEquals(HC2020 + 2 * 123456 * 0.4, organizacionA.calcularHuellaAnual(2020), 0);
    }

    @Test
    public void calcularHCOrganizacionMensual() {
        Assert.assertEquals(648.74 * 0.6 + 7890 * 0.4, organizacionB.calcularHuellaMensual(2021, 8), 0);
    }

    @Test
    public void impactoMiembroEnOrganizacion() {
        Assert.assertEquals(
                7890 * 0.4 / (648.74 * 0.6 + 7890 * 0.4),
                miembroB.impactoSobreElTotalDeOrganizacionMensual(organizacionB, 2021, 8),
                0
        );
    }

    @Test
    public void indicadorPorSector() {
        Assert.assertEquals(
                7890 * 0.4,
                organizacionB.indicadorHCPorSectorMensual(sectorB,2021, 8),
                0
        );
    }
}
