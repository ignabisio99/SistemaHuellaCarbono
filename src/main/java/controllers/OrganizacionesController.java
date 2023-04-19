package controllers;

import domain.entities.actores.miembros.Miembro;
import domain.entities.actores.miembros.MiembroPorOrganizacion;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.huellaDeCarbono.FactorDeEmision;
import domain.entities.importacionDeDatos.LectorExcel;
import domain.entities.reportes.ReporteOrganizacion;
import domain.entities.reportes.ReporteSectorTerritorial;
import domain.entities.usuarios.Usuario;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class OrganizacionesController {
    // VER PERFIL, MODIFICAR PERFIL, SUBIR MEDICIONES, VER MEDICIONES, VER REPORTES,
    // CALCULAR HUELLA, ACEPTAR MIEMBROS, VER MIEMBROS, AGREGAR SECTOR(?),
    // AGREGAR CONTACTO, VER GUIA DE RECOMENDACIONES.

    private Repositorio<Miembro> repoMiembro;
    private Repositorio<Organizacion> repoOrganizacion;
    private Repositorio<MiembroPorOrganizacion> repoMiembroPorOrg;
    private Repositorio<FactorDeEmision> repoFactores;
    private Repositorio<Usuario> repoUsuario;

    public OrganizacionesController() {
        repoMiembro = FactoryRepositorio.get(Miembro.class);
        repoOrganizacion = FactoryRepositorio.get(Organizacion.class);
        repoMiembroPorOrg = FactoryRepositorio.get(MiembroPorOrganizacion.class);
        repoFactores = FactoryRepositorio.get(FactorDeEmision.class);
        repoUsuario = FactoryRepositorio.get(Usuario.class);
    }

    public ModelAndView guiaDeRecomendaciones(Request request, Response response) {
        return new ModelAndView(null, "/organizaciones/guiaDeRecomendaciones.hbs");
    }

    public Organizacion buscarOrganizacion(Request request) {
        return (Organizacion) repoUsuario.buscar(request.session().attribute("id")).getActor();
    }

    public ModelAndView miembros(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>() {{
            put("miembros", buscarOrganizacion(request).convertirADTO().getMiembros());
        }}, "organizaciones/miembrosOrg.hbs");
    }

    public ModelAndView solicitudes(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>() {{
            put("solicitudes", buscarOrganizacion(request).convertirADTO().getSolicitudes());
        }}, "organizaciones/solicitudesPendientes.hbs");
    }

    private Miembro buscarMiembro(Request request) {
        return repoMiembro.buscar(new Integer(request.params("id")));
    }

    private MiembroPorOrganizacion buscarMiembroPorOrganizacion(Request request) {
        return buscarMiembro(request).getMiembroPorOrganizacion(buscarOrganizacion(request));
    }

    public Response aceptarVinculacion(Request request, Response response) {
        MiembroPorOrganizacion miembroPorOrganizacion = buscarMiembroPorOrganizacion(request);
        miembroPorOrganizacion.aceptar();
        repoMiembroPorOrg.modificar(miembroPorOrganizacion);
        response.redirect("/organizacion/solicitudes");
        return response;
    }

    public Response rechazarVinculacion(Request request, Response response) {
        MiembroPorOrganizacion miembroPorOrganizacion = buscarMiembroPorOrganizacion(request);
        repoMiembroPorOrg.eliminar(miembroPorOrganizacion);
        response.redirect("/organizacion/solicitudes");
        return response;
    }

    public ModelAndView mediciones(Request request, Response response) {
        return new ModelAndView(null, "organizaciones/registroDeMediciones.hbs");
    }

    public ModelAndView pantallaRegistroDeMediciones(Request request, Response response) {
        return new ModelAndView(null, "/organizaciones/registrarNuevaMedicion.hbs");
    }

    public ModelAndView pantallaMediciones(Request request, Response response) {
        return new ModelAndView(new HashMap<String, Object>() {{
            put("actividades", buscarOrganizacion(request).convertirADTO().getListadoDeImportaciones());
        }}, "/organizaciones/vistaMediciones.hbs");
    }

    public Response subirArchivo(Request request, Response response) {
        LectorExcel.setFactoresDeEmision(repoFactores.buscarTodos().stream().filter(f -> f.getTipoDeConsumo()!=null).collect(Collectors.toList()));
        Organizacion organizacion = buscarOrganizacion(request);
        LectorExcel.cargarExcel(organizacion, request);
        repoOrganizacion.modificar(organizacion);
        response.redirect("/organizacion/registrarMediciones");
        return response;
    }

    public ModelAndView calcularHuella(Request request, Response response) {
        return new ModelAndView(null, "organizaciones/calcularHuella.hbs");
    }

    public ModelAndView calcularHuellaMensual(Request request, Response response) {
        Organizacion organizacion = buscarOrganizacion(request);
        HashMap<String, Object> map = new HashMap<>();
        switch(request.queryParams("tipo_huella")){
            case "Mensual": map.put("huella", organizacion.calcularHuellaMensual(new Integer(request.queryParams("anio")), new Integer(request.queryParams("mes"))));
                break;
            case "Anual": map.put("huella", organizacion.calcularHuellaAnual(new Integer(request.queryParams("anio"))));
        }
        return new ModelAndView(map, "organizaciones/visualizarHuella.hbs");
    }

    public ModelAndView reportes(Request request, Response response){
        return new ModelAndView(null, "/organizaciones/reportes-org.hbs");
    }


    public ModelAndView visualizarReportes(Request request, Response response) {
        ReporteOrganizacion reporte =new ReporteOrganizacion(new Integer(request.queryParams("anio")), new Integer(request.queryParams("mes")), buscarOrganizacion(request));
        HashMap<String, Object> map = new HashMap<>();
        switch(request.queryParams("tipo_reporte")){
            case "Calcular HC Total Por Categoria":
                map.put("reporte", new HashMap<String,Double>()
                {{put(buscarOrganizacion(request).getTipoDeOrganizacion().toString(),reporte.calcularHCTotalPorCategoria(repoOrganizacion.buscarTodos()));}});
                map.put("columna1", "Tipo De Organización");
                map.put("columna2", "Huella");
                break;
            case "Composición por Actividad": map.put("reporte", reporte.composicionDeHCPorAct());
                map.put("columna1", "Actividad");
                map.put("columna2", "Porcentaje huella");
                break;
            case "Composición por Tipo de Actividad": map.put("reporte", reporte.composicionDeHCPorTipoDeAct());
                map.put("columna1", "Tipo de Actividad");
                map.put("columna2", "Porcentaje huella");
                break;
            case "Evolución mensual": map.put("reporte", reporte.evolucionDeHCMensual());
                map.put("columna1", "Fecha");
                map.put("columna2", "Huella");
                break;
            case "Evolución anual": map.put("reporte", reporte.evolucionDeHCAnual());
                map.put("columna1", "Año");
                map.put("columna2", "Huella");
                break;
        }
        return new ModelAndView(map, "organizaciones/visualizarReportes.hbs");
    }
}
