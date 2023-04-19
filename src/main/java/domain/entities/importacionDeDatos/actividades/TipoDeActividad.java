package domain.entities.importacionDeDatos.actividades;

import lombok.Getter;

public enum TipoDeActividad {
    COMBUSTION_FIJA(1),
    COMBUSTION_MOVIL(1),
    ELECTRICIDAD_ADQUIRIDA_Y_CONSUMIDA(2),
    LOGISTICA_DE_PRODUCTOS_Y_RESIDUOS(3),
    TRAYECTO(3);

    @Getter
    private final int alcance;

    TipoDeActividad(int alcance) {
        this.alcance = alcance;
    }
}