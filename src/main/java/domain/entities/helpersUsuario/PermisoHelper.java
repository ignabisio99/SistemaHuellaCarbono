package domain.entities.helpersUsuario;

import domain.entities.usuarios.Permiso;
import spark.Request;

public class PermisoHelper {

    public static Boolean usuarioTienePermiso(Request request, Permiso permiso){
        return UsuarioLogueadoHelper.usuarioLogueado(request).getRol() == permiso;
    }
}
