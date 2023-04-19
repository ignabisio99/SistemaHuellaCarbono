package domain.trayectos;

import domain.BaseTest;
import domain.entities.trayectos.Tramo;
import domain.entities.trayectos.TramoTransportePublico;
import domain.entities.trayectos.Trayecto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.YearMonth;

public class TramoTransportePublicoTest extends BaseTest {
    private Tramo tramo1, tramo1Vuelta, tramo2, tramo3;

    @Before
    public void initTramos() {
        tramo1 = new TramoTransportePublico(subteC, retiro, diagonalNorte);
        tramo1Vuelta = new TramoTransportePublico(subteC, diagonalNorte, retiro);
        tramo2 = new TramoTransportePublico(subteD, nueveDeJulio, bulnes);
        tramo3 = new TramoTransportePublico(colectivo64A, santaFe1, luisMariaCampos);
    }

    @Test
    public void crearTrayecto() {
        miembroA.agregarTrayecto(organizacionA, new Trayecto(YearMonth.of(2020, 8), tramo1, tramo2, tramo3));
        Assert.assertEquals(tramo1.getValorHC() + tramo2.getValorHC() + tramo3.getValorHC(),
                miembroA.calcularHuellaMensual(2020, 8), 0);
    }

    @Test
    public void idaYVuelta() {
        Assert.assertEquals(tramo1.getValorHC(), tramo1Vuelta.getValorHC(), 0);
    }
}
