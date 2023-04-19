package domain.entities.importacionDeDatos.consumos;

import domain.entities.huellaDeCarbono.FactorDeEmision;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("simple")
@Getter
public class ConsumoSimple extends Consumo {
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_consumo")
    private TipoDeConsumo tipoDeConsumo;

    @Column(name = "valor")
    private String valor;

    @Column(name = "unidad")
    private String unidad;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "fe_id", referencedColumnName = "id")
    @Setter
    private FactorDeEmision factorDeEmision;

    public ConsumoSimple() {

    }

    public ConsumoSimple(TipoDeConsumo tipoDeConsumo, String valor) {
        this.tipoDeConsumo = tipoDeConsumo;
        this.valor = valor;
        this.unidad = tipoDeConsumo.getUnidad();
    }

    public ConsumoSimple(TipoDeConsumo tipoDeConsumo, String valor, FactorDeEmision factorDeEmision) {
        this.tipoDeConsumo = tipoDeConsumo;
        this.valor = valor;
        this.unidad = tipoDeConsumo.getUnidad();
        this.factorDeEmision = factorDeEmision;
    }

    @Override
    public double calcularHC() {
        double fe = factorDeEmision == null ? 1.0 : factorDeEmision.getValor();
        try {
            double valor = Double.parseDouble(this.valor);
            return fe * valor;
        } catch (NumberFormatException e) {
           return fe;
        }
    }

    @Override
    public String getNombreConsumo() {
        switch (tipoDeConsumo) {
            case CATEGORIA:
                return valor;
            case DISTANCIA: case PESO: case MEDIO_DE_TRANSPORTE:
                return null;
            default:
                return tipoDeConsumo.name();
        }
    }
}