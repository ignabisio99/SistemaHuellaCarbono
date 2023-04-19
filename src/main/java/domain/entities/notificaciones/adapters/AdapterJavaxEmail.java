package domain.entities.notificaciones.adapters;

import domain.entities.LectorProperties;
import domain.entities.actores.organizaciones.Contacto;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AdapterJavaxEmail implements AdapterRecordatorioEmail {
    private String email;
    private String contrasenia;

    @Override
    public void enviarNotificacion(String urlGuia, List<Contacto> contactos) {
        try {
            getUser();

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            prop.put("mail.smtp.port", "587"); //TLS Port
            prop.put("mail.smtp.auth", "true"); //enable authentication
            prop.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, contrasenia);
                }
            };

            Session session = Session.getInstance(prop, auth);
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(email));
            String asunto = "Te brindamos la siguiente guia de recomendaciones";
            msg.setSubject(asunto, "UTF-8");
            msg.setText(urlGuia, "UTF-8");

            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(
                            contactos.stream()
                                    .map(Contacto::getMail)
                                    .collect(Collectors.joining(","))
                    )
            );

            Transport.send(msg);
            System.out.println("Email Sent Successfully!!");
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void getUser() {
        Properties properties = new LectorProperties()
                .leerProperties("notificaciones/remitente.properties");

        this.email = properties.getProperty("email");
        this.contrasenia = properties.getProperty("contrasenia");
    }

    /*public void init () {
        this.prop = new Properties();
        this.prop.put("mail.smtp.host", "smtp.gmail.com");
        this.prop.setProperty("mail.smtp.starttls.enable", "true");
        this.prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        this.prop.setProperty("mail.smtp.port", "587");
        this.prop.setProperty("mail.smtp.auth", "true");
        this.prop.setProperty("mail.smtp.user", email);
    }

    public AdapterJavaxEmail(String email, String contrasenia) {
        // ESTE EMAIL ES DE LA PERSONA REMITENTE, QUIEN ENVIA EL MAIL
        this.email = email;
        this.contrasenia = contrasenia;
        this.init();
    }

    @Override
    public void enviarNotificacion(String urlGuia, List<Contacto> listaDeContactos) {
        try {
            Session sesion = Session.getDefaultInstance(this.prop);
            MimeMessage msj = new MimeMessage(sesion);
            msj.setFrom(new InternetAddress(this.email)); // EMAIL DEL REMITENTE

            for (Contacto listaDeContacto : listaDeContactos) {
                msj.addRecipient(Message.RecipientType.TO, new InternetAddress(listaDeContacto.getMail()));
                msj.setSubject("Te brindamos la siguiente guia de recomendaciones");
                msj.setText("¡Hola! ¡Esperamos que te encuentres bien! " +
                        "A continuación te brindamos el siguiente enlace para que puedas chequear las últimas recomendaciones " +
                        "para mejorar el nivel de tu huella de carbono." + urlGuia + "¡Saludos!");

                Transport t = sesion.getTransport("smtp");
                t.connect(this.email, this.contrasenia);
                t.sendMessage(msj, msj.getAllRecipients());
                t.close();
          }
          catch (Exception e) { e.printStackTrace(); }
        }
    }*/
}
