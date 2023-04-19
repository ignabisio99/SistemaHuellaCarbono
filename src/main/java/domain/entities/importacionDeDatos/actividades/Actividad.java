package domain.entities.importacionDeDatos.actividades;

import db.EntidadPersistente;
import domain.entities.importacionDeDatos.consumos.Consumo;
import lombok.Getter;

import javax.persistence.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "actividad")
@Getter
public class Actividad extends EntidadPersistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_actividad")
    private TipoDeActividad tipoDeActividad;

    @Column(name = "alcance")
    private int alcance;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "consumo_id", referencedColumnName = "id")
    private Consumo consumo;

    @Enumerated(EnumType.STRING)
    private Periodicidad periodicidad;

    @Column(name = "periodo_de_imputacion", columnDefinition = "DATE")
    private YearMonth perDeImputacion;

    @Column(name = "valor_hc")
    private double valorHC;

    public Actividad() {
    }

    public Actividad(
            String tipoDeActividad,
            Consumo consumo,
            String periodicidad,
            YearMonth perDeImputacion
    ) {
        this.tipoDeActividad = TipoDeActividad.valueOf(tipoDeActividad);
        this.consumo = consumo;
        this.valorHC = consumo.calcularHC();
        this.alcance = this.tipoDeActividad.getAlcance();
        this.periodicidad = Periodicidad.valueOf(periodicidad);
        this.perDeImputacion = perDeImputacion;
    }

    public boolean esDelAnio(int anio) {
        return periodicidad == Periodicidad.ANUAL && perDeImputacion.getYear() == anio;
    }

    public boolean esDelAnioYMes(YearMonth fecha) {
        return periodicidad == Periodicidad.MENSUAL && perDeImputacion.equals(fecha);
    }

    public ActividadDTO convertirADTO() {
        return new ActividadDTO(this);
    }

    public static class ActividadDTO {
        private TipoDeActividad tipoDeActividad;
        private int alcance;
        private Consumo consumo;
        private Periodicidad periodicidad;
        private YearMonth perDeImputacion;
        private double valorHC;

        public ActividadDTO(Actividad actividad) {
            tipoDeActividad = actividad.tipoDeActividad;
            alcance = actividad.alcance;
            consumo = actividad.consumo;
            periodicidad = actividad.periodicidad;
            perDeImputacion = actividad.perDeImputacion;
            valorHC = actividad.valorHC;
        }

        public String getNombre() {
            return tipoDeActividad.name();
        }

        public String getConsumo() {
            return consumo.getNombreConsumo();
        }

        public String getPeriodicidad() {
            return periodicidad.name();
        }

        public String getPerDeImputacion() {
            switch (periodicidad) {
                case MENSUAL:
                    return perDeImputacion.format(DateTimeFormatter.ofPattern("MM/yyyy"));
                case ANUAL:
                    return perDeImputacion.format(DateTimeFormatter.ofPattern("yyyy"));
            }
            return null;
        }

        public String getValorHC() {
            return String.format("%.2f", valorHC);
        }
    }
}