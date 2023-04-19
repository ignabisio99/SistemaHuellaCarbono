package domain.servicioExterno;

import domain.entities.ubicaciones.Localidad;
import domain.entities.ubicaciones.Direccion;
import domain.entities.services.geodds.ServicioDeUbicaciones;
import domain.entities.services.geodds.adapters.AdapterServicio;
import domain.entities.trayectos.Distancia;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class GeoDdsMockTest {
    private AdapterServicio adapterMock;
    private ServicioDeUbicaciones servicioGeodds;

    @Before
    public void init() {
        adapterMock = mock(AdapterServicio.class);
        servicioGeodds = ServicioDeUbicaciones.getInstancia();
        servicioGeodds.setAdapter(adapterMock);
    }

    @Test
    public void GeoDdsServiceProveeDistanciaTest() {
        Direccion direccion1 = new Direccion(new Localidad(), "Italia", "300");
        Direccion direccion2 = new Direccion(new Localidad(), "San Raimundo", "67");

        Distancia distancia = new Distancia(123456, "KM");

        when(this.adapterMock.distancia(direccion1, direccion2)).thenReturn(distancia);

        Assert.assertEquals(distancia,
                this.servicioGeodds.obtenerDistancia(direccion1, direccion2));
    }
}
