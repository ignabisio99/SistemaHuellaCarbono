package controllers;

import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.actores.sectores.SectorProvincial;
import domain.entities.actores.sectores.SectorTerritorial;
import domain.entities.reportes.ReporteSectorTerritorial;
import domain.entities.usuarios.Usuario;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AgenteController {
    private Repositorio<Usuario> repoSector;
    private Repositorio<SectorProvincial> repoProvincias;
    public AgenteController(){
        this.repoSector = FactoryRepositorio.get(Usuario.class);
        repoProvincias = FactoryRepositorio.get(SectorProvincial.class);
    }

    public ModelAndView guiaDeRecomendaciones(Request request, Response response) {
        return new ModelAndView(null, "/agentes/guiaDeRecomendaciones.hbs");
    }

    public SectorTerritorial buscarSectorTerritorial(Request request){
        return (SectorTerritorial) repoSector.buscar(request.session().attribute("id")).getActor();
    }

    public ModelAndView reportes(Request request, Response response){
        return new ModelAndView(null, "/agentes/reportes-agente.hbs");
    }


    public ModelAndView visualizarReportes(Request request, Response response) {
        ReporteSectorTerritorial reporte =new ReporteSectorTerritorial(new Integer(request.queryParams("anio")), new Integer(request.queryParams("mes")), buscarSectorTerritorial(request));
        HashMap<String, Object> map = new HashMap<>();
        switch(request.queryParams("tipo_reporte")){
            case "Composición a nivel país": map.put("reporte", reporte.composicionDeHCNivelPaisPorProvincia(repoProvincias.buscarTodos()));
                map.put("columna1", "Provincia");
                map.put("columna2", "Porcentaje huella");
                break;
            case "Composición por sector": map.put("reporte", reporte.composicionDeHCPorSector());
                map.put("columna1", "Sector");
                map.put("columna2", "Porcentaje huella");
                break;
            case "Huella total": map.put("reporte", new HashMap<String,Double>(){{put(buscarSectorTerritorial(request).getNombre(),reporte.calcularHCTotal());}});
                map.put("columna1", "Sector");
                map.put("columna2", "Huella");
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
        return new ModelAndView(map, "agentes/visualizarReportes.hbs");
    }
    //VER REPORTES, VER GUIA DE RECOMENDACION
}




