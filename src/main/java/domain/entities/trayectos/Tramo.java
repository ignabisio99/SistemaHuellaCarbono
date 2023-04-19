package domain.entities.trayectos;

import db.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table(name = "tramo")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Tramo extends EntidadPersistente {
    public abstract double getValorHC();
    public abstract double valorHCTramoCompartido();
    public abstract Distancia calcularDistancia();
}