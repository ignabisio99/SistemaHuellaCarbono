package domain.ORM;

import domain.BaseTest;
import db.EntityManagerHelper;
import domain.entities.trayectos.TramoTransportePrivado;
import domain.entities.trayectos.TramoTransportePublico;
import domain.entities.trayectos.Trayecto;
import domain.entities.usuarios.Rol;
import domain.entities.usuarios.Permiso;
import domain.entities.usuarios.Usuario;
import org.junit.Test;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static domain.entities.usuarios.Permiso.*;

//TODO: revisar tests
public class PersistenciaEntidadesTest extends BaseTest {
    private Map<Permiso, Rol> roles;
    private List<Usuario> usuarios;

    @Test
    public void getUsuario() {
        Usuario usuario = (Usuario) EntityManagerHelper
                .getEntityManager()
                .createQuery("from Usuario where nombreDeUsuario='organizacionA' and contrasenia='123456'")
                .getSingleResult();
        System.out.println("Usuario: " + usuario.getNombreDeUsuario() + "\nRol: " + usuario.getRol());
    }

    @Test
    public void persistenciaUsuarios() {
        tramos();
        usuarios();

        EntityManagerHelper.beginTransaction();

        EntityManagerHelper.persist(carhue);
        EntityManagerHelper.persist(santaClara);

        EntityManagerHelper.persist(subteC);
        EntityManagerHelper.persist(colectivo64A);
        EntityManagerHelper.persist(colectivo114);

        roles.values().forEach(EntityManagerHelper::persist);
        usuarios.forEach(EntityManagerHelper::persist);

        EntityManagerHelper.commit();
        EntityManagerHelper.closeEntityManager();
        EntityManagerHelper.closeEntityManagerFactory();
    }

    private void tramos() {
        TramoTransportePrivado tramoTransportePrivado1 = new TramoTransportePrivado(auto, direccion1, direccion2);
        TramoTransportePrivado tramoTransportePrivado2 = new TramoTransportePrivado(auto, direccion3, direccion4);
        TramoTransportePublico tramoTransportePublico = new TramoTransportePublico(subteD, olleros, palermo);

        miembroA.agregarTrayecto(
                organizacionA,
                new Trayecto(YearMonth.now(), tramoTransportePrivado1, tramoTransportePrivado2, tramoTransportePublico)
        );

        miembroB.solicitarVinculacionConOrg(organizacionA, sectorA);
    }

    private void roles() {
        roles = new HashMap<>();
        for(Permiso permiso : Permiso.values())
            roles.put(permiso, new Rol(permiso));
    }

    private void usuarios() {
        roles();
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("organizacionA", "123456", organizacionA, roles.get(ORGANIZACION)));
        usuarios.add(new Usuario("organizacionB", "123456", organizacionB, roles.get(ORGANIZACION)));
        usuarios.add(new Usuario("miembroA", "123456", miembroA, roles.get(MIEMBRO)));
        usuarios.add(new Usuario("miembroB", "123456", miembroB, roles.get(MIEMBRO)));
        usuarios.add(new Usuario("miembroC", "123456", miembroC, roles.get(MIEMBRO)));
        usuarios.add(new Usuario("sectorAdolfoAlsina", "123456", sectorAdolfoAlsina, roles.get(AGENTE_SECTORIAL)));
        usuarios.add(new Usuario("sectorMarChiquita", "123456", sectorMarChiquita, roles.get(AGENTE_SECTORIAL)));
        usuarios.add(new Usuario("sectorBuenosAires", "123456", sectorBuenosAires, roles.get(AGENTE_SECTORIAL)));
    }
}
