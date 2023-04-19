package domain.repositories.data.dataConcrets;

import db.EntidadPersistente;
import domain.entities.actores.organizaciones.Contacto;

import java.util.ArrayList;
import java.util.List;

public class DataTrayecto {
    private static List<EntidadPersistente> trayectos = new ArrayList<>();

    public static List<EntidadPersistente> getList() {
        //Aca van los datos en concreto (guardarlos en la lista)

        return trayectos;
    }

    public static void addAll(List<EntidadPersistente> listClass) {
        trayectos.addAll(listClass);
    }
}
