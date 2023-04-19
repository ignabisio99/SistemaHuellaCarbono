package domain.entities.trayectos;

import domain.entities.transporte.transportePublico.Parada;
import domain.entities.transporte.transportePublico.TransportePublico;

import javax.persistence.*;

@Entity
@Table(name = "tramo_trasporte_publico")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id")) })
public class TramoTransportePublico extends Tramo {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transporte_publico_id", referencedColumnName = "id")
    private TransportePublico transportePublico;

    @ManyToOne
    @JoinColumn(name = "parada_origen_id", referencedColumnName = "id")
    private Parada paradaOrigen;

    @ManyToOne
    @JoinColumn(name = "parada_destino_id", referencedColumnName = "id")
    private Parada paradaDestino;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "valor", column = @Column(name = "distancia_valor")),
            @AttributeOverride( name = "unidad", column = @Column(name = "distancia_unidad"))
    })
    private Distancia distancia;

    public TramoTransportePublico() {
    }

    public TramoTransportePublico(
            TransportePublico transportePublico,
            Parada paradaOrigen,
            Parada paradaDestino
    ) {
        this.transportePublico = transportePublico;
        this.paradaOrigen = paradaOrigen;
        this.paradaDestino = paradaDestino;
        this.distancia = calcularDistancia();
    }

    @Override
    public double getValorHC() {
        return transportePublico.getFactorDeEmision() * distancia.getValor();
    }

    @Override
    public double valorHCTramoCompartido() {
        return getValorHC();
    }

    @Override
    public Distancia calcularDistancia() {
        double distancia;
        if (paradaOrigen.equals(paradaDestino))
            distancia = 0;
        else if (transportePublico.estaAntesDe(paradaOrigen, paradaDestino))
            distancia = paradaOrigen.distanciaHasta(paradaDestino);
        else
            distancia = paradaDestino.distanciaHasta(paradaOrigen);
        return new Distancia(distancia);
    }
}