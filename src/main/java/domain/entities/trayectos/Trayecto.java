package domain.entities.trayectos;

import db.EntidadPersistente;
import lombok.Getter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.*;

@Entity
@Table(name = "trayecto")
@Getter
public class Trayecto extends EntidadPersistente {
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Tramo> tramos;

    @Column(name = "fecha", columnDefinition = "DATE")
    private YearMonth fecha;

    @Column(name = "valor_hc")
    private double valorHC;

    @Column(name = "valor_hc_organizacion")
    private double valorHCPorOrganizacion;

    public Trayecto() {

    }

    public Trayecto(YearMonth fechaRealizado, List<Tramo> tramos) {
        this.tramos = tramos;
        this.valorHC = this.tramos.stream().mapToDouble(Tramo::getValorHC).sum();
        this.valorHCPorOrganizacion = this.tramos.stream().mapToDouble(Tramo::valorHCTramoCompartido).sum();
        this.fecha = fechaRealizado;
    }

    public Trayecto(YearMonth fechaRealizado, Tramo ... tramos) {
        this.tramos = Arrays.asList(tramos);
        this.valorHC = this.tramos.stream().mapToDouble(Tramo::getValorHC).sum();
        this.valorHCPorOrganizacion = this.tramos.stream().mapToDouble(Tramo::valorHCTramoCompartido).sum();
        this.fecha = fechaRealizado;
    }

    public boolean esDelAnio(int anio) {
        return fecha.getYear() == anio;
    }

    public boolean esDelAnioYMes(int anio, int mes) {
        return fecha.equals(YearMonth.of(anio, mes));
    }

    // ----------------- DTO --------------

    public TrayectoDTO convertirADTO(){
        return new TrayectoDTO(this);
    }

    public class TrayectoDTO{
        public List<Tramo> tramos;
        public YearMonth fecha;
        public double valorHC;
        public double valorHCPorOrganizacion;

        public TrayectoDTO(Trayecto trayecto){
            tramos = trayecto.tramos;
            fecha = trayecto.fecha;
            valorHC = trayecto.valorHC;
            valorHCPorOrganizacion = trayecto.valorHCPorOrganizacion;
        }
    }
}