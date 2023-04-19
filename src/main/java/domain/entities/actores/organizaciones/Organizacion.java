package domain.entities.actores.organizaciones;

import domain.entities.actores.Actor;
import domain.entities.huellaDeCarbono.HuellaDeCarbono;
import domain.entities.importacionDeDatos.actividades.Actividad;
import domain.entities.importacionDeDatos.actividades.Periodicidad;
import domain.entities.actores.miembros.MiembroPorOrganizacion;
import domain.entities.actores.miembros.Miembro;
import domain.entities.trayectos.Trayecto;
import domain.entities.ubicaciones.Direccion;
import domain.entities.actores.sectores.SectorMunicipal;
import lombok.Getter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "organizacion")
public class Organizacion extends Actor {
    @Column(name = "razon_social")
    private String razonSocial; // nombre de la Organizacion

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_organizacion")
    @Getter
    private TipoDeOrganizacion tipoDeOrganizacion;

    @Column(name = "subclasificacion")
    private String subclasificacion;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Direccion direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_municipal_id", referencedColumnName = "id")
    private SectorMunicipal sectorMunicipal;

    @OneToMany(mappedBy = "organizacion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Sector> sectores;

    @OneToMany(mappedBy = "organizacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    private List<MiembroPorOrganizacion> miembrosPorOrganizacion;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    @Getter
    private List<Actividad> listadoDeImportaciones;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    @Getter
    private Set<Contacto> contactos;

    public Organizacion() {
        this.sectores = new ArrayList<>();
        this.miembrosPorOrganizacion = new ArrayList<>();
        this.listadoDeImportaciones = new ArrayList<>();
        this.contactos = new LinkedHashSet<>();
        this.huellasDeCarbono = new ArrayList<>();
    }

    public Organizacion(String razonSocial, TipoDeOrganizacion tipoDeOrganizacion, String subclasificacion) {
        this.razonSocial = razonSocial;
        this.tipoDeOrganizacion = tipoDeOrganizacion;
        this.subclasificacion = subclasificacion;

        this.sectores = new ArrayList<>();
        this.miembrosPorOrganizacion = new ArrayList<>();
        this.listadoDeImportaciones = new ArrayList<>();
        this.contactos = new LinkedHashSet<>();
        this.huellasDeCarbono = new ArrayList<>();
    }

    public boolean esDelTipo(TipoDeOrganizacion tipoDeOrganizacion) {
        return tipoDeOrganizacion.equals(this.tipoDeOrganizacion);
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
        this.direccion.agregarASectorMunicipal(this);
        this.sectorMunicipal = direccion.getSectorMunicipal();
    }

    public void agregarSectores(Sector ... sectores) {
        Stream.of(sectores).forEach(s -> s.setOrganizacion(this));
        Collections.addAll(this.sectores, sectores);
    }

    public MiembroPorOrganizacion getMiembroPorOrganizacion(Miembro miembro) {
        return miembrosPorOrganizacion.stream().filter(m -> m.esElMiembro(miembro)).findFirst().orElse(null);
    }

    public void recibirSolicitud(MiembroPorOrganizacion miembroPorOrganizacion) {
        miembrosPorOrganizacion.add(miembroPorOrganizacion);
    }

    public void aceptarVinculacion(Miembro miembro) {
        getMiembroPorOrganizacion(miembro).aceptar();
    }

    @Override
    public void agregarHuella(HuellaDeCarbono huellaDeCarbono) {
        super.agregarHuella(huellaDeCarbono);
        sectorMunicipal.agregarHuella(new HuellaDeCarbono(huellaDeCarbono));
    }

    public void agregarActividad(Actividad actividad) {
        listadoDeImportaciones.add(actividad);
        HuellaDeCarbono huellaDeCarbono = new HuellaDeCarbono(actividad);
        agregarHuella(huellaDeCarbono);
    }

    public void agregarTrayecto(Trayecto trayecto) {
        double valor = trayecto.getValorHCPorOrganizacion();
        YearMonth fecha = trayecto.getFecha();

        for(Periodicidad periodicidad : Periodicidad.values())
            this.agregarHuella(new HuellaDeCarbono(valor, periodicidad, fecha));
    }

    public void agregarContactos(Contacto ... contactos) {
        Collections.addAll(this.contactos, contactos);
    }

    public double indicadorHCPorSectorAnual(Sector sector, int anio) {
        return sector.calcularHuellaAnual(anio) / sector.cantidadDeMiembros();
    }

    public double indicadorHCPorSectorMensual(Sector sector, int anio, int mes) {
        return sector.calcularHuellaMensual(anio, mes) / sector.cantidadDeMiembros();
    }

    // --------------------- DTO ------------------

    public OrganizacionDTO convertirADTO(){
        return new OrganizacionDTO(this);
    }

    public static class OrganizacionDTO {
        @Getter
        public int id;
        public String razonSocial;
        public TipoDeOrganizacion tipoDeOrganizacion;
        public String subclasificacion;
        public Direccion direccion;
        public SectorMunicipal sectorMunicipal;
        public List<Sector> sectores;
        @Getter
        public List<MiembroPorOrganizacion> miembrosPorOrganizacion;
        @Getter
        public List<Actividad> listadoDeImportaciones;
        @Getter
        public Set<Contacto> contactos;
        public List<HuellaDeCarbono> huellasDeCarbono;

        public OrganizacionDTO(Organizacion organizacion) {
            id = organizacion.id;
            razonSocial = organizacion.razonSocial;
            tipoDeOrganizacion = organizacion.tipoDeOrganizacion;
            subclasificacion = organizacion.subclasificacion;
            direccion = organizacion.direccion;
            sectorMunicipal = organizacion.sectorMunicipal;
            sectores = organizacion.sectores;
            miembrosPorOrganizacion = organizacion.miembrosPorOrganizacion;
            listadoDeImportaciones = organizacion.listadoDeImportaciones;
            contactos = organizacion.contactos;
            huellasDeCarbono = organizacion.huellasDeCarbono;
        }

        public String getNombre() {
            return razonSocial;
        }

        public List<Miembro.MiembroDTO> getSolicitudes() {
            return miembrosPorOrganizacion.stream().filter(m -> !m.isAccepted())
                    .map(m -> m.getMiembro().convertirADTO()).collect(Collectors.toList());
        }

        public List<Miembro.MiembroDTO> getMiembros() {
            return miembrosPorOrganizacion.stream().filter(MiembroPorOrganizacion::isAccepted)
                    .map(m -> m.getMiembro().convertirADTO()).collect(Collectors.toList());
        }

        public List<Actividad.ActividadDTO> getListadoDeImportaciones() {
            return listadoDeImportaciones.stream().map(Actividad::convertirADTO).collect(Collectors.toList());
        }
    }
}
