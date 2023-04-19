package domain.entities.actores.organizaciones;

import db.EntidadPersistente;
import domain.entities.actores.miembros.MiembroPorOrganizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sector")
@Getter
public class Sector extends EntidadPersistente {
    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    @Setter
    private Organizacion organizacion;

    @OneToMany(mappedBy = "sector", fetch = FetchType.LAZY)
    private List<MiembroPorOrganizacion> miembros;

    public Sector() {
        this.miembros = new ArrayList<>();
    }

    public Sector(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    public void agregarMiembroPorOrganizacion(MiembroPorOrganizacion miembro) {
        miembros.add(miembro);
    }

    public int cantidadDeMiembros() {
        return miembros.size();
    }

    public double calcularHuellaAnual(int anio) {
        return miembros.stream().mapToDouble(m -> m.huellaPorOrganizacionAnual(anio)).sum();
    }

    public double calcularHuellaMensual(int anio, int mes) {
        return miembros.stream().mapToDouble(m -> m.huellaPorOrganizacionMensual(anio, mes)).sum();
    }

    // ------------------ DTO ------------------

    public SectorDTO convertirADTO(){
        return new SectorDTO(this);
    }

    public class SectorDTO{
        public String nombre;
        public Organizacion organizacion;
        public List<MiembroPorOrganizacion> miembros;

        public SectorDTO(Sector sector){
            nombre = sector.nombre;
            organizacion = sector.organizacion;
            miembros = sector.miembros;
        }
    }

}
