package domain.validador;

import org.junit.Assert;
import org.junit.Test;

public class ContrasenaSeguraTest extends BaseTest {
    @Test
    public void testNoTieneNumeros() {
        this.setContrasena("UTNfrbaDdS");
        Assert.assertFalse(validador.esSegura(contrasena));
    }

    @Test
    public void testNoTieneMinusculas() {
        this.setContrasena("UTNFRBA2022");
        Assert.assertFalse(validador.esSegura(contrasena));
    }

    @Test
    public void testNoTieneMayusculas() {
        this.setContrasena("utnfrba2022");
        Assert.assertFalse(validador.esSegura(contrasena));
    }

    @Test
    public void testEstaEnElTop() {
        this.setContrasena("1Qaz2wSx");
        Assert.assertFalse(validador.esSegura(contrasena));
    }

    @Test
    public void testLongitudMenorAOcho() {
        this.setContrasena("UTNba22");
        Assert.assertFalse(validador.esSegura(contrasena));
    }

    @Test
    public void testSegura() {
        this.setContrasena("UTNfrba2022");
        Assert.assertTrue(validador.esSegura(contrasena));
        System.out.println("Contraseña segura.");
    }
}
