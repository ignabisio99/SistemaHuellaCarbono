package domain.entities.importacionDeDatos.consumos;

import db.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table(name = "consumo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Consumo extends EntidadPersistente {
    public abstract double calcularHC();
    public abstract String getNombreConsumo();
}
