package domain.entities.trayectos;

import lombok.Getter;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Distancia {
    public double valor;
    public String unidad;

    public Distancia() {

    }

    public Distancia(double valor) {
        this.valor = valor;
        this.unidad = "KM";
    }

    public Distancia(double valor, String unidad) {
        this.valor = valor;
        this.unidad = unidad;
    }
}
