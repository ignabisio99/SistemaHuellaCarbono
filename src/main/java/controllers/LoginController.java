package controllers;

import domain.entities.usuarios.Usuario;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;

public class LoginController {
    private Repositorio<Usuario> repoUsuario;

    public LoginController() {
        repoUsuario = FactoryRepositorio.get(Usuario.class);
    }

    public ModelAndView pantallaDeLogin(Request request, Response response) {
        return new ModelAndView(null, "index/login.hbs");
    }

    public Response login(Request request, Response response) {
        try {
            List<Usuario> usuarios = repoUsuario.buscarTodos();
            Optional<Usuario> usuario = usuarios.stream().filter(u ->
                    u.getNombreDeUsuario().equals(request.queryParams("nombre_de_usuario")) &&
                    u.getContrasenia().equals(request.queryParams("contrasenia")))
                    .findFirst();

            if(usuario.isPresent()) {
                request.session(true);
                request.session().attribute("id", usuario.get().getId());
                switch(usuario.get().getRol()) {
                    case MIEMBRO:
                        response.redirect("/miembro/registrarTrayecto");
                        break;
                    case ORGANIZACION:
                        response.redirect("/organizacion/guia");
                        break;
                    case AGENTE_SECTORIAL:
                        response.redirect("/agente/guia");
                }
            }
            else response.redirect("/login");
        }
        catch (Exception ex) {
            response.redirect("/login");
        }
        return response;
    }

    public Response logout(Request request, Response response) {
        request.session().invalidate();
        response.redirect("/login");
        return response;
    }

    public ModelAndView prohibido(Request request, Response response) {
        return new ModelAndView(null, "prohibido.hbs");
    }
}
