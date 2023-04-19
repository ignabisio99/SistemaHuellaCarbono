package domain.entities.actores.miembros;

import db.EntidadPersistente;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.actores.organizaciones.Sector;
import domain.entities.trayectos.Trayecto;
import lombok.Getter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "miembro_organizacion")
public class MiembroPorOrganizacion extends EntidadPersistente {
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    @Getter
    private Organizacion organizacion;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", referencedColumnName = "id")
    private Sector sector;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "miembro_id", referencedColumnName = "id")
    @Getter
    private Miembro miembro;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "miembro_organizacion_id", referencedColumnName = "id")
    private List<Trayecto> trayectos;

    @Column(name = "esta_aceptado")
    @Getter
    private boolean accepted;

    public MiembroPorOrganizacion() {}

    public MiembroPorOrganizacion(Miembro miembro, Organizacion organizacion, Sector sector) {
        this.miembro = miembro;
        this.organizacion = organizacion;
        this.sector = sector;
        this.trayectos = new ArrayList<>();
        this.accepted = false;
    }

    public boolean esDeLaOrganizacion(Organizacion organizacion) {
        return organizacion.equals(this.organizacion);
    }
    public boolean esElMiembro(Miembro miembro) {
        return miembro.equals(this.miembro);
    }

    public void aceptar() {
        this.accepted = true;
        this.sector.agregarMiembroPorOrganizacion(this);
        this.miembro.agregarMiembroPorOrganizacion(this);
    }

    public void agregarTrayecto(Trayecto trayecto) {
        trayectos.add(trayecto);
        organizacion.agregarTrayecto(trayecto);
    }

    public double calcularHuellaAnual(int anio) {
        return trayectos.stream()
                .filter(t -> t.esDelAnio(anio))
                .mapToDouble(Trayecto::getValorHC)
                .sum();
    }

    public double calcularHuellaMensual(int anio, int mes) {
        return trayectos.stream()
                .filter(t -> t.esDelAnioYMes(anio, mes))
                .mapToDouble(Trayecto::getValorHC)
                .sum();
    }

    public double huellaPorOrganizacionAnual(int anio) {
        return trayectos.stream()
                .filter(t -> t.esDelAnio(anio))
                .mapToDouble(Trayecto::getValorHCPorOrganizacion)
                .sum();
    }

    public double huellaPorOrganizacionMensual(int anio, int mes) {
        return trayectos.stream()
                .filter(t -> t.esDelAnioYMes(anio, mes))
                .mapToDouble(Trayecto::getValorHCPorOrganizacion)
                .sum();
    }

    public double impactoSobreElTotalDeOrganizacionAnual(int anio) {
        return huellaPorOrganizacionAnual(anio) / organizacion.calcularHuellaAnual(anio);
    }

    public double impactoSobreElTotalDeOrganizacionMensual(int anio, int mes) {
        return huellaPorOrganizacionMensual(anio, mes) / organizacion.calcularHuellaMensual(anio, mes);
    }

    public double calcularHuellaTotalDesde(YearMonth fecha) {
        return trayectos.stream()
                .filter(t -> t.getFecha().compareTo(fecha) >= 0)
                .mapToDouble(Trayecto::getValorHCPorOrganizacion)
                .sum();
    }

    // ------------------- DTO -----------------------

    public MiembroPorOrganizacionDTO convertirADTO(){
        return new MiembroPorOrganizacionDTO(this);
    }

    public static class MiembroPorOrganizacionDTO {
        public Organizacion organizacion;
        public Sector sector;
        public Miembro miembro;
        public List<Trayecto> trayectos;

        public MiembroPorOrganizacionDTO(MiembroPorOrganizacion miembroPorOrganizacion){
            organizacion = miembroPorOrganizacion.organizacion;
            sector = miembroPorOrganizacion.sector;
            miembro = miembroPorOrganizacion.miembro;
            trayectos = miembroPorOrganizacion.trayectos;
        }
    }
}
