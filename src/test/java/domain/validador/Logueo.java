package domain.validador;

import domain.entities.validador.Validacion;
import domain.entities.validador.Validador;
import domain.entities.validador.validaciones.*;

import java.util.HashMap;
import java.util.Scanner;

public class Logueo {
    public static void main(String[] args) {
        Validador validador = new Validador();

        validador.agregarValidaciones(new HashMap<String, Validacion>() {{
            put("mayus", new AlMenosUnaMayuscula());
            put("minus", new AlMenosUnaMinuscula());
            put("nro", new AlMenosUnNumero());
            put("top", new NoEstaEnElTop10000());
            put("long", new LongitudMayorIgualAOcho());
        }});

        String nombreUsuario, contrasena = null;
        Scanner entrada = new Scanner(System.in);

        System.out.print("Usuario: ");
        nombreUsuario = entrada.nextLine();

        do {
            if(contrasena != null)
                System.out.println(validador.mensajeDeError() + "\n");
            System.out.print("Contraseña: ");
            contrasena = entrada.nextLine();
        } while(!validador.esSegura(contrasena));

        System.out.println("Contraseña segura.");
        entrada.close();

        new Usuario(nombreUsuario, contrasena);
    }
}