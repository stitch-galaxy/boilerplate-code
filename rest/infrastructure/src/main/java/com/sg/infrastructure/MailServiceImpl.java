/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure;

import com.sg.domain.services.MailService;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.TokenSignature;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author Admin
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final VelocityEngine velocityEngine;

    @Value("${site.url}")
    private String siteUrl;

    @Value("${site.notification.email}")
    private String siteNotificationEmail;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender,
                           VelocityEngine velocityEngine
    ) {
        this.mailSender = mailSender;
        this.velocityEngine = velocityEngine;
    }

    @Override
    public void sendRegistrationConfirmationEmail(Email email,
                                                  TokenSignature signature) {

        MimeMessagePreparator preparator = (MimeMessage mimeMessage) -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            
            message.setTo(email.getEmail());
            message.setFrom(siteNotificationEmail);
            message.setSubject("Registration confirmation");
            Map model = new HashMap();
            model.put("link", StringEscapeUtils.escapeHtml(String.format("https://%s/registration-confirmation.html?token=%s", siteUrl, signature.getToken())));
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine,
                    "registration-confirmation.vm",
                    "UTF-8",
                    model);
            message.setText(text, true);
        };

        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
        }
    }

    @Override
    public void sendResetPasswordLink(Email email,
                                      TokenSignature signature) {
        MimeMessagePreparator preparator = (MimeMessage mimeMessage) -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            
            message.setTo(email.getEmail());
            message.setFrom(siteNotificationEmail);
            message.setSubject("Password reset");
            Map model = new HashMap();
            model.put("link", StringEscapeUtils.escapeHtml(String.format("https://%s/password-reset.html?token=%s", siteUrl, signature.getToken())));
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine,
                    "password-reset.vm",
                    "UTF-8",
                    model);
            message.setText(text, true);
        };

        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
        }
    }

}
