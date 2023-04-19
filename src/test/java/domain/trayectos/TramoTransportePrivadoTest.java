package domain.trayectos;

import domain.BaseTest;
import domain.entities.actores.miembros.Miembro;

import domain.entities.transporte.vehiculosPrivados.TransportePrivado;
import domain.entities.trayectos.TramoTransportePrivado;
import domain.entities.trayectos.Trayecto;
import org.junit.Assert;
import org.junit.Test;

import java.time.YearMonth;

import static domain.entities.transporte.vehiculosPrivados.CriterioDeVehiculo.BICICLETA_O_PIE;
import static domain.entities.transporte.vehiculosPrivados.TipoVehiculo.AUTOMOVIL;

public class TramoTransportePrivadoTest extends BaseTest {
    private YearMonth fecha = YearMonth.of(2020, 7);

    @Test
    public void geoDdsServiceProveeDistanciaTest() {
        Assert.assertEquals(123456, servicioGeodds.obtenerDistancia(direccion1, direccion2).getValor(), 0);
    }

    @Test
    public void trayectoMiembro() {
        miembroA.agregarTrayecto(
                organizacionA,
                new Trayecto(
                    fecha,
                    new TramoTransportePrivado(new TransportePrivado("BICICLETA_O_PIE"), direccion1, direccion2),
                    new TramoTransportePrivado(new TransportePrivado("AUTOMOVIL"), direccion3, direccion4)
                )
        );

        Assert.assertEquals(7890 * 0.4, miembroA.calcularHuellaMensual(2020, 7), 0);
    }

    @Test
    public void tramosCompartidos() {
        Miembro otroMiembro = new Miembro();
        otroMiembro.solicitarVinculacionConOrg(organizacionA, sectorA);
        organizacionA.aceptarVinculacion(otroMiembro);

        miembroA.agregarTrayecto(
                organizacionA,
                new Trayecto(
                    fecha,
                    new TramoTransportePrivado(auto, direccion1, direccion2),
                    new TramoTransportePrivado(auto, direccion2, direccion3, fecha, otroMiembro.getMiembroPorOrganizacion(organizacionA))
                )
        );

        Assert.assertEquals(
                0.4 * (123456 + 7890),
                miembroA.calcularHuellaMensual(2020, 7),
                0
        );

        Assert.assertEquals(
                7890 * 0.4,
                otroMiembro.calcularHuellaMensual(fecha.getYear(), fecha.getMonthValue()),
                0
        );

        Assert.assertEquals(
                0.4 * (123456 + 7890) + HC2020,
                organizacionA.calcularHuellaAnual(2020),
                0
        );
    }
}