package com.truemedgroup.reclutamiento.services.interfaces;

/**
 * @author Carlos Arturo Guzmán García
 * @version 1.0.0
 * Servicio encargado de procesar todos los mails enviados desde la API
 **/
public interface MailService {

    public Boolean sentEmailForgotPassword(String email, String token);

    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach, String fileName, String inlineImages);
}
