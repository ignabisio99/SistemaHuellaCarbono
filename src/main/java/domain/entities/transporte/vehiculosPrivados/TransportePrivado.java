package domain.entities.transporte.vehiculosPrivados;

import db.EntidadPersistente;
import domain.entities.huellaDeCarbono.FactorDeEmision;
import domain.entities.importacionDeDatos.consumos.TipoDeConsumo;
import domain.entities.transporte.transportePublico.TransportePublico;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transporte_privado")
@Setter
public class TransportePrivado extends EntidadPersistente {
    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo")
    private TipoVehiculo tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "clase_vehiculo")
    private CriterioDeVehiculo criterioDeVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_combustible")
    private TipoCombustible tipoCombustible;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "fe_id", referencedColumnName = "id")
    private FactorDeEmision factorDeEmision;

    public TransportePrivado() {
    }

    public TransportePrivado(String criterio, String tipoVehiculo, String tipoCombustible) {
        this.criterioDeVehiculo = CriterioDeVehiculo.valueOf(criterio);
        this.tipoVehiculo = TipoVehiculo.valueOf(tipoVehiculo);
        this.tipoCombustible = TipoCombustible.valueOf(tipoCombustible);
        this.factorDeEmision = new FactorDeEmision(0.4, "lt");
    }

    public TransportePrivado(String criterio, String nombre) {
        this.criterioDeVehiculo = CriterioDeVehiculo.valueOf(criterio);
        this.nombre = nombre;
        this.factorDeEmision = new FactorDeEmision(0.4, "lt");
    }

    public TransportePrivado(String tipoVehiculo) {
        try {
            this.tipoVehiculo = TipoVehiculo.valueOf(tipoVehiculo);
        } catch (IllegalArgumentException e) {
            this.criterioDeVehiculo = CriterioDeVehiculo.valueOf(tipoVehiculo);
        }
        this.factorDeEmision = new FactorDeEmision(0.4, "lt");
    }

    public double getFactorDeEmision() {
        return factorDeEmision.getValor();
    }

    // ------------------- DTO -----------------------

    public TransportePrivadoDTO convertirADTO(){
        return new TransportePrivadoDTO(this);
    }

    public class TransportePrivadoDTO{
        public TipoVehiculo tipoVehiculo;
        public CriterioDeVehiculo criterioDeVehiculo;
        public TipoCombustible tipoCombustible;
        public FactorDeEmision factorDeEmision;

        public TransportePrivadoDTO(TransportePrivado transportePrivado){
            tipoVehiculo = transportePrivado.tipoVehiculo;
            criterioDeVehiculo = transportePrivado.criterioDeVehiculo;
            tipoCombustible = transportePrivado.tipoCombustible;
            factorDeEmision = transportePrivado.factorDeEmision;
        }
    }
}
