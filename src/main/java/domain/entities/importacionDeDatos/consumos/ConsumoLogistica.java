package domain.entities.importacionDeDatos.consumos;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("logistica")
public class ConsumoLogistica extends Consumo {
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "consumo_logistica_id", referencedColumnName = "id")
    @Getter
    private List<Consumo> consumos;

    @Column(name = "constante")
    private double constante;

    public ConsumoLogistica() {
        this.consumos = new ArrayList<>();
        this.constante = 1.0;
    }

    @Override
    public double calcularHC() {
        double valorConsumo = consumos.stream()
                .mapToDouble(Consumo::calcularHC)
                .reduce(1, (factor, valor) -> factor * valor);
        return valorConsumo * constante;
    }

    @Override
    public String getNombreConsumo() {
        return consumos.stream().filter(Objects::nonNull)
                .findFirst().map(Consumo::getNombreConsumo).orElse("");
    }

    public void agregarConsumo(Consumo consumo) {
        consumos.add(consumo);
    }
}