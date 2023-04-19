package domain.validador;

import org.junit.Assert;
import org.junit.Test;

public class DesactivarValidacionTest extends BaseTest {
    @Test
    public void desactivarTop() {
        validador.eliminarValidacion("top");
        Assert.assertTrue(validador.esSegura("1Qaz2wSx"));
        System.out.println("Contraseña segura.");
    }

    @Test
    public void estaEnElTop() {
        Assert.assertFalse(validador.esSegura("1Qaz2wSx"));
    }
}