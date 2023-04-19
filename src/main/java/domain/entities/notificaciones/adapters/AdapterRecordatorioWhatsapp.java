package domain.entities.notificaciones.adapters;

import domain.entities.actores.organizaciones.Contacto;

import java.util.List;

public interface AdapterRecordatorioWhatsapp {
    void enviarNotificacion(String urlGuia, List<Contacto> contactos);
}
