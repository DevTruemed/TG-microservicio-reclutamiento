package com.truemedgroup.reclutamiento.services.impl;

import com.truemedgroup.commonsRecruit.usuario.PostRapArchivo;
import com.truemedgroup.reclutamiento.repositories.postulaciones.PostulacionesRapidasRepository;
import com.truemedgroup.reclutamiento.services.interfaces.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PostulacionesRapidasRepository postulacionesRapidasRepository;

    @Override
    public Boolean sentEmailForgotPassword(String email, String token) {
        return sendEmail(email, "Recuperación de contraseña", "Hemos recibido una solicitud para recuperar tu contraeña, para completar el proceso" +
                "accede al siguiente link: https://reclutamiento.truemedgroup.com/changePassword/"+token);
    }

    private Boolean sendEmail(String to, String subject, String content) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom("Reclutamiento Truemed <customerservice@truemedgroup.com>");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        email.setReplyTo("do-not-reply");
        try {
            mailSender.send(email);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

    }

    @Override
    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach, String fileName, String inlineImages)
    {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(new InternetAddress("customerservice@truemedgroup.com"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            byte[] byteArr = Base64.getDecoder().decode(fileToAttach.split(",")[1]);
            helper.addAttachment(fileName, new ByteArrayResource(byteArr));

            if (inlineImages.length() > 0) {
                String[] inlineArray = inlineImages.split(",");
                if (inlineArray.length > 0) {
                    for (String inline: inlineArray) {
                        String archivoData = postulacionesRapidasRepository.getArchivoById(Integer.parseInt(inline));
                        byte[] byteInlin = Base64.getDecoder().decode(archivoData.split(",")[1]);
                        helper.addInline("image00" + inline, new ByteArrayResource(byteInlin), "image/png");
                    }
                }
            }


            mailSender.send(mimeMessage);

            System.out.println("Email sending complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
