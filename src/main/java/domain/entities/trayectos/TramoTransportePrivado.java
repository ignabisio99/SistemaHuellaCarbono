package domain.entities.trayectos;

import domain.entities.actores.miembros.MiembroPorOrganizacion;
import domain.entities.actores.miembros.Miembro;
import domain.entities.services.geodds.ServicioDeUbicaciones;
import domain.entities.transporte.vehiculosPrivados.TransportePrivado;
import domain.entities.ubicaciones.Direccion;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "tramo_transporte_privado")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id")) })
public class TramoTransportePrivado extends Tramo {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transporte_privado_id", referencedColumnName = "id")
    private TransportePrivado transportePrivado;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_origen_id", referencedColumnName = "id")
    private Direccion direccionOrigen;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_destino_id", referencedColumnName = "id")
    private Direccion direccionDestino;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "valor", column = @Column(name = "distancia_valor")),
            @AttributeOverride( name = "unidad", column = @Column(name = "distancia_unidad"))
    })
    private Distancia distancia;

    @OneToMany
    @JoinColumn(name = "tramo_compartido_id", referencedColumnName = "id")
    private List<Miembro> miembrosCompartidos;

    @Transient
    private boolean yaMeCalcularon;

    @Transient
    private ServicioDeUbicaciones servicio;

    public TramoTransportePrivado() {
    }

    public TramoTransportePrivado(
            TransportePrivado transportePrivado,
            Direccion direccionOrigen,
            Direccion direccionDestino
    ) {
        this.transportePrivado = transportePrivado;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.servicio = ServicioDeUbicaciones.getInstancia();
        this.distancia = calcularDistancia();
    }

    public TramoTransportePrivado(
            TransportePrivado transportePrivado,
            Direccion direccionOrigen,
            Direccion direccionDestino,
            YearMonth fecha,
            MiembroPorOrganizacion ... miembros
    ) {
        this.transportePrivado = transportePrivado;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.servicio = ServicioDeUbicaciones.getInstancia();
        this.distancia = calcularDistancia();
        this.yaMeCalcularon = false;
        this.miembrosCompartidos = Stream.of(miembros).map(m -> {
            m.getMiembro().agregarTrayecto(m.getOrganizacion(), new Trayecto(fecha, this));
            return m.getMiembro();
        }).collect(Collectors.toList());
    }

    @Override
    public double getValorHC() {
        return transportePrivado.getFactorDeEmision() * distancia.getValor();
    }

    @Override
    public double valorHCTramoCompartido() {
        if (yaMeCalcularon) return 0;
        yaMeCalcularon = true;
        return getValorHC();
    }

    @Override
    public Distancia calcularDistancia() {
        return this.servicio.obtenerDistancia(direccionOrigen, direccionDestino);
    }
}
