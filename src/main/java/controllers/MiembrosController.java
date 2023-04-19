package controllers;

import domain.entities.actores.miembros.Miembro;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.transporte.transportePublico.Parada;
import domain.entities.transporte.transportePublico.TipoDeTransportePublico;
import domain.entities.transporte.transportePublico.TransportePublico;
import domain.entities.transporte.vehiculosPrivados.TipoCombustible;
import domain.entities.transporte.vehiculosPrivados.TipoVehiculo;
import domain.entities.transporte.vehiculosPrivados.TransportePrivado;
import domain.entities.trayectos.Tramo;
import domain.entities.trayectos.TramoTransportePrivado;
import domain.entities.trayectos.TramoTransportePublico;
import domain.entities.trayectos.Trayecto;
import domain.entities.ubicaciones.Direccion;
import domain.entities.ubicaciones.Localidad;
import domain.entities.usuarios.Usuario;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

// CARGAR TRAYECTOS, VINCULAR CON ORGANIZACION, CALCULAR SU HC,
// VER OTROS REPORTES, CREAR REPORTE, VER PERFIL, CREAR PERFIL(?),
// MODIFICAR PERFIL(?), VER GUIA DE RECOMENDACIONES.

public class MiembrosController {
    private Repositorio<Miembro> repoMiembro;
    private Repositorio<Organizacion> repoOrganizacion;
    private Repositorio<Usuario> repoUsuario;
    private Repositorio<Localidad> repoLocalidad;
    private Repositorio<TransportePublico> repoTP;
    private Repositorio<Parada> repoParadas;
    private List<Tramo> tramos;

    public MiembrosController(){
        this.repoMiembro = FactoryRepositorio.get(Miembro.class);
        this.repoOrganizacion = FactoryRepositorio.get(Organizacion.class);
        this.repoUsuario = FactoryRepositorio.get(Usuario.class);
        this.repoLocalidad = FactoryRepositorio.get(Localidad.class);
        this.repoTP = FactoryRepositorio.get(TransportePublico.class);
        this.repoParadas = FactoryRepositorio.get(Parada.class);
        this.tramos = new ArrayList<>();
    }

    public Miembro buscarMiembroDesdeUsuario(Request request){
        return (Miembro) repoUsuario.buscar(request.session().attribute("id")).getActor();
    }

    public ModelAndView registrarTrayecto(Request request, Response response) {
        Map<String, Object> map = new HashMap<>();
        List<Organizacion.OrganizacionDTO> organizaciones =
                buscarMiembroDesdeUsuario(request).convertirADTO().obtenerOrganizaciones();

        map.put("organizaciones", organizaciones);
        map.put("transportes", new ArrayList<String>(){{
            add("Vehículo particular");
            add("Servicio contratado");
            add("Transporte público");
            add("Bicicleta o a pie");
        }});

        List<Localidad> localidades = repoLocalidad.buscarTodos();
        map.put("localidades", localidades);

        map.put("tipoVehiculo", Arrays.stream(TipoVehiculo.values()).map(Enum::toString).collect(Collectors.toList()));
        map.put("tipoCombustible", Arrays.stream(TipoCombustible.values()).map(Enum::toString).collect(Collectors.toList()));
        map.put("tipoTP", Arrays.stream(TipoDeTransportePublico.values()).map(Enum::toString).collect(Collectors.toList()));
        map.put("transportesPublicos",
                repoTP.buscarTodos().stream()
                        .map(TransportePublico::convertirADTO)
                        .collect(Collectors.toList())
        );

        return new ModelAndView(map, "miembros/registrarTrayecto.hbs");
    }

    public Response registrarTramo(Request request, Response response) {

        if(request.queryParams("transporte").equals("2")) {
            tramos.add(new TramoTransportePublico(
                    repoTP.buscar(new Integer(request.queryParams("transporte_publico"))),
                    repoParadas.buscar(new Integer(request.queryParams("parada_origen"))),
                    repoParadas.buscar(new Integer(request.queryParams("parada_destino")))
            ));
        }

        else {
            Localidad localidadOrigen, localidadDestino;
            Direccion direccionOrigen = null, direccionDestino = null;
            TransportePrivado transportePrivado = null;

            switch (request.queryParams("transporte")) {
                case "0":
                    localidadOrigen = repoLocalidad.buscar(new Integer(request.queryParams("localidad_origen_auto")));
                    localidadDestino = repoLocalidad.buscar(new Integer(request.queryParams("localidad_destino_auto")));

                    direccionOrigen = new Direccion(localidadOrigen, request.queryParams("calle_origen_auto"), request.queryParams("altura_origen_auto"));
                    direccionDestino = new Direccion(localidadDestino, request.queryParams("calle_destino_auto"), request.queryParams("altura_destino_auto"));

                    transportePrivado = new TransportePrivado(
                            "PARTICULAR",
                            request.queryParams("tipo_vehiculo"),
                            request.queryParams("tipo_combustible")
                    );

                    System.out.println("hola hola hola holaaaaaaaaaaaaaaaaaa");

                    break;

                case "1":
                    localidadOrigen = repoLocalidad.buscar(new Integer(request.queryParams("localidad_origen_servicio")));
                    localidadDestino = repoLocalidad.buscar(new Integer(request.queryParams("localidad_destino_servicio")));

                    direccionOrigen = new Direccion(localidadOrigen, request.queryParams("calle_origen_servicio"), request.queryParams("altura_origen_servicio"));
                    direccionDestino = new Direccion(localidadDestino, request.queryParams("calle_destino_servicio"), request.queryParams("altura_destino_servicio"));

                    transportePrivado = new TransportePrivado(
                            "SERVICIO_CONTRATADO",
                            request.queryParams("servicio_contratado")
                    );
                    break;

                case "3":
                    localidadOrigen = repoLocalidad.buscar(new Integer(request.queryParams("localidad_origen_bici")));
                    localidadDestino = repoLocalidad.buscar(new Integer(request.queryParams("localidad_destino_bici")));

                    direccionOrigen = new Direccion(localidadOrigen, request.queryParams("calle_origen_bici"), request.queryParams("altura_origen_bici"));
                    direccionDestino = new Direccion(localidadDestino, request.queryParams("calle_destino_bici"), request.queryParams("altura_destino_bici"));

                    transportePrivado = new TransportePrivado("BICICLETA_O_PIE");
                    break;
            }

            tramos.add(new TramoTransportePrivado(transportePrivado, direccionOrigen, direccionDestino));
        }

        response.redirect("/miembro/registrarTrayecto");
        return response;
    }

    public Response agregarTrayecto(Request request, Response response) {
        Miembro miembro = buscarMiembroDesdeUsuario(request);
        miembro.agregarTrayecto(
                repoOrganizacion.buscar(new Integer(request.queryParams("organizacion"))),
                new Trayecto(YearMonth.now(), tramos)
        );
        repoMiembro.modificar(miembro);
        System.out.println("TRAMOS: " + tramos.size());
        tramos.clear();
        response.redirect("/miembro/registrarTrayecto");
        return response;
    }


    public ModelAndView calcularHuella(Request request, Response response) {
        return new ModelAndView(null, "miembros/calcularHuella.hbs");
    }

    public ModelAndView calcularHuellaMensual(Request request, Response response) {
        Miembro miembro = buscarMiembroDesdeUsuario(request);
        HashMap<String, Object> map = new HashMap<>();
        switch(request.queryParams("tipo_huella")){
            case "Mensual": map.put("huella", miembro.calcularHuellaMensual(new Integer(request.queryParams("anio")), new Integer(request.queryParams("mes"))));
                break;
            case "Anual": map.put("huella", miembro.calcularHuellaAnual(new Integer(request.queryParams("anio"))));
        }
        return new ModelAndView(map, "miembros/visualizarHuella.hbs");
    }
}