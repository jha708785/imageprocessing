/*
package com.guzloo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmailService {

    //@Autowired
    //private JavaMailSender mailSender;
    public void sendMail(String toEmail, String subject, String message, File file){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(message);
            helper.addAttachment(file.getName(), file);

            mailSender.send(mimeMessage);
            System.out.println(" Email sent with attachment");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
*/
