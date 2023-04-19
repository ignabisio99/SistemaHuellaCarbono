package domain;

import domain.entities.huellaDeCarbono.FactorDeEmision;
import domain.entities.importacionDeDatos.LectorExcel;
import domain.entities.importacionDeDatos.consumos.TipoDeConsumo;
import domain.entities.actores.miembros.Miembro;
import domain.entities.actores.miembros.TipoDeDocumento;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.actores.organizaciones.Sector;
import domain.entities.actores.organizaciones.TipoDeOrganizacion;
import domain.entities.services.geodds.ServicioDeUbicaciones;
import domain.entities.services.geodds.adapters.AdapterServicio;
import domain.entities.trayectos.Distancia;
import domain.entities.ubicaciones.Localidad;
import domain.entities.ubicaciones.Municipio;
import domain.entities.ubicaciones.Provincia;
import domain.entities.transporte.transportePublico.Parada;
import domain.entities.transporte.transportePublico.TransportePublico;
import domain.entities.transporte.vehiculosPrivados.CriterioDeVehiculo;
import domain.entities.transporte.vehiculosPrivados.TipoCombustible;
import domain.entities.transporte.vehiculosPrivados.TransportePrivado;
import domain.entities.ubicaciones.Direccion;
import domain.entities.actores.sectores.SectorMunicipal;
import domain.entities.actores.sectores.SectorProvincial;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseTest {
    protected TransportePublico subteC, subteD, colectivo64A, colectivo114;
    protected TransportePrivado auto;
    protected Organizacion organizacionA, organizacionB;
    protected Sector sectorA, sectorB;
    protected Miembro miembroA, miembroB, miembroC;
    protected Direccion direccion1, direccion2, direccion3, direccion4;
    protected Municipio marChiquita, adolfoAlsina;
    protected Provincia buenosAires;
    protected SectorMunicipal sectorMarChiquita, sectorAdolfoAlsina;
    protected SectorProvincial sectorBuenosAires;
    protected Localidad santaClara, carhue;
    protected ServicioDeUbicaciones servicioGeodds;
    protected double HC2020, HC2021;
    protected Parada diagonalNorte, retiro, nueveDeJulio, bulnes, santaFe1, luisMariaCampos, olleros, palermo;

    @Before
    public void initOrganizaciones() {
        initUbicaciones();
        initMiembros();
        initServicioMock();

        sectorA = new Sector("Sector A");
        sectorB = new Sector("Sector B");

        organizacionA = new Organizacion("Organizacion A", TipoDeOrganizacion.EMPRESA, "mediana");
        organizacionA.agregarSectores(sectorA);
        organizacionA.setDireccion(direccion1);

        organizacionB = new Organizacion("Organizacion B", TipoDeOrganizacion.EMPRESA, "mediana");
        organizacionB.agregarSectores(sectorB);
        organizacionB.setDireccion(direccion4);

        miembroA.solicitarVinculacionConOrg(organizacionA, sectorA);
        organizacionA.aceptarVinculacion(miembroA);

        miembroB.solicitarVinculacionConOrg(organizacionB, sectorB);
        organizacionB.aceptarVinculacion(miembroB);

        miembroC.solicitarVinculacionConOrg(organizacionA, sectorA);
        organizacionA.aceptarVinculacion(miembroC);

        initExcel();

        HC2020 = 324.41 * 0 + 46.2 * 0.1 + 46534.0 * 0.2 + 64.57 * 0.3 + 574.35 * 0.4 + 746536.00 * 0.1 + 352.35 * 45425.23 * 1.0 * 1.0;
        HC2021 = 6453.46 * 0.8 + 352.35 * 45425.23 * 1.0 * 1.0;
    }

    public void initServicioMock() {
        AdapterServicio adapterMock = mock(AdapterServicio.class);
        servicioGeodds = ServicioDeUbicaciones.getInstancia();
        servicioGeodds.setAdapter(adapterMock);

        Distancia distancia1 = new Distancia(123456, "KM");
        Distancia distancia2 = new Distancia(7890, "KM");

        when(adapterMock.distancia(direccion1, direccion2)).thenReturn(distancia1);
        when(adapterMock.distancia(direccion2, direccion3)).thenReturn(distancia2);
        when(adapterMock.distancia(direccion3, direccion4)).thenReturn(distancia2);
    }

    public void initUbicaciones() {
        buenosAires = new Provincia(168, "BUENOS AIRES");
        sectorBuenosAires = buenosAires.getSectorProvincial();

        marChiquita = new Municipio(445, "MAR CHIQUITA");
        marChiquita.setProvincia(buenosAires);
        sectorMarChiquita = marChiquita.getSectorMunicipal();

        adolfoAlsina = new Municipio(330, "ADOLFO ALSINA");
        adolfoAlsina.setProvincia(buenosAires);
        sectorAdolfoAlsina = adolfoAlsina.getSectorMunicipal();

        santaClara = new Localidad(457, "SANTA CLARA DEL MAR", 7609);
        santaClara.setMunicipio(marChiquita);

        carhue = new Localidad(3279, "CARHUE", 6430);
        carhue.setMunicipio(adolfoAlsina);

        direccion1 = new Direccion(carhue, "Italia", "300");
        direccion2 = new Direccion(santaClara, "San Raimundo", "67");
        direccion3 = new Direccion(carhue, "maipu", "100");
        direccion4 = new Direccion(santaClara, "O'Higgins", "200");
    }

    public void initMiembros() {
        miembroA = new Miembro();
        miembroA.setNombreYApellido("Adrián", "Agüero");
        miembroA.setTipoDeDocumento(TipoDeDocumento.DNI);
        miembroA.setNumeroDocumento(42658789);
        miembroA.setDireccion(direccion1);

        miembroB = new Miembro();
        miembroB.setNombreYApellido("Bartolomé", "Balcarce");
        miembroB.setTipoDeDocumento(TipoDeDocumento.DNI);
        miembroB.setNumeroDocumento(43657124);
        miembroB.setDireccion(direccion2);

        miembroC = new Miembro();
        miembroC.setNombreYApellido("Camilo", "Cáceres");
        miembroC.setTipoDeDocumento(TipoDeDocumento.DNI);
        miembroC.setNumeroDocumento(41567321);
        miembroC.setDireccion(direccion3);
    }

    public void initExcel() {
        List<FactorDeEmision> factores = new ArrayList<>();
        for(int i = 0; i < TipoDeConsumo.values().length; i++) {
            TipoDeConsumo tipoDeConsumo = TipoDeConsumo.values()[i];
            switch (TipoDeConsumo.values()[i]) {
                case DISTANCIA: case CATEGORIA: case PESO:
                    break;
                default:
                    factores.add(new FactorDeEmision(tipoDeConsumo, 0.1 * i));
            }
        }
        LectorExcel.setFactoresDeEmision(factores);

        String path = "cargaDeDatos/datosDeActividades2.xlsx";

        LectorExcel lectorExcel = new LectorExcel();
        lectorExcel.cargarExcel(organizacionA, path);
        lectorExcel.cargarExcel(organizacionB, path);
    }

    @Before
    public void initMediosDeTransporte() {
        auto = new TransportePrivado("AUTOMOVIL");
        auto.setCriterioDeVehiculo(CriterioDeVehiculo.PARTICULAR);
        auto.setTipoCombustible(TipoCombustible.GASOIL);

        retiro = new Parada("Retiro", 1);
        diagonalNorte = new Parada("Diagonal norte", 4);

        nueveDeJulio = new Parada("Nueve de julio", 2);
        bulnes = new Parada("Bulnes", 8);
        olleros = new Parada("Olleros", 13);
        palermo = new Parada("Palermo", 11);

        santaFe1 = new Parada("Avenida Santa Fe1", 6);
        luisMariaCampos = new Parada("Avenida Luis María Campos", 9);

        subteC = new TransportePublico("SUBTE", "C",
            new ArrayList<Parada>() {{
                add(retiro);
                add(new Parada("General San Martín", 2));
                add(new Parada("Lavalle", 3));
                add(diagonalNorte);
                add(new Parada("Avenida de mayo", 5));
                add(new Parada("Moreno", 6));
                add(new Parada("Independencia", 7));
                add(new Parada("San Juan", 8));
                add(new Parada("Constitución", 9));
            }}
        );

        subteD = new TransportePublico("SUBTE", "D",
            new ArrayList<Parada>() {{
                add(new Parada("Catedral", 1));
                add(nueveDeJulio);
                add(new Parada("Tribunales", 3));
                add(new Parada("Callao", 4));
                add(new Parada("Facultad de medicina", 5));
                add(new Parada("Pueyrredón", 6));
                add(new Parada("Agüero", 7));
                add(bulnes);
                add(new Parada("Scalabrini Ortiz", 9));
                add(new Parada("Plaza Italia", 10));
                add(palermo);
                add(new Parada("Ministro Carranza", 12));
                add(olleros);
                add(new Parada("José Hernández", 14));
                add(new Parada("Juramento", 15));
                add(new Parada("Congreso de Tucumán", 16));
            }}
        );

        colectivo64A = new TransportePublico("COLECTIVO", "64A",
            new ArrayList<Parada>() {{
                add(new Parada("Avenida de Mayo", 1));
                add(new Parada("Avenida Rivadavia", 2));
                add(new Parada("Avenida Pueyrredón", 3));
                add(new Parada("Beruti", 4));
                add(new Parada("Ecuador", 5));
                add(santaFe1);
                add(new Parada("Plaza Italia", 7));
                add(new Parada("Avenida Santa Fe2", 8));
                add(luisMariaCampos);
            }}
        );

        colectivo114 = new TransportePublico("COLECTIVO", "114",
                new ArrayList<Parada>() {{
                    add(new Parada("UTN Campus", 11.5));
                    add(new Parada("UTN Medrano", 0));
                }}
        );
    }
}
