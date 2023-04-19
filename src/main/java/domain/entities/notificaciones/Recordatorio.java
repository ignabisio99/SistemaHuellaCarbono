package domain.entities.notificaciones;

import domain.entities.notificaciones.adapters.AdapterRecordatorioEmail;
import domain.entities.notificaciones.adapters.AdapterRecordatorioWhatsapp;
import domain.entities.actores.organizaciones.Contacto;
import domain.entities.actores.organizaciones.Organizacion;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Recordatorio {
    public static Recordatorio instancia = null;
    private static List<Organizacion> organizaciones;
    private static AdapterRecordatorioEmail adapterMail;
    private static AdapterRecordatorioWhatsapp adapterWhatsapp;
    private static String cuerpo;

    private Recordatorio() {
        organizaciones = new ArrayList<>();
    }

    public static Recordatorio getInstancia() {
        if (instancia == null) instancia = new Recordatorio();
        return instancia;
    }

    public void iniciarRecordatorio(ConfiguracionCron cron) {
        try {
            JobDetail job = JobBuilder.newJob(Notificacion.class)
                    .withIdentity("job", "group1").build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("cronTrigger", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron.getCronExpresion()))
                    .build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);

            Thread.sleep(100000);

            scheduler.shutdown();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static class Notificacion implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            List<Contacto> listaDeContactos = obtenerListadosDeContactos();
            adapterMail.enviarNotificacion(cuerpo, listaDeContactos);
            adapterWhatsapp.enviarNotificacion(cuerpo, listaDeContactos);
        }
    }

    public void agregarOrganizacion(Organizacion organizacion) {
        organizaciones.add(organizacion);
    }

    public void setAdapterMail(AdapterRecordatorioEmail adapterMail) {
        Recordatorio.adapterMail = adapterMail;
    }

    public void setAdapterWhatsapp(AdapterRecordatorioWhatsapp adapterWhatsapp) {
        Recordatorio.adapterWhatsapp = adapterWhatsapp;
    }

    public void setCuerpoMensaje(String cuerpoMensaje) {
        Recordatorio.cuerpo = cuerpoMensaje;
    }

    public static List<Contacto> obtenerListadosDeContactos() {
        return Recordatorio.organizaciones.stream()
                .flatMap(o -> o.getContactos().stream())
                .collect(Collectors.toList());
    }
}
