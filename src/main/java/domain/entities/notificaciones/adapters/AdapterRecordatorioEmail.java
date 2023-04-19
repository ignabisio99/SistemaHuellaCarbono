package domain.entities.notificaciones.adapters;

import domain.entities.actores.organizaciones.Contacto;

import java.util.List;

public interface AdapterRecordatorioEmail {
    void enviarNotificacion(String cuerpo, List<Contacto> contactos);
}
