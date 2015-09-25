/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure;

import com.sg.domain.services.MailService;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.TokenSignature;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
//    private final VelocityEngine velocityEngine;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender
    //,VelocityEngine velocityEngine
    ) {
        this.mailSender = mailSender;
//        this.velocityEngine = velocityEngine;
    }

    @Override
    public void sendEmailVerificationMessage(Email email,
                                             TokenSignature signature) {
//        MimeMessagePreparator preparator = new MimeMessagePreparator() {
//
//            @Override
//            public void prepare(MimeMessage mimeMessage) throws Exception {
//                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//                message.setTo(email.getEmail());
//                message.setFrom("webmaster@csonth.gov.uk"); // could be parameterized...
//                Map model = new HashMap();
//                model.put("email", email.getEmail());
//                model.put("token", signature.getToken());
//
//                String text = VelocityEngineUtils.mergeTemplateIntoString(
//                        velocityEngine,
//                        "com/dns/registration-confirmation.vm",
//                        "UTF-8",
//                        model);
//                message.setText(text, true);
//            }
//        };
//        this.mailSender.send(preparator);
//        

        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {

                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(email.getEmail()));
                mimeMessage.setFrom(new InternetAddress("mail@mycompany.com"));
                mimeMessage.setText("Hello world!");
            }
        };

        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
        }
    }

}
