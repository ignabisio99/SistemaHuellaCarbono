package domain.entities.huellaDeCarbono;

import db.EntidadPersistente;
import domain.entities.importacionDeDatos.actividades.Actividad;
import domain.entities.importacionDeDatos.actividades.Periodicidad;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.YearMonth;

@Entity
@Table(name = "huella_de_carbono")
@Getter
public class HuellaDeCarbono extends EntidadPersistente {
    @Column(name = "valor")
    @Setter
    private double valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidad")
    private Periodicidad periodicidad;

    @Column(name = "periodo_de_imputacion", columnDefinition = "DATE")
    private YearMonth perDeImputacion;

    @Column(name = "unidad")
    private String unidad;

    public HuellaDeCarbono() {
    }

    public HuellaDeCarbono(double valor, Periodicidad periodicidad, YearMonth perDeImputacion) {
        this.valor = valor;
        this.periodicidad = periodicidad;
        this.perDeImputacion = perDeImputacion;
        this.unidad = "kgCO2eq";
    }

    public HuellaDeCarbono(Actividad actividad) {
        this.valor = actividad.getValorHC();
        this.periodicidad = actividad.getPeriodicidad();
        this.perDeImputacion = actividad.getPerDeImputacion();
        this.unidad = "kgCO2eq";
    }

    public HuellaDeCarbono(HuellaDeCarbono copia) {
        this.valor = copia.getValor();
        this.periodicidad = copia.getPeriodicidad();
        this.perDeImputacion = copia.getPerDeImputacion();
        this.unidad = copia.getUnidad();
    }

    public void sumarValor(double valor) {
        this.valor += valor;
    }

    public int getYear() {
        return perDeImputacion.getYear();
    }

    public boolean esDelAnio(int anio) {
        return periodicidad == Periodicidad.ANUAL && perDeImputacion.getYear() == anio;
    }

    public boolean esDelAnioYMes(YearMonth yearMonth) {
        return periodicidad == Periodicidad.MENSUAL && perDeImputacion.equals(yearMonth);
    }

    public boolean compararCon(HuellaDeCarbono huellaDeCarbono) {
        switch (periodicidad) {
            case ANUAL:
                return huellaDeCarbono.esDelAnio(perDeImputacion.getYear());
            case MENSUAL:
                return huellaDeCarbono.esDelAnioYMes(perDeImputacion);
        }
        return false;
    }

    public void convertirUnidad(String unidadAConvertir) {
        unidad = unidadAConvertir;
        switch (unidadAConvertir) {
            case "tnCO2eq":
                valor /= 1000;
                break;
            case "gCO2eq":
                valor *= 1000;
                break;
        }
    }

    // -------------------- DTO -----------------

    public HuellaDeCarbonoDTO convertirADTO(){
        return new HuellaDeCarbonoDTO(this);
    }

    public class HuellaDeCarbonoDTO{
        public double valor;
        public Periodicidad periodicidad;
        public YearMonth periodoImputacion;
        public String unidad;

        public HuellaDeCarbonoDTO(HuellaDeCarbono huellaDeCarbono){
            valor = huellaDeCarbono.valor;
            periodicidad = huellaDeCarbono.periodicidad;
            periodoImputacion = huellaDeCarbono.perDeImputacion;
            unidad = huellaDeCarbono.unidad;
        }
    }
}
