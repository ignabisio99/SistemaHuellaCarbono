package domain.validador;

import domain.entities.validador.Validacion;
import domain.entities.validador.Validador;
import domain.entities.validador.validaciones.*;
import org.junit.After;
import org.junit.Before;

import java.util.HashMap;
import java.util.Objects;

public class BaseTest {
    protected Validador validador = new Validador();
    protected String contrasena;

    @Before
    public void inicializar() {
        validador.agregarValidaciones(new HashMap<String, Validacion>() {{
            put("mayus", new AlMenosUnaMayuscula());
            put("minus", new AlMenosUnaMinuscula());
            put("nro", new AlMenosUnNumero());
            put("top", new NoEstaEnElTop10000());
            put("long", new LongitudMayorIgualAOcho());
        }});
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
        System.out.println(contrasena);
    }

    @After
    public void mensajeDeError() {
        String mensajeDeError = validador.mensajeDeError();
        if(!Objects.equals(mensajeDeError, ""))
            System.out.println(mensajeDeError + "\n");
    }
}
