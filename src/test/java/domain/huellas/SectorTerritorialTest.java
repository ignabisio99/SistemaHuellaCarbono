package domain.huellas;

import domain.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class SectorTerritorialTest extends BaseTest {
    @Test
    public void huellaAdolfoAlsina2020() {
        Assert.assertEquals(organizacionA.calcularHuellaAnual(2020), sectorAdolfoAlsina.calcularHuellaAnual(2020), 0);
    }

    @Test
    public void huellaAdolfoAlsina2021() {
        Assert.assertEquals(organizacionA.calcularHuellaAnual(2021), sectorAdolfoAlsina.calcularHuellaAnual(2021), 0);
    }

    @Test
    public void huellaMarChiquita2020() {
        Assert.assertEquals(organizacionB.calcularHuellaAnual(2020), sectorMarChiquita.calcularHuellaAnual(2020), 0);
    }

    @Test
    public void huellaMarChiquita2021() {
        Assert.assertEquals(organizacionB.calcularHuellaAnual(2021), sectorMarChiquita.calcularHuellaAnual(2021), 0);
    }

    @Test
    public void huellaSectorProvincial2020() {
        Assert.assertEquals(
                sectorAdolfoAlsina.calcularHuellaAnual(2020) + sectorMarChiquita.calcularHuellaAnual(2020),
                sectorBuenosAires.calcularHuellaAnual(2020),
                0
        );
    }

    @Test
    public void huellaSectorProvincial2021() {
        Assert.assertEquals(
                sectorAdolfoAlsina.calcularHuellaAnual(2021) + sectorMarChiquita.calcularHuellaAnual(2021),
                sectorBuenosAires.calcularHuellaAnual(2021),
                0
        );
    }
}
