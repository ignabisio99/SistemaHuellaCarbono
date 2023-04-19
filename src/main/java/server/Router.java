package server;

import controllers.*;
import domain.entities.helpersUsuario.PermisoHelper;
import domain.entities.usuarios.Permiso;
import middlewares.AuthMiddleware;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import utils.BooleanHelper;
import utils.HandlebarsTemplateEngineBuilder;

public class Router {
    private static HandlebarsTemplateEngine engine;

    private static void initEngine() {
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .build();
    }

    public static void init() {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Router.configure();
    }

    private static void configure(){
        IndexController indexController = new IndexController();

        Spark.path("/index", () ->
                Spark.get("", indexController::pantallaDeInicio, engine)
        );

        LoginController loginController = new LoginController();

        Spark.path("/login", () -> {
            Spark.get("", loginController::pantallaDeLogin, engine);
            Spark.post("", loginController::login);
            Spark.post("/logout", loginController::logout);
        });

        Spark.path("/logout", () -> Spark.get("", loginController::logout));

        CrearUsuarioController crearUsuarioController = new CrearUsuarioController();

        //ya no debería ir no?
        Spark.path("/crearUsuario", () ->
                Spark.get("", crearUsuarioController::pantallaDeRegistro, engine)
        );

        // ---------------------------------------------------------------------//

        ReporteController reporteController = new ReporteController();

        //---------------------------- MIEMBROS ------------------------------//

        MiembrosController miembrosController = new MiembrosController();

        Spark.path("/miembro", () -> {

            Spark.before("/*", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermiso(request, Permiso.MIEMBRO)) {
                    response.redirect("/prohibido"); //no está todavia la pantalla
                    Spark.halt();
                }
            }));

            Spark.get("/registrarTrayecto", miembrosController::registrarTrayecto, engine);
            Spark.post("/registrarTramo", miembrosController::registrarTramo);
            Spark.post("/registrarTrayecto", miembrosController::agregarTrayecto);
            Spark.get("/calcularHuella", miembrosController::calcularHuella,engine);
            Spark.post("/calcularHuella", miembrosController::calcularHuellaMensual,engine);
        });

        //------------------------------- ORGANIZACION ---------------------------//

        OrganizacionesController organizacionesController = new OrganizacionesController();

        Spark.path("/organizacion", () -> {

            Spark.before("/*", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermiso(request, Permiso.ORGANIZACION)){
                    response.redirect("/prohibido"); //no está todavia la pantalla
                    Spark.halt();
                }
            }));

            Spark.get("/miembros", organizacionesController::miembros, engine);
            Spark.get("/mediciones", organizacionesController::mediciones, engine);
            Spark.get("/solicitudes", organizacionesController::solicitudes, engine);
            Spark.get("/guia", organizacionesController::guiaDeRecomendaciones, engine);
            Spark.get("/registrarMediciones", organizacionesController::pantallaRegistroDeMediciones, engine);
            Spark.get("/vistaMediciones", organizacionesController::pantallaMediciones, engine);

            Spark.post("/aceptar/:id", organizacionesController::aceptarVinculacion);
            Spark.post("/rechazar/:id", organizacionesController::rechazarVinculacion);
            Spark.post("/subirArchivo", organizacionesController::subirArchivo);

            Spark.get("/calcularHuella", organizacionesController::calcularHuella,engine);
            Spark.post("/calcularHuella", organizacionesController::calcularHuellaMensual,engine);

            Spark.get("/reporte", organizacionesController::reportes, engine);
            Spark.post("/visualizarReportes", organizacionesController::visualizarReportes, engine);
        });

        // --------------------------------- AGENTE SECTORIAL ----------------------------- //

        AgenteController agenteController = new AgenteController();

        Spark.path("/agente", () ->{
            Spark.before("/*", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermiso(request, Permiso.AGENTE_SECTORIAL)) {
                    response.redirect("/prohibido"); //no está todavia la pantalla
                    Spark.halt();
                }
            }));

            Spark.get("/guia", agenteController::guiaDeRecomendaciones, engine);
            Spark.get("/reporte", agenteController::reportes, engine);
            Spark.post("/visualizarReportes", agenteController::visualizarReportes, engine);
        });
    }
}