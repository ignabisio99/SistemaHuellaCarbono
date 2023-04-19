package domain.entities.actores;

import db.EntidadPersistente;
import domain.entities.huellaDeCarbono.HuellaDeCarbono;
import domain.entities.importacionDeDatos.actividades.Periodicidad;
import domain.entities.usuarios.Usuario;
import lombok.Setter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "actor")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Actor extends EntidadPersistente {
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", referencedColumnName = "id")
    protected List<HuellaDeCarbono> huellasDeCarbono;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @Setter
    private Usuario usuario;

    public double calcularHuellaAnual(int anio) {
        return huellasDeCarbono.stream()
                .filter(h -> h.esDelAnio(anio)).findFirst()
                .map(HuellaDeCarbono::getValor).orElse(0.0);
    }

    public double calcularHuellaMensual(int anio, int mes) {
        return huellasDeCarbono.stream()
                .filter(h -> h.esDelAnioYMes(YearMonth.of(anio, mes))).findFirst()
                .map(HuellaDeCarbono::getValor).orElse(0.0);
    }

    public double calcularHuellaTotalDesde(YearMonth fecha) {
        return huellasDeCarbono.stream()
                .filter(h -> h.getPerDeImputacion().compareTo(fecha) >= 0)
                .mapToDouble(HuellaDeCarbono::getValor).sum();
    }

    public List<HuellaDeCarbono> huellasAnualesDesde(int anio) {
        return huellasDeCarbono.stream()
                .filter(h -> h.getPeriodicidad().equals(Periodicidad.ANUAL) &&
                        h.getPerDeImputacion().compareTo(YearMonth.of(anio, 1)) >= 0)
                .collect(Collectors.toList());
    }

    public List<HuellaDeCarbono> huellasMensualesDesde(YearMonth fecha) {
        return huellasDeCarbono.stream()
                .filter(h -> h.getPeriodicidad().equals(Periodicidad.MENSUAL) &&
                        h.getPerDeImputacion().compareTo(fecha) >= 0)
                .collect(Collectors.toList());
    }

    public void agregarHuella(HuellaDeCarbono huellaDeCarbono) {
        Optional<HuellaDeCarbono> huella = huellasDeCarbono.stream().filter(h -> h.compararCon(huellaDeCarbono)).findFirst();
        if(huella.isPresent()) huella.get().sumarValor(huellaDeCarbono.getValor());
        else huellasDeCarbono.add(huellaDeCarbono);
    }
}
