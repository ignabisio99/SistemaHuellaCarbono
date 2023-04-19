package domain.entities.actores.miembros;

import domain.entities.actores.Actor;
import domain.entities.huellaDeCarbono.HuellaDeCarbono;
import domain.entities.importacionDeDatos.actividades.Periodicidad;
import domain.entities.actores.organizaciones.Organizacion;
import domain.entities.actores.organizaciones.Sector;
import domain.entities.ubicaciones.Direccion;
import domain.entities.trayectos.Trayecto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "miembros")
public class Miembro extends Actor {
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    @Setter
    private TipoDeDocumento tipoDeDocumento;

    @Column(name = "numero_documento", unique = true)
    @Setter
    private int numeroDocumento;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    @Setter
    private Direccion direccion;

    @OneToMany(mappedBy = "miembro", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<MiembroPorOrganizacion> miembrosPorOrganizacion;

    public Miembro() {
        this.miembrosPorOrganizacion = new ArrayList<>();
        this.huellasDeCarbono = new ArrayList<>();
    }

    public void setNombreYApellido(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public void solicitarVinculacionConOrg(Organizacion organizacion, Sector sector) {
        organizacion.recibirSolicitud(new MiembroPorOrganizacion(this, organizacion, sector));
    }

    public MiembroPorOrganizacion getMiembroPorOrganizacion(Organizacion organizacion) {
        return miembrosPorOrganizacion.stream().filter(m -> m.esDeLaOrganizacion(organizacion)).findFirst().orElse(null);
    }

    public void agregarMiembroPorOrganizacion(MiembroPorOrganizacion miembroPorOrganizacion) {
        miembrosPorOrganizacion.add(miembroPorOrganizacion);
    }

    public void agregarTrayecto(Organizacion organizacion, Trayecto trayecto) {
        double valor = trayecto.getValorHC();
        YearMonth fecha = trayecto.getFecha();

        for(Periodicidad periodicidad : Periodicidad.values())
            agregarHuella(new HuellaDeCarbono(valor, periodicidad, fecha));

        getMiembroPorOrganizacion(organizacion).agregarTrayecto(trayecto);
    }

    public double impactoSobreElTotalDeOrganizacionAnual(Organizacion organizacion, int anio) {
        return getMiembroPorOrganizacion(organizacion).impactoSobreElTotalDeOrganizacionAnual(anio);
    }

    public double impactoSobreElTotalDeOrganizacionMensual(Organizacion organizacion, int anio, int mes) {
        return getMiembroPorOrganizacion(organizacion).impactoSobreElTotalDeOrganizacionMensual(anio, mes);
    }

    public List<MiembroPorOrganizacion> getMiembrosPorOrganizacion(){
        return this.miembrosPorOrganizacion;
    }

    // ----------- DTO -------------

    public MiembroDTO convertirADTO(){
        return new MiembroDTO(this);
    }

    public static class MiembroDTO {
        @Getter
        public int id;
        public String nombre;
        public String apellido;
        public TipoDeDocumento tipoDeDocumento;
        @Getter
        public int numeroDocumento;
        public Direccion direccion;
        public List<MiembroPorOrganizacion> miembrosPorOrganizacion;
        public List<HuellaDeCarbono> huellasDeCarbono;

        public MiembroDTO(Miembro miembro) {
            id = miembro.id;
            nombre = miembro.nombre;
            apellido = miembro.apellido;
            tipoDeDocumento = miembro.tipoDeDocumento;
            numeroDocumento = miembro.numeroDocumento;
            direccion = miembro.direccion;
            miembrosPorOrganizacion = miembro.miembrosPorOrganizacion;
            huellasDeCarbono = miembro.huellasDeCarbono;
        }

        public String getNombreYApellido() {
            return nombre + " " + apellido;
        }

        public String getDireccion() {
            return direccion.getCalle() + " " + direccion.getAltura();
        }

        public String getTipoDocumento() {
            return tipoDeDocumento.name();
        }

        public List<Organizacion.OrganizacionDTO> obtenerOrganizaciones(){
            return miembrosPorOrganizacion.stream().map(m -> m.getOrganizacion().convertirADTO()).collect(Collectors.toList());
        }
    }

}
